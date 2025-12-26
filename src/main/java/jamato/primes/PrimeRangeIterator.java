package jamato.primes;

import java.util.PrimitiveIterator;

/**
 * An {@link Iterator} that yields prime numbers in a given range.
 *
 * @author JSiebel
 *
 */
class PrimeRangeIterator implements PrimitiveIterator.OfInt{
	
	private int next;
	
	private boolean hasNext;
	
	private final int upperBoundInclusive;
	
	/**
	 * Creates a new instance.
	 *
	 * @param lowerBound the lower bound, the first prime returned is the least prime greater than or equal to this
	 * value
	 * @param upperBoundInclusive the upper bound, the last prime returned is the greatest prime less than or equal to
	 * this value
	 */
	public PrimeRangeIterator(int lowerBound, int upperBoundInclusive){
		this.next = Primes.calculateNextPrimeAfter(lowerBound - 1);
		this.hasNext = next <= upperBoundInclusive;
		this.upperBoundInclusive = upperBoundInclusive;
	}
	
	@Override
	public boolean hasNext(){
		return hasNext;
	}
	
	@Override
	public int nextInt(){
		int result = next;
		if (next == Primes.GREATEST_INT_PRIME){
			hasNext = false;
		}else{
			next = Primes.calculateNextPrimeAfter(next);
			hasNext = next <= upperBoundInclusive;
		}
		return result;
	}
}