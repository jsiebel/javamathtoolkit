package jamato.util.largecollection;

import java.math.BigInteger;
import java.util.List;

/**
 * An unmodifiable {@link List} which can hold a large number of elements.
 *
 * @param <E> the type of elements in this list
 * 
 * @author JSiebel
 */
public interface LargeUnmodifiableList<T> extends List<T>{
	
	/**
	 * Returns the actual number of elements in this list. The number returned is accurate even if this list contains
	 * more than {@code Integer.MAX_VALUE} elements, unlike the value returned by {@link #size()}.
	 * 
	 * @return the number of elements in this list
	 */
	BigInteger actualSize();
	
	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >= size()})
	 */
	T get(BigInteger index);
	
	/**
	 * Returns a view of the portion of this list between the specified {@code fromIndex}, inclusive, and
	 * {@code toIndex}, exclusive
	 *
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex high endpoint (exclusive) of the subList
	 * @return a view of the specified range within this list
	 * @throws IndexOutOfBoundsException if an endpoint index value is out of range
	 * ({@code fromIndex < 0 || toIndex > size})
	 * @throws IllegalArgumentException if the endpoint indices are out of order ({@code fromIndex > toIndex})
	 */
	LargeUnmodifiableList<T> subList(BigInteger fromIndex, BigInteger toIndex);
}
