package jamato.algebra;

/**
 * This class has methods for modulo calculations.
 * <p>
 * In all methods, the modulus is expected to be a positive number. An exception is thrown if the modulus is zero or
 * negative. The result is always a non-negative number in <code>[0 .. modulus-1]</code>.
 */
public final class Modulo{
	
	private static final int SQRT_MAX_INT = (int) Math.sqrt(Integer.MAX_VALUE);
	
	private static final long SQRT_MAX_LONG = (long) Math.sqrt(Long.MAX_VALUE);
	
	private Modulo(){
		// Utility class
	}
	
	/**
	 * Calculates the product of two values with respect to the given modulus.
	 *
	 * @param factor1 the first factor
	 * @param factor2 the second factor
	 * @param modulus the modulus, must be positive
	 * @return the product with respect to the modulus; the result is in <code>[0 .. modulus-1]</code>
	 */
	public static int multiply(int factor1, int factor2, int modulus){
		checkModulus(modulus);
		return Math.floorMod((long) factor1 * factor2, modulus);
	}
	
	/**
	 * Calculates the product of two values with respect to the given modulus.
	 *
	 * @param factor1 the first factor
	 * @param factor2 the second factor
	 * @param modulus the modulus, must be positive
	 * @return the product with respect to the modulus; the result is in <code>[0 .. modulus-1]</code>
	 */
	public static long multiply(long factor1, long factor2, long modulus){
		checkModulus(modulus);
		long multiplicand = Math.floorMod(factor1, modulus);
		long multiplier = Math.floorMod(factor2, modulus);
		long result = 0;
		while (multiplier != 0){
			if ((multiplier & 1L) != 0){
				result += multiplicand;
				if (result >= modulus || result < 0){
					result -= modulus;
				}
			}
			multiplier >>>= 1;
			multiplicand <<= 1;
			if (multiplicand >= modulus || multiplicand < 0){
				multiplicand -= modulus;
			}
		}
		return result;
	}
	
	/**
	 * Calculates the inverse of the given value with respect to the given modulus. The inverse to a value is a number
	 * where <code>value * inverse ≡ 1 (mod modulus)</code>.
	 *
	 * @param value the value to be inverted
	 * @param modulus the modulus, must be positive
	 * @return the inverse if one exists, 0 otherwise; the result is in <code>[0 .. modulus-1]</code>
	 * @throws ArithmeticException if the modulus is ≤ 0 or {@code value} and {@code modulus} are not relatively prime.
	 */
	public static int invert(int value, int modulus){
		checkModulus(modulus);
		// Apply the Euclidean algorithm, starting with:
		// 1 * modulus + 0 * value = modulus and
		// 0 * modulus + 1 * value = value
		// The modulus coefficients are not used
		int vFactor1 = 0;
		int constant1 = modulus;
		int vFactor2 = 1;
		int constant2 = Math.floorMod(value, modulus);
		while (constant2 > 1){
			// Invariants:
			// vFactor1 * value ≡ constant1 (mod modulus) and
			// vFactor2 * value ≡ constant2 (mod modulus)
			int subtractionFactor = constant1 / constant2;
			int nextVFactor = vFactor1 - subtractionFactor * vFactor2;
			int nextConstant = constant1 - subtractionFactor * constant2;
			vFactor1 = vFactor2;
			constant1 = constant2;
			vFactor2 = nextVFactor;
			constant2 = nextConstant;
		}
		if (constant2 == 1){
			// vFactor2 * value ≡ 1 (mod modulus)
			return Math.floorMod(vFactor2, modulus);
		}else{
			throw new ArithmeticException("Number not invertible: " + value + "(mod " + modulus + ")");
		}
	}
	
