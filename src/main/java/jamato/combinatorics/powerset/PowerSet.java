package jamato.combinatorics.powerset;

import java.math.BigInteger;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import jamato.util.indexedsublist.BigIndexedSubList;
import jamato.util.indexedsublist.IndexedSubList;
import jamato.util.largecollection.LargeUnmodifiableList;

/**
 * A power set contains all possible sub-sets of a given input collection.
 * <p>
 * Note: Despite the name, which is chosen to match the name used in set theory, this class implements the {@link List}
 * interface. The sub-"sets" returned are also lists. The order is kept consistently for all lists, and also keep the
 * order of the input collection (if defined).
 * 
 * @author JSiebel
 *
 * @param <T> the type of elements in the input collection
 */
public class PowerSet<T> extends AbstractList<List<T>> implements LargeUnmodifiableList<List<T>> {
	
	private final List<T> baseList;
	
	/**
	 * Creates a new power set.
	 * 
	 * @param baseList an input collection on which this power set is based
	 */
	public PowerSet(Collection<T> baseList) {
		this.baseList = new ArrayList<>(baseList);
	}
	
	@Override
	public Stream<List<T>> stream() {
		if (baseList.size() < Long.SIZE - 1) {
			return LongStream.rangeClosed(0, (1L << baseList.size()) - 1)
					.mapToObj(bitMask -> new IndexedSubList<>(baseList, bitMask));
		} else if (baseList.size() == Long.SIZE) {
			return LongStream
					.concat(LongStream.rangeClosed(0, Long.MAX_VALUE), LongStream.rangeClosed(Long.MIN_VALUE, -1))
					.mapToObj(bitMask -> new IndexedSubList<>(baseList, bitMask));
		} else {
			return Stream.concat(
					LongStream
							.concat(
									LongStream.rangeClosed(0, Long.MAX_VALUE),
									LongStream.rangeClosed(Long.MIN_VALUE, -1))
							.mapToObj(bitMask -> new IndexedSubList<>(baseList, bitMask)),
					Stream.iterate(
							BigInteger.ONE.shiftLeft(Long.SIZE),
							index -> !index.testBit(baseList.size()),
							BigInteger.ONE::add).map(bitMask -> new BigIndexedSubList<>(baseList, bitMask)));
		}
	}
	
	@Override
	public boolean contains(Object o) {
		if (o instanceof List) {
			Iterator<?> baseIterator = baseList.iterator();
			for (Object element : (List<?>) o) {
				boolean found = false;
				while (!found && baseIterator.hasNext()) {
					Object next = baseIterator.next();
					found = Objects.equals(element, next);
				}
				if (!found) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Iterator<List<T>> iterator() {
		return listIterator(0);
	}
	
	@Override
	public ListIterator<List<T>> listIterator(int index) {
		if (baseList.size() < Integer.SIZE - 1) {
			return new PowerSetListIterator<>(baseList, index);
		} else {
			return new BigPowerSetListIterator<>(baseList, BigInteger.valueOf(index));
		}
	}
	
	@Override
	public int size() {
		if (baseList.size() < Integer.SIZE - 1) {
			return 1 << baseList.size();
		} else {
			return Integer.MAX_VALUE;
		}
	}
	
	@Override
	public BigInteger actualSize() {
		return BigInteger.ONE.shiftLeft(baseList.size());
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public List<T> get(int index) {
		if (index < 0 || baseList.size() < Integer.SIZE - 1 && index >= size()) {
			throw new IndexOutOfBoundsException(index);
		}
		return new IndexedSubList<>(baseList, index);
	}
	
	@Override
	public List<T> get(BigInteger index) {
		if (index.signum() < 0 || index.bitLength() > baseList.size()) {
			throw new IndexOutOfBoundsException("Index out of range: " + index);
		}
		return new BigIndexedSubList<>(baseList, index);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof PowerSet) {
			return ((PowerSet<?>) o).baseList.equals(baseList);
		} else {
			return super.equals(o);
		}
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
