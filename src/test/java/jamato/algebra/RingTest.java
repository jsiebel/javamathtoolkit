package jamato.algebra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RingTest {
	
	@ParameterizedTest
	@MethodSource
	void testSubtract(int minuend, int subtrahend, int result) {
		assertEquals(new TestRing(result), new TestRing(minuend).subtract(new TestRing(subtrahend)));
	}
	
	static Stream<Arguments> testSubtract() {
		return Stream.of(
				Arguments.of(0, 5, 6),
				Arguments.of(7, 4, 3));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiply(int factor1, int factor2, int result) {
		assertEquals(new TestRing(result), new TestRing(factor1).multiply(factor2));
	}
	
	static Stream<Arguments> testMultiply() {
		return Stream.of(
				Arguments.of(7, -2, 7 * -2),
				Arguments.of(7, -1, 7 * -1),
				Arguments.of(7, 0, 7 * 0),
				Arguments.of(7, 1, 7 * 1),
				Arguments.of(7, 2, 7 * 2),
				Arguments.of(7, 3, 7 * 3));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivide(int dividend, int divisor, int result) {
		assertEquals(new TestRing(result), new TestRing(dividend).divide(new TestRing(divisor)));
		
	}
	
	static Stream<Arguments> testDivide() {
		return Stream.of(
				Arguments.of(1, 4, 3),
				Arguments.of(6, 4, 7),
				Arguments.of(0, 8, 0));
	}
	
	@ParameterizedTest
	@MethodSource
	void testDivideByInt(int dividend, int divisor, int result) {
		assertEquals(new TestRing(result), new TestRing(dividend).divide(divisor));
	}
	
	static Stream<Arguments> testDivideByInt() {
		return Stream.of(
				Arguments.of(1, 4, 3),
				Arguments.of(6, 4, 7),
				Arguments.of(0, 8, 0));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPow(int base, int exponent, int result) {
		assertEquals(new TestRing(result), new TestRing(base).pow(exponent));
	}
	
	static Stream<Arguments> testPow() {
		return Stream.of(
				Arguments.of(0, 258, 0),
				Arguments.of(1, 447, 1),
				Arguments.of(2, 6, 9),
				Arguments.of(3, -5, 1),
				Arguments.of(4, 0, 1));
	}
	
	private static class TestRing implements Ring<TestRing> {
		
		static final int MODULUS = 11;
		
		final int value;
		
		TestRing(int value) {
			this.value = Math.floorMod(value, MODULUS);
		}
		
		@Override
		public TestRing negate() {
			return new TestRing(-value);
		}
		
		@Override
		public boolean isZero() {
			return value == 0;
		}
		
		@Override
		public TestRing add(TestRing summand) {
			return new TestRing(value + summand.value);
		}
		
		@Override
		public TestRing invert() {
			if (value == 0) {
				throw new ArithmeticException();
			} else {
				return pow(MODULUS - 2);
			}
		}
		
		@Override
		public TestRing multiply(TestRing factor) {
			return new TestRing(value * factor.value);
		}
		
		@Override
		public boolean equals(Object obj) {
			return value == ((TestRing) obj).value;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(value);
		}
		
		@Override
		public String toString() {
			return "TestRing[" + value + "]";
		}
	}
}
