package jamato.combinatorics;

import static java.math.BigInteger.ONE;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import jamato.algebra.GCD;
import jamato.algebra.Ring;

/**
 * Provides functions to calculate the binomial coefficient. The binomial coefficient is the number of k-element subsets
 * of an n-element set. It also appears in the extended form of <code>(a^b)^n</code>.
 * 
 * @author JSiebel
 *
 */
public class BinomialCoefficient {
	
	/**
	 * Hidden constructor.
	 */
	protected BinomialCoefficient() {
	}
	
	/**
	 * Calculates the binomial coefficient for the given n and k parameters. The value is
	 * <code>n⋅(n-1)⋅...⋅(n-k+1) / k!</code>, or <code>0</code> if k is negative. Negative n arguments are allowed. If
	 * the binomial coefficient exceeds the <code>int</code> range (which is possible for n ≥ 34 or n ≤ -3), incorrect
	 * results may be returned.
	 * 
	 * @param n the n parameter
	 * @param k the k parameter
	 * @return the binomial coefficient <code>binom(n, k)</code>
	 */
	public static int binom(int n, int k) {
		if (k < 0) {
			return 0;
		} else if (n < 0) {
			if (-k > n) {
				return calculate(n, k);
			} else if ((k & 1) == (n & 1)) {
				return -calculate(-1 - k, -1 - n);
			} else {
				return calculate(-1 - k, -1 - n);
			}
		} else {
			if (k <= n >> 1) {
				return calculate(n, k);
			} else if (k <= n) {
				return calculate(n, n - k);
			} else {
				return 0;
			}
		}
	}
	
	/**
	 * Calculates the binomial coefficient without any special case handling.
	 * 
	 * @param n the n parameter
	 * @param k the k parameter
	 * @return the binomial coefficient <code>binom(n, k)</code>
	 */
	private static int calculate(int n, int k) {
		long result = 1;
		for (int i = 0; i < k; i++) {
			result *= n - i;
			result /= i + 1;
		}
		return (int) result;
	}
	
	/**
	 * Calculates the binomial coefficient for the given n and k parameters. The value is
	 * <code>n⋅(n-1)⋅...⋅(n-k+1) / k!</code>, or <code>0</code> if k is negative. Negative n arguments are allowed. If
	 * the binomial coefficient exceeds the <code>long</code> range (which is possible for n ≥ 67 or n ≤ -3), incorrect
	 * results may be returned.
	 * 
	 * @param n the n parameter
	 * @param k the k parameter
	 * @return the binomial coefficient <code>binom(n, k)</code>
	 */
	public static long binom(long n, long k) {
		if (k < 0) {
			return 0;
		} else if (n < 0) {
			if (-k > n) {
				return calculate(n, k);
			} else if ((k & 1) == (n & 1)) {
				return -calculate(-1 - k, -1 - n);
			} else {
				return calculate(-1 - k, -1 - n);
			}
		} else {
			if (k <= n >> 1) {
				return calculate(n, k);
			} else if (k <= n) {
				return calculate(n, n - k);
			} else {
				return 0;
			}
		}
	}
	
	/**
	 * Calculates the binomial coefficient without any special case handling.
	 * 
	 * @param n the n parameter
	 * @param k the k parameter
	 * @return the binomial coefficient <code>binom(n, k)</code>
	 */
	private static long calculate(long n, long k) {
		long result = 1;
		for (int i = 0; i < k; i++) {
			long numerator = n - i;
			int denominator = i + 1;
			if (Long.numberOfLeadingZeros(result) + Long.numberOfLeadingZeros(numerator) > Long.SIZE) {
				result *= numerator;
				result /= denominator;
			} else {
				long gcd = GCD.of(numerator, denominator);
				result /= denominator / gcd;
				result *= numerator / gcd;
			}
		}
		return result;
	}
	
	/**
	 * Calculates the binomial coefficient for the given n and k parameters. The value is
	 * <code>n⋅(n-1)⋅...⋅(n-k+1) / k!</code>, or <code>0</code> if k is negative. Negative and non-integer n arguments
	 * are allowed. Results may be inaccurate due to floating point arithmetic restrictions or overflows.
	 * 
	 * @param n the n parameter
	 * @param k the k parameter
	 * @return the binomial coefficient <code>binom(n, k)</code>
	 */
	public static double binom(double n, long k) {
		if (k < 0) {
			return 0;
		}
		double result = 1;
		for (int i = 0; i < k; i++) {
			result *= n - i;
			result /= i + 1;
		}
		return result;
	}
	
