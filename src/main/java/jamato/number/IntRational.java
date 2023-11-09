package jamato.number;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

import jamato.algebra.GCD;
import jamato.algebra.Ring;

/**
 * An immutable rational number with integer numerator and denominator. The numerator and denominator are reduced, the
 * numerator is never negative. In case of an overflow of the numerator or denominator in the result of an operation,
 * the result is approximated.
 * <p>
 * Operations on the special values {@link #POSITIVE_INFINITY}, {@link #NEGATIVE_INFINITY} and {@link #NAN} behave like
 * their equivalents in the {@link Double} class.
 * 
 * @author JSiebel
 *
 */
public class IntRational extends Number implements Ring<IntRational>, Comparable<IntRational>{
	
	private static final long serialVersionUID = -1358115182615374506L;

	/** The IntRational constant 0 */
	public static final IntRational ZERO = new IntRational(0, 1);
	
	/** The IntRational constant 1 */
	public static final IntRational ONE = new IntRational(1, 1);
	
	/** The IntRational constant NAN */
	public static final IntRational NAN = new IntRational(0, 0);
	
	/** The IntRational constant positive infinity */
	public static final IntRational POSITIVE_INFINITY = new IntRational(1, 0);
	
	/** The IntRational constant negative infinity */
	public static final IntRational NEGATIVE_INFINITY = new IntRational(-1, 0);

	/** The numerator of this IntRational. */
	public final int numerator;

	/** The denominator of this IntRational. */
	public final int denominator;

	/**
	 * Creates an IntRational with the value equal to {@code numerator/denominator}. If the denominator is {@code 0},
	 * the result is {@link #POSITIVE_INFINITY} (for positive numerators), {@link #NEGATIVE_INFINITY} (for negative
	 * numerators), or {@link #NAN} (if the numerator is {@code 0}).
	 * 
	 * @param numerator the numerator
	 * @param denominator the denominator
	 */
	public IntRational(int numerator, int denominator) {
		this(numerator, denominator, GCD.of(numerator, denominator));
	}
	
	/**
	 * Creates an IntRational with the value equal to {@code numerator/1}.
	 * 
	 * @param numerator the numerator
	 */
	public IntRational(int numerator) {
		this(numerator, 1);
	}
	
	protected IntRational(int numerator, int denominator, int gcd) {
		if (gcd == 0) {
			this.numerator = 0;
			this.denominator = 0;
		} else if (denominator < 0) {
			this.numerator = -numerator / gcd;
			this.denominator = -denominator / gcd;
		} else {
			this.numerator = numerator / gcd;
			this.denominator = denominator / gcd;
		}
	}
	
	/**
	 * Creates an IntRational from a long numerator and denominator. If either is not in integer range, the result is
	 * approximated. The nomiator and the denominator must be coprime.
	 * 
	 * @param numerator the numerator
	 * @param denominator the denominator
	 */
	protected IntRational(long numerator, long denominator) {
		if (denominator == 0) {
			this.numerator = Long.signum(numerator);
			this.denominator = 0;
		} else if (numerator == 0) {
			this.numerator = 0;
			this.denominator = 1;
		} else {
			while ((Math.abs(numerator) | Math.abs(denominator)) >> (Integer.SIZE - 1) != 0) {
				numerator /= 2;
				denominator /= 2;
				long gcd = GCD.of(numerator, denominator);
				numerator /= gcd;
				denominator /= gcd;
			}
			this.numerator = (int) numerator;
			this.denominator = (int) denominator;
		}
	}
	
