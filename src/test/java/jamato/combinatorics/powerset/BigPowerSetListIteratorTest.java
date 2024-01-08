package jamato.combinatorics.powerset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigInteger;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link PowerSetListIterator} class.
 * 
 * @author JSiebel
 *
 */
class BigPowerSetListIteratorTest {
	
	@ParameterizedTest
	@MethodSource
	void hasNextTest(List<String> baseList, BigInteger startIndex, boolean result) {
		ListIterator<List<String>> listIterator = new BigPowerSetListIterator<>(baseList, startIndex);
		assertEquals(result, listIterator.hasNext());
	}
	
	static Stream<Arguments> hasNextTest() {
		return Stream.of(
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(0b0), true),
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(0b111), true),
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(0b1000), false),
				arguments(List.of(), BigInteger.valueOf(0b0), true),
				arguments(List.of(), BigInteger.valueOf(0b1), false));
	}
	
	@ParameterizedTest
	@MethodSource
	void hasPreviousTest(List<String> baseList, BigInteger startIndex, boolean result) {
		ListIterator<List<String>> listIterator = new BigPowerSetListIterator<>(baseList, startIndex);
		assertEquals(result, listIterator.hasPrevious());
	}
	
	static Stream<Arguments> hasPreviousTest() {
		return Stream.of(
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(0), false),
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(1), true),
				arguments(List.of(), BigInteger.valueOf(0), false),
				arguments(List.of(), BigInteger.valueOf(1), true));
	}
	
	@ParameterizedTest
	@MethodSource
	void navigationTest(List<String> baseList, BigInteger startIndex, List<String> result1, List<String> result2) {
		ListIterator<List<String>> listIterator = new BigPowerSetListIterator<>(baseList, startIndex);
		assertEquals(result1, listIterator.next());
		assertEquals(result2, listIterator.next());
		assertEquals(result2, listIterator.previous());
		assertEquals(result1, listIterator.previous());
	}
	
	static Stream<Arguments> navigationTest() {
		return Stream.of(
				arguments(List.of("a", "b", "c", "d"), BigInteger.valueOf(0b101), List.of("a", "c"), List.of("b", "c")),
				arguments(List.of("a", "b", "c", "d"), BigInteger.valueOf(0b1101), List.of("a", "c", "d"), List.of("b", "c", "d")));
	}
	
	@ParameterizedTest
	@MethodSource
	void nextExceptionTest(List<String> baseList, BigInteger startIndex) {
		ListIterator<List<String>> listIterator = new BigPowerSetListIterator<>(baseList, startIndex);
		assertThrows(NoSuchElementException.class, listIterator::next);
	}
	
	static Stream<Arguments> nextExceptionTest() {
		return Stream.of(
				arguments(List.of(), BigInteger.valueOf(0b1)),
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(0b1000)));
	}
	
	@ParameterizedTest
	@MethodSource
	void previousExceptionTest(List<String> baseList, BigInteger startIndex) {
		ListIterator<List<String>> listIterator = new BigPowerSetListIterator<>(baseList, startIndex);
		assertThrows(NoSuchElementException.class, listIterator::previous);
	}
	
	static Stream<Arguments> previousExceptionTest() {
		return Stream.of(
				arguments(List.of(), BigInteger.valueOf(0b0)),
				arguments(List.of("a", "b", "c"), BigInteger.valueOf(0b0)));
	}
	
	@ParameterizedTest
	@MethodSource
	void testNextIndex(List<String> baseList, BigInteger startIndex, int result) {
		ListIterator<List<String>> listIterator = new BigPowerSetListIterator<>(baseList, startIndex);
		assertEquals(result, listIterator.nextIndex());
	}
	
	static Stream<Arguments> testNextIndex() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c", "d", "e"), BigInteger.valueOf(0b0), 0b0),
				Arguments.of(List.of("a", "b", "c", "d", "e"), BigInteger.valueOf(0b100000), 0b100000));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPreviousIndex(List<String> baseList, BigInteger startIndex, int result) {
		ListIterator<List<String>> listIterator = new BigPowerSetListIterator<>(baseList, startIndex);
		assertEquals(result, listIterator.previousIndex());
	}
	
	static Stream<Arguments> testPreviousIndex() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c", "d", "e"), BigInteger.valueOf(0b1), 0b0),
				Arguments.of(List.of("a", "b", "c", "d", "e"), BigInteger.valueOf(0b100000), 0b11111));
	}
}
