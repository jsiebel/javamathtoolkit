package jamato.number;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jamato.algebra.Exponentiation;
import jamato.algebra.Ring;

/**
 * An immutable complex number with double precision.
 * 
 * @author JSiebel
 *
 */
public class DoubleComplex implements Ring<DoubleComplex> {
	
	/** The complex number {@code zero}. */
	public static final DoubleComplex ZERO = new DoubleComplex(0);
	/** The complex number {@code one}. */
	public static final DoubleComplex ONE = new DoubleComplex(1);
	/** The complex number {@code i}. */
	public static final DoubleComplex I = new DoubleComplex(0, 1);
	
	/** The real component of this number. */
	public final double r;
	/** The imaginary component of this number. */
	public final double i;
	
	/**
	 * Creates a complex number with the given components.
	 * 
	 * @param r the real component
	 * @param i the imaginary component
	 */
	public DoubleComplex(double r, double i) {
		this.r = r;
		this.i = i;
	}
	
	/**
	 * Creates a complex number by polar coordinates.
	 * @param z the absolute value of the number
	 * @param phi the argument/phase of the number
	 * @return the complex number
	 */
	public static DoubleComplex polar(double z, double phi) {
		return new DoubleComplex(Math.cos(phi) * z, Math.sin(phi) * z);
	}
	
	/**
	 * Creates a complex number with the given real component and a zero imaginary
	 * component.
	 * 
	 * @param r the real component
	 */
	public DoubleComplex(double r) {
		this(r, 0);
	}
	
	@Override
	public DoubleComplex negate() {
		return new DoubleComplex(-r, -i);
	}
	
	@Override
	public boolean isZero() {
		return r == 0 && i == 0;
	}
	
	@Override
	public DoubleComplex add(DoubleComplex summand) {
		return new DoubleComplex(r + summand.r, i + summand.i);
	}
	
	/**
	 * Returns the sum {@code (this + summand)}.
	 * 
	 * @param summand value to be added to this object
	 * @return {@code (this + summand)}
	 */
	public DoubleComplex add(double summand) {
		return new DoubleComplex(r + summand, i);
	}
	
	@Override
	public DoubleComplex subtract(DoubleComplex subtrahend) {
		return new DoubleComplex(r - subtrahend.r, i - subtrahend.i);
	}
	
	/**
	 * Returns the value {@code (this - subtrahend)}.
	 * 
	 * @param subtrahend value to be subtracted from this T
	 * @return {@code (this - subtrahend)}
	 */
	public DoubleComplex subtract(double subtrahend) {
		return new DoubleComplex(r - subtrahend, i);
	}
	
	@Override
	public DoubleComplex invert() {
		double squaredAbsolute = r * r + i * i;
		return new DoubleComplex(r / squaredAbsolute, -i / squaredAbsolute);
	}
	
	@Override
	public boolean isOne() {
		return r == 1 && i == 0;
	}
	
	@Override
	public DoubleComplex multiply(DoubleComplex factor) {
		return new DoubleComplex(r * factor.r - i * factor.i, r * factor.i + i * factor.r);
	}
	
	@Override
	public DoubleComplex multiply(long factor) {
		return new DoubleComplex(factor * r, factor * i);
	}
	
	/**
	 * Returns a multiple of this.
	 * 
	 * @param factor a factor
	 * @return {@code (this * factor)}
	 */
	public DoubleComplex multiply(double factor) {
		return new DoubleComplex(factor * r, factor * i);
	}
	
	@Override
	public DoubleComplex divide(long divisor) {
		return new DoubleComplex(r / divisor, i / divisor);
	}
	
	/**
	 * Returns the value {@code (this / divisor)}.
	 * 
	 * @param divisor value by which this number is to be divided.
	 * @return {@code (this / divisor)}
	 * @throws ArithmeticException if the divisor is zero
	 */
	public DoubleComplex divide(double divisor) {
		return new DoubleComplex(r / divisor, i / divisor);
	}
	
	@Override
	public DoubleComplex pow(int exponent) {
		return Exponentiation.pow(this, exponent, ONE);
	}
	
	/**
	 * Returns the absolute value of this.
	 * 
	 * @return the absolute value of this
	 */
	public double abs() {
		return Math.hypot(r, i);
	}
	
	/**
	 * Returns the distance (absolute difference) between this and the given complex number.
	 * @param c a complex number
	 * @return the distance
	 */
	public double distanceTo(DoubleComplex c) {
		return Math.hypot(r - c.r, i - c.i);
	}
	
	/**
	 * Returns the complex conjugate of this.
	 * 
	 * @return the complex conjugate of this
	 */
	public DoubleComplex conjugate() {
		return new DoubleComplex(r, -i);
	}
	
	/**
	 * Returns {@code true} if this is finite, {@code false} otherwise. A complex
	 * number is finite if neither component is {@code NaN} or {@code infinite}.
	 * 
	 * @return <code>true</code> if this is finite
	 */
	public boolean isFinite() {
		return Double.isFinite(r) && Double.isFinite(i);
	}
	
	/**
	 * Returns <code>true</code> if this has at least one {@code NaN} component.
	 * 
	 * @return <code>true</code> if this is {@code NaN}.
	 */
	public boolean isNaN() {
		return Double.isNaN(r) || Double.isNaN(i);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof DoubleComplex) {
			DoubleComplex doubleComplex = (DoubleComplex) obj;
			return r == doubleComplex.r && i == doubleComplex.i;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(r, i);
	}
	
	@Override
	public String toString() {
		String imaginaryAbsoluteString = Math.abs(i) == 1 ? "i" : Math.abs(i) + "i";
		if (i == 0) {
			return Double.toString(r);
		} else if (r != 0) {
			String imaginarySign = i < 0 ? "-" : "+";
			return r + imaginarySign + imaginaryAbsoluteString;
		} else if (i < 0) {
			return "-" + imaginaryAbsoluteString;
		} else {
			return imaginaryAbsoluteString;
		}
	}
	
	private static final String REAL_PATTERN_STRING = "^(\\-?[\\d\\.]+)$";
	private static final String IMAGINARY_PATTERN_STRING = "^(\\-?[\\d\\.]*)i$";
	private static final String COMPLEX_PATTERN_STRING = "^((\\-?[\\d\\.]+)\\+?(\\-?[\\d\\.]*))i$";
	private static final Pattern COMPLEX_PATTERN = Pattern
			.compile(REAL_PATTERN_STRING + "|" + IMAGINARY_PATTERN_STRING + "|" + COMPLEX_PATTERN_STRING);
	
	/**
	 * Creates a complex number from a string such as {@code 3+i} or {@code -42i}.
	 * 
	 * @param s a string
	 * @return a complex number
	 */
	public static DoubleComplex valueOf(String s) {
		Matcher matcher = COMPLEX_PATTERN.matcher(s);
		if (matcher.find()) {
			if (matcher.group(1) != null) {
				double r = Double.valueOf(matcher.group(1));
				return new DoubleComplex(r);
			} else if (matcher.group(2) != null) {
				double i = valueOfImaginary(matcher.group(2));
				return new DoubleComplex(0, i);
			} else {
				double r = Double.valueOf(matcher.group(4));
				double i = valueOfImaginary(matcher.group(5));
				return new DoubleComplex(r, i);
			}
		} else {
			throw new NumberFormatException("Invalid complex number: " + s);
		}
	}
	
	private static double valueOfImaginary(String match) {
		if (match.isEmpty()) {
			return 1;
		} else if (match.equals("-")) {
			return -1;
		} else {
			return Double.valueOf(match);
		}
	}
}
