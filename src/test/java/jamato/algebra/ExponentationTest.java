package jamato.algebra;

import static jamato.algebra.Exponentation.pow;
import static jamato.algebra.Exponentation.powMod;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jamato.number.IntRational;

class ExponentationTest {

	@ParameterizedTest
	@MethodSource
	void testPow(int base, int exponent, int result) {
		assertEquals(result, pow(base, exponent));
	}
	
	static Stream<Arguments> testPow(){
		return Stream.of(
				Arguments.of(2, 5, 32),
				Arguments.of(-7, 2, 49),
				Arguments.of(-11, 5, -161051),
				Arguments.of(Integer.MAX_VALUE, 1, Integer.MAX_VALUE),
				Arguments.of(Integer.MIN_VALUE, 0, 1),
				Arguments.of(0, 0, 1),
				Arguments.of(-1, Integer.MAX_VALUE, -1));
	}
	
	@Test
	void testPowLong() {
		assertEquals(1_000_000_000_000L, pow(100L, 6));
	}
	
	@Test
	void testPowRing() {
		assertEquals(IntRational.ONE, pow(IntRational.ONE, Integer.MIN_VALUE, IntRational.ONE));
	}
	

	@ParameterizedTest
	@MethodSource
	void testPowMod(int base, int exponent, int modulus, int result) {
		assertEquals(result, powMod(base, exponent, modulus));
	}
	
	static Stream<Arguments> testPowMod() {
		return Stream.of(
				Arguments.of(0, 0, 37, 1),
				Arguments.of(-7, 3, 1000, -343),
				Arguments.of(1000, 371, 9, 1));
	}
}
