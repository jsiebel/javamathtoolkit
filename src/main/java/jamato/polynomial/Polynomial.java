package jamato.polynomial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jamato.algebra.Exponentation;
import jamato.algebra.Ring;

/**
 * A polynomial is the sum of multiples of powers of a variable.
 * @author JSiebel
 *
 * @param <T> the type of the polynomial's variable and coefficients
 */
public class Polynomial<T extends Ring<T>> implements Ring<Polynomial<T>>, UnaryOperator<T>{

	protected final int degree;
	protected final List<T> coefficients;

	/**
	 * Creates a Polynomial with the given coefficients.
	 * 
	 * @param coefficients the coefficients, ordered from lowest to highest; may not
	 *                     be empty
	 * @throws IllegalArgumentException is {@code coefficients} is empty
	 */
	public Polynomial(List<T> coefficients) {
		if (coefficients.isEmpty()) {
			throw new IllegalArgumentException();
		}else {
			int degree = coefficients.size() - 1;
			while (degree > 0 && coefficients.get(degree).isZero()) {
				degree--;
			}
			this.coefficients = new ArrayList<>(coefficients.subList(0, degree+1));
			this.degree = degree;
		}
	}
	
	protected Polynomial(int degree, List<T> coefficients){
		this.degree = degree;
		this.coefficients = coefficients;
	}

	protected Polynomial(int degree, Stream<T> coefficients) {
		this(degree, coefficients.collect(Collectors.toList()));
	}

