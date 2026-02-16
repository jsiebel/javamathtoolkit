package jamato.util.indexedsublist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link BigIndexedSubListIterator} class.
 * 
 * @author JSiebel
 */
class BigIndexedSubListIteratorTest {
	
	@ParameterizedTest
	@MethodSource
	void hasNextTest(List<?> list, int[] mask, int startIndex, boolean result) {
		ListIterator<?> listIterator = new BigIndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result, listIterator.hasNext());
	}
	
	static Stream<Arguments> hasNextTest() {
		return Stream.of(
				arguments(List.of("a", "b", "c"), new int[] {0b101}, 0, true),
				arguments(List.of("a", "b", "c"), new int[] {0b101}, 1, true),
				arguments(List.of("a", "b", "c"), new int[] {0b101}, 2, false),
				arguments(List.of(), new int[] {}, 0, false),
				arguments(List.of("a", "b", "c", "d"), new int[] {0b0000}, 0, false),
				arguments(listOfSize(256), new int[] {0, 0b100, 0, 0b100}, 0, true),
				arguments(listOfSize(256), new int[] {0, 0b100, 0, 0b100}, 1, true),
				arguments(listOfSize(256), new int[] {0, 0b100, 0, 0b100}, 2, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void hasPreviousTest(List<?> list, int[] mask, int startIndex, boolean result) {
		ListIterator<?> listIterator = new BigIndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result, listIterator.hasPrevious());
	}
	
	static Stream<Arguments> hasPreviousTest() {
		return Stream.of(
				arguments(List.of("a", "b", "c"), new int[] {0b101}, 0, false),
				arguments(List.of("a", "b", "c"), new int[] {0b101}, 1, true),
				arguments(List.of(), new int[] {0b0}, 0, false),
				arguments(List.of("a", "b", "c", "d"), new int[] {0b0000}, 0, false),
				arguments(listOfSize(256), new int[] {0, 4, 0, 4}, 0, false),
				arguments(listOfSize(256), new int[] {0, 4, 0, 4}, 1, true),
				arguments(listOfSize(256), new int[] {0, 4, 0, 4}, 2, true));
	}
	
	@ParameterizedTest
	@MethodSource
	void navigationTest(List<?> list, int[] mask, int startIndex, Object result1, Object result2) {
		ListIterator<?> listIterator = new BigIndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result1, listIterator.next());
		assertEquals(result2, listIterator.next());
		assertEquals(result2, listIterator.previous());
		assertEquals(result1, listIterator.previous());
	}
	
	static Stream<Arguments> navigationTest() {
		return Stream.of(
				arguments(List.of("a", "b", "c", "d"), new int[] {0b101}, 0, "a", "c"),
				arguments(List.of("a", "b", "c", "d"), new int[] {0b1101}, 1, "c", "d"),
				arguments(listOfSize(256), new int[] {0b100, 0, 0, 0b100000, 0}, 0, 2, 101));
	}
	
	@ParameterizedTest
	@MethodSource
	void nextExceptionTest(List<?> list, int[] mask, int startIndex) {
		ListIterator<?> listIterator = new BigIndexedSubListIterator<>(list, mask, startIndex);
		assertThrows(NoSuchElementException.class, listIterator::next);
	}
	
	static Stream<Arguments> nextExceptionTest() {
		return Stream.of(
				arguments(List.of(), new int[] {0b0}, 0),
				arguments(List.of("a", "b", "c"), new int[] {0b101}, 2),
				arguments(listOfSize(256), new int[] {0, 4, 0, 4}, 2));
	}
	
	@ParameterizedTest
	@MethodSource
	void previousExceptionTest(List<?> list, int[] mask, int startIndex) {
		ListIterator<?> listIterator = new BigIndexedSubListIterator<>(list, mask, startIndex);
		assertThrows(NoSuchElementException.class, listIterator::previous);
	}
	
	static Stream<Arguments> previousExceptionTest() {
		return Stream.of(
				arguments(List.of(), new int[] {0b0}, 0),
				arguments(List.of("a", "b", "c"), new int[] {0b101}, 0),
				arguments(listOfSize(256), new int[] {0, 4, 0, 4}, 0));
	}
	
	@ParameterizedTest
	@MethodSource
	void testNextIndex(List<?> list, int[] mask, int startIndex, int result) {
		ListIterator<?> listIterator = new BigIndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result, listIterator.nextIndex());
	}
	
	static Stream<Arguments> testNextIndex() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c", "d", "e"), new int[] {0b11110}, 0, 0),
				Arguments.of(List.of("a", "b", "c", "d", "e"), new int[] {0b11110}, 1, 1),
				Arguments.of(List.of("a", "b", "c", "d", "e"), new int[] {0b10110}, 2, 2),
				Arguments.of(List.of("a", "b", "c", "d", "e"), new int[] {0b10110}, 3, 3));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPreviousIndex(List<?> list, int[] mask, int startIndex, int result) {
		ListIterator<?> listIterator = new BigIndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result, listIterator.previousIndex());
	}
	
	static Stream<Arguments> testPreviousIndex() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c", "d", "e"), new int[] {0b11110}, 1, 0),
				Arguments.of(List.of("a", "b", "c", "d", "e"), new int[] {0b10110}, 2, 1),
				Arguments.of(List.of("a", "b", "c", "d", "e"), new int[] {0b10110}, 3, 2));
	}
	
	private static List<?> listOfSize(int size) {
		return IntStream.range(0, size).mapToObj(i -> i).toList();
	}
}
