package jamato.primes;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfLong;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

/**
 * Provides methods concerning long primes.
 *
 * @author JSiebel
 *
 */
public final class LongPrimes{
	
	/** The greatest prime number that fits in a long. */
	public static final long GREATEST_LONG_PRIME = 9223372036854775783L;
	
	/** The number of primes in the long range (see OEIS sequence A086681). */
	public static final long NUMBER_OF_LONG_PRIMES = 216289611853439384L;
	
	/** The smallest prime number that doesn't fit in an integer. */
	private static final long SMALLEST_NON_INT_PRIME = 2147483659L;
	
	/**
	 * A sieve used for simple composite checks. Using 6 primes to create the sieve leaves 1/2 * 2/3 * 4/5 * 6/7 * 10/11
	 * * 12/13 = 192/1001 ≈ 19 % of all numbers as prime candidates.
	 */
	private static final PrimesSieve SIEVE = new PrimesSieve(2, 3, 5, 7, 11, 13);
	
	private LongPrimes(){
		// no instances
	}
	
	/**
	 * Checks if the given number is a prime. This method uses a deterministic variant of the Miller-Rabin primality
	 * test with parameters taken from <a href="https://miller-rabin.appspot.com">miller-rabin.appspot.com</a>.
	 *
	 * @param number the number to be checked
	 * @return <code>true</code> if the number is prime, <code>false</code> if it is composite
	 */
	public static boolean isPrime(long number){
		if (number < 2){
			return false;
		}else if (number <= Integer.MAX_VALUE){
			return Primes.isPrime((int) number);
		}else{
			return SIEVE.isCandidate(number) && isSievedNumberPrime(number);
		}
	}
	
	/**
	 * Checks if the given number is prime. If the number is divisible by one of the sieve primes, the result is
	 * undefined.
	 *
	 * @param number the number to be checked
	 * @return <code>true</code> if the number is prime
	 */
	private static boolean isSievedNumberPrime(long number){
		if (number < 273919523041L){
			return MillerRabinTest.test(number, 15)
					&& MillerRabinTest.test(number, 7363882082L) && MillerRabinTest.test(number, 992620450144556L);
		}else if (number < 68631876934741L){
			return MillerRabinTest.test(number, 2) && MillerRabinTest.test(number, 2411878016334956535L)
					&& MillerRabinTest.test(number, 6013387904567421573L)
					&& MillerRabinTest.test(number, 5659550249276598413L);
		}else if (number < 7999252175582851L){
			return MillerRabinTest.test(number, 2) && MillerRabinTest.test(number, 24130806001517L)
					&& MillerRabinTest.test(number, 149795463772692060L)
					&& MillerRabinTest.test(number, 186635894390467037L)
					&& MillerRabinTest.test(number, 3967304179347715805L);
		}else if (number < 585226005592931977L){
			return MillerRabinTest.test(number, 2) && MillerRabinTest.test(number, 123635709730000L)
					&& MillerRabinTest.test(number, 9233062284813009L)
					&& MillerRabinTest.test(number, 43835965440333360L)
					&& MillerRabinTest.test(number, 761179012939631437L)
					&& MillerRabinTest.test(number, 1263739024124850375L);
		}else{
			return MillerRabinTest.test(number, 2) && MillerRabinTest.test(number, 325)
					&& MillerRabinTest.test(number, 9375)
					&& MillerRabinTest.test(number, 28178) && MillerRabinTest.test(number, 450775)
					&& MillerRabinTest.test(number, 9780504) && MillerRabinTest.test(number, 1795265022);
		}
	}
	
	/**
	 * Returns an array of all primes that are greater or equal to the given lower bound, and smaller than the given
	 * upper bound.
	 *
	 * @param lowerBound the lower bound for the primes, inclusive
	 * @param upperBound the upper bound for the primes, exclusive
	 * @return an array of prime numbers
	 */
	public static long[] rangeArray(long lowerBound, long upperBound){
		return rangeStream(lowerBound, upperBound).toArray();
	}
	
	/**
	 * A stream of all long primes in order. The stream's size is {@link #NUMBER_OF_LONG_PRIMES}.
	 *
	 * @return a stream of primes.
	 */
	public static LongStream stream(){
		return rangeStream(0);
	}
	
	/**
	 * A stream of primes, beginning with the given lower bound.
	 *
	 * @param lowerBound the lower bound of the stream, the first element is the least prime ≥ this
	 * @return a stream of primes
	 */
	public static LongStream rangeStream(long lowerBound){
		if (lowerBound < Primes.GREATEST_INT_PRIME){
			return LongStream.concat(
					Primes.rangeStream((int) Math.max(lowerBound, 2)).asLongStream(),
					rangeStream(SMALLEST_NON_INT_PRIME));
		}else{
			return SIEVE.stream(lowerBound - 1, GREATEST_LONG_PRIME).filter(LongPrimes::isSievedNumberPrime);
		}
	}
	
