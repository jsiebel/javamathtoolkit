//
// PowerSetSubListTest.java
//
// Copyright (C) 2026
// GEBIT Solutions GmbH,
// Berlin, Duesseldorf, Stuttgart (Germany)
// All rights reserved.
//
package jamato.combinatorics.powerset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link PowerSetSublist} class.
 * 
 * @author JSiebel
 */
class PowerSetSublistTest {
	
	@ParameterizedTest
	@MethodSource
	void testStream(List<?> baseList, BigInteger fromIndex, BigInteger toIndex, List<?> resultList) {
		Stream<?> stream = new PowerSet<>(baseList).subList(fromIndex, toIndex).stream();
		assertEquals(resultList, stream.toList());
	}
	
	static Stream<Arguments> testStream() {
		return Stream.of(
				arguments(
						List.of(),
						BigInteger.ZERO,
						BigInteger.ONE,
						List.of(List.of())),
				arguments(
						List.of("a", "b", "c"),
						BigInteger.ZERO,
						BigInteger.valueOf(8),
						List.of(List.of(), List.of("a"), List.of("b"), List.of("a", "b"), List.of("c"),
								List.of("a", "c"), List.of("b", "c"), List.of("a", "b", "c"))),
				arguments(
						List.of("a", "b", "c", "d"),
						BigInteger.TEN,
						BigInteger.valueOf(16),
						List.of(
								List.of("b", "d"),
								List.of("a", "b", "d"),
								List.of("c", "d"),
								List.of("a", "c", "d"),
								List.of("b", "c", "d"),
								List.of("a", "b", "c", "d"))),
				arguments(
						listOfSize(4),
						BigInteger.TWO.pow(3),
						BigInteger.TWO.pow(3).add(BigInteger.valueOf(5)),
						List.of(List.of(3), List.of(0, 3), List.of(1, 3), List.of(0, 1, 3), List.of(2, 3))),
				arguments(
						listOfSize(64),
						BigInteger.TWO.pow(63),
						BigInteger.TWO.pow(63).add(BigInteger.valueOf(5)),
						List.of(List.of(63), List.of(0, 63), List.of(1, 63), List.of(0, 1, 63), List.of(2, 63))),
				arguments(
						listOfSize(100),
						BigInteger.TWO.pow(64),
						BigInteger.TWO.pow(64).add(BigInteger.valueOf(5)),
						List.of(List.of(64), List.of(0, 64), List.of(1, 64), List.of(0, 1, 64), List.of(2, 64))));
	}
	
	@ParameterizedTest
	@MethodSource
	void testContains(List<?> baseList, BigInteger fromIndex, BigInteger toIndex, Object testObject, boolean result) {
		List<?> powerSetSublist = new PowerSet<>(baseList).subList(fromIndex, toIndex);
		boolean actualResult = powerSetSublist.contains(testObject);
		assertEquals(result, actualResult);
	}
	
	static Stream<Arguments> testContains() {
		return Stream.of(
				arguments(List.of("a", "b", "c"), new BigInteger("100", 2), new BigInteger("1000", 2), "unrelatedType",
						false),
				arguments(List.of(), new BigInteger("0"), new BigInteger("0"), List.of(), false),
				arguments(List.of("a", "b", "c"), new BigInteger("0"), new BigInteger("0"), List.of(), false),
				arguments(List.of("a", "b", "c"), new BigInteger("0"), new BigInteger("1"), List.of(), true),
				arguments(List.of("a", "b", "c"), new BigInteger("0"), new BigInteger("10", 2), List.of(), true),
				arguments(List.of("a", "b", "c"), new BigInteger("0"), new BigInteger("1000", 2), List.of(), true),
				arguments(List.of("a", "a"), new BigInteger("0"), new BigInteger("100", 2), List.of("a"), true),
				arguments(List.of("a", "b", "c"), new BigInteger("0", 2), new BigInteger("11", 2),
						List.of("b"), true),
				arguments(List.of("a", "b", "c"), new BigInteger("10", 2), new BigInteger("101", 2), List.of("b"),
						true),
				arguments(List.of("a", "b", "c"), new BigInteger("100", 2), new BigInteger("1000", 2),
						List.of("a", "c"), true),
				arguments(List.of("a", "b", "c", "c"), new BigInteger("111", 2), new BigInteger("1111", 2),
						List.of("a", "b", "c"), true),
				arguments(List.of("a", "a", "a", "a"), new BigInteger("0", 2), new BigInteger("1111", 2),
						List.of("a", "a", "a", "a"), false),
				arguments(List.of("a", "b", "c", "d"), new BigInteger("1000", 2), new BigInteger("1111", 2),
						List.of("a"), false),
				arguments(List.of("a", "b", "b", "c"), new BigInteger("1000", 2), new BigInteger("1100", 2),
						List.of("a", "b", "c"), true),
				arguments(List.of("a", "b", "c"), new BigInteger("100", 2), new BigInteger("110", 2), List.of("a", "c"),
						true),
				arguments(List.of("a", "a"), new BigInteger("00", 2), new BigInteger("100", 2), List.of("a", "a", "a"),
						false),
				arguments(List.of("a", "a", "a"), new BigInteger("00", 2), new BigInteger("1000", 2), List.of("a", "a"),
						true),
				arguments(List.of("a", "b", "c"), new BigInteger("101", 2), new BigInteger("110", 2), List.of("a", "c"),
						true),
				arguments(List.of("a", "a", "a"), new BigInteger("111", 2), new BigInteger("1000", 2),
						List.of("a", "a", "a", "b"), false),
				arguments(List.of("a", "a"), new BigInteger("10", 2), new BigInteger("11", 2), List.of("a", "a"),
						false),
				arguments(List.of("a", "b", "c", "d"), new BigInteger("0100", 2), new BigInteger("1001", 2),
						List.of("a", "a", "d"), false),
				arguments(List.of("a", "b", "c", "d"), new BigInteger("0100", 2), new BigInteger("1101", 2),
						List.of("a", "c", "d"), false),
				arguments(List.of("a", "b", "c", "d"), new BigInteger("0100", 2), new BigInteger("1101", 2),
						List.of("a", "b", "d"), true),
				arguments(List.of("a", "b", "c", "d"), new BigInteger("0010", 2), new BigInteger("1101", 2),
						List.of("a", "b", "d"), true),
				arguments(List.of("a", "b", "b", "a"), new BigInteger("0", 2), new BigInteger("1001", 2),
						List.of("b"), true));
	}
	
	@Test
	void testAdd() {
		List<List<String>> powerSetSublist = new PowerSet<>(List.of("a", "b")).subList(BigInteger.ZERO, BigInteger.TWO);
		List<String> newElement = List.of("a");
		assertThrows(UnsupportedOperationException.class, () -> powerSetSublist.add(newElement));
	}
	
	private static List<?> listOfSize(int size) {
		return IntStream.range(0, size).boxed().toList();
	}
}
