package jamato.primes;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

/**
 * A {@link Spliterator} that yields prime numbers using a cache. Primes in the cache are read from it, and newly
 * calculated primes are added to the cache.
 *
 * @author JSiebel
 *
 */
class PrimeCacheSpliterator extends Spliterators.AbstractIntSpliterator{
	
	private final PrimeCache cache;
	
	private int nextIndex;
	
	private final int endIndex;
	
	/**
	 * Creates a new instance.
	 *
	 * @param cache the cache to be filled
	 * @param startIndex the index of the first prime to be returned
	 * @param endIndex the index of the last prime (exclusive)
	 */
	public PrimeCacheSpliterator(PrimeCache cache, int startIndex, int endIndex){
		super(endIndex - startIndex,
				Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.SIZED);
		if (startIndex > cache.size()){
			throw new IllegalArgumentException();
		}
		this.cache = cache;
		this.nextIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	@Override
	public boolean tryAdvance(IntConsumer action){
		Objects.requireNonNull(action);
		if (nextIndex == endIndex){
			return false;
		}
		int next;
		if (nextIndex < cache.size()){
			next = cache.get(nextIndex);
		}else{
			next = Primes.calculateNextPrimeAfter(cache.max());
			cache.add(next);
		}
		nextIndex++;
		action.accept(next);
		return true;
	}
}