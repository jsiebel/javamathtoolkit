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
	
	@ParameterizedTest
	@MethodSource
	void testIntOffsetOfNthBit(int value, int n, int result) {
		int actualResult = BitOperations.offsetOfNthBit(value, n);
		assertEquals(result, actualResult);
	}
	
	static Stream<Arguments> testIntOffsetOfNthBit() {
		return Stream.of(
				arguments(0b0, 0, 32),
				arguments(0b1, 0, 0),
				arguments(0b1, 1, 32),
				arguments(0b1, 2, 32),
				arguments(0b10, 0, 1),
				arguments(0b10000000_00000000_00000000_00000000, 0, 31),
				arguments(0b10000000_00000000_00000000_00000000, 1, 32),
				arguments(0b11111111_11111111_11111111_11111111, 0, 0),
				arguments(0b11111111_11111111_11111111_11111111, 1, 1),
				arguments(0b11111111_11111111_11111111_11111111, 31, 31),
				arguments(0b11111111_11111111_11111111_11111111, 32, 32),
				arguments(0b01100011_00111000_10110110_11010101, 9, 15));
	}
	
	@ParameterizedTest
	@MethodSource
	void testLongOffsetOfNthBit(long value, int n, long result) {
		long actualResult = BitOperations.offsetOfNthBit(value, n);
		assertEquals(result, actualResult);
	}
	
	static Stream<Arguments> testLongOffsetOfNthBit() {
		return Stream.of(
				arguments(0b0L, 0, 64),
				arguments(0b1L, 0, 0),
				arguments(0b1L, 1, 64),
				arguments(0b1L, 2, 64),
				arguments(0b10L, 0, 1),
				arguments(0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L, 0, 63),
				arguments(0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L, 1, 64),
				arguments(0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111111L, 0, 0),
				arguments(0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111111L, 1, 1),
				arguments(0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111111L, 63, 63),
				arguments(0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111111L, 64, 64),
				arguments(0b11011000_11011100_01110101_01100011_01100011_00111000_10110110_11010101L, 9, 15));
	}
}
