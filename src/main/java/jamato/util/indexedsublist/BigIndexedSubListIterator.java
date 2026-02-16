package jamato.util.indexedsublist;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import jamato.util.bitoperations.BitOperations;

/**
 * A {@link ListIterator} for {@link BigIndexedSubList}s. This iterator does not support any modifying operations.
 * 
 * @author JSiebel
 * @param <E> the type of elements returned by this list iterator
 */
class BigIndexedSubListIterator<E> implements ListIterator<E> {
	
	/** The list that is referenced by the indices. */
	private final List<E> list;
	
	/**
	 * The index mask array. The offset <code>i</code> bit of the array entry at index <code>j</code> indicates the
	 * presence of the base list element with index <code>32*j+i</code>.
	 */
	private final int[] maskArray;
	
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
	 * The bit index in the current mask array element. The value is <code>32</code> if {@link #hasNext()} is false.
	 */
	private int maskBitIndex;
	
	/**
	 * Creates a new BigIndexedSubListIterator for a {@link BigIndexedSubList}.
	 * 
	 * @param list the list that is referenced by the indices
	 * @param indexMask a bit mask indicating which elements of the base list are contained in the BigIndexedSubList.
	 * @param startIndex the initial index in the BigIndexedSubList
	 */
	public BigIndexedSubListIterator(List<E> list, int[] maskArray, int startIndex) {
		this.list = list;
		this.maskArray = maskArray.length == 0 ? new int[] {0} : maskArray;
		this.index = startIndex;
		int totalBitcount = 0;
		
		this.maskArrayIndex = 0;
		while (maskArrayIndex < this.maskArray.length - 1
				&& totalBitcount + Integer.bitCount(this.maskArray[maskArrayIndex]) <= startIndex) {
			totalBitcount += Integer.bitCount(this.maskArray[maskArrayIndex]);
			this.maskArrayIndex++;
		}
		this.maskBitIndex = BitOperations.offsetOfNthBit(this.maskArray[maskArrayIndex], startIndex - totalBitcount);
	}
	
	@Override
	public boolean hasNext() {
		return maskBitIndex < Integer.SIZE;
	}
	
	@Override
	public E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		E result = list.get(maskArrayIndex << 5 | maskBitIndex);
		index++;
		maskBitIndex = BitOperations.indexOfNextHigherOneBit(maskArray[maskArrayIndex], maskBitIndex);
		if (maskBitIndex == Integer.SIZE && maskArrayIndex < maskArray.length - 1) {
			maskArrayIndex++;
			while (maskArrayIndex < maskArray.length - 1 && maskArray[maskArrayIndex] == 0) {
				maskArrayIndex++;
			}
			maskBitIndex = Integer.numberOfTrailingZeros(maskArray[maskArrayIndex]);
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
			maskBitIndex = 31 - Integer.numberOfLeadingZeros(maskArray[maskArrayIndex]);
		}
		return list.get(maskArrayIndex << 5 | maskBitIndex);
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
