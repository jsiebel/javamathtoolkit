package jamato.combinatorics.powerset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link PowerSet} class.
 * 
 * @author JSiebel
 *
 */
class PowerSetTest {
	
	@ParameterizedTest
	@MethodSource
	void testStream(List<?> baseList, BigInteger skip, long limit, List<?> resultList) {
		Stream<?> stream = new PowerSet<>(baseList).stream();
		stream = streamSkip(stream, skip);
		assertEquals(resultList, stream.limit(limit).toList());
	}
	
	static Stream<Arguments> testStream() {
		return Stream.of(
				arguments(
						List.of(),
						BigInteger.ZERO,
						5,
						List.of(List.of())),
				arguments(
						List.of("a", "b", "c", "d"),
						BigInteger.TEN,
						6,
						List.of(
								List.of("b", "d"),
								List.of("a", "b", "d"),
								List.of("c", "d"),
								List.of("a", "c", "d"),
								List.of("b", "c", "d"),
								List.of("a", "b", "c", "d")))
		
//				 The following tests are not feasible as their runtime is way too long:
		
//				arguments(
//						listOfSize(64),
//						BigInteger.TWO.pow(63),
//						5,
//						List.of(List.of(63), List.of(0, 63), List.of(1, 63), List.of(0, 1, 63), List.of(2, 63))),
//				arguments(
//						listOfSize(100),
//						BigInteger.TWO.pow(63),
//						5,
//						List.of(List.of(63), List.of(0, 63), List.of(1, 63), List.of(0, 1, 63), List.of(2, 63))),
//				arguments(
//						listOfSize(100),
//						BigInteger.TWO.pow(64),
//						5,
//						List.of(List.of(64), List.of(0, 64), List.of(1, 64), List.of(0, 1, 64), List.of(2, 64)))
		);
	}
	
	/**
	 * Returns a stream consisting of the remaining elements of the given stream after discarding the first n elements
	 * of the stream. If this stream contains fewer than n elements then an empty stream will be returned.
	 * 
	 * @param stream a stream
	 * @param skip the number of leading elements to skip
	 * @return the new stream
	 */
	private static Stream<?> streamSkip(Stream<?> stream, BigInteger skip) {
		BigInteger remainingSkip = skip;
		while (remainingSkip.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
			stream = stream.skip(Long.MAX_VALUE);
			remainingSkip = remainingSkip.subtract(BigInteger.valueOf(Long.MAX_VALUE));
		}
		stream = stream.skip(remainingSkip.longValue());
		return stream;
	}
	
	@ParameterizedTest
	@MethodSource
	void testContains(List<?> baseList, Object testObject, boolean expectedResult) {
		assertEquals(expectedResult, new PowerSet<>(baseList).contains(testObject));
	}
	
	static Stream<Arguments> testContains() {
		return Stream.of(
				arguments(List.of("a", "b", "c"), "unrelated type", false),
				arguments(List.of("a", "b", "c"), List.of(), true),
				arguments(List.of("a", "b", "c"), List.of("a", "c"), true),
				arguments(List.of("a", "b", "c"), List.of("c", "b"), false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSize(List<?> baseList, int expectedSize) {
		assertEquals(expectedSize, new PowerSet<>(baseList).size());
	}
	
	static Stream<Arguments> testSize() {
		return Stream.of(
				arguments(List.of(), 1),
				arguments(List.of("a", "b", "c"), 8),
				arguments(listOfSize(30), 1 << 30),
				arguments(listOfSize(31), Integer.MAX_VALUE),
				arguments(listOfSize(32), Integer.MAX_VALUE));
	}
	
	@ParameterizedTest
	@MethodSource
	void testActualSize(List<?> baseList, BigInteger expectedSize) {
		assertEquals(expectedSize, new PowerSet<>(baseList).actualSize());
	}
	
	static Stream<Arguments> testActualSize() {
		return Stream.of(
				arguments(List.of(), BigInteger.valueOf(1)),
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(8)),
				arguments(listOfSize(30), BigInteger.valueOf(1 << 30)),
				arguments(listOfSize(31), BigInteger.valueOf(1L << 31)),
				arguments(listOfSize(32), BigInteger.valueOf(1L << 32)),
				arguments(listOfSize(100), BigInteger.TWO.pow(100)));
	}
	
	@Test
	void testIsEmpty() {
		assertFalse(new PowerSet<>(Collections.emptyList()).isEmpty());
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetInt(List<?> baseList, int index, List<?> expectedItem) {
		assertEquals(expectedItem, new PowerSet<>(baseList).get(index));
	}
	
	static Stream<Arguments> testGetInt() {
		return Stream.of(
				arguments(List.of(), 0, Collections.emptyList()),
				arguments(List.of("a", "b", "c"), 0b111, List.of("a", "b", "c")),
				arguments(listOfSize(100), 0b11001, List.of(0, 3, 4)),
				arguments(listOfSize(31), Integer.MAX_VALUE, listOfSize(31)),
				arguments(listOfSize(32), Integer.MAX_VALUE, listOfSize(31)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetIntException(List<?> baseList, int index, List<?> expectedItem) {
		PowerSet<?> powerSet = new PowerSet<>(baseList);
		assertThrows(IndexOutOfBoundsException.class, () -> powerSet.get(index));
	}
	
	static Stream<Arguments> testGetIntException() {
		return Stream.of(
				arguments(List.of(), 1, Collections.emptyList()),
				arguments(List.of("a", "b", "c"), -1, List.of("a", "b", "c")),
				arguments(List.of("a", "b", "c"), 0b1000, List.of("a", "b", "c")));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetBigInteger(List<?> baseList, BigInteger index, List<?> expectedItem) {
		assertEquals(expectedItem, new PowerSet<>(baseList).get(index));
	}
	
	static Stream<Arguments> testGetBigInteger() {
		return Stream.of(
				arguments(List.of(), BigInteger.ZERO, Collections.emptyList()),
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(0b111), List.of("a", "b", "c")),
				arguments(listOfSize(100), BigInteger.ONE.shiftLeft(99), List.of(99)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetBigIntegerException(List<?> baseList, BigInteger index) {
		PowerSet<?> powerSet = new PowerSet<>(baseList);
		assertThrows(IndexOutOfBoundsException.class, () -> powerSet.get(index));
	}
	
	static Stream<Arguments> testGetBigIntegerException() {
		return Stream.of(
				arguments(List.of(), BigInteger.ONE),
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(-1)),
				arguments(listOfSize(100), BigInteger.ONE.shiftLeft(100)));
	}
	
	private static List<?> listOfSize(int size) {
		return IntStream.range(0, size).boxed().toList();
	}
}
