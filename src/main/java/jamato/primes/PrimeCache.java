package jamato.primes;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * A cache for int primes.
 *
 * @author JSiebel
 *
 */
class PrimeCache{
	
	/**
	 * An array of primes. The cache can grow when calling methods in this class. It is filled up to index
	 * {@link #cacheSize} (exclusive) and may contain zeros in the end.
	 */
	private int[] cache = { 2, 3, 5, 7, 11, 13, 17, 19 };
	
	/** The number of primes currently cached. */
	private int size = cache.length;
	
	/**
	 * Returns the current size of the cache, i.e. the number of primes stored.
	 *
	 * @return the size of the cache
	 */
	int size(){
		return size;
	}
	
	/**
	 * The greatest prime currently stored.
	 *
	 * @return the greatest prime in the cache
	 */
	int max(){
		return cache[size - 1];
	}
	
	/**
	 * Adds the next prime number to the cache. The caller must make sure it's the correct one.
	 * 
	 * @param prime the next prime number
	 */
	void add(int prime){
		if (size == cache.length){
			grow();
		}
		cache[size++] = prime;
	}
	
	/**
	 * Adds the next prime number to the cache without checking its size. The caller must make sure the number fits (by
	 * calling {@link #growTo(int)}), and that it's the correct number.
	 * 
	 * @param prime the next prime number
	 */
	void addUnchecked(int prime){
		cache[size++] = prime;
	}
	
	/**
	 * Replaces the cache array by a bigger one, up to a maximum size of <code>Primes.NUMBER_OF_INT_PRIMES</code>.
	 */
	private void grow(){
		if (cache.length < Primes.NUMBER_OF_INT_PRIMES / 2){
			cache = Arrays.copyOf(cache, 2 * cache.length);
		}else if (cache.length < Primes.NUMBER_OF_INT_PRIMES){
			cache = Arrays.copyOf(cache, Primes.NUMBER_OF_INT_PRIMES);
		}else{
			// the cache is already big enough to hold all int primes
		}
	}
	
	/**
	 * Replaces the cache array by a bigger one, up to a maximum size of <code>Primes.NUMBER_OF_INT_PRIMES</code>.
	 */
	void growTo(int minimumSize){
		if (minimumSize < Primes.NUMBER_OF_INT_PRIMES){
			while (minimumSize > cache.length){
				grow();
			}
		}
	}
	
	/**
	 * Returns <code>true</code> if the cache contains the given number, <code>false</code> otherwise.
	 *
	 * @param number a number
	 * @return <code>true</code> if the cache contains the number
	 */
	boolean contains(int number){
		return Arrays.binarySearch(cache, 0, size, number) >= 0;
	}
	
	/**
	 * Returns the number at the given index.
	 *
	 * @param index the index of the number
	 * @return the number in the cache
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range (<code> index < 0 || index >= size()</code>)
	 */
	public int get(int index){
		return cache[index];
	}
	
	/**
	 * Returns a copy of a part of the cache.
	 *
	 * @param fromIndex the index of the lowest prime returned (inclusive)
	 * @param toIndex the index after the last prime returned (exclusive)
	 * @return an array of cache entries
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 * (<code> fromIndex < 0 || toIndex >= size()</code>)
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 */
	int[] getArray(int fromIndex, int toIndex){
		return Arrays.copyOfRange(cache, fromIndex, toIndex);
	}
	
	/**
	 * Returns a stream of cache elements.
	 *
	 * @param fromIndex the index of the lowest prime returned (inclusive)
	 * @param toIndex the index after the last prime returned (exclusive)
	 * @return a stream of cache elements
	 */
	IntStream stream(int fromIndex, int toIndex){
		return Arrays.stream(cache, fromIndex, toIndex);
	}
	
	/**
	 * Returns the index of the given number in the cache, if the cache contains it; or the index at which it would be
	 * inserted otherwise.
	 *
	 * @param number a number
	 * @return the number's insertion index
	 */
	int getInsertionIndex(int number){
		int index = Arrays.binarySearch(cache, 0, size, number);
		if (index < 0){
			return -index - 1;
		}else{
			return index;
		}
	}
}
