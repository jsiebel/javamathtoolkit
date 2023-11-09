package jamato.number;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

import jamato.algebra.GCD;
import jamato.algebra.Ring;

/**
 * An immutable arbitrary-precision rational number. The numerator and
 * denominator are reduced, the numerator is never negative.
 * <p>
 * Operations on the special values {@link #POSITIVE_INFINITY},
 * {@link #NEGATIVE_INFINITY} and {@link #NAN} behave like their equivalents in
 * the {@link Double} class.
 * 
 * @author JSiebel
 *
 */
public class BigRational extends Number implements Ring<BigRational>, Comparable<BigRational>{
	
	private static final int DOUBLE_MANTISSA_BITS = 52;

	private static final long serialVersionUID = -1358115182615374506L;
	
	/** The BigRational constant 0 */
	public static final BigRational ZERO = new BigRational(BigInteger.ZERO, BigInteger.ONE);
	/** The BigRational constant 1 */
	public static final BigRational ONE = new BigRational(BigInteger.ONE, BigInteger.ONE);
	/** The BigRational constant NAN */
	public static final BigRational NAN = new BigRational(BigInteger.ZERO, BigInteger.ZERO);
	/** The BigRational constant positive infinity */
	public static final BigRational POSITIVE_INFINITY = new BigRational(BigInteger.ONE, BigInteger.ZERO);
	/** The BigRational constant negative infinity */
	public static final BigRational NEGATIVE_INFINITY = new BigRational(BigInteger.ONE.negate(), BigInteger.ZERO);
	
	/** The numerator of this BigRational. */
	public final BigInteger numerator;

	/** The denominator of this BigRational. The denominator is never negative. */
	public final BigInteger denominator;
	
	/**
	 * Creates a BigRational with the value equal to {@code numerator/denominator}. If the denominator is {@code 0}, the
	 * result is {@link #POSITIVE_INFINITY} (for positive numerators), {@link #NEGATIVE_INFINITY} (for negative
	 * numerators), or {@link #NAN} (if the numerator is {@code 0}).
	 * 
	 * @param numerator the numerator
	 * @param denominator the denominator
	 */
	public BigRational(BigInteger numerator, BigInteger denominator) {
		this(numerator, denominator, GCD.of(numerator, denominator));
	}
	
