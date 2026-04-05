package jamato.combinatorics.powerset;

import java.math.BigInteger;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import jamato.util.indexedsublist.IndexedSubList;
import jamato.util.largecollection.LargeUnmodifiableList;

/**
 * A sub list of a power set contains the possible sub-sets of a given input collection in a certain index range. The
 * index is a bit representation encoding which base list element is contained: If the <code>i</code>th bit of a power
 * set element index is set, then it contains the <code>i</code>th element of the power set's base list.
 *
 * @author JSiebel
 * @param <T> the type of elements in the input collection; elements of this collection have the type {@code List<T>}
 */
class PowerSetSublist<T> extends AbstractList<List<T>> implements LargeUnmodifiableList<List<T>>, RandomAccess {
	
	/** The PowerSet of which this is a sublist. */
	private final PowerSet<T> powerSet;
	
	/** The low endpoint (inclusive) of the subList. */
	private final BigInteger fromIndex;
	
	/** The high endpoint (exclusive) of the subList. */
	private final BigInteger toIndex;
	
	/**
	 * Creates a new sub list of a power set.
	 * 
	 * @param baseList an input collection on which this power set is based
	 * @param fromIndex low endpoint (inclusive) of the sub-list
	 * @param toIndex high endpoint (exclusive) of the sub-list
	 * @throws IndexOutOfBoundsException if an endpoint index value is out of range
	 *             {@code (fromIndex < 0 || toIndex > size)}
	 * @throws IllegalArgumentException if the endpoint indices are out of order ({@code fromIndex > toIndex})
	 */
	PowerSetSublist(PowerSet<T> powerSet, BigInteger fromIndex, BigInteger toIndex) {
		this.powerSet = powerSet;
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
		if (fromIndex.signum() < 0) {
			throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
		}
		if (toIndex.compareTo(powerSet.actualSize()) > 0) {
			throw new IndexOutOfBoundsException("toIndex = " + toIndex);
		}
		if (fromIndex.compareTo(toIndex) > 0) {
			throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
		}
	}
	
	@Override
	public Stream<List<T>> stream() {
		if (toIndex.bitLength() < Long.SIZE - 1) {
			return LongStream.range(fromIndex.longValue(), toIndex.longValue())
					.mapToObj(bitMask -> new IndexedSubList<>(powerSet.baseList, bitMask));
		} else {
			return Stream.iterate(fromIndex, index -> !index.equals(toIndex), BigInteger.ONE::add)
					.map(powerSet::get);
		}
	}
	