	/**
	 * Calculates the inverse of the given value with respect to the given modulus. The inverse to a value is a number
	 * where <code>value * inverse ≡ 1 (mod modulus)</code>.
	 *
	 * @param value the value to be inverted
	 * @param modulus the modulus, must be positive
	 * @return the inverse if one exists, 0 otherwise; the result is in <code>[0 .. modulus-1]</code>
	 * @throws ArithmeticException if the modulus is ≤ 0 or {@code value} and {@code modulus} are not relatively prime.
	 */
	public static long invert(long value, long modulus){
		checkModulus(modulus);
		// Apply the Euclidean algorithm, starting with:
		// 1 * modulus + 0 * value = modulus and
		// 0 * modulus + 1 * value = value
		// The modulus coefficients are not used
		long vFactor1 = 0;
		long constant1 = modulus;
		long vFactor2 = 1;
		long constant2 = Math.floorMod(value, modulus);
		while (constant2 > 1){
			// Invariants:
			// vFactor1 * value ≡ constant1 (mod modulus) and
			// vFactor2 * value ≡ constant2 (mod modulus)
			long subtractionFactor = constant1 / constant2;
			long nextVFactor = vFactor1 - subtractionFactor * vFactor2;
			long nextConstant = constant1 - subtractionFactor * constant2;
			vFactor1 = vFactor2;
			constant1 = constant2;
			vFactor2 = nextVFactor;
			constant2 = nextConstant;
		}
		if (constant2 == 1){
			// vFactor2 * value ≡ 1 (mod modulus)
			return Math.floorMod(vFactor2, modulus);
		}else{
			throw new ArithmeticException("Number not invertible: " + value + "(mod " + modulus + ")");
		}
	}
	
	/**
	 * Returns <code>(base<sup>exponent</sup>) % modulus</code>.
	 * 
	 * @param base the base of the exponentiation
	 * @param exponent the exponent of the exponentiation
	 * @param modulus the modulus
	 * @return <code>base<sup>exponent</sup> % modulus</code>
	 */
	public static int pow(int base, int exponent, int modulus){
		checkModulus(modulus);
		int basePower = Math.floorMod(base, modulus);
		int remainingExponent = exponent;
		if (exponent < 0){
			basePower = invert(base, modulus);
			remainingExponent = -remainingExponent;
		}
		if (modulus <= SQRT_MAX_INT){
			int result = 1;
			while (remainingExponent != 0){
				if ((remainingExponent & 1) == 1){
					result = result * basePower % modulus;
				}
				remainingExponent >>>= 1;
				basePower = basePower * basePower % modulus;
			}
			return result;
		}else{
			long result = 1;
			while (remainingExponent != 0){
				if ((remainingExponent & 1) == 1){
					result = result * basePower % modulus;
				}
				remainingExponent >>>= 1;
				basePower = (int) ((long) basePower * basePower % modulus);
			}
			return (int) result;
		}
	}
	
	/**
	 * Returns <code>(base<sup>exponent</sup>) % modulus</code>.
	 * 
	 * @param base the base of the exponentiation
	 * @param exponent the exponent of the exponentiation
	 * @param modulus the modulus
	 * @return <code>base<sup>exponent</sup> % modulus</code>
	 */
	public static long pow(long base, long exponent, long modulus){
		checkModulus(modulus);
		long basePower = Math.floorMod(base, modulus);
		long remainingExponent = exponent;
		if (exponent < 0){
			basePower = invert(base, modulus);
			remainingExponent = -remainingExponent;
		}
		long result = 1;
		if (modulus <= SQRT_MAX_LONG){
			while (remainingExponent != 0){
				if ((remainingExponent & 1) == 1){
					result = result * basePower % modulus;
				}
				remainingExponent >>>= 1;
				basePower = basePower * basePower % modulus;
			}
		}else{
			while (remainingExponent != 0){
				if ((remainingExponent & 1) == 1){
					result = multiply(result, basePower, modulus);
				}
				remainingExponent >>>= 1;
				basePower = multiply(basePower, basePower, modulus);
			}
		}
		return result;
	}
	
	/**
	 * Checks if the given modulus is valid (i.e. ≥1), and throws an exception if not.
	 *
	 * @param modulus the modulus to be checked
	 */
	private static void checkModulus(long modulus){
		if (modulus <= 0){
			throw new ArithmeticException("Invalid modulus: " + modulus);
		}
	}
}
