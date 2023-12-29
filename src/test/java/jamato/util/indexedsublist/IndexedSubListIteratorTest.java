package jamato.util.indexedsublist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link IndexedSubListIterator} class.
 * 
 * @author JSiebel
 *
 */
class IndexedSubListIteratorTest {
	
	@ParameterizedTest
	@MethodSource
	void hasNextTest(List<?> list, long mask, int startIndex, boolean result) {
		ListIterator<?> listIterator = new IndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result, listIterator.hasNext());
	}
	
	static Stream<Arguments> hasNextTest() {
		return Stream.of(
				arguments(List.of("a", "b", "c"), 0b101, 0, true),
				arguments(List.of("a", "b", "c"), 0b101, 1, true),
				arguments(List.of("a", "b", "c"), 0b101, 2, false),
				arguments(List.of(), 0b0, 0, false),
				arguments(List.of("a", "b", "c", "d"), 0b0000, 0, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void hasPreviousTest(List<?> list, long mask, int startIndex, boolean result) {
		ListIterator<?> listIterator = new IndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result, listIterator.hasPrevious());
	}
	
	static Stream<Arguments> hasPreviousTest() {
		return Stream.of(
				arguments(List.of("a", "b", "c"), 0b101, 0, false),
				arguments(List.of("a", "b", "c"), 0b101, 1, true),
				arguments(List.of(), 0b0, 0, false),
				arguments(List.of("a", "b", "c", "d"), 0b0000, 0, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void navigationTest(List<?> list, long mask, int startIndex, String result1, String result2) {
		ListIterator<?> listIterator = new IndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result1, listIterator.next());
		assertEquals(result2, listIterator.next());
		assertEquals(result2, listIterator.previous());
		assertEquals(result1, listIterator.previous());
	}
	
	static Stream<Arguments> navigationTest() {
		return Stream.of(
				arguments(List.of("a", "b", "c", "d"), 0b101, 0, "a", "c"),
				arguments(List.of("a", "b", "c", "d"), 0b1101, 1, "c", "d"));
	}
	
	@ParameterizedTest
	@MethodSource
	void nextExceptionTest(List<?> list, long mask, int startIndex) {
		ListIterator<?> listIterator = new IndexedSubListIterator<>(list, mask, startIndex);
		assertThrows(NoSuchElementException.class, listIterator::next);
	}
	
	static Stream<Arguments> nextExceptionTest() {
		return Stream.of(
				arguments(List.of(), 0b0, 0),
				arguments(List.of("a", "b", "c"), 0b101, 2));
	}
	
	@ParameterizedTest
	@MethodSource
	void previousExceptionTest(List<?> list, long mask, int startIndex) {
		ListIterator<?> listIterator = new IndexedSubListIterator<>(list, mask, startIndex);
		assertThrows(NoSuchElementException.class, listIterator::previous);
	}
	
	static Stream<Arguments> previousExceptionTest() {
		return Stream.of(
				arguments(List.of(), 0b0, 0),
				arguments(List.of("a", "b", "c"), 0b101, 0));
	}
	
	@ParameterizedTest
	@MethodSource
	void testNextIndex(List<?> list, long mask, int startIndex, int result) {
		ListIterator<?> listIterator = new IndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result, listIterator.nextIndex());
	}
	
	static Stream<Arguments> testNextIndex() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b11110, 0, 0),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b11110, 1, 1),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b10110, 2, 2),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b10110, 3, 3));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPreviousIndex(List<?> list, long mask, int startIndex, int result) {
		ListIterator<?> listIterator = new IndexedSubListIterator<>(list, mask, startIndex);
		assertEquals(result, listIterator.previousIndex());
	}
	
	static Stream<Arguments> testPreviousIndex() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b11110, 1, 0),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b10110, 2, 1),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b10110, 3, 2));
	}
}
