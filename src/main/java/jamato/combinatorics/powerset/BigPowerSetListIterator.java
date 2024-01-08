package jamato.combinatorics.powerset;

import java.math.BigInteger;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import jamato.util.indexedsublist.BigIndexedSubList;

/**
 * A {@link ListIterator} for big {@link PowerSet}s. This iterator does not support any modifying operations.
 * 
 * @author JSiebel
 *
 * @param <E> the type of elements returned by this list iterator
 */
class BigPowerSetListIterator<E> implements ListIterator<List<E>> {
	
	/** The list of which subsets are returned. */
	private List<E> baseList;
	
	/** The next index. */
	private BigInteger index;
	
	/**
	 * Creates a new PowerSetListIterator.
	 * 
	 * @param baseList the list of which subsets are returned
	 * @param startIndex the index of the first element to be returned
	 */
	BigPowerSetListIterator(List<E> baseList, BigInteger startIndex) {
		this.baseList = baseList;
		this.index = startIndex;
	}
	
	@Override
	public boolean hasNext() {
		return index.bitLength() <= baseList.size();
	}
	
	@Override
	public List<E> next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		List<E> result = new BigIndexedSubList<>(baseList, index);
		index = index.add(BigInteger.ONE);
		return result;
	}
	
	@Override
	public boolean hasPrevious() {
		return index.signum() > 0;
	}
	
	@Override
	public List<E> previous() {
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}
		index = index.subtract(BigInteger.ONE);
		return new BigIndexedSubList<>(baseList, index);
	}
	
	/**
	 * {@inheritDoc} If the index is to big to fit into an <code>int</code>, only the low-order 32 bits are returned.
	 */
	@Override
	public int nextIndex() {
		return index.intValue();
	}
	
	/**
	 * {@inheritDoc} If the index is to big to fit into an <code>int</code>, only the low-order 32 bits are returned.
	 */
	@Override
	public int previousIndex() {
		return index.intValue() - 1;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void set(List<E> e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(List<E> e) {
		throw new UnsupportedOperationException();
	}
}
