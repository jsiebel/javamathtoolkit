package jamato.primes;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

/**
 * A {@link Spliterator} that yields primes in the given range.
 *
 * @author JSiebel
 *
 */
class PrimeSpliterator extends Spliterators.AbstractIntSpliterator{
	
	/**
	 * The minimum size of a stream returned by {@link #trySplit}. The value is chosen that the stream returned always
	 * contains at least on sieve candidate.
	 */
	private static final int SPLIT_LIMIT = 25;
	
	private final int upperBoundInclusive;
	
	private int previous;
	
	/**
	 * Creates a new PrimeSpliterator that yields primes in the given range.
	 *
	 * @param lowerBound the lower bound, the first prime it the lowest prime greater than or equal to this value
	 * @param upperBoundInclusive the upper bound, the last prime is the greatest prime lower than or equal to this
	 * value
	 */
	public PrimeSpliterator(int lowerBound, int upperBoundInclusive){
		super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL);
		this.upperBoundInclusive = upperBoundInclusive;
		previous = lowerBound - 1;
	}
	
	@Override
	public boolean tryAdvance(IntConsumer action){
		Objects.requireNonNull(action);
		if (previous < upperBoundInclusive){
			int next = Primes.calculateNextPrimeAfter(previous);
			if (next <= upperBoundInclusive){
				action.accept(next);
				previous = next;
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	@Override
	public OfInt trySplit(){
		int splitSize = (upperBoundInclusive - previous) / 2;
		if (splitSize < SPLIT_LIMIT){
			return null;
		}else{
			int low = previous;
			int mid = previous + splitSize;
			previous = mid;
			return new PrimeSpliterator(low, mid);
		}
	}
}