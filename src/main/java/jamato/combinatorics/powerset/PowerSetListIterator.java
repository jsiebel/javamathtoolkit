package jamato.combinatorics.powerset;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import jamato.util.indexedsublist.IndexedSubList;

/**
 * A {@link ListIterator} for big {@link PowerSet}s. This iterator does not support any modifying operations.
 * 
 * @author JSiebel
 *
 * @param <E> the type of elements returned by this list iterator
 */
class PowerSetListIterator<T> implements ListIterator<List<T>> {
	
	/** The list of which subsets are returned. */
	private List<T> baseList;
	
	/** The next index. */
	private long index;
	
	/**
	 * Creates a new PowerSetListIterator.
	 * 
	 * @param baseList the list of which subsets are returned
	 * @param startIndex index of the first element to be returned
	 */
	public PowerSetListIterator(List<T> baseList, long startIndex) {
		this.baseList = baseList;
		this.index = startIndex;
	}
	
	@Override
	public boolean hasNext() {
		return index < 1L << baseList.size();
	}
	
	@Override
	public List<T> next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		List<T> result = new IndexedSubList<>(baseList, index);
		index++;
		return result;
	}
	
	@Override
	public boolean hasPrevious() {
		return index > 0;
	}
	
	@Override
	public List<T> previous() {
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}
		index--;
		return new IndexedSubList<>(baseList, index);
	}
	
	@Override
	public int nextIndex() {
		return (int) index;
	}
	
	@Override
	public int previousIndex() {
		return (int) index - 1;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void set(List<T> e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(List<T> e) {
		throw new UnsupportedOperationException();
	}
}
