package jamato.util.indexedsublist;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import jamato.util.bitoperations.BitOperations;

/**
 * A {@link ListIterator} for {@link BigIndexedSubList}s. This iterator does not support any modifying operations.
 * 
 * @author JSiebel
 *
 * @param <E> the type of elements returned by this list iterator
 */
class BigIndexedSubListIterator<E> implements ListIterator<E> {
	
	/** The list that is referenced by the indices. */
	private final List<E> list;
	
	/**
	 * The index mask. The <code>2^i</code> bit indicates the presence of the base list element with index
	 * <code>i</code>.
	 */
	private final long[] maskArray;
	
	/**
	 * The index of the current <code>IndexedSubList</code> element. The value is 0 if {@link #hasPrevious()} is false,
	 * and equal to the <code>IndexedSubList</code>'s size if {@link #hasNext()} is false.
	 */
	private int index;
	
	/**
	 * The index of the current mask array element.
	 */
	private int maskArrayIndex;
	
	/**
	 * The bit index in the current mask array element. The value is <code>64</code> if {@link #hasNext()} is false.
	 */
	private int maskBitIndex;
	
	/**
	 * Creates a new IndexedSubListIterator for a {@link BigIndexedSubList}.
	 * 
	 * @param list the list that is referenced by the indices
	 * @param indexMask a bit mask indicating which elements of the base list are contained in the BigIndexedSubList.
	 * @param startIndex the initial index in the IndexedSubList
	 */
	public BigIndexedSubListIterator(List<E> list, long[] maskArray, int startIndex) {
		this.list = list;
		this.maskArray = maskArray.length == 0 ? new long[] { 0 } : maskArray;
		int totalBitcount = 0;
		
		this.maskArrayIndex = 0;
		while (maskArrayIndex < this.maskArray.length - 1
				&& totalBitcount + Long.bitCount(this.maskArray[maskArrayIndex]) <= startIndex) {
			totalBitcount += Long.bitCount(this.maskArray[maskArrayIndex]);
			this.maskArrayIndex++;
		}
		long mask = this.maskArray[maskArrayIndex];
		while (totalBitcount < startIndex) {
			totalBitcount++;
			mask &= mask - 1;
		}
		this.maskBitIndex = Long.numberOfTrailingZeros(mask);
		this.index = startIndex;
	}
	
	@Override
	public boolean hasNext() {
		return maskBitIndex < Long.SIZE;
	}
	
	@Override
	public E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		E result = list.get(maskArrayIndex << 6 | maskBitIndex);
		index++;
		maskBitIndex = BitOperations.indexOfNextHigherOneBit(maskArray[maskArrayIndex], maskBitIndex);
		if (maskBitIndex == 64 && maskArrayIndex < maskArray.length - 1) {
			maskArrayIndex++;
			while (maskArrayIndex < maskArray.length - 1 && maskArray[maskArrayIndex] == 0) {
				maskArrayIndex++;
			}
			maskBitIndex = Long.numberOfTrailingZeros(maskArray[maskArrayIndex]);
		}
		return result;
	}
	
	@Override
	public boolean hasPrevious() {
		return index > 0;
	}
	
	@Override
	public E previous() {
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}
		index--;
		maskBitIndex = BitOperations.indexOfNextLowerOneBit(maskArray[maskArrayIndex], maskBitIndex);
		if (maskBitIndex == -1) {
			maskArrayIndex--;
			while (maskArray[maskArrayIndex] == 0) {
				maskArrayIndex--;
			}
			maskBitIndex = 63 - Long.numberOfLeadingZeros(maskArray[maskArrayIndex]);
		}
		return list.get(maskArrayIndex << 6 | maskBitIndex);
	}
	
	@Override
	public int nextIndex() {
		return index;
	}
	
	@Override
	public int previousIndex() {
		return index - 1;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void set(E e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(E e) {
		throw new UnsupportedOperationException();
	}
}
