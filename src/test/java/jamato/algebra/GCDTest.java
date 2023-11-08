package jamato.algebra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GCDTest {
	
	@ParameterizedTest
	@MethodSource
	void testGdc(int base, int exponent, int result) {
		assertEquals(result, GCD.of(base, exponent));
	}
	
	static Stream<Arguments> testGdc() {
		return Stream.of(
				Arguments.of(12, 30, 6),
				Arguments.of(-18, 45, 9),
				Arguments.of(27, -45, 9),
				Arguments.of(-78, -91, 13),
				Arguments.of(1, 3123422, 1),
				Arguments.of(3123422, 1, 1),
				Arguments.of(284, 0, 284),
				Arguments.of(0, 284, 284),
				Arguments.of(-284, 0, 284),
				Arguments.of(0, -284, 284),
				Arguments.of(0, 0, 0));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGdcLong(long base, long exponent, long result) {
		assertEquals(result, GCD.of(base, exponent));
	}
	
	static Stream<Arguments> testGdcLong() {
		return Stream.of(
				Arguments.of(12, 30, 6),
				Arguments.of(12L, 30L, 6L),
				Arguments.of(-18L, 45L, 9L),
				Arguments.of(27L, -45L, 9L),
				Arguments.of(-78L, -91L, 13L),
				Arguments.of(1L, 3123422L, 1L),
				Arguments.of(3123422L, 1L, 1L),
				Arguments.of(284L, 0L, 284L),
				Arguments.of(0L, 284L, 284L),
				Arguments.of(-284L, 0L, 284L),
				Arguments.of(0L, -284L, 284L),
				Arguments.of(0L, 0L, 0L));
	}
}