	@Override
	public boolean contains(Object o) {
		if (isEmpty()) {
			return false;
		} else if (o instanceof List) {
			BigInteger maxIndex = toIndex.subtract(BigInteger.ONE);
			return contains((List<?>) o, fromIndex, maxIndex);
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if a list is an element in the given sublist range. The sublist range is the bit mask range from
	 * <code>minIndex</code> to <code>maxIndex</code> (inclusive), where only the lowest bits are used.
	 * 
	 * @param list the element whose presence in this list is to be tested
	 * @param minIndex the lowest index in the sublist range
	 * @param maxIndex the highest index in the sublist range
	 * @return {@code true} if the sublist range contains the specified element
	 */
	private boolean contains(List<?> list, BigInteger minIndex, BigInteger maxIndex) {
		// The range is searched from the highest to the lowest bit (and, equivalently, from the highest to the lowest
		// index in the list parameter). If the range includes both 0 and 1 in the current bit, the search is split to
		// evaluate both cases separately.
		int nextIndex = list.size() - 1;
		int nextMaskIndex = maxIndex.bitLength() - 1;
		// First, process the indices where all bit masks in range have the same value.
		while (nextMaskIndex >= nextIndex && nextIndex >= 0
				&& minIndex.testBit(nextMaskIndex) == maxIndex.testBit(nextMaskIndex)) {
			if (!minIndex.testBit(nextMaskIndex)) {
				// The next object is not available in any mask in range, so it is skipped.
				nextMaskIndex--;
			} else if (Objects.equals(list.get(nextIndex), powerSet.baseList.get(nextMaskIndex))) {
				// The next list element matches the base element at this bit position. It is included in each mask in
				// range, so it is taken as a match.
				nextIndex--;
				nextMaskIndex--;
			} else {
				// The next object is an element in all masks in range, but it is not a match. No complete matching is
				// possible.
				return false;
			}
		}
		if (nextMaskIndex == -1) {
			// The mask range has identical bits in all indices, i.e. the range has the size of 1.
			return nextIndex == -1;
		} else if (nextIndex == -1) {
			// No more elements to be matched. The all-zero mask (empty subset) must be in the range, which requires
			// minIndex to have no set bits in positions 0..nextMaskIndex.
			return minIndex.equals(BigInteger.ZERO) || minIndex.getLowestSetBit() > nextMaskIndex;
		} else if (nextMaskIndex < nextIndex) {
			// The mask remainder to be checked has fewer elements than the remainder of the searched object.
			return false;
		} else if (Objects.equals(list.get(nextIndex), powerSet.baseList.get(nextMaskIndex))) {
			// The next object can be taken as a match. However, this greedy path may not be the correct one for a
			// matching, because later indexes might be missing. Therefore, the search is split in two parts, one where
			// the current mask bit is 0, and one where it's 1. For the latter, the next list object is a match.
			return lowerBoundRangeContains(list.subList(0, nextIndex + 1), minIndex, nextMaskIndex)
					|| upperBoundRangeContains(list.subList(0, nextIndex), maxIndex, nextMaskIndex);
		} else {
			// If the next object doesn't match, the mask cannot have a 1 bit next. The search is therefore reduced to
			// the part of the mask range where the current mask bit is not set.
			return lowerBoundRangeContains(list.subList(0, nextIndex + 1), minIndex, nextMaskIndex);
		}
	}
	
	/**
	 * Checks if a list is an element in the given sublist upper-bound range. The sublist range is the bit mask range
	 * from <code>0</code> to <code>maxIndex</code> (inclusive), where only the lowest bits are used.
	 * 
	 * @param list the element whose presence in this list is to be tested
	 * @param maxIndex the highest index in the sublist range
	 * @param relevantMaskLength the number of relevant bits in the mask index bounds; any higher bits are ignored
	 * @return {@code true} if this collection contains the specified element
	 */
	private boolean upperBoundRangeContains(List<?> list, BigInteger maxIndex, int relevantMaskLength) {
		int nextIndex = list.size() - 1;
		int nextMaskIndex = relevantMaskLength - 1;
		// First, process the indices where all bit masks in range have the same value.
		while (nextMaskIndex >= nextIndex && nextIndex >= 0 && !maxIndex.testBit(nextMaskIndex)) {
			// The next object is not available in any mask in range, so it is skipped.
			nextMaskIndex--;
		}
		if (nextMaskIndex == -1) {
			// The remaining mask range has identical bits in all indices, i.e. the range has the size of 1.
			return nextIndex == -1;
		} else if (nextIndex == -1) {
			// No more elements to be matched, this is satisfied by the lower-bound zero mask.
			return true;
		} else if (nextMaskIndex < nextIndex) {
			// The mask remainder to be checked has fewer elements than the remainder of the searched object.
			return false;
		} else if (Objects.equals(list.get(nextIndex), powerSet.baseList.get(nextMaskIndex))) {
			// The next object can be taken as a match. However, this greedy path may not be the correct one for a
			// matching, because later indexes might be missing. The search is split in two parts, one where the current
			// mask bit is 0, and one where it's 1. For the latter, the next list object is a match.
			return fullRangeContains(list.subList(0, nextIndex + 1), nextMaskIndex)
					|| upperBoundRangeContains(list.subList(0, nextIndex), maxIndex, nextMaskIndex);
		} else {
			// If the next object doesn't match, the mask cannot have a 1 bit next. The search is therefore reduced to
			// the part of the mask range where the current mask bit is not set.
			return fullRangeContains(list.subList(0, nextIndex + 1), nextMaskIndex);
		}
	}
	
	/**
	 * Checks if a list is an element in the given lower-bound sublist range. The sublist range is the bit mask range
	 * from <code>minIndex</code> to <code>111...111</code> (inclusive), where only the lowest bits are used.
	 * 
	 * @param list the element whose presence in this list is to be tested
	 * @param minIndex the lowest index in the sublist range
	 * @param relevantMaskLength the number of relevant bits in the mask index bounds; any higher bits are ignored
	 * @return {@code true} if this collection contains the specified element
	 */
	private boolean lowerBoundRangeContains(List<?> list, BigInteger minIndex, int relevantMaskLength) {
		// The range is searched from highest to lowest bit (and, equivalently, from the highest to the lowest index in
		// the list parameter). The loop advances through leading 1-bits of minIndex, where all masks in range share
		// the same bit value. The first 0-bit of minIndex is where the range splits, and is handled in the branches
		// below.
		int nextIndex = list.size() - 1;
		int nextMaskIndex = relevantMaskLength - 1;
		// First, process the bit positions where all masks in range have bit 1.
		while (nextMaskIndex >= nextIndex && nextIndex >= 0 && minIndex.testBit(nextMaskIndex)) {
			if (Objects.equals(list.get(nextIndex), powerSet.baseList.get(nextMaskIndex))) {
				// The next list element matches the base element at this bit position. It is included in each mask in
				// range, so it is taken as a match.
				nextIndex--;
				nextMaskIndex--;
			} else {
				// The next object is an element in all masks in range, but it is not a match. No complete matching is
				// possible.
				return false;
			}
		}
		if (nextMaskIndex == -1) {
			// The remaining mask range has identical bits in all indices, i.e. the range has the size of 1.
			return nextIndex == -1;
		} else if (nextIndex == -1) {
			// No more elements to be matched. The all-zero mask (empty subset) must be in the range, which requires
			// minIndex to have no set bits in positions 0..nextMaskIndex.
			return minIndex.equals(BigInteger.ZERO) || minIndex.getLowestSetBit() > nextMaskIndex;
		} else if (nextMaskIndex < nextIndex) {
			// The mask remainder to be checked has fewer elements than the remainder of the searched object.
			return false;
		} else if (Objects.equals(list.get(nextIndex), powerSet.baseList.get(nextMaskIndex))) {
			// The next object can be taken as a match. However, this greedy path may not be the correct one for a
			// matching, because later indexes might be missing. The search is split in two parts, one where the current
			// mask bit is 0, and one where it's 1. For the latter, the next list object is a match.
			return lowerBoundRangeContains(list.subList(0, nextIndex + 1), minIndex, nextMaskIndex)
					|| fullRangeContains(list.subList(0, nextIndex), nextMaskIndex);
		} else {
			// If the next object doesn't match, the mask cannot have a 1 bit next. The search is therefore reduced to
			// the part of the mask range where the current mask bit is not set.
			return lowerBoundRangeContains(list.subList(0, nextIndex + 1), minIndex, nextMaskIndex);
		}
	}
	
	/**
	 * Checks if the elements in the given list appear (in the right order) in the given part of the base list. This is
	 * sufficient because any ordered subset of the base list is a candidate, so any elements may be skipped.
	 * 
	 * @param list the element whose presence in this collection is to be tested
	 * @param relevantMaskLength the number of relevant bits; only the base list elements at positions
	 *            0..relevantMaskLength-1 are considered
	 * @return {@code true} if the full range [0, 111...1] contains the specified element
	 */
	private boolean fullRangeContains(List<?> list, int relevantMaskLength) {
		int nextIndex = list.size() - 1;
		int nextMaskIndex = relevantMaskLength - 1;
		while (nextMaskIndex >= nextIndex && nextIndex >= 0) {
			if (Objects.equals(list.get(nextIndex), powerSet.baseList.get(nextMaskIndex))) {
				nextIndex--;
			}
			nextMaskIndex--;
		}
		// nextMaskIndex may be 0 or greater; any remaining elements can be skipped by choosing a mask with zeros in
		// these indices.
		return nextIndex == -1;
	}
	
	@Override
	public int size() {
		if (actualSize().bitLength() < Integer.SIZE) {
			return actualSize().intValue();
		} else {
			return Integer.MAX_VALUE;
		}
	}
	
	@Override
	public BigInteger actualSize() {
		return toIndex.subtract(fromIndex);
	}
	
	@Override
	public boolean isEmpty() {
		return fromIndex.equals(toIndex);
	}
	
	@Override
	public List<T> get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException(index);
		}
		return powerSet.get(fromIndex.add(BigInteger.valueOf(index)));
	}
	
