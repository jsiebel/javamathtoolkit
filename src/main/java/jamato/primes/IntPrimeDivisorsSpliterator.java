package jamato.primes;

import java.math.BigInteger;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

/**
 * A spliterator that that traverses all int prime divisors of a number. Dividing primes larger than
 * {@link Integer#MAX_VALUE} are not returned.
 *
 * @author JSiebel
 *
 */
class IntPrimeDivisorsSpliterator extends Spliterators.AbstractIntSpliterator{
	
	/** An iterator providing divisor candidate primes. */
	PrimitiveIterator.OfInt iterator = Primes.iterator();
	
	/**
	 * If this is set, this is the remainder of the input number after dividing it by all primes that have been returned
	 * so far. If this is <code>null</code>, the remainder is in the {@link #intRemainder} field instead.
	 */
	BigInteger bigRemainder;
	
	/**
	 * The remainder of the input number after dividing it by all primes that have been returned so far, unless
	 * {@link #bigRemainder} is set.
	 */
	int intRemainder;
	
	int limit;
	
	/**
	 * Creates an IntPrimeDivisorsSpliterator for the given number.
	 *
	 * @param number the number for which the prime divisors are returned; must be positive
	 * @throws IllegalArgumentException if {@code number} is 0 or negative
	 */
	IntPrimeDivisorsSpliterator(BigInteger number){
		super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL);
		if (number.signum() <= 0){
			throw new IllegalArgumentException("Argument must be positive, but was " + number + ".");
		}
		if (sqrtFitsInt(number)){
			this.limit = number.sqrt().intValueExact();
		}else{
			this.limit = Integer.MAX_VALUE;
		}
		this.bigRemainder = number;
	}
	
	/**
	 * Creates an IntPrimeDivisorsSpliterator for the given number.
	 *
	 * @param number the number for which the prime divisors are returned; must be positive
	 * @throws IllegalArgumentException if {@code number} is 0 or negative
	 */
	IntPrimeDivisorsSpliterator(int number){
		super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL);
		if (number <= 0){
			throw new IllegalArgumentException("Argument must be positive, but was " + number + ".");
		}
		this.limit = (int) Math.sqrt(number);
		this.intRemainder = number;
	}
	
	@Override
	public boolean tryAdvance(IntConsumer action){
		Objects.requireNonNull(action);
		if (bigRemainder == null){
			return tryAdvanceInt(action);
		}else if (fitsInt(bigRemainder)){
			intRemainder = bigRemainder.intValueExact();
			bigRemainder = null;
			return tryAdvanceInt(action);
		}else{
			return tryAdvanceBigInteger(action);
		}
	}
	
	
	/**
	 * Find the next prime divisor of the int remainder.
	 *
	 * @param action the action
	 * @return {@code false} if no remaining elements existed upon entry to this method, else {@code true}.
	 */
	private boolean tryAdvanceInt(IntConsumer action){
		if (intRemainder == 1){
			return false;
		}
		int p;
		do{
			p = iterator.nextInt();
		}while (p <= limit && intRemainder % p != 0);
		if (p > limit){
			action.accept(intRemainder);
			intRemainder = 1;
			return true;
		}
		action.accept(p);
		
		boolean even = true;
		do{
			intRemainder /= p;
			even = !even;
			if (even){
				limit /= p;
			}
		}while (intRemainder % p == 0);
		if (!even){
			limit = (int) Math.sqrt(intRemainder);
		}
		return true;
	}
	
	/**
	 * Find the next prime divisor of the BigInteger remainder. The remainder is larger than Integer.MAX_VALUE.
	 *
	 * @param action the action
	 * @return {@code false} if no remaining elements existed upon entry to this method, else {@code true}.
	 */
	private boolean tryAdvanceBigInteger(IntConsumer action){
		BigInteger bigLimit = BigInteger.valueOf(limit);
		int p;
		BigInteger bigP;
		do{
			if (!iterator.hasNext()){
				return false;
			}
			p = iterator.nextInt();
			bigP = BigInteger.valueOf(p);
		}while (bigP.compareTo(bigLimit) <= 0 && bigRemainder.mod(bigP).signum() != 0);
		
		if (bigP.compareTo(bigLimit) > 0){
			// The remainder is a non-int prime, so there are no more int divisors.
			return false;
		}
		action.accept(p);
		do{
			bigRemainder = bigRemainder.divide(bigP);
		}while (bigRemainder.mod(bigP).signum() == 0);
		
		if (sqrtFitsInt(bigRemainder)){
			limit = bigRemainder.sqrt().intValueExact();
		}
		return true;
	}
	
	private static boolean sqrtFitsInt(BigInteger number){
		return number.bitLength() <= 2 * (Integer.SIZE - 1);
	}
	
	private static boolean fitsInt(BigInteger number){
		return number.bitLength() <= Integer.SIZE - 1;
	}
}