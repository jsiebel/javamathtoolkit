package jamato.primes;

import java.util.Arrays;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfInt;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import jamato.algebra.Exponentiation;

/**
 * Provides methods concerning primes in the integer range.
 *
 * @author JSiebel
 *
 */
public final class Primes{
	
	/** The greatest prime number that fits in an integer, <code>2^31-1</code>. */
	public static final int GREATEST_INT_PRIME = 2147483647;
	
	/** The number of primes in the integer range. */
	public static final int NUMBER_OF_INT_PRIMES = 105097565;
	
	/** The maximum distance between two primes in the integer range (1453168141 and 1453168947). */
	private static final int GREATEST_PRIME_GAP = 806;
	
	private static PrimeCache cache = new PrimeCache();
	
	/**
	 * A sieve used for simple composite checks. Using 6 primes to create the sieve leaves 1/2 * 2/3 * 4/5 * 6/7 * 10/11
	 * * 12/13 = 192/1001 ≈ 19 % of all numbers as prime candidates.
	 */
	private static final PrimesSieve SIEVE = new PrimesSieve(2, 3, 5, 7, 11, 13);
	
	private Primes(){
		// no instances
	}
	
	/**
	 * Checks if the given number is a prime.
	 *
	 * @param number the number to be checked
	 * @return <code>true</code> if the number is prime, <code>false</code> if it is composite
	 */
	public static boolean isPrime(int number){
		if (number < cache.max()){
			return cache.contains(number);
		}else{
			return SIEVE.isCandidate(number) && isSievedNumberPrime(number);
		}
	}
	
	/**
	 * Checks if the given number is prime. If the number is divisible by one of the sieve primes, the result is
	 * undefined.
	 *
	 * @param n the number to be checked
	 * @return <code>true</code> if the number is prime
	 */
	private static boolean isSievedNumberPrime(int n){
		if (n < 2047){
			return millerRabinTest(n, 2);
		}else if (n < 9080191){
			return millerRabinTest(n, 31) && millerRabinTest(n, 73);
		}else{
			return millerRabinTest(n, 2) && millerRabinTest(n, 7) && millerRabinTest(n, 61);
		}
	}
	
	/**
	 * Checks if a number is a prime according to the Miller-Rabin test with the given base.
	 *
	 * @param n the number to be checked
	 * @param base the base used for the check
	 * @return <code>true</code> if the number is a prime or a pseudo-prime, <code>false</code> otherwise
	 * @see "https://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test"
	 */
	private static boolean millerRabinTest(int n, int base){
		int s = Integer.numberOfTrailingZeros(n - 1);
		int d = (n - 1) >>> s;
		
		int p = Exponentiation.powMod(base, d, n);
		if (p == 1 || p == n - 1){
			// base ^ d ≡ 1 or base ^ d ≡ -1 (mod n)
			return true;
		}else{
			for (int r = 1; r < s; r++){
				p = (int) ((long) p * p % n);
				if (p == n - 1){
					// base ^ (2^r * d) ≡ -1 (mod n)
					return true;
				}
			}
			// base ^ (2^s * d) = base ^ (n-1) ≢ 1
			return false;
		}
	}
	
	/**
	 * Returns an array of primes, starting from the given lower index (inclusive), and ending with the given upper
	 * index (exclusive).
	 *
	 * @param fromIndex the index of the lowest prime returned
	 * @param toIndex the index after the last prime returned
	 * @return an array of prime numbers
	 */
	public static int[] array(int fromIndex, int toIndex){
		rangeCheck(fromIndex, toIndex);
		fillCacheToIndex(toIndex);
		return cache.getArray(fromIndex, toIndex);
	}
	
