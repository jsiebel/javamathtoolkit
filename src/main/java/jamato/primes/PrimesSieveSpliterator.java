package jamato.primes;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

/**
 * A Spliterator for candidate numbers returned by a prime sieve.
 *
 * @author JSiebel
 *
 */
final class PrimesSieveSpliterator extends Spliterators.AbstractIntSpliterator{
	
	/** The prime sieve that defines the next element */
	private PrimesSieve primeSieve;
	
	/** The previously returned value, all upcoming values are strictly greater than this. */
	private int currentValue;
	
	/** The upper search limit, inclusive. */
	private int upperBound;
	
	/**
	 * Creates a Spliterator for the given prime sieve which processes all candidates in the given range.
	 *
	 * @param primeSieve a prime sieve
	 * @param startExclusive the start value (exclusive), must not be negative
	 * @param endInclusive the upper bound of the range, inclusive
	 */
	PrimesSieveSpliterator(PrimesSieve primeSieve, int startExclusive, int endInclusive){
		super((long) (endInclusive - startExclusive) * primeSieve.numberOfCandidates / primeSieve.size,
				Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.IMMUTABLE);
		this.primeSieve = primeSieve;
		this.currentValue = startExclusive;
		this.upperBound = endInclusive;
	}
	
	@Override
	public OfInt trySplit(){
		if (primeSieve.getNextCandidate(currentValue) < upperBound){
			int splitPoint = (currentValue + upperBound) >>> 1;
			PrimesSieveSpliterator primesSieveSpliterator = new PrimesSieveSpliterator(primeSieve, currentValue,
					splitPoint);
			currentValue = splitPoint;
			return primesSieveSpliterator;
		}else{
			return null;
		}
	}
	
	@Override
	public boolean tryAdvance(IntConsumer action){
		int nextCandidate = primeSieve.getNextCandidate(currentValue);
		if (Integer.compareUnsigned(nextCandidate, upperBound) <= 0){
			action.accept(nextCandidate);
			currentValue = nextCandidate;
			return true;
		}else{
			return false;
		}
	}
}