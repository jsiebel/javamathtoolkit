package jamato.number;

import static jamato.number.BigRational.NAN;
import static jamato.number.BigRational.NEGATIVE_INFINITY;
import static jamato.number.BigRational.ONE;
import static jamato.number.BigRational.POSITIVE_INFINITY;
import static jamato.number.BigRational.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BigRationalTest {
	
	@ParameterizedTest
	@MethodSource
	void testDoubleConstructor(double value, BigRational result) {
		assertEquals(result, new BigRational(value));
	}
	
	static Stream<Arguments> testDoubleConstructor() {
		double greatestNonInteger = Math.pow(2, 52) - 0.5;
		double smallesNonInteger = -Math.pow(2, 52) + 0.5;
		return Stream.of(
				Arguments.of(0.0, BigRational.ZERO),
				Arguments.of(1.0, BigRational.ONE),
				Arguments.of(1.5, new BigRational(3, 2)),
				Arguments.of(0.0625, new BigRational(1, 16)),
				Arguments.of(1234.0, new BigRational(1234)),
				Arguments.of(987654321.0, new BigRational(987654321)),
				Arguments.of(2.15, new BigRational(43, 20)),
				Arguments.of(-2.15, new BigRational(-43, 20)),
				Arguments.of(
						greatestNonInteger,
						new BigRational(BigInteger.TWO.pow(53).subtract(BigInteger.ONE), BigInteger.TWO)),
				Arguments.of(
						smallesNonInteger,
						new BigRational(BigInteger.TWO.pow(53).subtract(BigInteger.ONE).negate(), BigInteger.TWO)),
				Arguments.of(1e-9, new BigRational(1, 1_000_000_000)),
				Arguments.of(Math.nextUp(1d), new BigRational(4503599627370497L, 4503599627370496L)),
				Arguments.of(Double.MIN_VALUE, new BigRational(BigInteger.ONE, BigInteger.TWO.pow(1074))),
				Arguments.of(-Double.MIN_VALUE, new BigRational(BigInteger.ONE.negate(), BigInteger.TWO.pow(1074))),
				Arguments.of(Double.MIN_NORMAL, new BigRational(BigInteger.ONE, BigInteger.TWO.pow(1022))),
				Arguments.of(-Double.MIN_NORMAL, new BigRational(BigInteger.ONE.negate(), BigInteger.TWO.pow(1022))),
				Arguments.of(Math.pow(2, 150), new BigRational(BigInteger.TWO.pow(150))),
				Arguments.of(Math.pow(0.5, 160), new BigRational(BigInteger.ONE, BigInteger.TWO.pow(160))),
				Arguments.of(
						Math.pow(0.5, 180) / 3,
						new BigRational(BigInteger.ONE, BigInteger.TWO.pow(180).multiply(BigInteger.valueOf(3)))),
				Arguments.of(
						4.0 / 3 / Math.pow(2, Long.SIZE),
						new BigRational(BigInteger.ONE,
								BigInteger.valueOf(3).multiply(BigInteger.TWO.pow(Long.SIZE - 2)))),
				Arguments.of(Double.NaN, NAN),
				Arguments.of(Double.POSITIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(Double.NEGATIVE_INFINITY, NEGATIVE_INFINITY));
	}
	
	void testDoubleConstructorByDoubleValue(BigRational value, double result) {
		assertEquals(result, value.doubleValue(), 1e-9);
	}
	
	static Stream<Arguments> testDoubleConstructorByDoubleValue() {
		double phi = Math.sqrt(5) / 2 + 0.5;
		return Stream.of(
				Arguments.of(new BigRational(Math.PI), Math.PI),
				Arguments.of(new BigRational(phi), phi),
				Arguments.of(new BigRational(1 / phi), 1 / phi));
	}
	
	@ParameterizedTest
	@MethodSource
	void testNegate(BigRational value, BigRational result) {
		assertEquals(result, value.negate());
	}
	
	static Stream<Arguments> testNegate() {
		return Stream.of(
				Arguments.of(new BigRational(-34, 93), new BigRational(34, 93)),
				Arguments.of(ZERO, ZERO),
				Arguments.of(NAN, NAN),
				Arguments.of(NEGATIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsZero(BigRational value, boolean result) {
		assertEquals(result, value.isZero());
	}
	
	static Stream<Arguments> testIsZero() {
		return Stream.of(
				Arguments.of(new BigRational(9922, 112), false),
				Arguments.of(ZERO, true),
				Arguments.of(ONE, false),
				Arguments.of(NAN, false),
				Arguments.of(POSITIVE_INFINITY, false),
				Arguments.of(NEGATIVE_INFINITY, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAdd(BigRational summand1, BigRational summand2, BigRational result) {
		assertEquals(result, summand1.add(summand2));
	}
	
	static Stream<Arguments> testAdd() {
		return Stream.of(
				Arguments.of(new BigRational(2, 4), new BigRational(2, 8), new BigRational(3, 4)),
				Arguments.of(new BigRational(0, 1), new BigRational(0, 3400), new BigRational(0, 4)),
				Arguments.of(new BigRational(13, 30), new BigRational(2, 30), new BigRational(1, 2)),
				Arguments.of(
						new BigRational(BigInteger.ONE, new BigInteger("111111111")),
						new BigRational(111111111L),
						new BigRational(new BigInteger("12345678987654322"), new BigInteger("111111111"))),
				Arguments.of(new BigRational(1, Integer.MAX_VALUE), new BigRational(-1, Integer.MAX_VALUE), ZERO),
				Arguments.of(new BigRational(Integer.MAX_VALUE), new BigRational(-Integer.MAX_VALUE), ZERO),
				Arguments.of(ZERO, ZERO, ZERO),
				Arguments.of(ONE, NAN, NAN),
				Arguments.of(NAN, ONE, NAN),
				Arguments.of(POSITIVE_INFINITY, NEGATIVE_INFINITY, NAN),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY, NEGATIVE_INFINITY),
				
				Arguments.of(new BigRational(17, 18), new BigRational(-19), new BigRational(-325, 18)),
				Arguments.of(NAN, new BigRational(17), NAN),
				Arguments.of(POSITIVE_INFINITY, new BigRational(217), POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, new BigRational(99739), NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSubtract(BigRational minuend, BigRational subtrahend, BigRational result) {
		assertEquals(result, minuend.subtract(subtrahend));
	}
	
	static Stream<Arguments> testSubtract() {
		return Stream.of(
				Arguments.of(new BigRational(2, 4), new BigRational(2, 8), new BigRational(1, 4)),
				Arguments.of(new BigRational(0, 1), new BigRational(0, 3400), new BigRational(0, 4)),
				Arguments.of(new BigRational(27, 50), new BigRational(2, 50), new BigRational(1, 2)),
				Arguments.of(new BigRational(1, Integer.MAX_VALUE), new BigRational(1, Integer.MAX_VALUE), ZERO),
				Arguments.of(new BigRational(Integer.MAX_VALUE), new BigRational(Integer.MAX_VALUE), ZERO),
				Arguments.of(ONE, NAN, NAN),
				Arguments.of(NAN, ONE, NAN),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY, NAN),
				Arguments.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY, NAN),
				Arguments.of(POSITIVE_INFINITY, NEGATIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, POSITIVE_INFINITY, NEGATIVE_INFINITY),
				
				Arguments.of(new BigRational(100, 11), new BigRational(11), new BigRational(-21, 11)),
				Arguments.of(NAN, new BigRational(17), NAN),
				Arguments.of(POSITIVE_INFINITY, new BigRational(217), POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, new BigRational(9973), NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testInvert(BigRational value, BigRational result) {
		assertEquals(result, value.invert());
	}
	
	static Stream<Arguments> testInvert() {
		return Stream.of(
				Arguments.of(new BigRational(557, 26), new BigRational(26, 557)),
				Arguments.of(ZERO, POSITIVE_INFINITY),
				Arguments.of(NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, ZERO),
				Arguments.of(NEGATIVE_INFINITY, ZERO));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsOne(BigRational value, boolean result) {
		assertEquals(result, value.isOne());
	}
	
	static Stream<Arguments> testIsOne() {
		return Stream.of(
				Arguments.of(new BigRational(992, 1012), false),
				Arguments.of(ZERO, false),
				Arguments.of(ONE, true),
				Arguments.of(NAN, false),
				Arguments.of(POSITIVE_INFINITY, false),
				Arguments.of(NEGATIVE_INFINITY, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiply(BigRational factor1, BigRational factor2, BigRational result) {
		assertEquals(result, factor1.multiply(factor2));
	}
	
	static Stream<Arguments> testMultiply() {
		return Stream.of(
				Arguments.of(new BigRational(3, 8), new BigRational(4, 6), new BigRational(1, 4)),
				Arguments.of(new BigRational(4, 8), ZERO, ZERO),
				Arguments.of(new BigRational(25, 15), new BigRational(10), new BigRational(50, 3)),
				Arguments.of(new BigRational(253, -7), ZERO, ZERO),
				Arguments.of(NAN, new BigRational(5), NAN),
				Arguments.of(NAN, ONE, NAN),
				Arguments.of(POSITIVE_INFINITY, NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, ONE, POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, new BigRational(-35), POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, ONE, NEGATIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, new BigRational(-31), NEGATIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY, POSITIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivide(BigRational dividend, BigRational divisor, BigRational result) {
		assertEquals(result, dividend.divide(divisor));
	}
	
	static Stream<Arguments> testDivide() {
		return Stream.of(
				Arguments.of(new BigRational(3, 8), new BigRational(24, 5), new BigRational(5, 64)),
				Arguments.of(new BigRational(4, 8), new BigRational(3), new BigRational(1, 6)),
				Arguments.of(NAN, new BigRational(5), NAN),
				Arguments.of(NAN, ONE, NAN),
				Arguments.of(POSITIVE_INFINITY, NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, ONE, POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, new BigRational(-5), POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY, NAN),
				Arguments.of(NEGATIVE_INFINITY, ONE, NEGATIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, new BigRational(-11456), NEGATIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY, NAN),
				Arguments.of(ONE, NEGATIVE_INFINITY, ZERO));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPow(BigRational base, int exponent, BigRational result) {
		assertEquals(result, base.pow(exponent));
	}
	
	static Stream<Arguments> testPow() {
		return Stream.of(
				Arguments.of(new BigRational(-5, 9), 2, new BigRational(25, 81)),
				Arguments.of(new BigRational(8, 3), -3, new BigRational(27, 512)),
				Arguments.of(new BigRational(831), 0, ONE),
				Arguments.of(NAN, 72, NAN),
				Arguments.of(NEGATIVE_INFINITY, 93, NEGATIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, 94, POSITIVE_INFINITY),
				Arguments.of(POSITIVE_INFINITY, 95, POSITIVE_INFINITY),
				Arguments.of(ZERO, -96, POSITIVE_INFINITY),
				Arguments.of(ZERO, 97, ZERO));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSignum(BigRational value, int result) {
		assertEquals(result, value.signum());
	}
	
	static Stream<Arguments> testSignum() {
		return Stream.of(
				Arguments.of(new BigRational(-417, -211), 1),
				Arguments.of(new BigRational(new BigInteger("-1234567891011121314"), new BigInteger("1441223197")), -1),
				Arguments.of(NEGATIVE_INFINITY, -1),
				Arguments.of(ZERO, 0),
				Arguments.of(NAN, 0),
				Arguments.of(POSITIVE_INFINITY, 1));
	}
	
	@ParameterizedTest
	@MethodSource
	void testAbsolute(BigRational value, BigRational result) {
		assertEquals(result, value.absolute());
	}
	
	static Stream<Arguments> testAbsolute() {
		return Stream.of(
				Arguments.of(new BigRational(319, -12), new BigRational(319, 12)),
				Arguments.of(new BigRational(320, 13), new BigRational(320, 13)),
				Arguments.of(NEGATIVE_INFINITY, POSITIVE_INFINITY),
				Arguments.of(ZERO, ZERO),
				Arguments.of(NAN, NAN),
				Arguments.of(POSITIVE_INFINITY, POSITIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRound(BigRational value, RoundingMode roundingMode, BigInteger result) {
		assertEquals(result, value.round(roundingMode));
	}
	
	static Stream<Arguments> testRound() {
		return Stream.of(
				Arguments.of(new BigRational(0), RoundingMode.HALF_UP, BigInteger.valueOf(0)),
				Arguments.of(new BigRational(1000), RoundingMode.HALF_UP, BigInteger.valueOf(1000)),
				Arguments.of(new BigRational(-1000), RoundingMode.HALF_UP, BigInteger.valueOf(-1000)),
				
				Arguments.of(new BigRational(-6.75), RoundingMode.DOWN, BigInteger.valueOf(-6)),
				Arguments.of(new BigRational(-6.75), RoundingMode.HALF_DOWN, BigInteger.valueOf(-7)),
				Arguments.of(new BigRational(-6.75), RoundingMode.HALF_EVEN, BigInteger.valueOf(-7)),
				Arguments.of(new BigRational(-6.75), RoundingMode.HALF_UP, BigInteger.valueOf(-7)),
				Arguments.of(new BigRational(-6.75), RoundingMode.UP, BigInteger.valueOf(-7)),
				
				Arguments.of(new BigRational(-2.5), RoundingMode.DOWN, BigInteger.valueOf(-2)),
				Arguments.of(new BigRational(-2.5), RoundingMode.HALF_DOWN, BigInteger.valueOf(-2)),
				Arguments.of(new BigRational(-2.5), RoundingMode.HALF_EVEN, BigInteger.valueOf(-2)),
				Arguments.of(new BigRational(-2.5), RoundingMode.HALF_UP, BigInteger.valueOf(-3)),
				Arguments.of(new BigRational(-2.5), RoundingMode.UP, BigInteger.valueOf(-3)),
				
				Arguments.of(new BigRational(2.5), RoundingMode.DOWN, BigInteger.valueOf(2)),
				Arguments.of(new BigRational(2.5), RoundingMode.HALF_DOWN, BigInteger.valueOf(2)),
				Arguments.of(new BigRational(2.5), RoundingMode.HALF_EVEN, BigInteger.valueOf(2)),
				Arguments.of(new BigRational(2.5), RoundingMode.HALF_UP, BigInteger.valueOf(3)),
				Arguments.of(new BigRational(2.5), RoundingMode.UP, BigInteger.valueOf(3)),
				
				Arguments.of(new BigRational(-3.5), RoundingMode.HALF_EVEN, BigInteger.valueOf(-4)),
				Arguments.of(NAN, RoundingMode.HALF_EVEN, BigInteger.ZERO));
	}
	
	@ParameterizedTest
	@MethodSource
	void testCeil(BigRational value, BigInteger result) {
		assertEquals(result, value.ceil());
	}
	
	static Stream<Arguments> testCeil() {
		return Stream.of(
				Arguments.of(new BigRational(-6.75), BigInteger.valueOf(-6)),
				Arguments.of(new BigRational(-2.5), BigInteger.valueOf(-2)),
				Arguments.of(new BigRational(2.5), BigInteger.valueOf(3)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testFloor(BigRational value, BigInteger result) {
		assertEquals(result, value.floor());
	}
	
	static Stream<Arguments> testFloor() {
		return Stream.of(
				Arguments.of(new BigRational(-6.75), BigInteger.valueOf(-7)),
				Arguments.of(new BigRational(-2.5), BigInteger.valueOf(-3)),
				Arguments.of(new BigRational(2.5), BigInteger.valueOf(2)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDoubleValue(BigRational value, double result) {
		assertEquals(result, value.doubleValue());
	}
	
	static Stream<Arguments> testDoubleValue() {
		BigInteger nonillionPlusOne = BigInteger.TEN.pow(30).add(BigInteger.ONE);
		BigInteger octillion = BigInteger.TEN.pow(27);
		BigInteger nonillionMinusOne = BigInteger.TEN.pow(30).subtract(BigInteger.ONE);
		return Stream.of(
				Arguments.of(new BigRational(BigInteger.ZERO, BigInteger.ZERO), Double.NaN),
				Arguments.of(new BigRational(BigInteger.ONE, BigInteger.ZERO), Double.POSITIVE_INFINITY),
				Arguments.of(new BigRational(BigInteger.valueOf(-341233), BigInteger.ZERO), Double.NEGATIVE_INFINITY),
				Arguments.of(new BigRational(BigInteger.valueOf(9)), 9),
				Arguments.of(new BigRational(BigInteger.valueOf(9000)), 9000),
				Arguments.of(new BigRational(BigInteger.valueOf(-7)), -7),
				Arguments.of(new BigRational(nonillionPlusOne, octillion), 1000),
				Arguments.of(new BigRational(1, 1000), 0.001),
				Arguments.of(new BigRational(octillion, nonillionPlusOne), 0.001),
				Arguments.of(new BigRational(nonillionMinusOne, octillion), 1000),
				Arguments.of(new BigRational(octillion, nonillionMinusOne), 0.001),
				Arguments
						.of(new BigRational(BigInteger.TWO.pow(Double.MAX_EXPONENT)), Math.pow(2, Double.MAX_EXPONENT)),
				Arguments.of(new BigRational(BigInteger.TWO.pow(Double.MAX_EXPONENT + 1)), Double.POSITIVE_INFINITY),
				Arguments.of(
						new BigRational(BigInteger.TWO.pow(Double.MAX_EXPONENT + 1), BigInteger.valueOf(-1)),
						Double.NEGATIVE_INFINITY),
				Arguments.of(new BigRational(BigInteger.TWO, BigInteger.valueOf(-3949113).pow(50)), 0),
				Arguments.of(
						new BigRational(BigInteger.ONE, BigInteger.TWO.pow(-Double.MIN_EXPONENT + 52)),
						Double.MIN_VALUE),
				Arguments.of(new BigRational(BigInteger.ONE, BigInteger.TWO.pow(-Double.MIN_EXPONENT + 52 + 1)), 0));
	}
	
	@ParameterizedTest
	@MethodSource
	void testFloatValue(BigRational value, float result) {
		assertEquals(result, value.floatValue(), 1e-3);
	}
	
	static Stream<Arguments> testFloatValue() {
		return Stream.of(
				Arguments.of(new BigRational(41000, 37), 41000f / 37),
				Arguments.of(NAN, Float.NaN),
				Arguments.of(POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
				Arguments.of(NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIntValue(BigRational value, int result) {
		assertEquals(result, value.intValue());
	}
	
	static Stream<Arguments> testIntValue() {
		return Stream.of(
				Arguments.of(new BigRational(103, 9), 103 / 9),
				Arguments.of(new BigRational(-7, 101), 0));
	}
	
	@ParameterizedTest
	@MethodSource
	void testLongValue(BigRational value, long result) {
		assertEquals(result, value.longValue());
	}
	
	static Stream<Arguments> testLongValue() {
		return Stream.of(
				Arguments.of(new BigRational(103, 9), 103L / 9),
				Arguments.of(new BigRational(-7, 101), 0L));
	}
	
	@ParameterizedTest
	@MethodSource
	void testCompareTo(BigRational value1, BigRational value2, int result) {
		assertEquals(result, value1.compareTo(value2));
	}
	
	static Stream<Arguments> testCompareTo() {
		return Stream.of(
				Arguments.of(new BigRational(-3, 99), new BigRational(-4, 100), 1),
				Arguments.of(
						new BigRational(Integer.MAX_VALUE, Integer.MAX_VALUE - 1),
						new BigRational(Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 2),
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