	/**
	 * Returns an array of all primes that are greater or equal to the given lower bound, and smaller than the given
	 * upper bound.
	 *
	 * @param lowerBound the lower bound for the primes, inclusive
	 * @param upperBound the upper bound for the primes, exclusive
	 * @return an array of prime numbers
	 */
	public static int[] rangeArray(int lowerBound, int upperBound){
		if (lowerBound > upperBound){
			throw new IllegalArgumentException("lowerBound > upperBound");
		}
		if (lowerBound < 0){
			lowerBound = 0;
		}
		int highestPrime = cache.max();
		if (lowerBound <= highestPrime + GREATEST_PRIME_GAP){
			// The distance is chosen so that when running the method with a lower bound equal to the greatest prime in
			// the cache, the cache is still filled.
			fillCacheToLimit(upperBound);
			int lowerBoundIndex = cache.getInsertionIndex(lowerBound);
			int upperBoundIndex = cache.getInsertionIndex(upperBound);
			return cache.getArray(lowerBoundIndex, upperBoundIndex);
		}else{
			// Using the cache would require calculating a lot of primes that are not returned, so don't use the cache.
			int next = lowerBound - 1;
			int nextIndex = 0;
			int[] result = new int[10];
			while (next < upperBound){
				if (nextIndex == result.length){
					result = Arrays.copyOf(result, result.length * 2);
				}
				next = calculateNextPrimeAfter(next);
				result[nextIndex++] = next;
			}
			return Arrays.copyOf(result, nextIndex - 1);
		}
	}
	
	/**
	 * A stream of all int primes in order. The stream's size is {@link #NUMBER_OF_INT_PRIMES}.
	 *
	 * @return a stream of primes.
	 */
	public static IntStream stream(){
		return cacheFillingStream(0, NUMBER_OF_INT_PRIMES);
	}
	
	/**
	 * A stream of int primes in order, starting at the given index. The stream's size is
	 * <code>({@link #NUMBER_OF_INT_PRIMES} - fromIndex)</code>.
	 *
	 * @return a stream of primes.
	 */
	public static IntStream stream(int fromIndex){
		rangeCheck(fromIndex);
		fillCacheToIndex(fromIndex);
		return cacheFillingStream(fromIndex, NUMBER_OF_INT_PRIMES);
	}
	
	/**
	 * A stream of int primes in order, starting at the given start index, and ending before the given end index.
	 *
	 * @return a stream of primes.
	 */
	public static IntStream stream(int fromIndex, int toIndex){
		rangeCheck(fromIndex, toIndex);
		if (toIndex <= cache.size()){
			return cache.stream(fromIndex, toIndex);
		}else{
			fillCacheToIndex(fromIndex);
			return cacheFillingStream(fromIndex, toIndex);
		}
	}
	
	/**
	 * A stream of primes, beginning with the given lower bound.
	 *
	 * @param lowerBound the lower bound of the stream, the first element is the least prime ≥ this
	 * @return a stream of primes
	 */
	public static IntStream rangeStream(int lowerBound){
		if (lowerBound < cache.max()){
			return stream(cache.getInsertionIndex(lowerBound));
		}else{
			return StreamSupport.intStream(new PrimeSpliterator(lowerBound, GREATEST_INT_PRIME), true);
		}
	}
	
	public static IntStream rangeStream(int lowerBound, int upperBound){
		if (upperBound < cache.max()){
			int lowerBoundIndex = cache.getInsertionIndex(lowerBound);
			int upperBoundIndex = cache.getInsertionIndex(upperBound);
			return cache.stream(lowerBoundIndex, upperBoundIndex);
		}else if (lowerBound < cache.max()){
			int lowerBoundIndex = cache.getInsertionIndex(lowerBound);
			return cacheFillingStream(lowerBoundIndex, NUMBER_OF_INT_PRIMES).takeWhile(n -> n < upperBound);
		}else{
			return StreamSupport.intStream(new PrimeSpliterator(lowerBound, upperBound - 1), true);
		}
	}
	
