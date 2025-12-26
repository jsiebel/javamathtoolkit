package jamato.primes;

import java.util.PrimitiveIterator;

/**
 * An {@link Iterator} that yields prime numbers from a cache while filling it when calculating new primes.
 *
 * @author JSiebel
 *
 */
class PrimeCacheIterator implements PrimitiveIterator.OfInt{
	
	private final PrimeCache cache;
	
	private int nextIndex;
	
	private final int toIndex;
	
	/**
	 * Creates a new instance.
	 *
	 * @param cache the cache to be filled
	 * @param startIndex the index of the first prime to be returned
	 * @param endIndex the index of the last prime (exclusive)
	 */
	public PrimeCacheIterator(PrimeCache cache, int fromIndex, int toIndex){
		this.cache = cache;
		this.nextIndex = fromIndex;
		this.toIndex = toIndex;
	}
	
	@Override
	public int nextInt(){
		if (nextIndex < cache.size()){
			return cache.get(nextIndex++);
		}else{
			int nextPrime = Primes.calculateNextPrimeAfter(cache.max());
			cache.add(nextPrime);
			nextIndex++;
			return nextPrime;
		}
	}
	
	@Override
	public boolean hasNext(){
		return nextIndex < toIndex;
	}
}