	/**
	 * Creates an IntRational with the given double value. This constructor tries to find the smallest numerator and
	 * denominator where {@code this.doubleValue() == value}.
	 * 
	 * @param value a double value
	 */
	public IntRational(double value) {
		if (!Double.isFinite(value)) {
			numerator = (int) Math.signum(value);
			denominator = 0;
		} else if (value < Integer.MIN_VALUE / 2 || value > Integer.MAX_VALUE / 2 + 1) {
			numerator = (int) value;
			denominator = 1;
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
			double floor = Math.floor(value);
			double remainder = value - floor;
			
			int a = 0;
			int b = 1;
			int c = 1;
			int d = 0;
			
			a = 1;
			b = (int) floor;
			c = 0;
			d = 1;
			
			remainder = 1 / remainder;
			floor = Math.floor(remainder);
			while (isInIntRange(c + floor * d) && isInIntRange(a + floor * b)) {
				int dNext = c + (int) floor * d;
				int bNext = a + (int) floor * b;
				
				a = b;
				b = bNext;
				c = d;
				d = dNext;
				
				remainder = 1 / (remainder - floor);
				floor = Math.floor(remainder);
			}
			if (Math.abs(value - (double) a / c) <= Math.abs(value - (double) b / d)) {
				int signum = Integer.signum(c);
				numerator = a * signum;
				denominator = c * signum;
			} else {
				int signum = Integer.signum(d);
				numerator = b * signum;
				denominator = d * signum;
			}
		}
	}

	private static boolean isInIntRange(double d) {
		return Integer.MIN_VALUE <= d && d <= Integer.MAX_VALUE;
	}
	
	@Override
	public IntRational negate() {
		if (numerator == Integer.MIN_VALUE) {
			return POSITIVE_INFINITY;
		} else {
			return new IntRational(-numerator, denominator, 1);
		}
	}
	
	@Override
	public boolean isZero() {
		return numerator == 0 && denominator != 0;
	}
	
