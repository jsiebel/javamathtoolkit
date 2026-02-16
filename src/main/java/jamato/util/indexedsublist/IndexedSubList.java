package jamato.util.indexedsublist;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jamato.util.bitoperations.BitOperations;

/**
 * An indexed sublist is a list that references elements of a base list by their indexes. Only the first 64 elements can
 * be referenced.
 * 
 * @author JSiebel
 * @param <E> the type of elements in this list
 */
public class IndexedSubList<E> extends AbstractList<E> {
	
	/** The list that is referenced by the indices. */
	private final List<E> baseList;
	
	/**
	 * The index mask. The <code>2^i</code> bit indicates the presence of the base list element with index
	 * <code>i</code>.
	 */
	private final long mask;
	
	/**
	 * Creates a new IndexedSubList of elements in the base list. The index mask's <code>2^i</code> bit indicates the
	 * presence of the base list element with index <code>i</code>. This list is backed by the base list; structural
	 * changes in that list are not allowed.
	 * 
	 * @param baseList the list that serves as a base for this list
	 * @param mask a bit mask indicating which elements of the base list are contained in this list
	 */
	public IndexedSubList(List<E> baseList, long mask) {
		this.baseList = baseList;
		this.mask = mask;
		if (baseList.size() < Long.SIZE - Long.numberOfLeadingZeros(mask)) {
			int invalidIndex = baseList.size() + Long.numberOfTrailingZeros(mask >> baseList.size());
			throw new IndexOutOfBoundsException(invalidIndex);
		}
	}
	
	@Override
	public int size() {
		return Long.bitCount(mask);
	}
	
	@Override
	public boolean isEmpty() {
		return mask == 0;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new IndexedSubListIterator<>(baseList, mask, 0);
	}
	
	@Override
	public ListIterator<E> listIterator(int index) {
		return new IndexedSubListIterator<>(baseList, mask, index);
	}
	
	@Override
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException(index);
		}
		return baseList.get(BitOperations.offsetOfNthBit(mask, index));
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