	/**
	 * An ordered stream of the primes, starting at the given index. This stream writes each prime it calculates to the
	 * cache.
	 * 
	 * @return an ordered stream of primes
	 */
	private static IntStream cacheFillingStream(int startIndex, int endIndex){
		return StreamSupport.intStream(new PrimeCacheSpliterator(cache, startIndex, endIndex), false);
	}
	
	public static PrimitiveIterator.OfInt iterator(){
		return new PrimeRangeCacheIterator(cache, 2, GREATEST_INT_PRIME);
	}
	
	public static PrimitiveIterator.OfInt iterator(int fromIndex){
		rangeCheck(fromIndex);
		fillCacheToIndex(fromIndex);
		return new PrimeCacheIterator(cache, fromIndex, NUMBER_OF_INT_PRIMES);
	}
	
	public static PrimitiveIterator.OfInt iterator(int fromIndex, int toIndex){
		rangeCheck(fromIndex, toIndex);
		fillCacheToIndex(fromIndex);
		return new PrimeCacheIterator(cache, fromIndex, toIndex);
	}
	
	public static PrimitiveIterator.OfInt rangeIterator(int lowerBound){
		if (cache.getInsertionIndex(lowerBound) < cache.size()){
			return new PrimeRangeCacheIterator(cache, lowerBound, GREATEST_INT_PRIME);
		}else{
			return new PrimeRangeIterator(lowerBound, GREATEST_INT_PRIME);
		}
	}
	
	public static PrimitiveIterator.OfInt rangeIterator(int lowerBound, int upperBound){
		if (cache.getInsertionIndex(lowerBound) < cache.size()){
			return new PrimeRangeCacheIterator(cache, lowerBound, upperBound - 1);
		}else{
			return new PrimeRangeIterator(lowerBound, upperBound - 1);
		}
	}
	
	public static int[] getDivisors(int n){
		int[] result = { 1 };
		OfInt primeIterator = iterator();
		
		int limit = (int) Math.floor(Math.sqrt(n));
		while (n > 1){
			int p = primeIterator.nextInt();
			
			if (p > limit){
				p = n;
			}
			
			if (n % p == 0){
				int exponent = 1;
				n /= p;
				while (n % p == 0){
					exponent++;
					n /= p;
					if (exponent % 2 == 0){
						limit /= p;
					}
				}
				result = multiplyArrayContents(result, p, result.length * (exponent + 1));
				if (exponent % 2 != 0){
					limit = (int) Math.floor((limit + 1) / Math.sqrt(p));
				}
			}
		}
		return result;
	}
	
	/**
	 * An array of all prime divisors of the given number.
	 *
	 * @param number a number
	 * @return an array of prime numbers
	 */
	public static int[] getPrimeDivisors(int number){
		return getPrimeDivisorsStream(number).toArray();
	}
	
	/**
	 * An ordered stream of all prime divisors of the given number. Each prime appears at most once, even if the number
	 * is divisible by it multiple times.
	 *
	 * @param number a number
	 * @return an array of prime numbers
	 */
	public static IntStream getPrimeDivisorsStream(int number){
		return StreamSupport.intStream(
				new Spliterators.AbstractIntSpliterator(Long.MAX_VALUE,
						Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL){
					
					PrimitiveIterator.OfInt iterator = iterator();
					
					int limit = (int) Math.sqrt(number);
					
					int remainder = number;
					
					@Override
					public boolean tryAdvance(IntConsumer action){
						Objects.requireNonNull(action);
						if (remainder <= 1){
							return false;
						}
						int p;
						do{
							p = iterator.nextInt();
							if (p > limit){
								p = remainder;
								break;
							}
						}while (remainder % p != 0);
						
						action.accept(p);
						
						boolean even = true;
						do{
							remainder /= p;
							even = !even;
							if (even){
								limit /= p;
							}
						}while (remainder % p == 0);
						if (!even){
							limit = (int) ((limit + 1) / Math.sqrt(p));
						}
						return true;
					}
				},
				false);
	}
	
