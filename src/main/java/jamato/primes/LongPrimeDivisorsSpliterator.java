package jamato.primes;

import java.math.BigInteger;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;

/**
 * A spliterator that that traverses all long prime divisors of a number. Dividing primes larger than
 * {@link Long#MAX_VALUE} are not returned.
 *
 * @author JSiebel
 *
 */
class LongPrimeDivisorsSpliterator extends Spliterators.AbstractLongSpliterator{
	
	/** An iterator providing divisor candidate primes. */
	PrimitiveIterator.OfLong iterator = LongPrimes.iterator();
	
	/**
	 * If this is set, this is the remainder of the input number after dividing it by all primes that have been returned
	 * so far. If this is <code>null</code>, the remainder is in the {@link #longRemainder} field instead.
	 */
	BigInteger bigRemainder;
	
	/**
	 * The remainder of the input number after dividing it by all primes that have been returned so far, unless
	 * {@link #bigRemainder} is set.
	 */
	long longRemainder;
	
	long limit;
	
	/**
	 * Creates a LongPrimeDivisorsSpliterator for the given number.
	 *
	 * @param number the number for which the prime divisors are returned; must be positive
	 * @throws IllegalArgumentException if {@code number} is 0 or negative
	 */
	LongPrimeDivisorsSpliterator(BigInteger number){
		super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL);
		if (number.signum() <= 0){
			throw new IllegalArgumentException("Argument must be positive, but was " + number + ".");
		}
		if (sqrtFitsLong(number)){
			this.limit = number.sqrt().longValueExact();
		}else{
			this.limit = Integer.MAX_VALUE;
		}
		this.bigRemainder = number;
	}
	
	/**
	 * Creates a LongPrimeDivisorsSpliterator for the given number.
	 *
	 * @param number the number for which the prime divisors are returned; must be positive
	 * @throws IllegalArgumentException if {@code number} is 0 or negative
	 */
	LongPrimeDivisorsSpliterator(long number){
		super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL);
		if (number <= 0){
			throw new IllegalArgumentException("Argument must be positive, but was " + number + ".");
		}
		this.limit = (long) Math.sqrt(number);
		this.longRemainder = number;
	}
	
	@Override
	public boolean tryAdvance(LongConsumer action){
		Objects.requireNonNull(action);
		if (bigRemainder == null){
			return tryAdvanceLong(action);
		}else if (fitsLong(bigRemainder)){
			longRemainder = bigRemainder.longValueExact();
			bigRemainder = null;
			return tryAdvanceLong(action);
		}else{
			return tryAdvanceBigInteger(action);
		}
	}
	
	/**
	 * Find the next prime divisor of the long remainder.
	 *
	 * @param action the action
	 * @return {@code false} if no remaining elements existed upon entry to this method, else {@code true}.
	 */
	private boolean tryAdvanceLong(LongConsumer action){
		if (longRemainder == 1){
			return false;
		}
		long p;
		do{
			p = iterator.nextLong();
		}while (p <= limit && longRemainder % p != 0);
		if (p > limit){
			action.accept(longRemainder);
			longRemainder = 1;
			return true;
		}
		action.accept(p);
		
		boolean even = true;
		do{
			longRemainder /= p;
			even = !even;
			if (even){
				limit /= p;
			}
		}while (longRemainder % p == 0);
		if (!even){
			limit = (long) Math.sqrt(longRemainder);
		}
		return true;
	}
	
	/**
	 * Find the next prime divisor of the BigInteger remainder. The remainder is larger than Integer.MAX_VALUE.
	 *
	 * @param action the action
	 * @return {@code false} if no remaining elements existed upon entry to this method, else {@code true}.
	 */
	private boolean tryAdvanceBigInteger(LongConsumer action){
		BigInteger bigLimit = BigInteger.valueOf(limit);
		long p;
		BigInteger bigP;
		do{
			if (!iterator.hasNext()){
				return false;
			}
			p = iterator.nextLong();
			bigP = BigInteger.valueOf(p);
		}while (bigP.compareTo(bigLimit) <= 0 && bigRemainder.mod(bigP).signum() != 0);
		
		if (bigP.compareTo(bigLimit) > 0){
			// The remainder is a non-long prime, so there are no more long divisors.
			return false;
		}
		action.accept(p);
		do{
			bigRemainder = bigRemainder.divide(bigP);
		}while (bigRemainder.mod(bigP).signum() == 0);
		
		if (sqrtFitsLong(bigRemainder)){
			limit = bigRemainder.sqrt().longValueExact();
		}
		return true;
	}
	
	private static boolean sqrtFitsLong(BigInteger number){
		return number.bitLength() <= 2 * (Long.SIZE - 1);
	}
	
	private static boolean fitsLong(BigInteger number){
		return number.bitLength() <= Long.SIZE - 1;
	}
}