	/**
	 * Calculates the binomial coefficient for the given n and k parameters. The value is
	 * <code>n⋅(n-1)⋅...⋅(n-k+1) / k!</code>, or <code>0</code> if k is negative. Negative n arguments are allowed.
	 * 
	 * @param n the n parameter
	 * @param k the k parameter
	 * @return the binomial coefficient <code>binom(n, k)</code>
	 */
	public static BigInteger binom(BigInteger n, BigInteger k) {
		if (k.signum() < 0) {
			return BigInteger.ZERO;
		} else if (n.signum() < 0) {
			if (k.negate().compareTo(n) > 0) {
				return calculate(n, k);
			} else if (k.testBit(0) == n.testBit(0)) {
				return calculate(k.negate().subtract(ONE), n.negate().subtract(ONE)).negate();
			} else {
				return calculate(k.negate().subtract(ONE), n.negate().subtract(ONE));
			}
		} else {
			if (k.compareTo(n.shiftRight(1)) <= 0) {
				return calculate(n, k);
			} else if (k.compareTo(n) <= 0) {
				return calculate(n, n.subtract(k));
			} else {
				return BigInteger.ZERO;
			}
		}
	}
	
	/**
	 * Calculates the binomial coefficient without any special case handling.
	 * 
	 * @param n the n parameter
	 * @param k the k parameter
	 * @return the binomial coefficient <code>binom(n, k)</code>
	 */
	private static BigInteger calculate(BigInteger n, BigInteger k) {
		BigInteger result = ONE;
		for (BigInteger numerator = n,
				denominator = ONE; denominator.compareTo(k) <= 0; numerator = numerator
						.subtract(ONE), denominator = denominator.add(ONE)) {
			result = result.multiply(numerator);
			result = result.divide(denominator);
		}
		return result;
	}
	
	/**
	 * Calculates the binomial coefficient for the given n and k parameters. The value is
	 * <code>n⋅(n-1)⋅...⋅(n-k+1) / k!</code>, or <code>0</code> if k is negative.
	 * 
	 * @param n the n parameter
	 * @param k the k parameter
	 * @param one the one element in the ring type
	 * @return the binomial coefficient <code>binom(n, k)</code>
	 */
	public static <T extends Ring<T>> T binom(T n, int k, T one) {
		if (k < 0) {
			return one.subtract(one);
		}
		T result = one;
		T nextN = n;
		for (int i = 1; i <= k; i++) {
			result = result.multiply(nextN);
			result = result.divide(i);
			nextN = nextN.subtract(one);
		}
		return result;
	}
	
	/**
	 * Creates a stream of binomial coefficients for the given n and <code>k=0,1,2,...</code>. The stream ends before
	 * returning the first zero. The stream is finite for n ≥ 0 (containing n+1 elements), and infinite for n < 0.
	 * 
	 * @param n the n parameter
	 * @return a stream of binomial coefficients <code>binom(n, k)</code>
	 */
	public static IntStream binomStream(int n) {
		long[] variables = new long[] { 1, n, 1 }; // Current value, next numerator, next denominator
		return Stream
				.iterate(
						variables,
						p -> p[0] != 0,
						p -> {
							p[0] = p[0] * p[1] / p[2];
							p[1]--;
							p[2]++;
							return p;
						})
				.mapToInt(p -> (int) p[0]);
	}
	
	/**
	 * Creates a stream of binomial coefficients for the given n and <code>k=0,1,2,...</code>. The stream ends before
	 * returning the first zero. The stream is finite for n ≥ 0 (containing n+1 elements), and infinite for n < 0.
	 * 
	 * @param n the n parameter
	 * @return a stream of binomial coefficients <code>binom(n, k)</code>
	 */
	public static LongStream binomStream(long n) {
		long[] variables = new long[] { 1, n, 1 }; // Current value, next numerator, next denominator
		return Stream
				.iterate(
						variables,
						p -> p[0] != 0,
						p -> {
							if (Long.numberOfLeadingZeros(p[0]) + Long.numberOfLeadingZeros(p[1]) > Long.SIZE) {
								p[0] = p[0] * p[1] / p[2];
							} else {
								long gcd = GCD.of(p[1], p[2]);
								p[0] = p[0] / (p[2] / gcd) * (p[1] / gcd);
							}
							p[1]--;
							p[2]++;
							return p;
						})
				.mapToLong(p -> p[0]);
	}
	
	/**
	 * Creates a stream of binomial coefficients for the given n and <code>k=0,1,2,...</code>. The stream ends before
	 * returning the first zero. The stream is finite for integer n ≥ 0 (containing n+1 elements), and infinite
	 * otherwise.
	 * 
	 * @param n the n parameter
	 * @return a stream of binomial coefficients <code>binom(n, k)</code>
	 */
	public static DoubleStream binomStream(double n) {
		double[] variables = new double[] { 1, n, 1 }; // Current value, next numerator, next denominator
		return Stream
				.iterate(
						variables,
						p -> p[0] != 0,
						p -> {
							p[0] = p[0] * p[1] / p[2];
							p[1] = p[1] - 1;
							p[2] = p[2] + 1;
							return p;
						})
				.mapToDouble(p -> p[0]);
	}
	
