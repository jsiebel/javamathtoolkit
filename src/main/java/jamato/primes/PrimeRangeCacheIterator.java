package jamato.primes;

import java.util.PrimitiveIterator;

/**
 * An {@link Iterator} that returns prime numbers using a cache. Primes in the cache are read from it, and newly
 * calculated primes are added to the cache.
 *
 * @author JSiebel
 *
 */
class PrimeRangeCacheIterator implements PrimitiveIterator.OfInt{
	
	private final PrimeCache cache;
	
	private int nextIndex;
	
	private final int upperBoundInclusive;
	
	/**
	 * Creates a new instance.
	 *
	 * @param cache the cache to be filled
	 * @param lowerBound the lower bound, the first prime it the lowest prime greater than or equal to this value. May
	 * not exceed the greatest value in the cache.
	 * @param upperBoundInclusive the upper bound, the last prime returned is the greatest prime lower than or equal to
	 * this value
	 */
	public PrimeRangeCacheIterator(PrimeCache cache, int lowerBound, int upperBoundInclusive){
		this.cache = cache;
		this.nextIndex = cache.getInsertionIndex(lowerBound);
		this.upperBoundInclusive = upperBoundInclusive;
		if (nextIndex >= cache.size()){
			throw new IllegalArgumentException("Lower bound is not in the cache: " + lowerBound);
		}
	}
	
	@Override
	public boolean hasNext(){
		return cache.get(nextIndex) <= upperBoundInclusive;
	}
	
	@Override
	public int nextInt(){
		int result = cache.get(nextIndex++);
		if (nextIndex == cache.size() && nextIndex < Primes.NUMBER_OF_INT_PRIMES){
			int nextPrime = Primes.calculateNextPrimeAfter(cache.max());
			cache.add(nextPrime);
		}
		return result;
	}
}