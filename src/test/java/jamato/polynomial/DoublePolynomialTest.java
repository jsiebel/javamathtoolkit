package jamato.polynomial;

import static jamato.polynomial.DoublePolynomial.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DoublePolynomialTest {
	
	@ParameterizedTest
	@MethodSource
	void testIntConstructor(double[] coefficients, int resultDegree, double[] resultCoefficients) {
		DoublePolynomial doublePolynomial = new DoublePolynomial(coefficients);
		assertEquals(resultDegree, doublePolynomial.getDegree());
		for (int i = 0; i < resultCoefficients.length; i++) {
			assertEquals(resultCoefficients[i], doublePolynomial.getCoefficient(i));
		}
	}
	
	static Stream<Arguments> testIntConstructor() {
		return Stream.of(
				Arguments.of(new double[] { 0 }, 0, new double[] { 0, 0 }),
				Arguments.of(new double[] { 1, 2, 4 }, 2, new double[] { 1, 2, 4, 0 }),
				Arguments.of(new double[] { 1, 2, 4, 0, 0 }, 2, new double[] {}),
				Arguments.of(new double[] { 1, 0 }, 0, new double[] {}),
				Arguments.of(new double[] { 0, 0 }, 0, new double[] { 0, 0 }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetDegree(DoublePolynomial DoublePolynomial, int result) {
		assertEquals(result, DoublePolynomial.getDegree());
	}
	
	static Stream<Arguments> testGetDegree() {
		return Stream.of(
				Arguments.of(new DoublePolynomial(0), 0),
				Arguments.of(new DoublePolynomial(0, 0.5), 1),
				Arguments.of(new DoublePolynomial(0, 0), 0),
				Arguments.of(new DoublePolynomial(0, 4, 0.5, 0.5), 3));
	}
	
	@ParameterizedTest
	@MethodSource
	void testValueOf(String string, DoublePolynomial result) {
		assertEquals(result, valueOf(string));
	}
	
	static Stream<Arguments> testValueOf() {
		return Stream.of(
				Arguments.of("-0.123", new DoublePolynomial(-0.123)),
				Arguments.of("-2x+1", new DoublePolynomial(1, -2)),
				Arguments.of("5x^2-3", new DoublePolynomial(-3, 0, 5)),
				Arguments.of("0.5A³+0.5+4x²", new DoublePolynomial(0.5, 0, 4, 0.5)),
				Arguments.of("-0.25 t² + 0.75 t^2 + 0 - 0.5 t² + 0t^15", new DoublePolynomial(0)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsZero(DoublePolynomial doublePolynomial, boolean result) {
		assertEquals(result, doublePolynomial.isZero());
	}
	
	static Stream<Arguments> testIsZero() {
		return Stream.of(
				Arguments.of(DoublePolynomial.ZERO, true),
				Arguments.of(new DoublePolynomial(-4), false),
				Arguments.of(new DoublePolynomial(0, 117), false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testNegate(DoublePolynomial doublePolynomial, DoublePolynomial result) {
		assertEquals(result, doublePolynomial.negate());
	}
	
	static Stream<Arguments> testNegate() {
		return Stream.of(
				Arguments.of(DoublePolynomial.ZERO, DoublePolynomial.ZERO),
				Arguments.of(new DoublePolynomial(-3, 11, 0, -2.25), new DoublePolynomial(3, -11, 0, 2.25)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAdd(DoublePolynomial summand1, DoublePolynomial summand2, DoublePolynomial result) {
		assertEquals(result, summand1.add(summand2));
	}
	
	static Stream<Arguments> testAdd() {
		return Stream.of(
				Arguments.of(valueOf("2x+4"), valueOf("-2x+2"), valueOf("6")),
				Arguments.of(new DoublePolynomial(0.5), new DoublePolynomial(0.5), new DoublePolynomial(1)),
				Arguments.of(valueOf("4x²+3x+2"), valueOf("-4x²-3x-2"), new DoublePolynomial(0)),
				Arguments.of(valueOf("3x²+2x+1"), valueOf("5x+4"), valueOf("3x²+7x+5")),
				Arguments.of(new DoublePolynomial(1), valueOf("5x+4"), valueOf("5x+5")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSubtract(DoublePolynomial minuend, DoublePolynomial subtrahend, DoublePolynomial result) {
		assertEquals(result, minuend.subtract(subtrahend));
	}
	
	static Stream<Arguments> testSubtract() {
		return Stream.of(
				Arguments.of(valueOf("2x+4"), valueOf("-2x+2"), valueOf("4x+2")),
				Arguments.of(new DoublePolynomial(0.5), new DoublePolynomial(0.5), new DoublePolynomial(0)),
				Arguments.of(valueOf("4x²+3x+2"), valueOf("4x²+3x+2"), new DoublePolynomial(0)),
				Arguments.of(valueOf("3x²+2x+1"), valueOf("5x+4"), valueOf("3x²-3x-3")),
				Arguments.of(new DoublePolynomial(1), valueOf("5x+4"), valueOf("-5x-3")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsOne(DoublePolynomial doublePolynomial, boolean result) {
		assertEquals(result, doublePolynomial.isOne());
	}
	
	static Stream<Arguments> testIsOne() {
		return Stream.of(
				Arguments.of(DoublePolynomial.ONE, true),
				Arguments.of(new DoublePolynomial(-4), false),
				Arguments.of(new DoublePolynomial(0, 117), false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiply(DoublePolynomial factor1, DoublePolynomial factor2, DoublePolynomial result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiply() {
		return Stream.of(
				Arguments.of(valueOf("4x³+3x²+2x+1"), valueOf("0x+0"), new DoublePolynomial(0)),
				Arguments.of(
						new DoublePolynomial(0.5, 0.5),
						new DoublePolynomial(1, 1),
						new DoublePolynomial(0.5, 1, 0.5)),
				Arguments.of(
						new DoublePolynomial(1, 2, 1, 2),
						new DoublePolynomial(0.5),
						new DoublePolynomial(0.5, 1, 0.5, 1)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiplyByDouble(DoublePolynomial factor1, double factor2, DoublePolynomial result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiplyByDouble() {
		return Stream.of(
				Arguments.of(valueOf("5x^4+4x³+3x²+2x+1"), 0, new DoublePolynomial(0)),
				Arguments.of(valueOf("6x^5+5x^4+4x³+3x²+2x+1"), 0, new DoublePolynomial(0)),
				Arguments.of(valueOf("6x³+5x²+4x+3"), -200, valueOf("-1200x³-1000x²-800x-600")),
				Arguments.of(valueOf("4x^5-33"), 0, new DoublePolynomial(0)),
				Arguments.of(new DoublePolynomial(1, 0, Double.MIN_VALUE), 0.1, new DoublePolynomial(0.1)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivide(DoublePolynomial dividend, DoublePolynomial divisor, DoublePolynomial result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivide() {
		return Stream.of(
				Arguments.of(valueOf("4x³+3x²+2x+1"), new DoublePolynomial(0.5), valueOf("8x³+6x²+4x+2")),
				Arguments.of(valueOf("x²+2x+1"), valueOf("x+1"), valueOf("x+1")),
				Arguments.of(valueOf("5x³+9x²+7x+3"), valueOf("x^4+5x³+6x²+2"), new DoublePolynomial(0)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivideByDouble(DoublePolynomial dividend, double divisor, DoublePolynomial result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivideByDouble() {
		return Stream.of(
				Arguments.of(valueOf("110x³+110x²+220x+110"), 11, valueOf("10x³+10x²+20x+10")),
				Arguments.of(new DoublePolynomial(1, 0, Double.MIN_VALUE), 7.13, new DoublePolynomial(1.0 / 7.13)),
				Arguments.of(new DoublePolynomial(0), 0, new DoublePolynomial(Double.NaN)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSubstitute(DoublePolynomial doublePolynomial, DoublePolynomial substituent, DoublePolynomial result) {
		assertEquals(result, doublePolynomial.substitute(substituent));
	}
	
	static Stream<Arguments> testSubstitute() {
		return Stream.of(
				Arguments.of(valueOf("x+1"), valueOf("x+1"), valueOf("x+2")),
				// (2x+1/2)² + 2(2x+1/2) + 4 = 4x² + 6x + 21/4
				Arguments.of(valueOf("x²+2x+4"), new DoublePolynomial(new double[] { 0.5, 2 }), valueOf("4x²+6x+5.25")),
				Arguments.of(valueOf("3"), valueOf("34.4x^33-2.910x^14"), valueOf("3")),
				Arguments.of(valueOf("33.2x^4+0.001x^2+97"), DoublePolynomial.ZERO, valueOf("97")),
				Arguments.of(valueOf("10x^5+28.5x+1.5"), DoublePolynomial.ONE, valueOf("40")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPow(DoublePolynomial base, int exponent, DoublePolynomial result) {
		assertEquals(result, base.pow(exponent));
	}
	
	static Stream<Arguments> testPow() {
		return Stream.of(
				Arguments.of(valueOf("x³+6x²+12x+8"), 0, valueOf("1")),
				Arguments.of(valueOf("1"), Integer.MAX_VALUE, valueOf("1")),
				Arguments.of(valueOf("x+2"), 3, valueOf("x³+6x²+12x+8")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDerivative(DoublePolynomial doublePolynomial, DoublePolynomial result) {
		assertEquals(result, doublePolynomial.derivative());
	}
	
	static Stream<Arguments> testDerivative() {
		return Stream.of(
				Arguments.of(new DoublePolynomial(0), new DoublePolynomial(0)),
				Arguments.of(new DoublePolynomial(1.22), new DoublePolynomial(0)),
				Arguments.of(valueOf("1.5x^10+2x²+15x+0.121"), valueOf("15x^9+4x+15")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testApply(DoublePolynomial doublePolynomial, double x, double result) {
		assertEquals(result, doublePolynomial.applyAsDouble(x), 0);
	}
	
	static Stream<Arguments> testApply() {
		return Stream.of(
				Arguments.of(valueOf("33x³"), 0, 0),
				Arguments.of(valueOf("0.25x^3-1.25"), 1, -1),
				Arguments.of(valueOf("3x³+3x²+3x+3"), 10, 3333));
	}
	
	@ParameterizedTest
	@MethodSource
	void testEquals(DoublePolynomial doublePolynomial1, DoublePolynomial doublePolynomial2, boolean result) {
		assertEquals(result, doublePolynomial1.equals(doublePolynomial2));
	}
	
	static Stream<Arguments> testEquals() {
		return Stream.of(
				Arguments.of(new DoublePolynomial(1, 2, 3), new DoublePolynomial(1, 2, 3), true),
				Arguments.of(new DoublePolynomial(1, 2, 3), new DoublePolynomial(0, 1, 2, 3), false),
				Arguments.of(new DoublePolynomial(1, -0.0, 1), new DoublePolynomial(1, 0.0, 1), true));
	}
	
	@ParameterizedTest
	@MethodSource
	void testHashcode(DoublePolynomial doublePolynomial1, DoublePolynomial doublePolynomial2, boolean result) {
		assertEquals(result, doublePolynomial1.hashCode() == doublePolynomial2.hashCode());
	}
	
	static Stream<Arguments> testHashcode() {
		return Stream.of(
				Arguments.of(new DoublePolynomial(1, 2, 3), new DoublePolynomial(1, 2, 3), true),
				Arguments.of(new DoublePolynomial(1, 2, 3), new DoublePolynomial(0, 1, 2, 3), false),
				Arguments.of(new DoublePolynomial(1, -0.0, 1), new DoublePolynomial(1, 0.0, 1), true));
	}
	
	@ParameterizedTest
	@MethodSource
	void testToString(DoublePolynomial doublePolynomial, String result) {
		assertEquals(result, doublePolynomial.toString());
	}
	
	static Stream<Arguments> testToString() {
		return Stream.of(
				Arguments.of(valueOf("0"), "0.0"),
				Arguments.of(valueOf("0.5"), "0.5"),
				Arguments.of(valueOf("-1"), "-1.0"),
				Arguments.of(valueOf("x"), "x"),
				Arguments.of(valueOf("x+1"), "x + 1.0"),
				Arguments.of(valueOf("-2+x"), "x - 2.0"),
				Arguments.of(valueOf("3-x"), "-x + 3.0"),
				Arguments.of(valueOf("-x-4"), "-x - 4.0"),
				Arguments.of(valueOf("-1+x+2x^2"), "2.0 x² + x - 1.0"),
				Arguments.of(valueOf("-5 x^4 - 4 x³ + 3 x² - x + 1"), "-5.0 x^4 - 4.0 x³ + 3.0 x² - x + 1.0"));
	}
}