	protected Polynomial(Stream<T> coefficients) {
		this(coefficients.collect(Collectors.toList()));
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
	public T getCoefficient(int degree) {
		if (degree < 0) {
			throw new IllegalArgumentException();
		}else if (degree <= this.degree) {
			return coefficients.get(degree);
		}else{
			return getZeroCoefficient();
		}
	}

	@Override
	public Polynomial<T> negate() {
		return new Polynomial<T>(degree, coefficients.stream().map(T::negate));
	}

	@Override
	public boolean isZero() {
		return degree == 0 && coefficients.get(0).isZero();
	}

	@Override
	public Polynomial<T> add(Polynomial<T> summand) {
		List<T> coefficients = new ArrayList<>(degree+1);
		int d;
		for (d = 0; d <= this.degree && d <= summand.degree; d++) {
			coefficients.add(this.coefficients.get(d).add(summand.coefficients.get(d)));
		}
		for (; d <= this.degree; d++) {
			coefficients.add(this.coefficients.get(d));
		}
		for (; d <= summand.degree; d++) {
			coefficients.add(summand.coefficients.get(d));
		}
		return new Polynomial<T>(coefficients);
	}

	@Override
	public Polynomial<T> subtract(Polynomial<T> subtrahend) {
		List<T> coefficients = new ArrayList<>(degree+1);
		int d = 0;
		for (; d <= this.degree && d <= subtrahend.degree; d++) {
			coefficients.add(this.coefficients.get(d).subtract(subtrahend.coefficients.get(d)));
		}
		for (; d <= this.degree; d++) {
			coefficients.add(this.coefficients.get(d));
		}
		for (; d <= subtrahend.degree; d++) {
			coefficients.add(subtrahend.coefficients.get(d).negate());
		}
		return new Polynomial<T>(coefficients);
	}
	
	@Override
	public boolean isOne() {
		return degree == 0 && coefficients.get(0).isOne();
	}

	@Override
	public Polynomial<T> multiply(Polynomial<T> factor) {
		List<T> coefficients = new ArrayList<>();
		for (int resultDegree = 0; resultDegree <= this.degree + factor.degree; resultDegree++) {
			int d = Math.max(0, resultDegree-factor.degree);
			int e = resultDegree - d;
			T r = this.coefficients.get(d).multiply(factor.coefficients.get(e));
			for (d++; d <= this.degree && d <= resultDegree; d++) {
				e = resultDegree - d;
				r = r.add(this.coefficients.get(d).multiply(factor.coefficients.get(e)));
			}
			coefficients.add(r);
		}
		return new Polynomial<T>(coefficients);
	}
	
	/**
	 * Returns a Polynomial with the value {@code (this * factor)}.
	 * 
	 * @param factor value to be multiplied by this T
	 * @return {@code (this * factor)}
	 */
	public Polynomial<T> multiply(T factor) {
		return new Polynomial<T>(coefficients.stream().map(c -> c.multiply(factor)));
	}

	/**
	 * Divides this polynomial by another, analogous to integer division. The result
	 * is a polynomial {@code b} with {@code this = b * divisor + r}, where
	 * {@code r.degree < divisor.degree}.
	 * 
	 * @param divisor the polynomial this is divided by
	 * @return {@code this/divisor}
	 * @throws ArithmeticException           if the divisor is zero
	 * @throws UnsupportedOperationException if the type of the coefficients can't
	 *                                       be {@link Ring#divide divide}d.
	 */
	@Override
	public Polynomial<T> divide(Polynomial<T> divisor) {
		int degree = this.degree - divisor.degree;
		if (divisor.isZero()) {
			throw new ArithmeticException("Division by zero");
		}else if (isZero()) {
			return this;
		}else if (degree < 0) {
			return getZero();
		} else {
			List<T> remainder = new ArrayList<>(coefficients);
			List<T> coefficients = new LinkedList<>();
			for (int d = degree; d >= 0; d--) {
				T nextCoefficient = remainder.get(d + divisor.degree).divide(divisor.coefficients.get(divisor.degree));
				coefficients.add(0, nextCoefficient);
				for (int e = 0; e < divisor.degree; e++) {
					remainder.set(d+e, remainder.get(d+e).subtract(divisor.coefficients.get(e).multiply(nextCoefficient)));
				}
			}
			return new Polynomial<T>(degree, coefficients);
		}
	}

	/**
	 * Divides this polynomial by the given divisor.
	 * 
	 * @param divisor the divisor this polynomial is divided by
	 * @return {@code this/divisor}
	 * @throws ArithmeticException           if the divisor is zero
	 * @throws UnsupportedOperationException if the type of the coefficients can't
	 *                                       be {@link Ring#divide divide}d.
	 */
	public Polynomial<T> divide(T divisor) {
		return new Polynomial<T>(coefficients.stream().map(c -> c.divide(divisor)));
	}
	
	@Override
	public Polynomial<T> divide(long divisor) {
		return new Polynomial<T>(coefficients.stream().map(c -> c.divide(divisor)));
	}

	/**
	 * Returns the polynomial <code>(this<sup>exponent</sup>)</code>.
	 * @param exponent the exponent of the exponentation, must be positive or zero
	 * @return <code>this<sup>exponent</sup></code>
	 * @throws ArithmeticException if the exponent is negative
	 */
	@Override
	public Polynomial<T> pow(int exponent) {
		if (exponent > 0) {
			return Exponentation.pow(this, exponent);
		}else if (exponent == 0) {
			return isZero() ? this : getOnePolynomial();
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
	public Polynomial<T> substitute(Polynomial<T> substituent) {
		if (degree == 0) {
			return this;
		}else {
			List<T> resultCoefficients = new ArrayList<>(degree * substituent.degree);
			resultCoefficients.add(coefficients.get(0).add(coefficients.get(1).multiply(substituent.coefficients.get(0))));
			for (int d=1; d<=substituent.degree; d++) {
				resultCoefficients.add(coefficients.get(1).multiply(substituent.coefficients.get(d)));
			}
			Polynomial<T> substituentPow = substituent;
			for (int d = 2; d <= degree; d++) {
				substituentPow = substituentPow.multiply(substituent);
				for (int e=0; e<resultCoefficients.size(); e++) {
					resultCoefficients.set(e, resultCoefficients.get(e).add(coefficients.get(d).multiply(substituentPow.coefficients.get(e))));
				}
				for (int e=resultCoefficients.size(); e <= substituentPow.degree; e++) {
					resultCoefficients.add(coefficients.get(d).multiply(substituentPow.coefficients.get(e)));
				}
			}
			return new Polynomial<T>(resultCoefficients);
		}
	}
	
	/**
	 * Returns the derivative of this polynomial.
	 * @return the derivative of this polynomial
	 */
	public Polynomial<T> derivative(){
		if (isZero()) {
			return this;
		} else if (degree == 0){
			return getZero() ;
		} else {
			List<T> coefficients = new ArrayList<>(degree);
			for (int d = 1; d <= degree; d++) {
				coefficients.add(this.coefficients.get(d).multiply(d));
			}
			return new Polynomial<T>(coefficients);
		}
	}

	@Override
	public T apply(T t) {
		T result = coefficients.get(0);
		T tPow = t;
		for (int d = 1; d <= degree; d++) {
			result = result.add(coefficients.get(d).multiply(tPow));
			tPow = tPow.multiply(t);
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof Polynomial<?>) {
			Polynomial<?> polynomial = (Polynomial<?>)obj;
			return coefficients.equals(polynomial.coefficients);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return coefficients.hashCode();
	}
	
	@Override
	public String toString() {
		if (degree == 0) {
			return coefficients.get(0).toString();
		} else {
			StringBuilder sb = new StringBuilder();
			if (coefficients.get(degree).isOne()) {
				// nothing
			}else if (coefficients.get(degree).negate().isOne()) {
				sb.append("-");
			}else if (hasInnerAddition(coefficients.get(degree))){
				sb.append("(");
				sb.append(coefficients.get(degree));
				sb.append(") ");
			}else {
				sb.append(coefficients.get(degree));
				sb.append(" ");
			}
			sb.append(variableExponentString(degree));
			for (int d = degree-1; d > 0; d--) {
				T coefficient = coefficients.get(d);
				if (!coefficient.isZero()) {
					String coefficientString = coefficient.toString();
					if (coefficient.isOne()) {
						sb.append(" + ");
					}else if (coefficient.negate().isOne()) {
						sb.append(" - ");
					}else if (hasInnerAddition(coefficientString)) {
						sb.append(" + (");
						sb.append(coefficientString);
						sb.append(") ");
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
			T coefficient = coefficients.get(0);
			if (!coefficient.isZero()) {
				String coefficientString = coefficient.toString();
				if (coefficientString.charAt(0) == '-' && !hasInnerAddition(coefficientString)) {
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
	
	private static boolean hasInnerAddition(Object expression) {
		String string = expression.toString();
		for (int i = 1; i < string.length(); i++) {
			if (string.charAt(i) == '+' || string.charAt(i) == '-') {
				return true;
			}
		}
		return false;
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
	 * Creates a polynomial from a string such as "{@code 5x^4-4x³+3x²}" or "{@code x+2x-3}".
	 * <p>
	 * The string must consist of one or more term, concatenated by a "{@code +}" or
	 * "{@code -}" sign. A term consists of an optional coefficient and an optional
	 * variable power, it may not be empty.
	 * <p>
	 * The coefficient's format is given by the {@code coefficientValueOf}
	 * parameter, it may not contain a "{@code +}" or "{@code -}" sign. If the
	 * coefficient is omitted, it is assumed to be {@code 1}.
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
	 * 
	 * @param <T>                the type of the polynomial's variable and
	 *                           coefficients
	 * @param s                  a string
	 * @param coefficientValueOf a function creating a coefficient from a string
	 * @param zero               the zero element of the coefficient type
	 * @param one                the one element of the coefficient type
	 * @return a polynomial
	 * @throws IllegalArgumentException if the string can't be parsed
	 */
	public static <T extends Ring<T>> Polynomial<T> valueOf(String s, Function<String, T> coefficientValueOf, T zero, T one){
		List<T> coefficients = new ArrayList<>();
		coefficients.add(zero);
		for (String term : TERM_SPLIT_PATTERN.split(s.replaceAll("\\s", ""))) {
			Matcher matcher = TERM_PATTERN.matcher(term);
			if (matcher.find()) {
				T coefficient;
				int degree;
				if (matcher.group(1).isEmpty()) {
					coefficient = one;
				}else if (matcher.group(1).equals("-")) {
					coefficient = one.negate();
				}else {
					coefficient = coefficientValueOf.apply(matcher.group(1));
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
					coefficients.add(zero);
				}
				coefficients.set(degree, coefficients.get(degree).add(coefficient));
			} else {
				throw new IllegalArgumentException("Invalid term: " + term);
			}
		}
		return new Polynomial<T>(coefficients);
	}

	/**
	 * Creates a polynomial from a string such as "{@code 5x^4-4x³+3x²}" or "{@code x+2x-3}".
	 * <p>
	 * The string must consist of one or more term, concatenated by a "{@code +}" or
	 * "{@code -}" sign. A term consists of an optional coefficient and an optional
	 * variable power, it may not be empty.
	 * <p>
	 * The coefficient's format is given by the {@code coefficientValueOf}
	 * parameter, it may not contain a "{@code +}" or "{@code -}" sign. If the
	 * coefficient is omitted, it is assumed to be {@code 1}.
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
	 * 
	 * @param <T>                the type of the polynomial's variable and
	 *                           coefficients
	 * @param s                  a string
	 * @param coefficientValueOf a function creating a coefficient from a string
	 * @return a polynomial
	 * @throws IllegalArgumentException if the string can't be parsed
	 */
	public static <S extends Ring<S>> Polynomial<S> valueOf(String s, Function<String, S> coefficientValueOf){
		S zero = coefficientValueOf.apply("0");
		S one = coefficientValueOf.apply("1");
		return valueOf(s, coefficientValueOf, zero, one);
	}
	
	/**
	 * Returns the zero coefficient.
	 * @return the zero coefficient
	 */
	protected T getZeroCoefficient() {
		T t = coefficients.get(0);
		return t.subtract(t);
	}

	/**
	 * Returns the constant zero polynomial.
	 * @return the constant zero polynomial 
	 */
	protected Polynomial<T> getZero() {
		return new Polynomial<T>(0, Collections.singletonList(getZeroCoefficient()));
	}

	/**
	 * Returns the constant one polynomial. 
	 * @return the constant one polynomial
	 * @throws UnsupportedOperationException if this is zero
	 */
	protected Polynomial<T> getOnePolynomial() {
		T t = coefficients.get(degree);
		T one = t.divide(t);
		return new Polynomial<>(1, Collections.singletonList(one));
	}
}
