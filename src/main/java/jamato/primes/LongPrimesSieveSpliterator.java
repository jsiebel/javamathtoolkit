package jamato.primes;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;

/**
 * A Spliterator for candidate long numbers returned by a prime sieve.
 *
 * @author JSiebel
 *
 */
final class LongPrimesSieveSpliterator extends Spliterators.AbstractLongSpliterator{
	
	/** The prime sieve that defines the next element */
	private final PrimesSieve primeSieve;
	
	/** The previously returned value, all upcoming values are strictly greater than this. */
	private long currentValue;
	
	/** The upper search limit, inclusive. */
	private final long upperBound;
	
	/**
	 * Creates a Spliterator for the given prime sieve which processes all candidates in the given range.
	 *
	 * @param primeSieve a prime sieve
	 * @param startExclusive the start value (exclusive), must not be negative
	 * @param endInclusive the upper bound of the range, inclusive
	 */
	LongPrimesSieveSpliterator(PrimesSieve primeSieve, long startExclusive, long endInclusive){
		super(endInclusive - startExclusive * primeSieve.numberOfCandidates / primeSieve.size,
				Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.IMMUTABLE);
		this.primeSieve = primeSieve;
		this.currentValue = startExclusive;
		this.upperBound = endInclusive;
	}
	
	@Override
	public OfLong trySplit(){
		if (primeSieve.getNextCandidate(currentValue) < upperBound){
			long splitPoint = (currentValue + upperBound) >>> 1;
			LongPrimesSieveSpliterator primesSieveSpliterator = new LongPrimesSieveSpliterator(primeSieve, currentValue,
					splitPoint);
			currentValue = splitPoint;
			return primesSieveSpliterator;
		}else{
			return null;
		}
	}
	
	@Override
	public boolean tryAdvance(LongConsumer action){
		long nextCandidate = primeSieve.getNextCandidate(currentValue);
		if (Long.compareUnsigned(nextCandidate, upperBound) <= 0){
			action.accept(nextCandidate);
			currentValue = nextCandidate;
			return true;
		}else{
			return false;
		}
	}
}