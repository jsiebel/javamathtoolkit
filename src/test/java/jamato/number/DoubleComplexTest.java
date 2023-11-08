package jamato.number;

import static jamato.number.DoubleComplex.I;
import static jamato.number.DoubleComplex.ONE;
import static jamato.number.DoubleComplex.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DoubleComplexTest {
	
	@ParameterizedTest
	@MethodSource
	void testDoubleConstructor(double real, double imaginary, DoubleComplex result) {
		assertEquals(result, new DoubleComplex(real, imaginary));
	}
	
	static Stream<Arguments> testDoubleConstructor() {
		return Stream.of(
				Arguments.of(0, 0, ZERO),
				Arguments.of(1, 0, ONE));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPolar(double z, double phi, DoubleComplex result) {
		assertEquals(0, result.distanceTo(DoubleComplex.polar(z, phi)), 1e-9);
	}
	
	static Stream<Arguments> testPolar() {
		return Stream.of(
				Arguments.of(0, 22, ZERO),
				Arguments.of(1, 0, ONE),
				Arguments.of(1, Math.PI / 2, I));
	}
	
	@ParameterizedTest
	@MethodSource
	void testNegate(DoubleComplex value, DoubleComplex result) {
		assertEquals(result, value.negate());
	}
	
	static Stream<Arguments> testNegate() {
		return Stream.of(
				Arguments.of(new DoubleComplex(-33.3, -43), new DoubleComplex(33.3, 43)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsZero(DoubleComplex value, boolean result) {
		assertEquals(result, value.isZero());
	}
	
	static Stream<Arguments> testIsZero() {
		return Stream.of(
				Arguments.of(ZERO, true),
				Arguments.of(ONE, false),
				Arguments.of(I, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAdd(DoubleComplex summand1, DoubleComplex summand2, DoubleComplex result) {
		assertEquals(result, summand1.add(summand2));
	}
	
	static Stream<Arguments> testAdd() {
		return Stream.of(
				Arguments.of(new DoubleComplex(2, 4), new DoubleComplex(1), new DoubleComplex(3, 4)),
				Arguments.of(new DoubleComplex(0, 1), new DoubleComplex(4), new DoubleComplex(4, 1)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSubtract(DoubleComplex minuend, DoubleComplex subtrahend, DoubleComplex result) {
		assertEquals(result, minuend.subtract(subtrahend));
	}
	
	static Stream<Arguments> testSubtract() {
		return Stream.of(
				Arguments.of(new DoubleComplex(2, 4), new DoubleComplex(1), new DoubleComplex(1, 4)),
				Arguments.of(new DoubleComplex(0, 1), new DoubleComplex(4), new DoubleComplex(-4, 1)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testInvert(DoubleComplex value, DoubleComplex result) {
		assertEquals(result, value.invert());
	}
	
	static Stream<Arguments> testInvert() {
		return Stream.of(
				Arguments.of(ONE, ONE),
				Arguments.of(new DoubleComplex(3, 4), new DoubleComplex(3.0 / 25, -4.0 / 25))
		// Arguments.of(NAN, ZERO.invert())
		);
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsOne(DoubleComplex value, boolean result) {
		assertEquals(result, value.isOne());
	}
	
	static Stream<Arguments> testIsOne() {
		return Stream.of(
				Arguments.of(ZERO, false),
				Arguments.of(ONE, true),
				Arguments.of(I, false));
	}

	@ParameterizedTest
	@MethodSource
	void testMultiply(DoubleComplex factor1, DoubleComplex factor2, DoubleComplex result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiply() {
		return Stream.of(
				Arguments.of(new DoubleComplex(2, 3), new DoubleComplex(4, 5), new DoubleComplex(-7, 22)));
	}

	@ParameterizedTest
	@MethodSource
	void testMultiplyByDouble(DoubleComplex factor1, double factor2, DoubleComplex result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiplyByDouble() {
		return Stream.of(
				Arguments.of(new DoubleComplex(4, 8), 0, ZERO),
				Arguments.of(new DoubleComplex(7, 3), 1.5, new DoubleComplex(10.5, 4.5)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivide(DoubleComplex dividend, DoubleComplex divisor, DoubleComplex result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivide() {
		return Stream.of(
				Arguments.of(new DoubleComplex(-7, 22), new DoubleComplex(2, 3), new DoubleComplex(4, 5)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivideByDouble(DoubleComplex dividend, double divisor, DoubleComplex result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivideByDouble() {
		return Stream.of(
				Arguments.of(new DoubleComplex(4, 8), 8, new DoubleComplex(0.5, 1)),
				Arguments.of(new DoubleComplex(4, 8), 4.0, new DoubleComplex(1, 2)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPow(DoubleComplex base, int exponent, DoubleComplex result) {
		assertEquals(result, base.pow(exponent));
	}
	
	static Stream<Arguments> testPow() {
		return Stream.of(
				Arguments.of(I, -4, ONE),
				Arguments.of(I, 0, ONE),
				Arguments.of(I, 1, I),
				Arguments.of(I, 5, I),
				Arguments.of(ZERO, 31, ZERO),
				Arguments.of(new DoubleComplex(2, 3), 4, new DoubleComplex(-119, -120)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAbs(DoubleComplex value, double result) {
		assertEquals(result, value.abs(), 0);
	}
	
	static Stream<Arguments> testAbs() {
		return Stream.of(
				Arguments.of(ZERO,  0),
				Arguments.of(ONE, 1),
				Arguments.of(I, 1),
				Arguments.of(new DoubleComplex(-6, 8),  10));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDistanceTo(DoubleComplex value1, DoubleComplex value2, double result) {
		assertEquals(result, value1.distanceTo(value2), 0);
	}
	
	static Stream<Arguments> testDistanceTo() {
		return Stream.of(
				Arguments.of(ZERO, ONE, 1),
				Arguments.of(ZERO, I, 1),
				Arguments.of(ONE, I, Math.sqrt(2)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testConjugate(DoubleComplex value, DoubleComplex result) {
		assertEquals(result, value.conjugate());
	}
	
	static Stream<Arguments> testConjugate() {
		return Stream.of(
				Arguments.of(ZERO, ZERO),
				Arguments.of(ONE, ONE),
				Arguments.of(I, new DoubleComplex(0, -1)),
				Arguments.of(new DoubleComplex(3.5, -2.6), new DoubleComplex(3.5, 2.6)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsFinite(DoubleComplex value, boolean result) {
		assertEquals(result, value.isFinite());
	}
	
	static Stream<Arguments> testIsFinite() {
		return Stream.of(
				Arguments.of(ZERO, true),
				Arguments.of(new DoubleComplex(7.7), true),
				Arguments.of(ZERO.invert(), false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsNaN(DoubleComplex value, boolean result) {
		assertEquals(result, value.isNaN());
	}
	
	static Stream<Arguments> testIsNaN() {
		return Stream.of(
				Arguments.of(ZERO, false),
				Arguments.of(new DoubleComplex(7.7), false),
				Arguments.of(ZERO.invert(), true));
	}
	
	@ParameterizedTest
	@MethodSource
	void testToString(DoubleComplex value, String result) {
		assertEquals(result, value.toString());
	}
	
	static Stream<Arguments> testToString() {
		return Stream.of(
				Arguments.of(ZERO.toString(), "0.0"),
				Arguments.of(I.toString(), "i"),
				Arguments.of(new DoubleComplex(0, -1), "-i"),
				Arguments.of(new DoubleComplex(47, -1), "47.0-i"),
				Arguments.of(new DoubleComplex(3, -2.5), "3.0-2.5i"));
	}
	
	@ParameterizedTest
	@MethodSource
	void testValueOf(String string, DoubleComplex result) {
		assertEquals(result, DoubleComplex.valueOf(string));
	}
	
	static Stream<Arguments> testValueOf() {
		return Stream.of(
				Arguments.of("1", ONE),
				Arguments.of("0i", ZERO),
				Arguments.of("0+1i", I),
				Arguments.of("i", I),
				Arguments.of("-i", new DoubleComplex(0, -1)),
				Arguments.of("2+i", new DoubleComplex(2, 1)),
				Arguments.of("2-i", new DoubleComplex(2, -1)),
				Arguments.of("1.1+1.2i", new DoubleComplex(1.1, 1.2)),
				Arguments.of("-1.3+1.4i", new DoubleComplex(-1.3, 1.4)),
				Arguments.of("1.5-1.6i", new DoubleComplex(1.5, -1.6)),
				Arguments.of("-1.7-1.8i", new DoubleComplex(-1.7, -1.8)));
	}
}
