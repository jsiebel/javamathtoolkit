package jamato.util.indexedsublist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link BigIndexedSubList} class.
 * 
 * @author JSiebel
 *
 */
class BigIndexedSubListTest {
	
	@ParameterizedTest
	@MethodSource
	void testLongArrayConstructorIndexCheck(List<?> baseList, long[] mask, int expectedIndex) {
		IndexOutOfBoundsException exception = assertThrows(
				IndexOutOfBoundsException.class,
				() -> new BigIndexedSubList<>(baseList, mask));
		assertEquals(new IndexOutOfBoundsException(expectedIndex).getMessage(), exception.getMessage());
	}
	
	static Stream<Arguments> testLongArrayConstructorIndexCheck() {
		return Stream.of(
				Arguments.of(List.of(), new long[] { 0b1 }, 0),
				Arguments.of(List.of("a", "b", "c"), new long[] { 0b1000 }, 3),
				Arguments.of(List.of("a", "b", "c"), new long[] { 0b0, 0b1 }, 64),
				Arguments.of(List.of("a", "b", "c"), new long[] { 0b0, 0b0, 0b1 }, 128));
	}
	
	@ParameterizedTest
	@MethodSource
	void testBigIntegerConstructorIndexCheck(List<?> baseList, BigInteger mask, int expectedIndex) {
		Exception exception = assertThrows(
				IndexOutOfBoundsException.class,
				() -> new BigIndexedSubList<>(baseList, mask));
		assertEquals(new IndexOutOfBoundsException(expectedIndex).getMessage(), exception.getMessage());
	}
	
	static Stream<Arguments> testBigIntegerConstructorIndexCheck() {
		return Stream.of(
				Arguments.of(List.of(), BigInteger.valueOf(0b1), 0),
				Arguments.of(List.of("a", "b", "c"), BigInteger.valueOf(0b1000), 3));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSize(List<?> baseList, BigInteger mask, int result) {
		assertEquals(result, new BigIndexedSubList<>(baseList, mask).size());
	}
	
	static Stream<Arguments> testSize() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c"), new BigInteger("000", 2), 0),
				Arguments.of(List.of("a", "b", "c"), new BigInteger("001", 2), 1),
				Arguments.of(List.of("a", "b", "c"), new BigInteger("111", 2), 3),
				Arguments.of(listOfSize(64), new BigInteger("1111", 2), 4));
	}
	
	@ParameterizedTest
	@MethodSource
	void testEmpty(List<?> baseList, long[] mask, boolean result) {
		assertEquals(result, new BigIndexedSubList<>(baseList, mask).isEmpty());
	}
	
	static Stream<Arguments> testEmpty() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c"), new long[] { 0b000 }, true),
				Arguments.of(List.of("a", "b", "c"), new long[] { 0b001 }, false),
				Arguments.of(List.of("a", "b", "c"), new long[] { 0b111 }, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGet(List<?> baseList, BigInteger mask, int index, Object result) {
		assertEquals(result, new BigIndexedSubList<>(baseList, mask).get(index));
	}
	
	static Stream<Arguments> testGet() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c", "d", "e"), BigInteger.valueOf(0b11110), 0, "b"),
				Arguments.of(List.of("a", "b", "c", "d", "e"), BigInteger.valueOf(0b11110), 1, "c"),
				Arguments.of(List.of("a", "b", "c", "d", "e"), BigInteger.valueOf(0b10110), 2, "e"),
				Arguments.of(listOfSize(22), new BigInteger("14", 16), 0, 2),
				Arguments.of(listOfSize(22), new BigInteger("14", 16), 1, 4),
				Arguments.of(listOfSize(70), new BigInteger("10000000000000002", 16), 0, 1),
				Arguments.of(listOfSize(70), new BigInteger("10000000000000002", 16), 1, 64),
				Arguments.of(listOfSize(129), new BigInteger("100000000000000010000000000000001", 16), 2, 128),
				Arguments.of(listOfSize(129), new BigInteger("100000f000000000f00000000f000000f", 16), 0, 0),
				Arguments.of(listOfSize(129), new BigInteger("100000f000000000f00000000f000000f", 16), 1, 1),
				Arguments.of(listOfSize(129), new BigInteger("100000f000000000f00000000f000000f", 16), 4, 28),
				Arguments.of(listOfSize(129), new BigInteger("100000f000000000f00000000f000000f", 16), 16, 128));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetException(List<?> baseList, long[] mask, int index) {
		BigIndexedSubList<?> BigIndexedSubList = new BigIndexedSubList<>(baseList, mask);
		assertThrows(IndexOutOfBoundsException.class, () -> BigIndexedSubList.get(index));
	}
	
	static Stream<Arguments> testGetException() {
		return Stream.of(
				Arguments.of(List.of(), new long[] { 0b0 }, 0),
				Arguments.of(List.of("a", "b", "c", "d", "e"), new long[] { 0b10110 }, -1),
				Arguments.of(List.of("a", "b", "c", "d", "e"), new long[] { 0b10110 }, 3),
				Arguments.of(List.of("a", "b", "c", "d", "e"), new long[] { 0b11111 }, 5));
	}
	
	private static List<?> listOfSize(int size) {
		return IntStream.range(0, size).boxed().toList();
	}
}
