package jamato.algebra;

/**
 * This class provides methods that calculate exponentiation.
 * @author JSiebel
 *
 */
public class Exponentiation {
	
	protected Exponentiation() {
	}

	/**
	 * Returns <code>(base<sup>exponent</sup>)</code>.
	 * @param base     the base of the exponentiation
	 * @param exponent the exponent of the exponentiation
	 * @return <code>base<sup>exponent</sup></code>
	 */
	public static int pow(int base, int exponent) {
		if (exponent < 0) {
			throw new ArithmeticException();
		}
		int result = 1;
		while (exponent > 0) {
			if ((exponent & 1) == 1) {
				result *= base;
			}
			exponent >>= 1;
			base *= base;
		}
		return result;
	}
	
	/**
	 * Returns <code>(base<sup>exponent</sup>)</code>.
	 * @param base     the base of the exponentiation
	 * @param exponent the exponent of the exponentiation
	 * @return <code>base<sup>exponent</sup></code>
	 */
	public static long pow(long base, long exponent) {
		long result = 1;
		while (exponent > 0) {
			if ((exponent & 1) == 1) {
				result *= base;
			}
			exponent >>= 1;
			base *= base;
		}
		return result;
	}
	
	private static final int SQRT_MAX_INT = 46340;
	
	/**
	 * Returns <code>(base<sup>exponent</sup>) % modulus</code>.
	 * @param base     the base of the exponentiation
	 * @param exponent the exponent of the exponentiation
	 * @param modulus the modulus
	 * @return <code>base<sup>exponent</sup> % modulus</code>
	 */
	public static int powMod(int base, int exponent, int modulus) {
		if (modulus > SQRT_MAX_INT) {
			return powMod((long)base, exponent, modulus);
		}else if (exponent < 0) {
			throw new ArithmeticException();
		}else if (modulus <= 0) {
			throw new IllegalArgumentException("Non-positive modulus: " + modulus);
		}
		int basePower = base % modulus;
		int result = 1;
		while (exponent > 0) {
			if ((exponent & 1) == 1) {
				result = result * basePower % modulus;
			}
			exponent >>= 1;
			basePower = basePower * basePower % modulus;
		}
		return result;
	}
	
	/**
	 * Returns <code>(base<sup>exponent</sup>) % modulus</code>.
	 * @param base     the base of the exponentiation
	 * @param exponent the exponent of the exponentiation
	 * @param modulus the modulus
	 * @return <code>base<sup>exponent</sup> % modulus</code>
	 */
	public static int powMod(long base, int exponent, int modulus) {
		if (exponent < 0) {
			throw new ArithmeticException();
		}else if (modulus <= 0) {
			throw new IllegalArgumentException("Non-positive modulus: " + modulus);
		}
		long basePower = base % modulus;
		long result = 1;
		while (exponent > 0) {
			if ((exponent & 1) == 1) {
				result = result * basePower % modulus;
			}
			exponent >>= 1;
			basePower = basePower * basePower % modulus;
		}
		return (int) result;
	}
	
	/**
	 * Returns the value <code>(base<sup>exponent</sup>)</code>. Exponentiation with
	 * negative or zero exponents depend on the base being invertible.
	 * 
	 * @param <T>      the class of the base and the result
	 * @param base     the base of the exponentiation
	 * @param exponent the exponent of the exponentiation
	 * @return <code>base<sup>exponent</sup></code>
	 * @throws UnsupportedOperationException if the exponent is negative or zero,
	 *                                       and the ring {@code T} has no
	 *                                       multiplicative inverse
	 * @throws ArithmeticException           if the exponent is negative or zero and
	 *                                       the base is zero
	 */
	public static <T extends Ring<T>> T pow(T base, int exponent){
		if (exponent < 0) {
			return powAbsolute(base.invert(), exponent);
		}else if (exponent == 0) {
			return base.divide(base);
		}else {
			return powAbsolute(base, exponent);
		}
	}
	
	/**
	 * Returns the value <code>(base<sup>exponent</sup>)</code>. Exponentiation with
	 * negative exponents depend on the base being invertible.
	 * 
	 * @param <T>      the class of the base and the result
	 * @param base     the base of the exponentiation
	 * @param exponent the exponent of the exponentiation
	 * @param one      the neutral element of the multiplication in the ring
	 * @return <code>base<sup>exponent</sup></code>
	 * @throws UnsupportedOperationException if the exponent is negative and the
	 *                                       ring {@code T} has no multiplicative
	 *                                       inverse
	 * @throws ArithmeticException           if the exponent is negative and the
	 *                                       base is zero
	 */
	public static <T extends Ring<T>> T pow(T base, int exponent, T one){
		if (exponent < 0) {
			return powAbsolute(base.invert(), exponent);
		}else if (exponent == 0) {
			return one;
		}else {
			return powAbsolute(base, exponent);
		}
	}
	
	/**
	 * Returns the value <code>(base<sup>|exponent|</sup>)</code>. The sign of the
	 * exponent is ignored.
	 * 
	 * @param <T>      the class of the base and the result
	 * @param base     the base of the exponentiation
	 * @param exponent the exponent of the exponentiation, must not be zero
	 * @return <code>base<sup>exponent</sup></code>
	 */
	private static <T extends Ring<T>> T powAbsolute(T base, int exponent){
		T result = null;
		while (result == null) {
			if (exponent % 2 != 0) {
				result = base;
			}
			base = base.multiply(base);
			exponent /= 2;
		}
		while (exponent != 0) {
			if (exponent % 2 != 0) {
				result = result.multiply(base);
			}
			base = base.multiply(base);
			exponent /= 2;
		}
		return result;
	}
}
