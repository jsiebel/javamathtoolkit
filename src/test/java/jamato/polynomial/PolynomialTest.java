package jamato.polynomial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jamato.number.IntRational;

class PolynomialTest {
	
	static final IntRational FOUR = new IntRational(4);
	
	static final IntRational TWO = new IntRational(2);
	
	static final IntRational ONE = new IntRational(1);
	
	static final IntRational HALF = new IntRational(1, 2);
	
	static final IntRational ZERO = new IntRational(0);
	
	@ParameterizedTest
	@MethodSource
	void testIntConstructor(IntRational[] coefficients, int resultDegree, IntRational[] resultCoefficients) {
		Polynomial<IntRational> intRationalPolynomial = valueOf(coefficients);
		assertEquals(resultDegree, intRationalPolynomial.getDegree());
		for (int i = 0; i < resultCoefficients.length; i++) {
			assertEquals(resultCoefficients[i], intRationalPolynomial.getCoefficient(i));
		}
	}
	
	static Stream<Arguments> testIntConstructor() {
		return Stream.of(
				Arguments.of(new IntRational[] { ZERO }, 0, new IntRational[] { ZERO, ZERO }),
				Arguments.of(new IntRational[] { ONE, TWO, FOUR }, 2, new IntRational[] { ONE, TWO, FOUR, ZERO }),
				Arguments.of(
						new IntRational[] { ONE, TWO, FOUR, ZERO, ZERO },
						2,
						new IntRational[] { ONE, TWO, FOUR, ZERO }),
				Arguments.of(new IntRational[] { ONE, ZERO }, 0, new IntRational[] { ONE, ZERO }),
				Arguments.of(new IntRational[] { ZERO, ZERO }, 0, new IntRational[] { ZERO }),
				Arguments.of(new IntRational[] { ZERO, HALF }, 1, new IntRational[] { ZERO, HALF, ZERO }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testStringConstructor(String string, Polynomial<IntRational> polynomial) {
		assertEquals(polynomial, valueOf(string));
	}
	
	static Stream<Arguments> testStringConstructor() {
		return Stream.of(
				Arguments.of("-2/37", valueOf(new IntRational(-2, 37))),
				Arguments.of("-2x+1", valueOf(ONE, new IntRational(-2))),
				Arguments.of("5x^2-3", valueOf(new IntRational(-3), ZERO, new IntRational(5))),
				Arguments.of("1/2A³+1/2+4x²", valueOf(HALF, ZERO, FOUR, HALF)),
				Arguments.of("-1/2 t² + 1/3 t^2 + 0 + 1/6 t² + 0t^15", valueOf(ZERO)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsZero(Polynomial<IntRational> doublePolynomial, boolean result) {
		assertEquals(result, doublePolynomial.isZero());
	}
	
	static Stream<Arguments> testIsZero() {
		return Stream.of(
				Arguments.of(valueOf("0"), true),
				Arguments.of(valueOf("-28"), false),
				Arguments.of(valueOf("-227x"), false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testNegate(Polynomial<IntRational> doublePolynomial, Polynomial<IntRational> result) {
		assertEquals(result, doublePolynomial.negate());
	}
	
	static Stream<Arguments> testNegate() {
		return Stream.of(
				Arguments.of(valueOf("0"), valueOf("0")),
				Arguments.of(valueOf("-52x³+x-12"), valueOf("52x³-x+12")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAdd(Polynomial<IntRational> summand1, Polynomial<IntRational> summand2, Polynomial<IntRational> result) {
		assertEquals(result, summand1.add(summand2));
	}
	
	static Stream<Arguments> testAdd() {
		return Stream.of(
				Arguments.of(valueOf("2x+4"), valueOf("-2x+2"), valueOf("6")),
				Arguments.of(valueOf(HALF), valueOf(HALF), valueOf(ONE)),
				Arguments.of(valueOf("4x²+3x+2"), valueOf("-4x²-3x-2"), valueOf(ZERO)),
				Arguments.of(valueOf("3x²+2x+1"), valueOf("5x+4"), valueOf("3x²+7x+5")),
				Arguments.of(valueOf(ONE), valueOf("5x+4"), valueOf("5x+5")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSubtract(Polynomial<IntRational> minuend, Polynomial<IntRational> subtrahend,
			Polynomial<IntRational> result) {
		assertEquals(result, minuend.subtract(subtrahend));
	}
	
	static Stream<Arguments> testSubtract() {
		return Stream.of(
				Arguments.of(valueOf("2x+4"), valueOf("-2x+2"), valueOf("4x+2")),
				Arguments.of(valueOf(HALF), valueOf(HALF), valueOf(ZERO)),
				Arguments.of(valueOf("4x²+3x+2"), valueOf("4x²+3x+2"), valueOf(ZERO)),
				Arguments.of(valueOf("3x²+2x+1"), valueOf("5x+4"), valueOf("3x²-3x-3")),
				Arguments.of(valueOf(ONE), valueOf("5x+4"), valueOf("-5x-3")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsOne(Polynomial<IntRational> doublePolynomial, boolean result) {
		assertEquals(result, doublePolynomial.isOne());
	}
	
	static Stream<Arguments> testIsOne() {
		return Stream.of(
				Arguments.of(valueOf("1"), true),
				Arguments.of(valueOf("-1"), false),
				Arguments.of(valueOf("x+1"), false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiply(Polynomial<IntRational> factor1, Polynomial<IntRational> factor2,
			Polynomial<IntRational> result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiply() {
		return Stream.of(
				Arguments.of(valueOf("4x³+3x²+2x+1"), valueOf("0x+0"), valueOf(ZERO)),
				Arguments.of(valueOf(HALF, HALF), valueOf(ONE, ONE), valueOf(HALF, ONE, HALF)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiplyByBaseType(Polynomial<IntRational> factor1, IntRational factor2, Polynomial<IntRational> result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiplyByBaseType() {
		return Stream.of(
				Arguments.of(valueOf("5x^4+4x³+3x²+2x+1"), ZERO, valueOf(ZERO)),
				Arguments.of(valueOf(ONE, TWO, ONE, TWO), new IntRational(1, 2), valueOf(HALF, ONE, HALF, ONE)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiplyByLong(Polynomial<IntRational> factor1, long factor2, Polynomial<IntRational> result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiplyByLong() {
		return Stream.of(
				Arguments.of(valueOf("6x^5+5x^4+4x³+3x²+2x+1"), 0, valueOf(ZERO)),
				Arguments.of(valueOf("6x³+5x²+4x+3"), -200, valueOf("-1200x³-1000x²-800x-600")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivide(Polynomial<IntRational> dividend, Polynomial<IntRational> divisor, Polynomial<IntRational> result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivide() {
		return Stream.of(
				Arguments.of(valueOf("0"), valueOf("x^4+5x³+6x²+2"), valueOf("0")),
				Arguments.of(valueOf("4x³+3x²+2x+1"), valueOf(HALF), valueOf("8x³+6x²+4x+2")),
				Arguments.of(valueOf("x²+2x+1"), valueOf("x+1"), valueOf("x+1")),
				Arguments.of(valueOf("5x³+9x²+7x+3"), valueOf("x^4+5x³+6x²+2"), valueOf(ZERO)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivideByBaseType(Polynomial<IntRational> dividend, IntRational divisor, Polynomial<IntRational> result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivideByBaseType() {
		return Stream.of(
				Arguments.of(valueOf("110x³+110x²+220x+110"), new IntRational(11), valueOf("10x³+10x²+20x+10")),
				Arguments.of(valueOf("4x³+3x²+2x+1"), HALF, valueOf("8x³+6x²+4x+2")),
				Arguments.of(valueOf("120x³+120x²+240x+120"), new IntRational(12L), valueOf("10x³+10x²+20x+10")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivideByLong(Polynomial<IntRational> dividend, long divisor, Polynomial<IntRational> result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivideByLong() {
		return Stream.of(
				Arguments.of(valueOf("110x³+110x²+220x+110"), 11, valueOf("10x³+10x²+20x+10")),
				Arguments.of(valueOf("120x³+120x²+240x+120"), 12, valueOf("10x³+10x²+20x+10")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSubstitute(Polynomial<IntRational> doublePolynomial, Polynomial<IntRational> substituent,
			Polynomial<IntRational> result) {
		assertEquals(result, doublePolynomial.substitute(substituent));
	}
	
	static Stream<Arguments> testSubstitute() {
		return Stream.of(
				Arguments.of(valueOf("x+1"), valueOf("x+1"), valueOf("x+2")),
				// (2x+1/2)² + 2(2x+1/2) + 4 = 4x² + 6x + 21/4
				Arguments.of(valueOf("x²+2x+4"), valueOf(HALF, TWO), valueOf("4x²+6x+21/4")),
				Arguments.of(valueOf("3"), valueOf("34/4x^33-2910x^14"), valueOf("3")),
				Arguments.of(valueOf("35/4x^4+1/1000x^2+97"), valueOf(ZERO), valueOf("97")),
				Arguments.of(valueOf("10x^5+57/2x+3/2"), valueOf(ONE), valueOf("40")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPow(Polynomial<IntRational> base, int exponent, Polynomial<IntRational> result) {
		assertEquals(result, base.pow(exponent));
	}
	
	static Stream<Arguments> testPow() {
		return Stream.of(
				Arguments.of(valueOf("x³+6x²+12x+8"), 0, valueOf("1")),
				Arguments.of(valueOf("-1"), Integer.MAX_VALUE, valueOf("-1")),
				Arguments.of(valueOf("x+2"), 3, valueOf("x³+6x²+12x+8")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDerivative(Polynomial<IntRational> doublePolynomial, Polynomial<IntRational> result) {
		assertEquals(result, doublePolynomial.derivative());
	}
	
	static Stream<Arguments> testDerivative() {
		return Stream.of(
				Arguments.of(valueOf("0"), valueOf("0")),
				Arguments.of(valueOf("1/13"), valueOf("0")),
				Arguments.of(valueOf("3/2x^10+2x²+15x+1/7"), valueOf("15x^9+4x+15")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testApply(Polynomial<IntRational> doublePolynomial, IntRational x, IntRational result) {
		assertEquals(result, doublePolynomial.apply(x));
	}
	
	static Stream<Arguments> testApply() {
		return Stream.of(
				Arguments.of(valueOf("33x³"), ZERO, ZERO),
				Arguments.of(valueOf("2/5x^3-7/5"), ONE, new IntRational(-1)),
				Arguments.of(valueOf("3x³+3x²+3x+3"), new IntRational(10), new IntRational(3333)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testToString(Polynomial<IntRational> doublePolynomial, String result) {
		assertEquals(result, doublePolynomial.toString());
	}
	
	static Stream<Arguments> testToString() {
		return Stream.of(
				Arguments.of(valueOf("0"), "0"),
				Arguments.of(valueOf("1/2"), "1/2"),
				Arguments.of(valueOf("-1"), "-1"),
				Arguments.of(valueOf("x"), "x"),
				Arguments.of(valueOf("x+1"), "x + 1"),
				Arguments.of(valueOf("-2+x"), "x - 2"),
				Arguments.of(valueOf("3-x"), "-x + 3"),
				Arguments.of(valueOf("-x-4"), "-x - 4"),
				Arguments.of(valueOf("-1+2x^2"), "2 x² - 1"),
				Arguments.of(valueOf("-6 x^5 + 5 x^4 - 4 x³ + x² - x + 1"), "-6 x^5 + 5 x^4 - 4 x³ + x² - x + 1"));
	}
	
	private static Polynomial<IntRational> valueOf(IntRational... coefficients) {
		return new Polynomial<>(List.of(coefficients));
	}
	
	private static Polynomial<IntRational> valueOf(String s) {
		return Polynomial.valueOf(s, IntRational::valueOf, IntRational.ZERO, IntRational.ONE);
	}
}
