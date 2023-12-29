package jamato.util.indexedsublist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link IndexedSubList} class.
 * 
 * @author JSiebel
 *
 */
class IndexedSubListTest {
	
	@ParameterizedTest
	@MethodSource
	void testConstructorIndexCheck(List<?> baseList, long mask, int expectedIndex) {
		IndexOutOfBoundsException exception = assertThrows(
				IndexOutOfBoundsException.class,
				() -> new IndexedSubList<>(baseList, mask));
		assertEquals(new IndexOutOfBoundsException(expectedIndex).getMessage(), exception.getMessage());
	}
	
	static Stream<Arguments> testConstructorIndexCheck() {
		return Stream.of(
				Arguments.of(List.of(), 0b1, 0),
				Arguments.of(List.of("a", "b", "c"), 0b1001, 3),
				Arguments.of(List.of("a", "b", "c"), 0b1010000, 4));
	}
	
	@ParameterizedTest
	@MethodSource
	void testSize(List<?> baseList, long mask, int result) {
		assertEquals(result, new IndexedSubList<>(baseList, mask).size());
	}
	
	static Stream<Arguments> testSize() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c"), 0b000, 0),
				Arguments.of(List.of("a", "b", "c"), 0b001, 1),
				Arguments.of(List.of("a", "b", "c"), 0b111, 3));
	}
	
	@ParameterizedTest
	@MethodSource
	void testEmpty(List<?> baseList, long mask, boolean result) {
		assertEquals(result, new IndexedSubList<>(baseList, mask).isEmpty());
	}
	
	static Stream<Arguments> testEmpty() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c"), 0b000, true),
				Arguments.of(List.of("a", "b", "c"), 0b001, false),
				Arguments.of(List.of("a", "b", "c"), 0b111, false));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGet(List<?> baseList, long mask, int index, String result) {
		assertEquals(result, new IndexedSubList<>(baseList, mask).get(index));
	}
	
	static Stream<Arguments> testGet() {
		return Stream.of(
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b11110, 0, "b"),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b11110, 1, "c"),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b10110, 2, "e"));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetException(List<?> baseList, long mask, int index) {
		IndexedSubList<?> indexedSubList = new IndexedSubList<>(baseList, mask);
		assertThrows(IndexOutOfBoundsException.class, () -> indexedSubList.get(index));
	}
	
	static Stream<Arguments> testGetException() {
		return Stream.of(
				Arguments.of(List.of(), 0b0, 0),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b10110, -1),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b10110, 3),
				Arguments.of(List.of("a", "b", "c", "d", "e"), 0b11111, 5));
	}
}
