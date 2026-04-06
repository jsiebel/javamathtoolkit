package jamato.primes;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigInteger;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PrimesTest{
	
	@BeforeEach
	void resetCache(){
		Primes.resetCache();
	}
	
	@ParameterizedTest
	@MethodSource
	void testIsPrime(int number, boolean expected){
		assertEquals(expected, Primes.isPrime(number));
	}
	
	static Stream<Arguments> testIsPrime(){
		return Stream.of(
				arguments(Integer.MIN_VALUE, false),
				arguments(0, false),
				arguments(1, false),
				arguments(2, true),
				arguments(11, true),
				arguments(12, false),
				arguments(9973, true),
				arguments(1949 * 7793, false),
				arguments(2221 * 4441, false),
				arguments(Primes.GREATEST_INT_PRIME - 1, false),
				arguments(Primes.GREATEST_INT_PRIME, true));
	}
	
	@ParameterizedTest
	@MethodSource
	void testArray(int fromIndex, int toIndex, int[] expected){
		assertArrayEquals(expected, Primes.array(fromIndex, toIndex));
	}
	
	static Stream<Arguments> testArray(){
		return Stream.of(
				arguments(0, 4, new int[]{ 2, 3, 5, 7 }),
				arguments(0, 11, new int[]{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 }),
				arguments(
						0,
						25,
						new int[]{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79,
								83, 89, 97 }),
				arguments(1000, 1010, new int[]{ 7927, 7933, 7937, 7949, 7951, 7963, 7993, 8009, 8011, 8017 }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testArrayLength(int input, int expectedLength){
		assertEquals(expectedLength, Primes.array(0, input).length);
	}
	
	static Stream<Arguments> testArrayLength(){
		return Stream.of(arguments(0, 0), arguments(78498, 78498));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeArray(int lowerBound, int upperBound, int[] expected){
		assertArrayEquals(expected, Primes.rangeArray(lowerBound, upperBound));
	}
	
	static Stream<Arguments> testRangeArray(){
		return Stream.of(
				arguments(0, 10, new int[]{ 2, 3, 5, 7 }),
				arguments(2, 7, new int[]{ 2, 3, 5 }),
				arguments(2, 8, new int[]{ 2, 3, 5, 7 }),
				arguments(15, 35, new int[]{ 17, 19, 23, 29, 31 }),
				arguments(Integer.MIN_VALUE, 1, new int[0]),
				arguments(
						1_000_000_000,
						1_000_000_207,
						new int[]{ 1000000007, 1000000009, 1000000021, 1000000033, 1000000087, 1000000093, 1000000097,
								1000000103, 1000000123, 1000000181 }),
				arguments(Primes.GREATEST_INT_PRIME - 10, Primes.GREATEST_INT_PRIME, new int[0]));
	}
	
	@Test
	void testStream(){
		int[] values = Primes.stream().limit(10).toArray();
		assertArrayEquals(new int[]{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 }, values);
	}
	
	@ParameterizedTest
	@MethodSource
	void testStreamWithFromIndex(int fromIndex, int[] expected){
		assertArrayEquals(expected, Primes.stream(fromIndex).limit(10).toArray());
	}
	
	static Stream<Arguments> testStreamWithFromIndex(){
		return Stream.of(
				arguments(0, new int[]{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 }),
				arguments(4, new int[]{ 11, 13, 17, 19, 23, 29, 31, 37, 41, 43 }),
				arguments(25, new int[]{ 101, 103, 107, 109, 113, 127, 131, 137, 139, 149 }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testStreamWithIndices(int fromIndex, int toIndex, int[] expected){
		assertArrayEquals(expected, Primes.stream(fromIndex, toIndex).limit(10).toArray());
	}
	
	static Stream<Arguments> testStreamWithIndices(){
		return Stream.of(
				arguments(0, 4, new int[]{ 2, 3, 5, 7 }),
				arguments(4, 14, new int[]{ 11, 13, 17, 19, 23, 29, 31, 37, 41, 43 }),
				arguments(25, 30, new int[]{ 101, 103, 107, 109, 113 }),
				arguments(44, 44, new int[]{}));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeStreamWithLowerBound(int lowerBound, int[] expected){
		assertArrayEquals(expected, Primes.rangeStream(lowerBound).limit(5).toArray());
	}
	
	static Stream<Arguments> testRangeStreamWithLowerBound(){
		return Stream.of(
				arguments(-10, new int[]{ 2, 3, 5, 7, 11 }),
				arguments(3, new int[]{ 3, 5, 7, 11, 13 }),
				arguments(15, new int[]{ 17, 19, 23, 29, 31 }),
				arguments(Primes.GREATEST_INT_PRIME, new int[]{ Primes.GREATEST_INT_PRIME }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeStreamWithBounds(int lowerBound, int upperBound, int[] expected){
		assertArrayEquals(expected, Primes.rangeStream(lowerBound, upperBound).toArray());
	}
	
	static Stream<Arguments> testRangeStreamWithBounds(){
		return Stream.of(
				arguments(-10, 10, new int[]{ 2, 3, 5, 7 }),
				arguments(3, 11, new int[]{ 3, 5, 7 }),
				arguments(15, 25, new int[]{ 17, 19, 23 }),
				arguments(17, 29, new int[]{ 17, 19, 23 }),
				arguments(100, 110, new int[]{ 101, 103, 107, 109 }),
				arguments(101, 109, new int[]{ 101, 103, 107 }),
				arguments(2147483629, Primes.GREATEST_INT_PRIME, new int[]{ 2147483629 }));
	}
	
	@Test
	void testIterator(){
		// when
		OfInt iterator = Primes.iterator();
		// then
		assertEquals(2, iterator.nextInt());
		assertEquals(3, iterator.nextInt());
		assertEquals(5, iterator.nextInt());
		assertEquals(7, iterator.nextInt());
		assertEquals(11, iterator.nextInt());
		assertEquals(13, iterator.nextInt());
		assertEquals(17, iterator.nextInt());
		assertEquals(19, iterator.nextInt());
		assertEquals(23, iterator.nextInt());
		assertTrue(iterator.hasNext());
	}
	
	@Test
	void testIteratorWithFromIndex(){
		// when
		OfInt iterator = Primes.iterator(5);
		// then
		assertEquals(13, iterator.nextInt());
		assertEquals(17, iterator.nextInt());
		assertEquals(19, iterator.nextInt());
		assertEquals(23, iterator.nextInt());
		assertTrue(iterator.hasNext());
	}
	
	@ParameterizedTest
	@MethodSource
	void testIteratorWithIndices(int fromIndex, int toIndex, int[] expected){
		// when
		OfInt iterator = Primes.iterator(fromIndex, toIndex);
		
		// then
		for (int expectedValue : expected){
			assertTrue(iterator.hasNext());
			assertEquals(expectedValue, iterator.nextInt());
		}
		assertFalse(iterator.hasNext());
	}
	
	static Stream<Arguments> testIteratorWithIndices(){
		return Stream.of(
				arguments(0, 5, new int[]{ 2, 3, 5, 7, 11 }),
				arguments(5, 10, new int[]{ 13, 17, 19, 23, 29 }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeIteratorWithLowerBound(int lowerBound, int[] expected){
		// when
		OfInt iterator = Primes.rangeIterator(lowerBound);
		
		// then
		for (int expectedValue : expected){
			assertTrue(iterator.hasNext());
			assertEquals(expectedValue, iterator.nextInt());
		}
		assertFalse(iterator.hasNext());
	}
	
	static Stream<Arguments> testRangeIteratorWithLowerBound(){
		return Stream.of(
				arguments(Primes.GREATEST_INT_PRIME - 10, new int[]{ Primes.GREATEST_INT_PRIME }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeIteratorWithIndices(int lowerBound, int upperBound, int[] expected){
		// when
		OfInt iterator = Primes.rangeIterator(lowerBound, upperBound);
		
		// then
		for (int expectedValue : expected){
			assertTrue(iterator.hasNext());
			assertEquals(expectedValue, iterator.nextInt());
		}
		assertFalse(iterator.hasNext());
	}
	
	static Stream<Arguments> testRangeIteratorWithIndices(){
		return Stream.of(
				arguments(Integer.MIN_VALUE, 5, new int[]{ 2, 3 }),
				arguments(2, 6, new int[]{ 2, 3, 5 }),
				arguments(17, 29, new int[]{ 17, 19, 23 }),
				arguments(17, 30, new int[]{ 17, 19, 23, 29 }),
				arguments(1000000, 1000100, new int[]{ 1000003, 1000033, 1000037, 1000039, 1000081, 1000099 }),
				arguments(Primes.GREATEST_INT_PRIME - 10, Primes.GREATEST_INT_PRIME, new int[0]));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetDivisors(int number, int[] expected){
		assertArrayEquals(expected, Primes.getDivisors(number));
	}
	
	static Stream<Arguments> testGetDivisors(){
		return Stream.of(
				arguments(36, new int[]{ 1, 2, 4, 3, 6, 12, 9, 18, 36 }),
				arguments(1, new int[]{ 1 }),
				arguments(2, new int[]{ 1, 2 }),
				arguments(4, new int[]{ 1, 2, 4 }),
				arguments(49, new int[]{ 1, 7, 49 }),
				arguments(8 * 49, new int[]{ 1, 2, 4, 8, 1 * 7, 2 * 7, 4 * 7, 8 * 7, 1 * 49, 2 * 49, 4 * 49, 8 * 49 }),
				arguments(9973, new int[]{ 1, 9973 }),
				arguments(Primes.GREATEST_INT_PRIME, new int[]{ 1, Primes.GREATEST_INT_PRIME }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetPrimeDivisors(int number, int[] expected){
		assertArrayEquals(expected, Primes.getPrimeDivisors(number));
	}
	
	static Stream<Arguments> testGetPrimeDivisors(){
		return Stream.of(arguments(3 * 5 * 7 * 7, new int[]{ 3, 5, 7 }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetPrimeDivisorsStreamInt(int number, int[] expected){
		assertArrayEquals(expected, Primes.getPrimeDivisorsStream(number).toArray());
	}
	
	static Stream<Arguments> testGetPrimeDivisorsStreamInt(){
		return Stream.of(
				arguments(1, new int[0]),
				arguments(2, new int[]{ 2 }),
				arguments(49, new int[]{ 7 }),
				arguments(27 * 13 * 13, new int[]{ 3, 13 }),
				arguments(Primes.GREATEST_INT_PRIME, new int[]{ Primes.GREATEST_INT_PRIME }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetIntPrimeDivisorsStream(BigInteger number, int[] expected){
		assertArrayEquals(expected, Primes.getIntPrimeDivisorsStream(number).toArray());
	}
	
	static Stream<Arguments> testGetIntPrimeDivisorsStream(){
		return Stream.of(
				arguments(BigInteger.valueOf(1), new int[0]),
				arguments(BigInteger.valueOf(2), new int[]{ 2 }),
				arguments(BigInteger.valueOf(11 * 11 * 11 * 17 * 23), new int[]{ 11, 17, 23 }),
				arguments(BigInteger.TWO.shiftLeft(100), new int[]{ 2 }),
				arguments(BigInteger.valueOf(7 * 2147483659L), new int[]{ 7 }),
				arguments(BigInteger.valueOf(71L * 73 * 166667), new int[]{ 71, 73, 166667 }),
				
				// This number is close to Integer.MAX_VALUE after the first factor is eliminated
				arguments(BigInteger.valueOf(8L * 3 * 137 * 263 * 19867).pow(2), new int[]{ 2, 3, 137, 263, 19867 }),
				
				arguments(BigInteger.valueOf(997 * 5).pow(6), new int[]{ 5, 997 }),
				arguments(BigInteger.TEN.pow(45), new int[]{ 2, 5 }));
	}
}