	/**
	 * Creates a BigRational with the value equal to {@code numerator/1}.
	 * 
	 * @param numerator the numerator
	 */
	public BigRational(BigInteger numerator) {
		this(numerator, BigInteger.ONE);
	}

	
	/**
	 * Creates a BigRational with the value equal to {@code numerator/denominator}. If the denominator is {@code 0}, the
	 * result is {@link #POSITIVE_INFINITY} (for positive numerators), {@link #NEGATIVE_INFINITY} (for negative
	 * numerators), or {@link #NAN} (if the numerator is {@code 0}).
	 * 
	 * @param numerator the numerator
	 * @param denominator the denominator
	 */
	public BigRational(long numerator, long denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator),
				BigInteger.valueOf(GCD.of(numerator, denominator)));
	}
	
	/**
	 * Creates a BigRational with the value equal to {@code numerator/1}.
	 * 
	 * @param numerator the numerator
	 */
	public BigRational(long numerator) {
		this(BigInteger.valueOf(numerator), BigInteger.ONE, BigInteger.ONE);
	}
	
	private BigRational(BigInteger numerator, BigInteger denominator, BigInteger gcd) {
		Objects.requireNonNull(numerator);
		Objects.requireNonNull(denominator);
		if (gcd.equals(BigInteger.ZERO)) {
			this.numerator = BigInteger.ZERO;
			this.denominator = BigInteger.ZERO;
		} else if (denominator.signum() < 0) {
			this.numerator = numerator.negate().divide(gcd);
			this.denominator = denominator.negate().divide(gcd);
		} else {
			this.numerator = numerator.divide(gcd);
			this.denominator = denominator.divide(gcd);
		}
	}
	
	/**
	 * Minimum number of zeros in a double value where it is assumed that the value
	 * is an integer scaled by a power of two.
	 */
	private static final int TRAILING_ZERO_LIMIT = 10;
	private static final long MANTISSA_MASK = 0x000fffffffffffffL;
	
	/**
	 * Creates a BigRational with the given double value. This constructor tries to find the smallest numerator and
	 * denominator where {@code this.doubleValue() == value}.
	 * 
	 * @param value a double value
	 */
	public BigRational(double value) {
		long mantissa = Double.doubleToRawLongBits(value) & MANTISSA_MASK;
		if (!Double.isFinite(value)) {
			numerator = BigInteger.valueOf((long) Math.signum(value));
			denominator = BigInteger.ZERO;
		}else if (value == 0) {
			numerator = BigInteger.ZERO;
			denominator = BigInteger.ONE;
		} else {
			/*
			 * The value is approximated by a continued fraction: Starting with
			 * value = r_0, in each step the remainder r_n is replaced by
			 * 		r_n = f_(n+1) + 1/r_(n+1)
			 * where f_n is an integer close to r_n. The resulting continued
			 * fraction (with a double at the innermost denominator)
			 * 		value = f_0+1/(f_1+1/(f_2+...1/(f_n+r_n)...))
			 * can be expressed as a simple fraction:
			 * 		value = (a_n+b_n*r_n) / (c_n+d_n*r_n).
			 * The fraction's coefficients can be calculated iteratively:
			 * 		a_1 = 1
			 * 		b_1 = f_1
			 * 		c_1 = 0
			 * 		d_1 = 1
			 * 		a_(n+1) = b_n
			 * 		b_(n+1) = a_n + b_n * f_(n+1)
			 * 		c_(n+1) = d_n
			 * 		d_(n+1) = c_n + d_n * f_(n+1)
			 * If the remainder r_n is zero, then the value = b_n / d_n
			 */
			int exponent;
			if (Math.getExponent(value) >= DOUBLE_MANTISSA_BITS) {
				exponent = Math.getExponent(value) - DOUBLE_MANTISSA_BITS;
			}else if (Long.numberOfTrailingZeros(mantissa) > TRAILING_ZERO_LIMIT) {
				/*
				 * The mantissa has many trailing zeros, so it's probably an integer scaled by a
				 * power of two.
				 */
				int numberOfTrailingZeros = mantissa == 0 ? DOUBLE_MANTISSA_BITS : Long.numberOfTrailingZeros(mantissa);
				exponent = Math.getExponent(value) - DOUBLE_MANTISSA_BITS + numberOfTrailingZeros;
			}else if (Math.getExponent(value) < 0) {
				/* Scale small values to prevent overflows when casting the inverse to long. */
				exponent = Math.getExponent(value);
			}else {
				exponent = 0;
			}
			double scaledValue = Math.scalb(value, -exponent);
			
			double rounded = Math.round(scaledValue);
			double remainder = scaledValue - rounded;
			long a = 1;
			long b = (long) rounded;
			long c = 0;
			long d = 1;
			double result = rounded;
			double bestDifference = Math.abs(remainder);
			long bestB = b;
			long bestD = d;
			while (remainder != 0 && bestDifference != 0) {
				remainder = 1 / remainder;
				rounded = Math.round(remainder);
				remainder -= rounded;
				
				long bNext = a + b * (long) rounded;
				long dNext = c + d * (long) rounded;
				a = b;
				b = bNext;
				c = d;
				d = dNext;

				result = (double) b / d;
				double difference = Math.abs(result - scaledValue);
				if (difference < bestDifference) {
					bestDifference = difference;
					bestB = b;
					bestD = d;
				}
			}
			if (bestD < 0) {
				bestB = -bestB;
				bestD = -bestD;
			}
			
			int commonTrailingZeros = Math.min(
					Long.numberOfTrailingZeros(bestB) + Math.max(exponent, 0),
					Long.numberOfTrailingZeros(bestD) + Math.max(-exponent, 0));
			int numeratorShift = Math.max(exponent, 0) - commonTrailingZeros;
			int denominatorShift = Math.max(-exponent, 0) - commonTrailingZeros;
			numerator = BigInteger.valueOf(bestB).shiftLeft(numeratorShift);
			denominator = BigInteger.valueOf(bestD).shiftLeft(denominatorShift);
		}
	}
	
	private static double divideAsDouble(BigInteger numerator, BigInteger denominator) {
		if (numerator.bitLength() <= DOUBLE_MANTISSA_BITS && denominator.bitLength() <= DOUBLE_MANTISSA_BITS) {
			return numerator.doubleValue() / denominator.doubleValue();
		} else {
			int signum = numerator.signum();
			numerator = numerator.abs();
			
			int lengthDifference = numerator.bitLength() - denominator.bitLength();
			int doubleMantissaBits = DOUBLE_MANTISSA_BITS;
			int exponent = doubleMantissaBits - lengthDifference;
			if (numerator.shiftRight(lengthDifference).compareTo(denominator) < 0) {
				exponent++;
			}
			BigInteger mantissa = numerator
					.shiftLeft(exponent)
					.add(denominator.shiftRight(1))
					.divide(denominator);
			return signum * Math.scalb(mantissa.doubleValue(), -exponent);
		}
	}

	@Override
	public BigRational negate() {
		return new BigRational(numerator.negate(), denominator, BigInteger.ONE);
	}
	
	@Override
	public boolean isZero() {
		return numerator.equals(BigInteger.ZERO) && !denominator.equals(BigInteger.ZERO);
	}
	
	@Override
	public BigRational add(BigRational summand) {
		if (isFinite() && summand.isFinite()) {
			BigInteger gcd = GCD.of(denominator, summand.denominator);
			return new BigRational(
					numerator.multiply(summand.denominator.divide(gcd))
							.add(summand.numerator.multiply(denominator.divide(gcd))),
					denominator.divide(gcd).multiply(summand.denominator));
		} else if (isFinite()) {
			return summand;
		} else if (summand.isFinite()) {
			return this;
		} else if (numerator == summand.numerator) {
			return this;
		} else {
			return NAN;
		}
	}

	/**
	 * Returns a BigRational with the value {@code (this + summand)}.
	 * @param summand value to be added to this BigRational
	 * @return {@code (this + summand)}
	 */
	public BigRational add(BigInteger summand) {
		if (isFinite()) {
			return new BigRational(numerator.add(denominator.multiply(summand)), denominator, BigInteger.ONE);
		} else {
			return this;
		}
	}
	
	/**
	 * Returns a BigRational with the value {@code (this + summand)}.
	 * 
	 * @param summand value to be added to this BigRational
	 * @return {@code (this + summand)}
	 */
	public BigRational add(long summand) {
		return add(BigInteger.valueOf(summand));
	}
	
	@Override
	public BigRational subtract(BigRational subtrahend) {
		if (isFinite() && subtrahend.isFinite()) {
			BigInteger gcd = GCD.of(denominator, subtrahend.denominator);
			return new BigRational(
					numerator.multiply(subtrahend.denominator.divide(gcd))
							.subtract(subtrahend.numerator.multiply(denominator.divide(gcd))),
					denominator.divide(gcd).multiply(subtrahend.denominator));
		} else if (isFinite()) {
			return subtrahend.negate();
		} else if (subtrahend.isFinite()) {
			return this;
		} else if (numerator.equals(subtrahend.numerator.negate())) {
			return this;
		} else {
			return NAN;
		}
	}
	
	/**
	 * Returns a BigRational with the value {@code (this - subtrahend)}.
	 * 
	 * @param subtrahend value to be subtracted from this BigRational
	 * @return {@code (this - subtrahend)}
	 */
	public BigRational subtract(BigInteger subtrahend) {
		if (isFinite()) {
			return new BigRational(numerator.subtract(denominator.multiply(subtrahend)), denominator, BigInteger.ONE);
		} else {
			return this;
		}
	}
	
	/**
	 * Returns a BigRational with the value {@code (this - subtrahend)}.
	 * 
	 * @param subtrahend value to be subtracted from this BigRational
	 * @return {@code (this - subtrahend)}
	 */
	public BigRational subtract(long subtrahend) {
		return subtract(BigInteger.valueOf(subtrahend));
	}
	
	@Override
	public boolean isOne() {
		return equals(ONE);
	}
	
	@Override
	public BigRational invert() {
		return new BigRational(denominator, numerator, BigInteger.ONE);
	}
	
	@Override
	public BigRational multiply(BigRational factor) {
		if (!isFinite() || !factor.isFinite()) {
			return getNonFiniteValueBySign(numerator.signum() * factor.signum());
		} else if (isZero() || factor.isZero()) {
			return ZERO;
		} else {
			BigInteger gcd1 = GCD.of(numerator, factor.denominator);
			BigInteger gcd2 = GCD.of(factor.numerator, denominator);
			return new BigRational(
					numerator.divide(gcd1).multiply(factor.numerator.divide(gcd2)),
					denominator.divide(gcd2).multiply(factor.denominator.divide(gcd1)),
					BigInteger.ONE);
		}
	}
	
	/**
	 * Returns a multiple of this.
	 * 
	 * @param factor an integer factor
	 * @return {@code (this * factor)}
	 */
	public BigRational multiply(BigInteger factor) {
		if (!isFinite()) {
			return getNonFiniteValueBySign(numerator.signum() * factor.signum());
		} else if (isZero() || factor.signum() == 0) {
			return ZERO;
		} else {
			BigInteger gcd = GCD.of(denominator, factor);
			return new BigRational(numerator.multiply(factor.divide(gcd)), denominator.divide(gcd), BigInteger.ONE);
		}
	}
	
	@Override
	public BigRational multiply(long factor) {
		return multiply(BigInteger.valueOf(factor));
	}
	
	/**
	 * Returns a BigRational with the value {@code (this / divisor)}.
	 * 
	 * @param divisor value by which this BigRational is to be divided.
	 * @return {@code (this / divisor)}
	 */
	@Override
	public BigRational divide(BigRational divisor) {
		if (divisor.isZero()) {
			return getNonFiniteValueBySign(numerator.signum());
		} else if (!isFinite() || divisor.isNaN()) {
			int invertedDivisorSignum = divisor.isFinite() ? divisor.signum() : 0;
			return getNonFiniteValueBySign(numerator.signum() * invertedDivisorSignum);
		} else if (isZero() || !divisor.isFinite()) {
			return ZERO;
		} else {
			BigInteger gcd1 = GCD.of(numerator, divisor.numerator);
			BigInteger gcd2 = GCD.of(divisor.denominator, denominator);
			return new BigRational(
					numerator.divide(gcd1).multiply(divisor.denominator.divide(gcd2)),
					denominator.divide(gcd2).multiply(divisor.numerator.divide(gcd1)),
					BigInteger.ONE);
		}
	}
	
	/**
	 * Returns a BigRational with the value {@code (this / divisor)}.
	 * 
	 * @param divisor value by which this BigRational is to be divided.
	 * @return {@code (this / divisor)}
	 */
	public BigRational divide(BigInteger divisor) {
		if (divisor.signum() == 0) {
			return getNonFiniteValueBySign(numerator.signum());
		} else if (!isFinite()) {
			return getNonFiniteValueBySign(numerator.signum() * divisor.signum());
		} else if (isZero()) {
			return ZERO;
		} else {
			BigInteger gcd = GCD.of(numerator, divisor);
			return new BigRational(numerator.divide(gcd), denominator.multiply(divisor.divide(gcd)), BigInteger.ONE);
		}
	}
	
	@Override
	public BigRational divide(long factor) {
		return divide(BigInteger.valueOf(factor));
	}
	
	/**
	 * Returns a BigRational with the value <code>(this<sup>exponent</sup>)</code>.
	 *
	 * @param exponent exponent to which this BigRational is to be raised
	 * @return <code>this<sup>exponent</sup></code>
	 */
	@Override
	public BigRational pow(int exponent) {
		if (exponent < 0) {
			return new BigRational(denominator.pow(-exponent), numerator.pow(-exponent), BigInteger.ONE);
		} else {
			return new BigRational(numerator.pow(exponent), denominator.pow(exponent), BigInteger.ONE);
		}
	}
	
	/**
	 * Returns the signum function of this.
	 *
	 * @return -1, 0 or 1 as the value of this BigRational is negative, zero/NaN or positive.
	 */
	public int signum() {
		return numerator.signum();
	}

	/**
	 * Returns the absolute value of this.
	 * 
	 * @return {@code (|this|)}
	 */
	public BigRational absolute() {
		if (numerator.signum() < 0) {
			return negate();
		} else {
			return this;
		}
	}

	/**
	 * Rounds this number towards negative infinity.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If this is NaN, the result is 0.
	 * <li>If this negative or positive infinity, an exception is thrown.
	 * </ul>
	 * 
	 * @return the rounded number as a {@link BigInteger}
	 * @throws ArithmeticException if this number is infinite
	 */
	public BigInteger floor() {
		return round(RoundingMode.FLOOR);
	}

	/**
	 * Rounds this number towards positive infinity.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If this is NaN, the result is 0.
	 * <li>If this negative or positive infinity, an exception is thrown.
	 * </ul>
	 * 
	 * @return the rounded number as a {@link BigInteger}
	 * @throws ArithmeticException if this number is infinite
	 */
	public BigInteger ceil() {
		return round(RoundingMode.CEILING);
	}

	/**
	 * Rounds this number to the next integer, or away from zero if the two neighbor
	 * integers have the same distance.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If this is NaN, the result is 0.
	 * <li>If this negative or positive infinity, an exception is thrown.
	 * </ul>
	 * 
	 * @return the rounded number as a {@link BigInteger}
	 * @throws ArithmeticException if this number is infinite
	 */
	public BigInteger round() {
		return round(RoundingMode.HALF_UP);
	}
	
	/**
	 * Rounds this number according to the given rounding mode.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If this is NaN, the result is 0.
	 * <li>If this negative or positive infinity, an exception is thrown.
	 * </ul>
	 * 
	 * @param roundingMode a {@link RoundingMode}
	 * @return the rounded number as a {@link BigInteger}
	 * @throws ArithmeticException if this number is infinite, or if roundingMode
	 *                             is {@link RoundingMode#UNNECESSARY} and this
	 *                             number is not an integer.
	 */
	public BigInteger round(RoundingMode roundingMode) {
		if (isInteger()) {
			return numerator;
		} else if (isNaN()) {
			return BigInteger.ZERO;
		} else if (!isFinite()) {
			throw new ArithmeticException();
		}
		BigInteger[] divideAndRemainder = numerator.divideAndRemainder(denominator);
		int halfIndicator = divideAndRemainder[1].abs().shiftLeft(1).compareTo(denominator);
		BigInteger quotient = divideAndRemainder[0];
		
		boolean awayFromZero;
		switch (roundingMode) {
		case UP:
			awayFromZero = true;
			break;
		case DOWN:
			awayFromZero = false;
			break;
		case CEILING:
			awayFromZero = numerator.signum() > 0;
			break;
		case FLOOR:
			awayFromZero = numerator.signum() < 0;
			break;
		case HALF_UP:
			awayFromZero = halfIndicator >= 0;
			break;
		case HALF_DOWN:
			awayFromZero = halfIndicator > 0;
			break;
		case HALF_EVEN:
			awayFromZero = halfIndicator > 0 || halfIndicator == 0 && divideAndRemainder[0].testBit(0);
			break;
		case UNNECESSARY:
		default:
			throw new ArithmeticException();
		}
		if (awayFromZero) {
			quotient = quotient.add(BigInteger.valueOf(numerator.signum()));
		}
		return quotient;
	}

    /**
     * Returns {@code true} if this is BigRational finite; returns {@code false} otherwise (for NaN and infinity).
     *
     * @return {@code true} if the argument is a finite, {@code false} otherwise
     */
	public boolean isFinite() {
		return denominator.signum() != 0;
	}

	/**
	 * Returns {@code true} if this is {@link #NAN}, {@code false} otherwise.
	 *
	 * @return {@code true} if this is NaN; {@code false}
	 *         otherwise.
	 */
	public boolean isNaN() {
		return equals(NAN);
	}

	/**
	 * Returns {@code true} if this is an integer, {@code false} otherwise.
	 *
	 * @return {@code true} if this is an integer; {@code false}
	 *         otherwise.
	 */
	public boolean isInteger() {
		return denominator.equals(BigInteger.ONE);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof BigRational) {
			BigRational intRational = (BigRational) obj;
			return numerator.equals(intRational.numerator) && denominator.equals(intRational.denominator);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(numerator, denominator);
	}
	
	@Override
	public String toString() {
		return denominator.equals(BigInteger.ONE) ? numerator + "" : numerator + "/" + denominator;
	}
	
	@Override
	public double doubleValue() {
		return divideAsDouble(numerator, denominator);
	}
	
	@Override
	public float floatValue() {
		return (float) doubleValue();
	}
	
	@Override
	public int intValue() {
		return bigIntegerValue().intValue();
	}
	
	@Override
	public long longValue() {
		return bigIntegerValue().longValue();
	}

    /**
     * Returns the value of the specified number as a {@code BigInteger}.
     *
     * @return  the numeric value represented by this object after conversion
     *          to type {@code BigInteger}
     */
	public BigInteger bigIntegerValue() {
		return numerator.divide(denominator);
	}

	@Override
	public int compareTo(BigRational o) {
		if (this.equals(o)) {
			return 0;
		} else if (isFinite() && o.isFinite()) {
			return numerator.multiply(o.denominator).compareTo(o.numerator.multiply(denominator));
		} else if (isNaN()) {
			return 1;
		} else if (o.isNaN()) {
			return -1;
		} else if (isFinite()) {
			return -o.numerator.signum();
		} else {
			return numerator.signum();
		}
	}
	
    /**
	 * Translates the decimal String representation of a BigRational into a
	 * BigRational. The String representation consists of either two decimal numbers
	 * separated by a slash ("/"), or a single decimal number. Whitespace is not
	 * allowed.
	 *
	 * @param string the decimal String representation of a BigRational
     * @return a BigRational with the specified value
	 * @throws NumberFormatException if {@code string} is not a valid representation
	 *                               of a BigRational
	 */
	public static BigRational valueOf(String string) {
		String[] parts = string.split("/");
		switch (parts.length) {
		case 1:
			return new BigRational(new BigInteger(parts[0]));
		case 2:
			return new BigRational(new BigInteger(parts[0]), new BigInteger(parts[1]));
		default:
			throw new NumberFormatException("Illegal BigRational string");
		}
	}
	
	/**
	 * Returns an non-finite value with the same sign as the argument: {@link #NEGATIVE_INFINITY}, {@link #NAN} or
	 * {@link #POSITIVE_INFINITY} for a negative, zero or positive argument.
	 * 
	 * @param signum a number
	 * @return a non-finite value with the argument's sign
	 */
	private static BigRational getNonFiniteValueBySign(int signum) {
		if (signum < 0) {
			return NEGATIVE_INFINITY;
		} else if (signum == 0) {
			return NAN;
		} else {
			return POSITIVE_INFINITY;
		}
	}
}