	/**
	 * Creates a stream of binomial coefficients for the given n and <code>k=0,1,2,...</code>. The stream ends before
	 * returning the first zero. The stream is finite for n ≥ 0 (containing n+1 elements), and infinite for n < 0.
	 * 
	 * @param n the n parameter
	 * @return a stream of binomial coefficients <code>binom(n, k)</code>
	 */
	public static Stream<BigInteger> binomStream(BigInteger n) {
		BigInteger[] variables = new BigInteger[] { ONE, n, ONE }; // Current value, next numerator, next denominator
		return Stream
				.iterate(
						variables,
						p -> p[0].signum() != 0,
						p -> {
							p[0] = p[0].multiply(p[1]).divide(p[2]);
							p[1] = p[1].subtract(ONE);
							p[2] = p[2].add(ONE);
							return p;
						})
				.map(p -> p[0]);
	}
	
	/**
	 * The internal state of the ring binom stream.
	 */
	private static class RingBinomStreamVariables<T extends Ring<T>> {
		/** The current value. */
		T value;
		
		/** The numerator for calculating the next value. */
		T numerator;
		
		/** The denominator for calculating the next value. */
		long denominator;
	}
	
	/**
	 * Creates a stream of binomial coefficients for the given n and <code>k=0,1,2,...</code>. The stream ends before
	 * returning the first zero. The stream may be finite or infinite, depending on the given n value and the
	 * {@link Ring}'s properties.
	 * 
	 * @param n the n parameter
	 * @param one the one element in the ring type
	 * @return a stream of binomial coefficients <code>binom(n, k)</code>
	 */
	public static <T extends Ring<T>> Stream<T> binomStream(T n, T one) {
		RingBinomStreamVariables<T> variables = new RingBinomStreamVariables<>();
		variables.numerator = n;
		variables.denominator = 1;
		variables.value = one;
		return Stream
				.iterate(
						variables,
						p -> !p.value.isZero(),
						p -> {
							p.value = p.value.multiply(p.numerator).divide(p.denominator);
							p.numerator = p.numerator.subtract(one);
							p.denominator++;
							return p;
						})
				.map(p -> p.value);
	}
	
	/**
	 * Returns the modulus of the values in the first <code>n+1</code> rows in Pascal's triangle.
	 * 
	 * @param nMax the maximum n parameter (inclusive)
	 * @return a list of (nMax+1) lists
	 */
	public static List<List<BigInteger>> getPascalsTriangle(int nMax) {
		if (nMax < 0) {
			return Collections.emptyList();
		}
		List<List<BigInteger>> result = new ArrayList<>(nMax + 1);
		result.add(Collections.singletonList(ONE));
		for (int n = 1; n <= nMax; n++) {
			List<BigInteger> row = new ArrayList<>(n + 1);
			row.add(ONE);
			int kLimit = (n >> 1) + 1;
			for (int k = 1; k < kLimit; k++) {
				row.add(result.get(n - 1).get(k - 1).add(result.get(n - 1).get(k)));
			}
			for (int k = kLimit; k <= n; k++) {
				row.add(row.get(n - k));
			}
			result.add(row);
		}
		return result;
	}
	
	/**
	 * Returns the modulus of the values in the first <code>n+1</code> rows in Pascal's triangle.
	 * 
	 * @param nMax the maximum n parameter (inclusive)
	 * @param modulus the modulus
	 * @return a list of (nMax+1) arrays
	 */
	public static List<int[]> getPascalsTriangle(int nMax, int modulus) {
		if (nMax < 0) {
			return Collections.emptyList();
		}
		List<int[]> result = new ArrayList<>(nMax + 1);
		result.add(new int[] { 1 });
		for (int n = 1; n <= nMax; n++) {
			int[] row = new int[n + 1];
			row[0] = 1;
			int kLimit = (n >> 1) + 1;
			for (int k = 1; k < kLimit; k++) {
				int nextValue = result.get(n - 1)[k - 1] + result.get(n - 1)[k];
				if (nextValue >= modulus || nextValue < 0) {
					nextValue -= modulus;
				}
				row[k] = nextValue;
			}
			for (int k = kLimit; k <= n; k++) {
				row[k] = row[n - k];
			}
			result.add(row);
		}
		return result;
	}
	
	/**
	 * Returns the modulus of the values in the first <code>n+1</code> rows in Pascal's triangle.
	 * 
	 * @param nMax the maximum n parameter (inclusive)
	 * @param modulus the modulus
	 * @return a list of (nMax+1) lists
	 */
	public static List<List<BigInteger>> getPascalsTriangle(int nMax, BigInteger modulus) {
		if (nMax < 0) {
			return Collections.emptyList();
		}
		List<List<BigInteger>> result = new ArrayList<>(nMax + 1);
		result.add(Collections.singletonList(ONE));
		for (int n = 1; n <= nMax; n++) {
			List<BigInteger> row = new ArrayList<>(n + 1);
			row.add(ONE);
			int kLimit = (n >> 1) + 1;
			for (int k = 1; k < kLimit; k++) {
				BigInteger nextValue = result.get(n - 1).get(k - 1).add(result.get(n - 1).get(k));
				if (nextValue.compareTo(modulus) >= 0) {
					nextValue = nextValue.subtract(modulus);
				}
				row.add(nextValue);
			}
			for (int k = kLimit; k <= n; k++) {
				row.add(row.get(n - k));
			}
			result.add(row);
		}
		return result;
	}
}
