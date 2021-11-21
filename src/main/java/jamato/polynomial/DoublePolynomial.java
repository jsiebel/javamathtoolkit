package jamato.polynomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;

import jamato.algebra.Exponentation;
import jamato.algebra.Ring;

/**
 * A DoublePolynomial is the sum of multiples of powers of a double variable.
 * 
 * @author JSiebel
 */
public class DoublePolynomial implements Ring<DoublePolynomial>, DoubleUnaryOperator{

	protected final int degree;
	protected final double[] coefficients;

	/** The constant zero polynomial. */
	public static final DoublePolynomial ZERO = new DoublePolynomial(0);
	
	/** The constant one polynomial. */
	public static final DoublePolynomial ONE = new DoublePolynomial(1);
	
	/**
	 * Creates a Polynomial with the given coefficients.
	 * 
	 * @param coefficients the coefficients, ordered from lowest to highest; may not
	 *                     be empty
	 * @throws IllegalArgumentException is {@code coefficients} is empty
	 */
	public DoublePolynomial(double... coefficients) {
		if (coefficients.length == 0) {
			this.coefficients = new double[] {0};
			this.degree = 0;
		}else {
			int degree = coefficients.length - 1;
			while (degree > 0 && coefficients[degree] == 0) {
				degree--;
			}
			this.coefficients = Arrays.copyOf(coefficients, degree+1);
			this.degree = degree;
		}
	}
	
	protected DoublePolynomial(int degree, double[] coefficients){
		this.degree = degree;
		this.coefficients = coefficients;
	}

	protected DoublePolynomial(int degree, DoubleStream coefficients) {
		this(degree, coefficients.toArray());
	}

	protected DoublePolynomial(DoubleStream coefficients) {
		this(coefficients.toArray());
	}

	/**
	 * Returns this polynomial's degree, that is the highest degree with a non-zero
	 * coefficient. If this is the zero polynomial, 0 is returned.
	 * 
	 * @return this polynomial's degree
	 */
	public int getDegree() {
		return degree;
	}
	
	/**
	 * Returns the coefficient of the given degree. If the given degree is higher
	 * than this polynomial's degree, zero is returned.
	 * 
	 * @param degree the degree
	 * @return the coefficient of the given degree
	 * @throws IllegalArgumentException if degree is negative
	 */
	public double getCoefficient(int degree) {
		if (degree < 0) {
			throw new IllegalArgumentException();
		}else if (degree <= this.degree) {
			return coefficients[degree];
		}else{
			return 0;
		}
	}

	@Override
	public DoublePolynomial negate() {
		return new DoublePolynomial(degree, Arrays.stream(coefficients).map(d -> -d));
	}

	@Override
	public boolean isZero() {
		return degree == 0 && coefficients[0] == 0;
	}

	@Override
	public DoublePolynomial add(DoublePolynomial summand) {
		int degree = Math.max(this.degree, summand.degree);
		double[] coefficients = Arrays.copyOf(this.coefficients, degree+1);
		for (int d = 0; d <= summand.degree; d++) {
			coefficients[d] += summand.coefficients[d];
		}
		return new DoublePolynomial(coefficients);
	}

	@Override
	public DoublePolynomial subtract(DoublePolynomial subtrahend) {
		int degree = Math.max(this.degree, subtrahend.degree);
		double[] coefficients = Arrays.copyOf(this.coefficients, degree+1);
		for (int d = 0; d <= subtrahend.degree; d++) {
			coefficients[d] -= subtrahend.coefficients[d];
		}
		return new DoublePolynomial(coefficients);
	}
	
	@Override
	public boolean isOne() {
		return degree == 0 && coefficients[0] == 1;
	}

	@Override
	public DoublePolynomial multiply(DoublePolynomial factor) {
		double[] coefficients = new double[this.degree + factor.degree + 1];
		for (int resultDegree = 0; resultDegree <= this.degree + factor.degree; resultDegree++) {
			for (int d = Math.max(0, resultDegree-factor.degree); d <= this.degree && d <= resultDegree; d++) {
				int e = resultDegree - d;
				coefficients[resultDegree] += this.coefficients[d] * factor.coefficients[e];
			}
		}
		return new DoublePolynomial(coefficients);
	}
	
	/**
	 * Returns a DoublePolynomial with the value {@code (this * factor)}.
	 * 
	 * @param factor value to be multiplied by this
	 * @return {@code (this * factor)}
	 */
	public DoublePolynomial multiply(double factor) {
		return new DoublePolynomial(Arrays.stream(coefficients).map(c -> c * factor));
	}