	/**
	 * Creates an array of the given size, and fills it with multiples of the original. The value at index
	 * <code>i*source.length+j</code> is <code>source[j] * factor^i</code>.
	 *
	 * @param source the source array
	 * @param factorBase the factor that the fields are multiplied with
	 * @param newSize the size of the result array
	 * @return the result array
	 */
	private static int[] multiplyArrayContents(int[] source, int factorBase, int newSize){
		int[] result = Arrays.copyOf(source, newSize);
		for (int i = source.length; i < result.length; i++){
			result[i] = result[i - source.length] * factorBase;
		}
		return result;
	}
	
	/**
	 * Checks if the given number is a valid prime index.
	 *
	 * @param index the lower index bound, inclusive
	 * @throws IndexOutOfBoundsException if <code>index < 0</code> or <code>index ≥ {@link #NUMBER_OF_INT_PRIMES}</code>
	 */
	private static void rangeCheck(int index){
		if (index < 0 || index >= NUMBER_OF_INT_PRIMES){
			throw new IndexOutOfBoundsException("fromIndex = " + index);
		}
	}
	
	/**
	 * Checks if the given range is valid for integer primes.
	 *
	 * @param fromIndex the lower index bound, inclusive
	 * @param toIndex the upper index bound, exclusive
	 * @throws IllegalArgumentException if <code>fromIndex > toIndex</code>
	 * @throws IndexOutOfBoundsException if <code>fromIndex < 0</code> or
	 * <code>toIndex > {@link #NUMBER_OF_INT_PRIMES}</code>
	 */
	private static void rangeCheck(int fromIndex, int toIndex){
		if (fromIndex > toIndex){
			throw new IllegalArgumentException("fromIndex > toIndex");
		}else if (fromIndex < 0){
			throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
		}else if (toIndex > NUMBER_OF_INT_PRIMES){
			throw new IndexOutOfBoundsException("toIndex = " + toIndex);
		}
	}
	
	/**
	 * Calculates the smallest prime number that is greater than the given number without using the cache.
	 *
	 * @param number a number, must not be smaller than the greatest sieve prime
	 * @return the next prime number
	 */
	static int calculateNextPrimeAfter(int number){
		int candidate = number;
		do{
			candidate = SIEVE.getNextCandidate(candidate);
		}while (!isSievedNumberPrime(candidate));
		return candidate;
	}
	
	/**
	 * Fills the cache so it contains all the primes up to the given index (exclusive). The cache may contain more
	 * entries.
	 * 
	 * @param index the index up to which the array is filled, must not be greater than {@link #NUMBER_OF_INT_PRIMES}
	 */
	private static void fillCacheToIndex(int index){
		cache.growTo(index);
		int nextPrime = cache.max();
		while (cache.size() < index){
			nextPrime = calculateNextPrimeAfter(nextPrime);
			cache.addUnchecked(nextPrime);
		}
	}
	
	/**
	 * Fills the cache so it contains all the primes up to the given number (exclusive). The cache may contain more
	 * entries.
	 * 
	 * @param index the index up to which the array is filled, must not be greater than {@link #NUMBER_OF_INT_PRIMES}
	 */
	private static void fillCacheToLimit(int upperBound){
		int highestPrime = cache.max();
		while (highestPrime < upperBound){
			highestPrime = addNextCacheEntry();
		}
	}
	
	/**
	 * Resets the cache and frees up the memory it uses.
	 */
	public static void resetCache(){
		cache = new PrimeCache();
	}
	
	/**
	 * Increases the size of the cache and adds the next entry. The cache must not be full (i.e.
	 * <code>cache.getSize() < NUMBER_OF_INT_PRIMES</code>.
	 *
	 * @return the added prime
	 */
	static int addNextCacheEntry(){
		int nextPrime = calculateNextPrimeAfter(cache.max());
		cache.add(nextPrime);
		return nextPrime;
	}
}