	@Override
	public IntRational add(IntRational summand) {
		if (isFinite() && summand.isFinite()) {
			long resultNumerator = (long) numerator * summand.denominator + (long) summand.numerator * denominator;
			long resultDenominator = (long) denominator * summand.denominator;
			long gcd = GCD.of(resultNumerator, resultDenominator);
			return new IntRational(resultNumerator / gcd, resultDenominator / gcd);
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
	 * Returns an IntRational with the value {@code (this + summand)}.
	 * 
	 * @param summand value to be added to this IntRational
	 * @return {@code (this + summand)}
	 */
	public IntRational add(int summand) {
		if (isFinite()) {
			return new IntRational(numerator + (long) denominator * summand, denominator);
		} else {
			return this;
		}
	}
	
	@Override
	public IntRational subtract(IntRational subtrahend) {
		if (isFinite() && subtrahend.isFinite()) {
			long resultNumerator = (long) numerator * subtrahend.denominator
					- (long) subtrahend.numerator * denominator;
			long resultDenominator = (long) denominator * subtrahend.denominator;
			long gcd = GCD.of(resultNumerator, resultDenominator);
			return new IntRational(resultNumerator / gcd, resultDenominator / gcd);
		} else if (isFinite()) {
			return subtrahend.negate();
		} else if (subtrahend.isFinite()) {
			return this;
		} else if (numerator == -subtrahend.numerator) {
			return this;
		} else {
			return NAN;
		}
	}
	
	/**
	 * Returns an IntRational with the value {@code (this - subtrahend)}.
	 * 
	 * @param subtrahend value to be subtracted from this IntRational
	 * @return {@code (this - subtrahend)}
	 */
	public IntRational subtract(int subtrahend) {
		if (isFinite()) {
			return new IntRational(numerator - (long) denominator * subtrahend, denominator);
		} else {
			return this;
		}
	}
	
	@Override
	public IntRational invert() {
		return new IntRational(denominator, numerator, 1);
	}
	
	@Override
	public boolean isOne() {
		return equals(ONE);
	}
	
	@Override
	public IntRational multiply(IntRational factor) {
		if (!isFinite() || !factor.isFinite()) {
			return getNonFiniteValueBySign(Integer.signum(numerator) * factor.signum());
		} else if (isZero() || factor.isZero()) {
			return ZERO;
		} else {
			int gcd1 = GCD.of(numerator, factor.denominator);
			int gcd2 = GCD.of(factor.numerator, denominator);
			return new IntRational(
					(long) numerator / gcd1 * factor.numerator / gcd2,
					(long) denominator / gcd2 * factor.denominator / gcd1);
		}
	}
	
	/**
	 * Returns a multiple of this.
	 * 
	 * @param factor an integer factor
	 * @return {@code (this * factor)}
	 */
	public IntRational multiply(int factor) {
		if (!isFinite()) {
			return getNonFiniteValueBySign(signum() * Integer.signum(factor));
		} else if (isZero() || factor == 0) {
			return ZERO;
		} else {
			int gcd = GCD.of(denominator, factor);
			return new IntRational((long) numerator * (factor / gcd), denominator / gcd);
		}
	}
	
	/**
	 * Returns the value {@code (this / divisor)}.
	 * 
	 * @param divisor value by which this is to be divided.
	 * @return {@code (this / divisor)}
	 */
	@Override
	public IntRational divide(IntRational divisor) {
		if (divisor.isZero()) {
			return getNonFiniteValueBySign(numerator);
		} else if (!isFinite() || divisor.isNaN()) {
			int invertedDivisorSignum = divisor.isFinite() ? divisor.signum() : 0;
			return getNonFiniteValueBySign(Integer.signum(numerator) * invertedDivisorSignum);
		} else if (isZero() || !divisor.isFinite()) {
			return ZERO;
		} else {
			int gcd1 = GCD.of(numerator, divisor.numerator);
			int gcd2 = GCD.of(divisor.denominator, denominator);
			return new IntRational(
					(long) numerator / gcd1 * divisor.denominator / gcd2,
					(long) denominator / gcd2 * divisor.numerator / gcd1);
		}
	}
	
	/**
	 * Returns the value {@code (this / divisor)}.
	 * 
	 * @param divisor value by which this is to be divided.
	 * @return {@code (this / divisor)}
	 */
	@Override
	public IntRational divide(long divisor) {
		if (isFinite()) {
			long gcd = GCD.of(numerator, divisor);
			return new IntRational(numerator / gcd, denominator * (divisor / gcd));
		} else {
			return getNonFiniteValueBySign(Integer.signum(numerator) * Long.signum(divisor));
		}
	}
	
	/**
	 * Returns the signum function of this.
	 *
	 * @return -1, 0 or 1 as the value of this is negative, zero/NaN or positive.
	 */
	public int signum() {
		return Integer.signum(numerator);
	}
	
	/**
	 * Returns the absolute value of this.
	 * 
	 * @return {@code (|this|)}
	 */
	public IntRational absolute() {
		if (numerator < 0) {
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
	 * <li>If this negative infinity, the result is {@code Integer.MIN_VALUE}.
	 * <li>If this positive infinity, the result is {@code Integer.MAX_VALUE}.
	 * </ul>
	 * 
	 * @return the rounded number as a {@link BigInteger}
	 */
	public int floor() {
		return round(RoundingMode.FLOOR);
	}
	
	/**
	 * Rounds this number towards positive infinity.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If this is NaN, the result is 0.
	 * <li>If this negative infinity, the result is {@code Integer.MIN_VALUE}.
	 * <li>If this positive infinity, the result is {@code Integer.MAX_VALUE}.
	 * </ul>
	 * 
	 * @return the rounded number as a {@link BigInteger}
	 */
	public int ceil() {
		return round(RoundingMode.CEILING);
	}
	
	/**
	 * Rounds this number to the next integer, or away from zero if the two neighbor integers have the same distance.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If this is NaN, the result is 0.
	 * <li>If this negative infinity, the result is {@code Integer.MIN_VALUE}.
	 * <li>If this positive infinity, the result is {@code Integer.MAX_VALUE}.
	 * </ul>
	 * 
	 * @return the rounded number as a {@link BigInteger}
	 */
	public int round() {
		return round(RoundingMode.HALF_UP);
	}
	
	/**
	 * Rounds this number according to the given rounding mode.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If this is NaN, the result is 0.
	 * <li>If this negative infinity, the result is {@code Integer.MIN_VALUE}.
	 * <li>If this positive infinity, the result is {@code Integer.MAX_VALUE}.
	 * </ul>
	 * 
	 * @param roundingMode a {@link RoundingMode}
	 * @return the rounded number
	 * @throws ArithmeticException if {@code roundingMode} is {@link RoundingMode#UNNECESSARY} and this number is not an
	 * integer.
	 */
	public int round(RoundingMode roundingMode) {
		if (isInteger()) {
			return numerator;
		} else if (isNaN()) {
			return 0;
		} else if (equals(POSITIVE_INFINITY)) {
			return Integer.MAX_VALUE;
		} else if (equals(NEGATIVE_INFINITY)) {
			return Integer.MIN_VALUE;
		}
		int remainder = numerator % denominator;
		int halfIndicator = Integer.compare(2 * Math.abs(remainder), denominator);
		int quotient = numerator / denominator;
		
		boolean awayFromZero;
		switch (roundingMode) {
		case UP:
			awayFromZero = true;
			break;
		case DOWN:
			awayFromZero = false;
			break;
		case CEILING:
			awayFromZero = numerator > 0;
			break;
		case FLOOR:
			awayFromZero = numerator < 0;
			break;
		case HALF_UP:
			awayFromZero = halfIndicator >= 0;
			break;
		case HALF_DOWN:
			awayFromZero = halfIndicator > 0;
			break;
		case HALF_EVEN:
			awayFromZero = halfIndicator > 0 || halfIndicator == 0 && quotient % 2 != 0;
			break;
		case UNNECESSARY:
		default:
			throw new ArithmeticException();
		}
		if (awayFromZero) {
			quotient += Integer.signum(numerator);
		}
		return quotient;
	}
	
	/**
	 * Returns {@code true} if this is IntRational finite; returns {@code false} otherwise (for NaN and infinity).
	 *
	 * @return {@code true} if the argument is a finite, {@code false} otherwise
	 */
	public boolean isFinite() {
		return denominator != 0;
	}
	
	/**
	 * Returns {@code true} if this is {@link #NAN}, {@code false} otherwise.
	 *
	 * @return {@code true} if this is NaN; {@code false} otherwise.
	 */
	
	public boolean isNaN() {
		return equals(NAN);
	}
	
	/**
	 * Returns {@code true} if this is an integer, {@code false} otherwise.
	 *
	 * @return {@code true} if this is an integer; {@code false} otherwise.
	 */
	public boolean isInteger() {
		return denominator == 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof IntRational) {
			IntRational intRational = (IntRational) obj;
			return numerator == intRational.numerator && denominator == intRational.denominator;
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
		return denominator == 1 ? numerator + "" : numerator + "/" + denominator;
	}
	
	@Override
	public double doubleValue() {
		return (double) numerator / denominator;
	}
	
	@Override
	public float floatValue() {
		return (float) numerator / denominator;
	}
	
	@Override
	public int intValue() {
		return numerator / denominator;
	}
	
	@Override
	public long longValue() {
		return intValue();
	}
	
	@Override
	public int compareTo(IntRational o) {
		if (this.equals(o)) {
			return 0;
		} else if (isFinite() && o.isFinite()) {
			return Long.compare((long) numerator * o.denominator, (long) denominator * o.numerator);
		} else if (isNaN()) {
			return 1;
		} else if (o.isNaN()) {
			return -1;
		} else if (isFinite()) {
			return -o.numerator;
		} else {
			return numerator;
		}
	}
	
	/**
	 * Translates the decimal String representation of an IntRational into an IntRational. The String representation
	 * consists of either two decimal numbers separated by a slash ("/"), or a single decimal number. Whitespace is not
	 * allowed.
	 *
	 * @param string the decimal String representation of an IntRational
	 * @return an IntRational with the specified value
	 * @throws NumberFormatException if {@code string} is not a valid representation of an IntRational
	 */
	public static IntRational valueOf(String string) {
		String[] parts = string.split("/");
		switch (parts.length) {
		case 1:
			return new IntRational(Integer.valueOf(parts[0]));
		case 2:
			return new IntRational(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
		default:
			throw new NumberFormatException();
		}
	}

	
	/**
	 * Returns an non-finite value with the same sign as the argument: {@link #NEGATIVE_INFINITY}, {@link #NAN} or
	 * {@link #POSITIVE_INFINITY} for a negative, zero or positive argument.
	 * 
	 * @param signum a number
	 * @return a non-finite value with the argument's sign
	 */
	private static IntRational getNonFiniteValueBySign(int signum) {
		if (signum < 0) {
			return NEGATIVE_INFINITY;
		} else if (signum == 0) {
			return NAN;
		} else {
			return POSITIVE_INFINITY;
		}
	}
}
