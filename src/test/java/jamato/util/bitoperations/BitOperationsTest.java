package jamato.util.bitoperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link BitOperations} class.
 * 
 * @author JSiebel
 *
 */
class BitOperationsTest {
	
	@ParameterizedTest
	@MethodSource
	void testIntegerIndexOfNextLowerOneBit(int value, int startIndex, int result) {
		int actualResult = BitOperations.indexOfNextLowerOneBit(value, startIndex);
		assertEquals(result, actualResult);
	}
	
	static Stream<Arguments> testIntegerIndexOfNextLowerOneBit() {
		return Stream.of(
				arguments(0b00000000, 0, -1),
				arguments(0b00000000, 4, -1),
				arguments(0b01001000, 0, -1),
				arguments(0b01001000, 3, -1),
				arguments(0b01001000, 4, 3),
				arguments(0b10000000_00000000_00000000_01001000, 31, 6),
				arguments(0b10000000_00000000_00000000_01001000, 32, 31));
	}
	
	@ParameterizedTest
	@MethodSource
	void testIntegerIndexOfNextHigherOneBit(int value, int startIndex, int result) {
		int actualResult = BitOperations.indexOfNextHigherOneBit(value, startIndex);
		assertEquals(result, actualResult);
	}
	
	static Stream<Arguments> testIntegerIndexOfNextHigherOneBit() {
		return Stream.of(
				arguments(0b00000000, 0, 32),
				arguments(0b00000000, 14, 32),
				arguments(0b01001000, 2, 3),
				arguments(0b01001000, 3, 6),
				arguments(0b01001000, 4, 6),
				arguments(0b10000000_00000000_00000000_01001000, 30, 31),
				arguments(0b10000000_00000000_00000000_01001000, 31, 32));
	}
	
	@ParameterizedTest
	@MethodSource
	void testLongIndexOfNextLowerOneBit(long value, int startIndex, int result) {
		int actualResult = BitOperations.indexOfNextLowerOneBit(value, startIndex);
		assertEquals(result, actualResult);
	}
	
	static Stream<Arguments> testLongIndexOfNextLowerOneBit() {
		return Stream.of(
				arguments(0b00000000L, 0, -1),
				arguments(0b00000000L, 4, -1),
				arguments(0b01001000L, 0, -1),
				arguments(0b01001000L, 3, -1),
				arguments(0b01001000L, 4, 3),
				arguments(0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_01001000L, 63, 6),
				arguments(0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_01001000L, 64, 63));
	}
	
	@ParameterizedTest
	@MethodSource
	void testLongIndexOfNextHigherOneBit(long value, int startIndex, int result) {
		int actualResult = BitOperations.indexOfNextHigherOneBit(value, startIndex);
		assertEquals(result, actualResult);
	}
	
	static Stream<Arguments> testLongIndexOfNextHigherOneBit() {
		return Stream.of(
				arguments(0b00000000L, 0, 64),
				arguments(0b00000000L, 14, 64),
				arguments(0b01001000L, 2, 3),
				arguments(0b01001000L, 3, 6),
				arguments(0b01001000L, 4, 6),
				arguments(0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_01001000L, 62, 63),
				arguments(0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_01001000L, 63, 64));
	}
}
