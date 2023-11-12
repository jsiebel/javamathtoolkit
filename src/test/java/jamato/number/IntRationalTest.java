package jamato.number;

import static jamato.number.IntRational.NAN;
import static jamato.number.IntRational.NEGATIVE_INFINITY;
import static jamato.number.IntRational.ONE;
import static jamato.number.IntRational.POSITIVE_INFINITY;
import static jamato.number.IntRational.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.RoundingMode;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IntRationalTest {
	
	@ParameterizedTest
	@MethodSource
	void testDoubleConstructor(double value, IntRational result) {
		assertEquals(result, new IntRational(value));
	}
	
	static Stream<Arguments> testDoubleConstructor() {
		return Stream.of(
				Arguments.of(0.0, new IntRational(0, 1)),
				Arguments.of(1.0, new IntRational(1, 1)),
				Arguments.of(1234.0, new IntRational(1234, 1)),
				
				Arguments.of(2.15, new IntRational(43, 20)),
				Arguments.of(-2.15, new IntRational(-43, 20)),
				
				Arguments.of(1e-9, new IntRational(1, 1_000_000_000)),
				Arguments.of(1e-10, new IntRational(0, 1)),
				
				Arguments.of(Integer.MAX_VALUE, new IntRational(Integer.MAX_VALUE, 1)),
				Arguments.of(0.5 * Integer.MAX_VALUE, new IntRational(Integer.MAX_VALUE, 2)),
				Arguments.of(0.5 * Integer.MAX_VALUE + 0.5, new IntRational(Integer.MAX_VALUE / 2 + 1, 1)),
				Arguments.of(0.5 * Integer.MAX_VALUE + 0.6, new IntRational(Integer.MAX_VALUE / 2 + 1, 1)),
				Arguments.of(1e10, new IntRational(Integer.MAX_VALUE, 1)),
				
				Arguments.of(Integer.MIN_VALUE, new IntRational(Integer.MIN_VALUE, 1)),
				Arguments.of(0.5 * Integer.MIN_VALUE, new IntRational(Integer.MIN_VALUE / 2, 1)),
				Arguments.of(0.5 * Integer.MIN_VALUE + 0.5, new IntRational(Integer.MIN_VALUE + 1, 2)),
				Arguments.of(0.5 * Integer.MIN_VALUE + 0.6, new IntRational(Integer.MIN_VALUE + 1, 2)),
				Arguments.of(Integer.MIN_VALUE, new IntRational(Integer.MIN_VALUE, 1)),
				
				Arguments.of(Double.NaN, NAN),
				Arguments.of(Double.POSITIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(Double.NEGATIVE_INFINITY, NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDoubleConstructorByDoubleValue(double value, double result) {
		assertEquals(result, new IntRational(value).doubleValue(), 1e-9);
	}
	
	static Stream<Arguments> testDoubleConstructorByDoubleValue() {
		double phi = Math.sqrt(5) / 2 + 0.5;
		return Stream.of(
				Arguments.of(Math.PI, Math.PI),
				Arguments.of(phi, phi),
				Arguments.of(1 / phi, 1 / phi));
	}
	
	@ParameterizedTest
	@MethodSource
	void testNegate(IntRational value, IntRational result) {
		assertEquals(result, value.negate());
	}
	
	static Stream<Arguments> testNegate() {
		return Stream.of(
				Arguments.of(new IntRational(-34, 93), new IntRational(34, 93)),
				Arguments.of(ZERO, ZERO),
				Arguments.of(NAN, NAN),
				Arguments.of(new IntRational(Integer.MIN_VALUE), POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsZero(IntRational value, boolean result) {
		assertEquals(result, value.isZero());
	}
	
	static Stream<Arguments> testIsZero() {
		return Stream.of(
				Arguments.of(new IntRational(9922, 112), false),
				Arguments.of(ZERO, true),
				Arguments.of(ONE, false),
				Arguments.of(NAN, false),
				Arguments.of(POSITIVE_INFINITY, false),
				Arguments.of(NEGATIVE_INFINITY, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAdd(IntRational summand1, IntRational summand2, IntRational result) {
		assertEquals(result, summand1.add(summand2));
	}
	
	static Stream<Arguments> testAdd() {
		return Stream.of(
				Arguments.of(new IntRational(2, 4), new IntRational(2, 8), new IntRational(3, 4)),
				Arguments.of(new IntRational(0, 1), new IntRational(0, 3400), new IntRational(0, 4)),
				Arguments.of(new IntRational(13, 30), new IntRational(2, 30), new IntRational(1, 2)),
				Arguments.of(new IntRational(1, Integer.MAX_VALUE), new IntRational(-1, Integer.MAX_VALUE), ZERO),
				Arguments.of(new IntRational(Integer.MAX_VALUE), new IntRational(-Integer.MAX_VALUE), ZERO),
				Arguments.of(
						new IntRational(1, 1_000_000_000 - 1),
						new IntRational(1, 1_000_000_000 + 1),
						new IntRational(2, 1_000_000_000)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAddNonFinite(IntRational summand1, IntRational summand2, IntRational result) {
		assertSame(result, summand1.add(summand2));
	}
	
	static Stream<Arguments> testAddNonFinite() {
		return Stream.of(
				Arguments.of(NAN, NAN, NAN),
				Arguments.of(NAN, NEGATIVE_INFINITY, NAN),
				Arguments.of(NAN, new IntRational(756), NAN),
				Arguments.of(NAN, POSITIVE_INFINITY, NAN),
				
				Arguments.of(NEGATIVE_INFINITY, NAN, NAN),
				Arguments.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY, NEGATIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, new IntRational(-757), NEGATIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, POSITIVE_INFINITY, NAN),
				
				Arguments.of(new IntRational(758), NAN, NAN),
				Arguments.of(new IntRational(-759), NEGATIVE_INFINITY, NEGATIVE_INFINITY),
				Arguments.of(new IntRational(760), POSITIVE_INFINITY, POSITIVE_INFINITY),
				
				Arguments.of(POSITIVE_INFINITY, NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, NEGATIVE_INFINITY, NAN),
				Arguments.of(POSITIVE_INFINITY, new IntRational(-761), POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY, POSITIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAddInt(IntRational summand1, int summand2, IntRational result) {
		assertEquals(result, summand1.add(summand2));
	}
	
	static Stream<Arguments> testAddInt() {
		return Stream.of(
				Arguments.of(NAN, 17, NAN),
				Arguments.of(POSITIVE_INFINITY, 217, POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, 9973, NEGATIVE_INFINITY),
				Arguments.of(new IntRational(17, 18), -19, new IntRational(-325, 18)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSubtract(IntRational minuend, IntRational subtrahend, IntRational result) {
		assertEquals(result, minuend.subtract(subtrahend));
	}
	
	static Stream<Arguments> testSubtract() {
		return Stream.of(
				Arguments.of(new IntRational(2, 4), new IntRational(2, 8), new IntRational(1, 4)),
				Arguments.of(new IntRational(0, 1), new IntRational(0, 3400), new IntRational(0, 4)),
				Arguments.of(new IntRational(27, 50), new IntRational(2, 50), new IntRational(1, 2)),
				Arguments.of(new IntRational(1, Integer.MAX_VALUE), new IntRational(1, Integer.MAX_VALUE), ZERO),
				Arguments.of(new IntRational(Integer.MAX_VALUE), new IntRational(Integer.MAX_VALUE), ZERO),
				Arguments.of(
						new IntRational(-1, 2_000_000_000 - 1),
						new IntRational(1, 2_000_000_000 + 1),
						new IntRational(-2, 2_000_000_000)),
				Arguments.of(new IntRational(-1_000_000_000), new IntRational(2_000_000_000), NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSubtractNonFinite(IntRational minuend, IntRational subtrahend, IntRational result) {
		assertSame(result, minuend.subtract(subtrahend));
	}
	
	static Stream<Arguments> testSubtractNonFinite() {
		return Stream.of(
				Arguments.of(NAN, NAN, NAN),
				Arguments.of(NAN, NEGATIVE_INFINITY, NAN),
				Arguments.of(NAN, new IntRational(-220), NAN),
				Arguments.of(NAN, POSITIVE_INFINITY, NAN),
				
				Arguments.of(NEGATIVE_INFINITY, NAN, NAN),
				Arguments.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY, NAN),
				Arguments.of(NEGATIVE_INFINITY, new IntRational(221), NEGATIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, POSITIVE_INFINITY, NEGATIVE_INFINITY),
				
				Arguments.of(new IntRational(-222), NAN, NAN),
				Arguments.of(new IntRational(223), NEGATIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(new IntRational(-224), POSITIVE_INFINITY, NEGATIVE_INFINITY),
				
				Arguments.of(POSITIVE_INFINITY, NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, NEGATIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, new IntRational(225), POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY, NAN));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSubtractInt(IntRational minuend, int subtrahend, IntRational result) {
		assertEquals(result, minuend.subtract(subtrahend));
	}
	
	static Stream<Arguments> testSubtractInt() {
		return Stream.of(
				Arguments.of(NAN, 17, NAN),
				Arguments.of(POSITIVE_INFINITY, 217, POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, 9973, NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testInvert(IntRational value, IntRational result) {
		assertEquals(result, value.invert());
	}
	
	static Stream<Arguments> testInvert() {
		return Stream.of(
				Arguments.of(new IntRational(557, 26), new IntRational(26, 557)),
				Arguments.of(ZERO, POSITIVE_INFINITY),
				Arguments.of(NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, ZERO),
				Arguments.of(NEGATIVE_INFINITY, ZERO));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsOne(IntRational value, boolean result) {
		assertEquals(result, value.isOne());
	}
	
	static Stream<Arguments> testIsOne() {
		return Stream.of(
				Arguments.of(new IntRational(992, 1012), false),
				Arguments.of(ZERO, false),
				Arguments.of(ONE, true),
				Arguments.of(NAN, false),
				Arguments.of(POSITIVE_INFINITY, false),
				Arguments.of(NEGATIVE_INFINITY, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiply(IntRational factor1, IntRational factor2, IntRational result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiply() {
		return Stream.of(
				Arguments.of(new IntRational(3, 8), new IntRational(4, 6), new IntRational(1, 4)),
				Arguments.of(NAN, ONE, NAN),
				Arguments.of(POSITIVE_INFINITY, NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, ONE, POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, ONE, NEGATIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY, POSITIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiplyByInt(IntRational factor1, int factor2, IntRational result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiplyByInt() {
		return Stream.of(
				Arguments.of(new IntRational(4, 8), 0, ZERO),
				Arguments.of(new IntRational(25, 15), 10, new IntRational(50, 3)),
				Arguments.of(new IntRational(253, -7), 0, ZERO),
				Arguments.of(NAN, 5, NAN),
				Arguments.of(NEGATIVE_INFINITY, -35, POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, -31, NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivide(IntRational dividend, IntRational divisor, IntRational result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivide() {
		return Stream.of(
				Arguments.of(new IntRational(3, 8), new IntRational(24, 5), new IntRational(5, 64)),
				
				Arguments.of(NAN, ONE, NAN),
				Arguments.of(POSITIVE_INFINITY, NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, ONE, POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY, NAN),
				Arguments.of(NEGATIVE_INFINITY, ONE, NEGATIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY, NAN),
				Arguments.of(ONE, NEGATIVE_INFINITY, ZERO));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivideByInt(IntRational dividend, int divisor, IntRational result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivideByInt() {
		return Stream.of(
				Arguments.of(new IntRational(4, 8), 3, new IntRational(1, 6)),
				Arguments.of(NAN, 5, NAN),
				Arguments.of(NEGATIVE_INFINITY, -5, POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, -11456, NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPow(IntRational base, int exponent, IntRational result) {
		assertEquals(result, base.pow(exponent));
	}
	
	static Stream<Arguments> testPow() {
		return Stream.of(
				Arguments.of(new IntRational(-5, 9), 2, new IntRational(25, 81)),
				Arguments.of(new IntRational(8, 3), -3, new IntRational(27, 512)),
				Arguments.of(new IntRational(831), 0, ONE),
				Arguments.of(NAN, 72, NAN),
				Arguments.of(NEGATIVE_INFINITY, 93, NEGATIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, 94, POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, 95, POSITIVE_INFINITY),
				Arguments.of(ZERO, -96, POSITIVE_INFINITY),
				Arguments.of(ZERO, 97, ZERO));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSignum(IntRational value, int result) {
		assertEquals(result, value.signum());
	}
	
	static Stream<Arguments> testSignum() {
		return Stream.of(
				Arguments.of(new IntRational(-417, -211), 1),
				Arguments.of(new IntRational(7190, -44582), -1),
				Arguments.of(NEGATIVE_INFINITY, -1),
				Arguments.of(ZERO, 0),
				Arguments.of(NAN, 0),
				Arguments.of(POSITIVE_INFINITY, 1));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAbsolute(IntRational value, IntRational result) {
		assertEquals(result, value.absolute());
	}
	
	static Stream<Arguments> testAbsolute() {
		return Stream.of(
				Arguments.of(new IntRational(319, -12), new IntRational(319, 12)),
				Arguments.of(new IntRational(320, 13), new IntRational(320, 13)),
				Arguments.of(NEGATIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(ZERO, ZERO),
				Arguments.of(NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRound(IntRational value, RoundingMode roundingMode, int result) {
		assertEquals(result, value.round(roundingMode));
	}
	
	static Stream<Arguments> testRound() {
		return Stream.of(
				Arguments.of(new IntRational(0), RoundingMode.HALF_UP, 0),
				Arguments.of(new IntRational(1000), RoundingMode.HALF_UP, 1000),
				Arguments.of(new IntRational(-1000), RoundingMode.HALF_UP, -1000),
				
				Arguments.of(new IntRational(-6.75), RoundingMode.DOWN, -6),
				Arguments.of(new IntRational(-6.75), RoundingMode.HALF_DOWN, -7),
				Arguments.of(new IntRational(-6.75), RoundingMode.HALF_EVEN, -7),
				Arguments.of(new IntRational(-6.75), RoundingMode.HALF_UP, -7),
				Arguments.of(new IntRational(-6.75), RoundingMode.UP, -7),
				
				Arguments.of(new IntRational(-2.5), RoundingMode.DOWN, -2),
				Arguments.of(new IntRational(-2.5), RoundingMode.HALF_DOWN, -2),
				Arguments.of(new IntRational(-2.5), RoundingMode.HALF_EVEN, -2),
				Arguments.of(new IntRational(-2.5), RoundingMode.HALF_UP, -3),
				Arguments.of(new IntRational(-2.5), RoundingMode.UP, -3),
				
				Arguments.of(new IntRational(2.5), RoundingMode.DOWN, 2),
				Arguments.of(new IntRational(2.5), RoundingMode.HALF_DOWN, 2),
				Arguments.of(new IntRational(2.5), RoundingMode.HALF_EVEN, 2),
				Arguments.of(new IntRational(2.5), RoundingMode.HALF_UP, 3),
				Arguments.of(new IntRational(2.5), RoundingMode.UP, 3),
				
				Arguments.of(new IntRational(-3.5), RoundingMode.HALF_EVEN, -4),
				Arguments.of(NAN, RoundingMode.HALF_EVEN, 0));
	}
	
	@ParameterizedTest
	@MethodSource
	void testCeil(IntRational value, int result) {
		assertEquals(result, value.ceil());
	}
	
	static Stream<Arguments> testCeil() {
		return Stream.of(
				Arguments.of(new IntRational(-6.75), -6),
				Arguments.of(new IntRational(-2.5), -2),
				Arguments.of(new IntRational(2.5), 3));
	}
	
	@ParameterizedTest
	@MethodSource
	void testFloor(IntRational value, int result) {
		assertEquals(result, value.floor());
	}
	
	static Stream<Arguments> testFloor() {
		return Stream.of(
				Arguments.of(new IntRational(-6.75), -7),
				Arguments.of(new IntRational(-2.5), -3),
				Arguments.of(new IntRational(2.5), 2));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDoubleValue(IntRational value, double result) {
		assertEquals(result, value.doubleValue(), 1e-9);
	}
	
	static Stream<Arguments> testDoubleValue() {
		return Stream.of(
				Arguments.of(new IntRational(430, 37000), 430.0 / 37000),
				Arguments.of(NAN, Double.NaN),
				Arguments.of(POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testFloatValue(IntRational value, float result) {
		assertEquals(result, value.floatValue(), 1e-3);
	}
	
	static Stream<Arguments> testFloatValue() {
		return Stream.of(
				Arguments.of(new IntRational(45000, 37), 45000.0f / 37),
				Arguments.of(NAN, Float.NaN),
				Arguments.of(POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIntValue(IntRational value, int result) {
		assertEquals(result, value.intValue());
	}
	
	static Stream<Arguments> testIntValue() {
		return Stream.of(
				Arguments.of(new IntRational(103, 9), 103 / 9),
				Arguments.of(new IntRational(-7, 101), 0));
	}
	
	@ParameterizedTest
	@MethodSource
	void testLongValue(IntRational value, long result) {
		assertEquals(result, value.longValue());
	}
	
	static Stream<Arguments> testLongValue() {
		return Stream.of(
				Arguments.of(new IntRational(101, 7), 101L / 7),
				Arguments.of(new IntRational(-7, 101), 0L));
	}
	
	@ParameterizedTest
	@MethodSource
	void testCompareTo(IntRational value1, IntRational value2, int result) {
		assertEquals(result, value1.compareTo(value2));
	}
	
	static Stream<Arguments> testCompareTo() {
		return Stream.of(
				Arguments.of(new IntRational(-3, 99), new IntRational(-4, 100), 1),
				Arguments.of(
						new IntRational(Integer.MAX_VALUE, Integer.MAX_VALUE - 1),
						new IntRational(Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 2),
						-1),
				Arguments.of(ZERO, ZERO, 0),
				Arguments.of(ZERO, ONE, -1),
				Arguments.of(ONE, ZERO, 1),
				Arguments.of(ONE, POSITIVE_INFINITY, -1),
				Arguments.of(ONE, NEGATIVE_INFINITY, 1),
				Arguments.of(ONE, NAN, -1),
				Arguments.of(NAN, NEGATIVE_INFINITY, 1),
				Arguments.of(POSITIVE_INFINITY, NAN, -1),
				Arguments.of(POSITIVE_INFINITY, ONE, 1));
	}
}
