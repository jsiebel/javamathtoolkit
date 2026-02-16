package jamato.util.indexedsublist;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import jamato.util.bitoperations.BitOperations;

/**
 * A {@link ListIterator} for {@link IndexedSubList}s. This iterator does not support any modifying operations.
 * 
 * @author JSiebel
 * @param <E> the type of elements returned by this list iterator
 */
class IndexedSubListIterator<E> implements ListIterator<E> {
	
	/** The list that is referenced by the indices. */
	private final List<E> list;
	
	/**
	 * The index mask. The <code>2^i</code> bit indicates the presence of the base list element with index
	 * <code>i</code>.
	 */
	private final long mask;
	
	/**
	 * The index of the current <code>IndexedSubList</code> element. The value is 0 if {@link #hasPrevious()} is false,
	 * and equal to the <code>IndexedSubList</code>'s size if {@link #hasNext()} is false.
	 */
	private int index;
	
	/**
	 * The index of the current base list element. The value is <code>64</code> if {@link #hasNext()} is false.
	 */
	private int maskIndex;
	
	/**
	 * Creates a new IndexedSubListIterator.
	 * 
	 * @param list the list that is referenced by the indices
	 * @param indexMask a bit mask indicating which elements of the base list are contained in the IndexedSubList
	 * @param startIndex the initial index in the IndexedSubList
	 */
	public IndexedSubListIterator(List<E> list, long mask, int startIndex) {
		this.list = list;
		this.mask = mask;
		this.index = startIndex;
		this.maskIndex = BitOperations.offsetOfNthBit(mask, startIndex);
	}
	
	@Override
	public boolean hasNext() {
		return maskIndex < Long.SIZE;
	}
	
	@Override
	public E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		E result = list.get(maskIndex);
		index++;
		maskIndex = BitOperations.indexOfNextHigherOneBit(mask, maskIndex);
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
		maskIndex = BitOperations.indexOfNextLowerOneBit(mask, maskIndex);
		return list.get(maskIndex);
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