	@Override
	public List<T> get(BigInteger index) {
		if (index.signum() < 0 || index.compareTo(actualSize()) >= 0) {
			throw new IndexOutOfBoundsException("Index out of range: " + index);
		}
		return powerSet.get(fromIndex.add(index));
	}
	
	@Override
	public LargeUnmodifiableList<List<T>> subList(int fromIndex, int toIndex) {
		return subList(BigInteger.valueOf(fromIndex), BigInteger.valueOf(toIndex));
	}
	
	@Override
	public LargeUnmodifiableList<List<T>> subList(BigInteger fromIndex, BigInteger toIndex) {
		if (fromIndex.signum() < 0) {
			throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
		}
		if (toIndex.compareTo(powerSet.actualSize()) > 0) {
			throw new IndexOutOfBoundsException("toIndex = " + toIndex);
		}
		if (fromIndex.compareTo(toIndex) > 0) {
			throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
		}
		return new PowerSetSublist<>(powerSet, this.fromIndex.add(fromIndex), this.toIndex.add(toIndex));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(powerSet, fromIndex, toIndex);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (getClass() == obj.getClass()) {
			PowerSetSublist other = (PowerSetSublist) obj;
			return Objects.equals(powerSet, other.powerSet)
					&& Objects.equals(fromIndex, other.fromIndex)
					&& Objects.equals(toIndex, other.toIndex);
		} else {
			return super.equals(obj);
		}
	}
}
