package jamato.util.indexedsublist;

import java.math.BigInteger;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * An indexed sublist is an immutable list that references elements of a base list by their indexes.
 * 
 * @author JSiebel
 *
 * @param <E> the type of elements in this list
 */
public class BigIndexedSubList<E> extends AbstractList<E> {
	
	/** The list that is referenced by the indices. */
	private final List<E> baseList;
	
	/**
	 * The index mask. The <code>2^i</code> bit of <code>indexMask[j]</code> indicates the presence of the base list
	 * element with index <code>64*j+i</code>.
	 */
	private final long[] indexMask;
	
	/** The size of this list. */
	private int size;
	
	/**
	 * Creates a new BigIndexedSubList of elements in the base list. The <code>2^i</code> bit of
	 * <code>indexMask[j]</code> indicates the presence of the base list element with index <code>64*j+i</code>. This
	 * list is backed by the base list; structural changes in that list are not allowed.
	 * 
	 * @param baseList the list that serves as a base for this list
	 * @param indexMask a bit mask indicating which elements of the base list are contained in this list
	 */
	public BigIndexedSubList(List<E> baseList, long... indexMask) {
		checkMaskBits(indexMask, baseList.size());
		this.baseList = baseList;
		this.indexMask = Arrays.copyOf(indexMask, (baseList.size() + 63) >> 6);
		for (long mask : indexMask) {
			this.size += Long.bitCount(mask);
		}
	}
	
	/**
	 * Checks if the given index mask contains no entries that exceed the given list size, i.e. references elements out
	 * of bounds of a list of that size.
	 * 
	 * @param indexMask a mask of indices
	 * @param listSize the size of a referenced list
	 * @throws IndexOutOfBoundsException if a bit in the mask references a non-existing list index
	 */
	private static void checkMaskBits(long[] indexMask, int listSize) {
		int lowestInvalidMaskArrayIndex = listSize >> 6;
		if (indexMask.length <= lowestInvalidMaskArrayIndex) {
			return;
		}
		int lowestInvalidBitIndex = listSize & (Long.SIZE - 1);
		if (indexMask[lowestInvalidMaskArrayIndex] >> lowestInvalidBitIndex != 0) {
			int lowestInvalidIndex = listSize
					+ Long.numberOfTrailingZeros(indexMask[lowestInvalidMaskArrayIndex] >> lowestInvalidBitIndex);
			throw new IndexOutOfBoundsException(lowestInvalidIndex);
		}
		for (int i = lowestInvalidMaskArrayIndex + 1; i < indexMask.length; i++) {
			if (indexMask[i] != 0) {
				int invalidIndex = Long.SIZE * i + Long.numberOfTrailingZeros(indexMask[i]);
				throw new IndexOutOfBoundsException(invalidIndex);
			}
		}
	}
	
	/**
	 * Creates a new BigIndexedSubList of elements in the base list. The index mask's <code>2^i</code> bit indicates the
	 * presence of the base list element with index <code>i</code>. This list is backed by the base list; structural
	 * changes in that list are not allowed.
	 * 
	 * @param baseList the list that serves as a base for this list
	 * @param indexMask a bit mask indicating which elements of the base list are contained in this list
	 */
	public BigIndexedSubList(List<E> baseList, BigInteger indexMask) {
		checkMaskBits(indexMask, baseList.size());
		this.baseList = baseList;
		this.indexMask = new long[(indexMask.bitLength() + 63) >> 6];
		byte[] byteArray = indexMask.signum() == 0 ? new byte[0] : indexMask.toByteArray();
		for (int i = 0; i < byteArray.length; i++) {
			int maskIndex = (byteArray.length - 1 - i) >> 3;
			int maskIndexOffset = 8 * ((byteArray.length - 1 - i) & 7);
			long byteValue = byteArray[i] & 255L;
			this.indexMask[maskIndex] |= byteValue << maskIndexOffset;
		}
		this.size = indexMask.bitCount();
	}
	
	/**
	 * Checks if the given index mask contains no entries that exceed the given list size, i.e. references elements out
	 * of bounds of a list of that size.
	 * 
	 * @param indexMask a mask of indices
	 * @param listSize the size of a referenced list
	 * @throws IndexOutOfBoundsException if a bit in the mask references a non-existing list index
	 */
	private static void checkMaskBits(BigInteger indexMask, int listSize) {
		if (indexMask.signum() < 0) {
			throw new IllegalArgumentException("Invalid index mask: " + indexMask);
		}
		if (indexMask.bitLength() > listSize) {
			int invalidIndex = indexMask.shiftRight(listSize).getLowestSetBit() + listSize;
			throw new IndexOutOfBoundsException(invalidIndex);
		}
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new BigIndexedSubListIterator<>(baseList, indexMask, 0);
	}
	
	@Override
	public ListIterator<E> listIterator(int index) {
		return new BigIndexedSubListIterator<>(baseList, indexMask, index);
	}
	
	@Override
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException(index);
		}
		int count = 0;
		int maskIndex;
		for (maskIndex = 0;; maskIndex++) {
			int nextCount = Long.bitCount(indexMask[maskIndex]);
			count += nextCount;
			if (count > index) {
				count -= nextCount;
				break;
			}
		}
		long remainingMask = indexMask[maskIndex];
		while (count < index) {
			count++;
			remainingMask &= remainingMask - 1;
		}
		return baseList.get(maskIndex << 6 | Long.numberOfTrailingZeros(remainingMask));
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}