	/**
	 * A stream of all primes in the given range.
	 *
	 * @param lowerBound the lower bound of the stream, the first element is the least prime ≥ this
	 * @param lowerBound the upper bound of the stream, exclusive
	 * @return a stream of primes
	 */
	public static LongStream rangeStream(long lowerBound, long upperBound){
		if (upperBound <= Integer.MAX_VALUE){
			return Primes.rangeStream((int) lowerBound, (int) upperBound).asLongStream();
		}else if (lowerBound < Primes.GREATEST_INT_PRIME){
			return LongStream.concat(
					Primes.rangeStream((int) lowerBound).asLongStream(),
					rangeStream(SMALLEST_NON_INT_PRIME, upperBound));
		}else{
			return SIEVE.stream(lowerBound - 1, upperBound - 1).filter(LongPrimes::isSievedNumberPrime);
		}
	}
	
	/**
	 * Returns an iterator over all long primes.
	 *
	 * @return an iterator over all long primes.
	 */
	public static PrimitiveIterator.OfLong iterator(){
		return new LongPrimeRangeIterator(2, GREATEST_LONG_PRIME);
	}
	
	/**
	 * Returns an iterator over the primes starting at the given value.
	 *
	 * @param lowerBound the starting point of the primes, inclusive
	 * @return an iterator over the primes starting at the given value
	 */
	public static PrimitiveIterator.OfLong rangeIterator(long lowerBound){
		return new LongPrimeRangeIterator(lowerBound, GREATEST_LONG_PRIME);
	}
	
	/**
	 * Returns an iterator over all primes in the given range.
	 *
	 * @param lowerBound the starting point of the primes, inclusive
	 * @param upperBound the end point of the primes, exclusive
	 * @return an iterator over all primes in the given range
	 */
	public static PrimitiveIterator.OfLong rangeIterator(long lowerBound, long upperBound){
		return new LongPrimeRangeIterator(lowerBound, upperBound - 1);
	}
	
	/**
	 * Returns an array of all positive divisors of the given number.
	 *
	 * @param number the number to be factorized
	 * @return an unordered array of divisors
	 */
	public static long[] getDivisors(long number){
		long[] result = { 1 };
		OfLong primeIterator = iterator();
		
		int limit = (int) Math.floor(Math.sqrt(number));
		while (number > 1){
			long p = primeIterator.nextLong();
			
			if (p > limit){
				p = number;
			}
			
			if (number % p == 0){
				int exponent = 1;
				number /= p;
				while (number % p == 0){
					exponent++;
					number /= p;
					if ((exponent & 1) == 0){
						limit /= p;
					}
				}
				result = multiplyArrayContents(result, p, result.length * (exponent + 1));
				if ((exponent & 1) != 0){
					limit = (int) Math.floor((limit + 1) / Math.sqrt(p));
				}
			}
		}
		return result;
	}
	
	/**
	 * Creates an array of the given size, and fills it with multiples of the original. The value at index
	 * <code>i*source.length+j</code> is <code>source[j] * factor^i</code>.
	 *
	 * @param source the source array
	 * @param factorBase the factor that the fields are multiplied with
	 * @param newSize the size of the result array
	 * @return the result array
	 */
	private static long[] multiplyArrayContents(long[] source, long factorBase, int newSize){
		long[] result = Arrays.copyOf(source, newSize);
		for (int i = source.length; i < result.length; i++){
			result[i] = result[i - source.length] * factorBase;
		}
		return result;
	}
	
	/**
	 * An array of all prime divisors of the given number.
	 *
	 * @param number a number
	 * @return an array of prime numbers
	 */
	public static long[] getPrimeDivisors(long number){
		return getPrimeDivisorsStream(number).toArray();
	}
	
	/**
	 * An ordered stream of all prime divisors of the given number. Each prime appears at most once, even if the number
	 * is divisible by it multiple times.
	 *
	 * @param number a number
	 * @return an array of prime numbers
	 */
	public static LongStream getPrimeDivisorsStream(long number){
		return StreamSupport.longStream(new LongPrimeDivisorsSpliterator(number), false);
	}
	
	/**
	 * An ordered stream of all prime divisors in the long range of the given number. Each prime appears at most once,
	 * even if the number is divisible by it multiple times.
	 *
	 * @param number a number
	 * @return an array of prime numbers
	 */
	public static LongStream getPrimeDivisorsStream(BigInteger number){
		return StreamSupport.longStream(new LongPrimeDivisorsSpliterator(number), false);
	}
	
	/**
	 * Calculates the smallest prime number that is greater than the given number without using the cache.
	 *
	 * @param number a number, must be smaller than the greatest sieve prime
	 * @return the next prime number
	 */
	static long calculateNextPrimeAfter(long number){
		long candidate = number;
		do{
			candidate = SIEVE.getNextCandidate(candidate);
		}while (!isSievedNumberPrime(candidate));
		return candidate;
	}
}