	/**
	 * Divides this polynomial by another, analogous to integer division. The result
	 * is a polynomial {@code b} with {@code this = b * divisor + r}, where
	 * {@code r.degree < divisor.degree}.
	 * 
	 * @param divisor the polynomial this is divided by
	 * @return {@code this/divisor}
	 * @throws ArithmeticException if the divisor is zero
	 */
	@Override
	public DoublePolynomial divide(DoublePolynomial divisor) {
		int degree = this.degree - divisor.degree;
		if (divisor.isZero()) {
			throw new ArithmeticException("Division by zero");
		}else if (isZero() || degree < 0) {
			return ZERO;
		} else {
			double[] remainder = coefficients.clone();
			double[] coefficients = new double[degree+1];
			for (int d = degree; d >= 0; d--) {
				double nextCoefficient = remainder[d + divisor.degree] / divisor.coefficients[divisor.degree];
				coefficients[d] = nextCoefficient;
				for (int e = 0; e < divisor.degree; e++) {
					remainder[d+e] -= divisor.coefficients[e] * nextCoefficient;
				}
			}
			return new DoublePolynomial(degree, coefficients);
		}
	}
	
	@Override
	public DoublePolynomial divide(long divisor) {
		return new DoublePolynomial(Arrays.stream(coefficients).map(c -> c / divisor));
	}

	/**
	 * Divides this polynomial by the given divisor.
	 * 
	 * @param divisor the divisor this polynomial is divided by
	 * @return {@code this/divisor}
	 */
	public DoublePolynomial divide(double divisor) {
		return new DoublePolynomial(Arrays.stream(coefficients).map(c -> c / divisor));
	}

	/**
	 * Returns a DoublePolynomial with the value <code>(this<sup>exponent</sup>)</code>.
	 * @param exponent the exponent of the exponentation, must be positive or zero
	 * @return <code>this<sup>exponent</sup></code>
	 * @throws ArithmeticException if the exponent is negative
	 */
	@Override
	public DoublePolynomial pow(int exponent) {
		if (exponent >= 0) {
			return Exponentation.pow(this, exponent, ONE);
		}else {
			throw new ArithmeticException();
		}
	}

	/**
	 * Substitutes the polynomial variable of this polynomial with the given
	 * polynomial. Example: Calling {@code substitute} on the polynomial
	 * {@code x²+4} with the parameter polynomial {@code x³+x} results in
	 * <code>(x³+x)²+4 = x<sup>6</sup>+2x<sup>4</sup>+x²+4</code>.
	 * 
	 * @param substituent the polynomial that replaces the variable
	 * @return the polynomial with the substituted variable.
	 */
	public DoublePolynomial substitute(DoublePolynomial substituent) {
		if (degree == 0) {
			return this;
		}else {
			double[] resultCoefficients = new double[degree * substituent.degree + 1];
			resultCoefficients[0] = coefficients[0] + coefficients[1] * substituent.coefficients[0];
			for (int d=1; d<=substituent.degree; d++) {
				resultCoefficients[d] = coefficients[1] * substituent.coefficients[d];
			}
			DoublePolynomial substituentPow = substituent;
			for (int d = 2; d <= degree; d++) {
				substituentPow = substituentPow.multiply(substituent);
				for (int e=0; e <= substituentPow.degree; e++) {
					resultCoefficients[e] += coefficients[d] * substituentPow.coefficients[e];
				}
			}
			return new DoublePolynomial(resultCoefficients);
		}
	}
	
	/**
	 * Returns the derivative of this polynomial.
	 * @return the derivative of this polynomial
	 */
	public DoublePolynomial derivative(){
		if (degree == 0){
			return ZERO ;
		} else {
			double[] coefficients = new double[degree];
			for (int d = 1; d <= degree; d++) {
				coefficients[d-1] = this.coefficients[d] * d;
			}
			return new DoublePolynomial(coefficients);
		}
	}

	@Override
	public double applyAsDouble(double t) {
		double result = coefficients[0];
		double tPow = t;
		for (int d = 1; d <= degree; d++) {
			result += coefficients[d] * tPow;
			tPow *= t;
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof DoublePolynomial) {
			DoublePolynomial polynomial = (DoublePolynomial)obj;
			return coefficientsEqual(coefficients, polynomial.coefficients);
		} else {
			return false;
		}
	}
	
