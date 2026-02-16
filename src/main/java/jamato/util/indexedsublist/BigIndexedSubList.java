package jamato.util.indexedsublist;

import java.math.BigInteger;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jamato.util.bitoperations.BitOperations;

/**
 * An indexed sublist is an immutable list that references elements of a base list by their indexes.
 * 
 * @author JSiebel
 * @param <E> the type of elements in this list
 */
public class BigIndexedSubList<E> extends AbstractList<E> {
	
	/** The list that is referenced by the indices. */
	private final List<E> baseList;
	
	/**
	 * The index mask. The <code>2<sup>i</sup></code> bit of <code>indexMask[j]</code> indicates the presence of the
	 * base list element with index <code>32*j+i</code>.
	 */
	private final int[] indexMask;
	
	/** The size of this list. */
	private int size;
	
	/**
	 * Creates a new BigIndexedSubList of elements in the base list. The <code>2^i</code> bit of
	 * <code>indexMask[j]</code> indicates the presence of the base list element with index <code>32*j+i</code>. This
	 * list is backed by the base list; structural changes in that list are not allowed. The size index mask doesn't
	 * need to match the size of the base list, but may not have one-bits outside the list's range.
	 * 
	 * @param baseList the list that serves as a base for this list
	 * @param indexMask a bit mask indicating which elements of the base list are contained in this list
	 * @throws IndexOutOfBoundsException if a bit in the mask references a non-existing list index
	 */
	public BigIndexedSubList(List<E> baseList, int... indexMask) {
		checkMaskBits(indexMask, baseList.size());
		this.baseList = baseList;
		this.indexMask = Arrays.copyOf(indexMask, (baseList.size() + Integer.SIZE - 1) / Integer.SIZE);
		for (int mask : indexMask) {
			this.size += Integer.bitCount(mask);
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
	private static void checkMaskBits(int[] indexMask, int listSize) {
		int lowestInvalidMaskArrayIndex = listSize / Integer.SIZE;
		if (indexMask.length <= lowestInvalidMaskArrayIndex) {
			return;
		}
		
		int indexMaskMaximumValidIndex = (listSize - 1) / Integer.SIZE;
		int nValidBitsInMaximumIndex = listSize - indexMaskMaximumValidIndex * Integer.SIZE;
		
		int remainder = indexMask[indexMaskMaximumValidIndex] >> nValidBitsInMaximumIndex;
		if (remainder != 0) {
			int invalidIndex = Integer.SIZE * indexMaskMaximumValidIndex + nValidBitsInMaximumIndex
					+ Integer.numberOfTrailingZeros(remainder);
			throw new IndexOutOfBoundsException(invalidIndex);
		}
		for (int i = indexMaskMaximumValidIndex + 1; i < indexMask.length; i++) {
			if (indexMask[i] != 0) {
				int invalidIndex = Integer.SIZE * i + Integer.numberOfTrailingZeros(indexMask[i]);
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
		this.size = indexMask.bitCount();
		this.indexMask = new int[(indexMask.bitLength() + Integer.SIZE - 1) / Integer.SIZE];
		byte[] byteArray = indexMask.signum() == 0 ? new byte[0] : indexMask.toByteArray();
		
		int nRelevantBytes = (indexMask.bitLength() + 7) >> 3;
		
		for (int i = 0; i < nRelevantBytes; i++) {
			int maskIndex = i / 4;
			int maskIndexOffset = 8 * (i - 4 * maskIndex);
			int byteValue = Byte.toUnsignedInt(byteArray[byteArray.length - 1 - i]);
			this.indexMask[maskIndex] |= byteValue << maskIndexOffset;
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
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException(index);
		}
		return new BigIndexedSubListIterator<>(baseList, indexMask, index);
	}
	
	@Override
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException(index);
		}
		int remainingBits = index;
		int maskIndex = 0;
		int nextCount;
		while ((nextCount = Integer.bitCount(indexMask[maskIndex])) <= remainingBits) {
			maskIndex++;
			remainingBits -= nextCount;
		}
		int baseIndex = maskIndex * Integer.SIZE + BitOperations.offsetOfNthBit(indexMask[maskIndex], remainingBits);
		return baseList.get(baseIndex);
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
