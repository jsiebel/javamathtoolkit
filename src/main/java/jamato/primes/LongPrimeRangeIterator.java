package jamato.primes;

import java.util.PrimitiveIterator;

/**
 * An {@link Iterator} that yields prime numbers in a given range.
 *
 * @author JSiebel
 *
 */
class LongPrimeRangeIterator implements PrimitiveIterator.OfLong{
	
	private long next;
	
	private boolean hasNext;
	
	private final long upperBoundInclusive;
	
	private final OfInt intPrimeIterator;
	
	/**
	 * Creates a new instance.
	 *
	 * @param lowerBound the lower bound, the first prime returned is the least prime greater than or equal to this
	 * value
	 * @param upperBoundInclusive the upper bound, the last prime returned is the greatest prime less than or equal to
	 * this value
	 */
	public LongPrimeRangeIterator(long lowerBound, long upperBoundInclusive){
		this.upperBoundInclusive = upperBoundInclusive;
		if (lowerBound <= Integer.MAX_VALUE){
			intPrimeIterator = Primes.rangeIterator((int) lowerBound);
			this.next = intPrimeIterator.next();
		}else{
			intPrimeIterator = Primes.rangeIterator(0, 0);
			this.next = LongPrimes.calculateNextPrimeAfter(lowerBound - 1);
		}
		this.hasNext = next <= upperBoundInclusive;
	}
	
	@Override
	public boolean hasNext(){
		return hasNext;
	}
	
	@Override
	public long nextLong(){
		long result = next;
		if (next == LongPrimes.GREATEST_LONG_PRIME){
			hasNext = false;
		}else if (intPrimeIterator.hasNext()){
			next = intPrimeIterator.next();
			hasNext = next <= upperBoundInclusive;
		}else{
			next = LongPrimes.calculateNextPrimeAfter(next);
			hasNext = next <= upperBoundInclusive;
		}
		return result;
	}
}