	/**
	 * Compares two coefficient arrays. Unlike {@link Arrays#equals(double[], double[])}, positive and negative zero are considered equal.
	 * @param coefficients1 a coefficient array
	 * @param coefficients2 a coefficient array
	 * @return <code>true</code> if both arrays are equal
	 */
	private static boolean coefficientsEqual(double[] coefficients1, double[] coefficients2) {
		if (coefficients1.length != coefficients2.length) {
			return false;
		}
		for (int i = 0; i < coefficients1.length; i++) {
			double c1 = coefficients1[i];
			double c2 = coefficients2[i];
			if (c1 != c2 && !(Double.isNaN(c1) && Double.isNaN(c2))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(coefficients);
	}
	
	@Override
	public String toString() {
		if (degree == 0) {
			return String.valueOf(coefficients[0]);
		} else {
			StringBuilder sb = new StringBuilder();
			if (coefficients[degree] == 1) {
				// nothing
			}else if (coefficients[degree] == -1) {
				sb.append("-");
			}else {
				sb.append(coefficients[degree]);
				sb.append(" ");
			}
			sb.append(variableExponentString(degree));
			for (int d = degree-1; d > 0; d--) {
				double coefficient = coefficients[d];
				if (coefficient != 0) {
					String coefficientString = String.valueOf(coefficient);
					if (coefficient == 1) {
						sb.append(" + ");
					}else if (coefficient == -1) {
						sb.append(" - ");
					}else if (coefficientString.charAt(0) == '-') {
						sb.append(" - ");
						sb.append(coefficientString.substring(1));
						sb.append(" ");
					}else {
						sb.append(" + ");
						sb.append(coefficientString);
						sb.append(" ");
					}
					sb.append(variableExponentString(d));
				}
			}
			double coefficient = coefficients[0];
			if (coefficient != 0) {
				String coefficientString = String.valueOf(coefficient);
				if (coefficientString.charAt(0) == '-') {
					sb.append(" - ");
					sb.append(coefficientString.substring(1));
				}else{
					sb.append(" + ");
					sb.append(coefficientString);
				}
			}
			return sb.toString();
		}
	}
	
	private static String variableExponentString(int degree) {
		switch (degree) {
		case 1: return "x";
		case 2: return "x²";
		case 3: return "x³";
		default: return "x^" + degree;
		}
	}
	
	private static final Pattern TERM_SPLIT_PATTERN = Pattern.compile("(?=\\-)|\\+");
	private static final Pattern TERM_PATTERN = Pattern.compile("^(\\–|.*?)([a-zA-Z](|²|³|\\^(\\d+)))?$");
	
	/**
	 * Creates a polynomial from a string such as "{@code 5x^4-4x³+3x²}" or
	 * "{@code x+2x-3}".
	 * <p>
	 * The string must consist of one or more term, concatenated by a "{@code +}" or
	 * "{@code -}" sign. A term consists of an optional coefficient and an optional
	 * variable power, it may not be empty.
	 * <p>
	 * The coefficient is a double as specified in {@link Double#valueOf}, it may
	 * not contain a "{@code +}" or "{@code -}" sign. If the coefficient is omitted,
	 * it is assumed to be {@code 1}.
	 * <p>
	 * The variable power can be empty (for the constant term), or the variable
	 * followed by
	 * <ul>
	 * <li>nothing (for the linear term)</li>
	 * <li>"{@code²}" or "{@code³}" (for the square or cubic term)</li>
	 * <li>the "{@code^}" sign and the term's degree</li>
	 * </ul>
	 * The variable can be any letter.
	 * 
	 * @param s a string
	 * @return a polynomial
	 * @throws IllegalArgumentException if the string can't be parsed
	 * @throws NumberFormatException    if a coefficient can't be parsed
	 */
	public static DoublePolynomial valueOf(String s){
		List<Double> coefficients = new ArrayList<>();
		coefficients.add(0d);
		for (String term : TERM_SPLIT_PATTERN.split(s.replaceAll("\\s", ""))) {
			Matcher matcher = TERM_PATTERN.matcher(term);
			if (matcher.find()) {
				double coefficient;
				int degree;
				if (matcher.group(1).isEmpty()) {
					coefficient = 1;
				}else if (matcher.group(1).equals("-")) {
					coefficient = -1;
				}else {
					coefficient = Double.valueOf(matcher.group(1));
				}
				if (matcher.group(2) == null) {
					degree = 0;
				}else {
					switch (matcher.group(3)) {
					case "": degree = 1; break;
					case "²": degree = 2; break;
					case "³": degree = 3; break;
					default: degree = Integer.valueOf(matcher.group(4));
					}
				}
				while (coefficients.size() <= degree) {
					coefficients.add(0d);
				}
				coefficients.set(degree, coefficients.get(degree) + coefficient);
			} else {
				throw new IllegalArgumentException("Invalid term: " + term);
			}
		}
		return new DoublePolynomial(coefficients.stream().mapToDouble(d -> d));
	}
}
