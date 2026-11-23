package jamato.primes;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigInteger;
import java.util.PrimitiveIterator.OfLong;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongPrimesTest{
	
	@ParameterizedTest
	@MethodSource
	void testIsPrime(long number, boolean expected){
		assertEquals(expected, LongPrimes.isPrime(number));
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
				arguments(LongPrimes.GREATEST_LONG_PRIME - 1, false),
				arguments(LongPrimes.GREATEST_LONG_PRIME, true));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeArray(long lowerBound, long upperBound, long[] expected){
		assertArrayEquals(expected, LongPrimes.rangeArray(lowerBound, upperBound));
	}
	
	static Stream<Arguments> testRangeArray(){
		return Stream.of(
				arguments(0, 10, new long[]{ 2, 3, 5, 7 }),
				arguments(2, 7, new long[]{ 2, 3, 5 }),
				arguments(2, 8, new long[]{ 2, 3, 5, 7 }),
				arguments(15, 35, new long[]{ 17, 19, 23, 29, 31 }),
				arguments(Integer.MIN_VALUE, 1, new long[0]),
				arguments(
						Integer.MAX_VALUE - 100,
						Integer.MAX_VALUE + 100L,
						new long[]{ 2147483549L, 2147483563L, 2147483579L, 2147483587L, 2147483629L, 2147483647L,
								2147483659L, 2147483693L, 2147483713L, 2147483743L }),
				arguments(LongPrimes.GREATEST_LONG_PRIME - 10, LongPrimes.GREATEST_LONG_PRIME, new long[0]),
				arguments(
						LongPrimes.GREATEST_LONG_PRIME - 10,
						LongPrimes.GREATEST_LONG_PRIME + 1,
						new long[]{ LongPrimes.GREATEST_LONG_PRIME }));
	}
	
	@Test
	void testStream(){
		long[] values = LongPrimes.stream().limit(10).toArray();
		assertArrayEquals(new long[]{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 }, values);
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeStreamWithLowerBound(long lowerBound, long[] expected){
		long[] actual = LongPrimes.rangeStream(lowerBound).limit(5).toArray();
		assertArrayEquals(expected, actual);
	}
	
	static Stream<Arguments> testRangeStreamWithLowerBound(){
		return Stream.of(
				arguments(-10, new long[]{ 2, 3, 5, 7, 11 }),
				arguments(3, new long[]{ 3, 5, 7, 11, 13 }),
				arguments(15, new long[]{ 17, 19, 23, 29, 31 }),
				arguments(
						Integer.MAX_VALUE - 50,
						new long[]{ 2147483629L, 2147483647L, 2147483659L, 2147483693L, 2147483713L }),
				arguments(LongPrimes.GREATEST_LONG_PRIME, new long[]{ LongPrimes.GREATEST_LONG_PRIME }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeStreamWithBounds(long lowerBound, long upperBound, long[] expected){
		long[] actual = LongPrimes.rangeStream(lowerBound, upperBound).toArray();
		assertArrayEquals(expected, actual);
	}
	
	static Stream<Arguments> testRangeStreamWithBounds(){
		return Stream.of(
				arguments(-10, 10, new long[]{ 2, 3, 5, 7 }),
				arguments(3, 11, new long[]{ 3, 5, 7 }),
				arguments(15, 25, new long[]{ 17, 19, 23 }),
				arguments(17, 29, new long[]{ 17, 19, 23 }),
				arguments(100, 110, new long[]{ 101, 103, 107, 109 }),
				arguments(101, 109, new long[]{ 101, 103, 107 }),
				arguments(9223372036854775643L, LongPrimes.GREATEST_LONG_PRIME, new long[]{ 9223372036854775643L }));
	}
	
	@Test
	void testIterator(){
		// when
		OfLong iterator = LongPrimes.iterator();
		// then
		assertEquals(2, iterator.nextLong());
		assertEquals(3, iterator.nextLong());
		assertEquals(5, iterator.nextLong());
		assertEquals(7, iterator.nextLong());
		assertEquals(11, iterator.nextLong());
		assertEquals(13, iterator.nextLong());
		assertEquals(17, iterator.nextLong());
		assertEquals(19, iterator.nextLong());
		assertEquals(23, iterator.nextLong());
		assertTrue(iterator.hasNext());
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeIteratorWithLowerBound(long lowerBound, long[] expected){
		// when
		OfLong iterator = LongPrimes.rangeIterator(lowerBound);
		
		// then
		for (long expectedValue : expected){
			assertTrue(iterator.hasNext());
			assertEquals(expectedValue, iterator.nextLong());
		}
	}
	
	static Stream<Arguments> testRangeIteratorWithLowerBound(){
		return Stream.of(
				arguments(Long.MIN_VALUE, new long[]{ 2, 3, 5, 7, 11 }),
				arguments(13, new long[]{ 13, 17, 19, 23 }),
				arguments(
						Integer.MAX_VALUE - 100,
						new long[]{ 2147483549L, 2147483563L, 2147483579L, 2147483587L, 2147483629L, 2147483647L,
								2147483659L, 2147483693L, 2147483713L, 2147483743L }),
				arguments(LongPrimes.GREATEST_LONG_PRIME - 10, new long[]{ LongPrimes.GREATEST_LONG_PRIME }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testRangeIteratorWithBounds(long lowerBound, long upperBound, long[] expected){
		// when
		OfLong iterator = LongPrimes.rangeIterator(lowerBound, upperBound);
		
		// then
		for (long expectedValue : expected){
			assertTrue(iterator.hasNext());
			assertEquals(expectedValue, iterator.nextLong());
		}
		assertFalse(iterator.hasNext());
	}
	
	static Stream<Arguments> testRangeIteratorWithBounds(){
		return Stream.of(
				arguments(Long.MIN_VALUE, 5, new long[]{ 2, 3 }),
				arguments(2, 6, new long[]{ 2, 3, 5 }),
				arguments(17, 29, new long[]{ 17, 19, 23 }),
				arguments(17, 30, new long[]{ 17, 19, 23, 29 }),
				arguments(1000000, 1000100, new long[]{ 1000003, 1000033, 1000037, 1000039, 1000081, 1000099 }),
				arguments(
						Integer.MAX_VALUE - 100,
						Integer.MAX_VALUE + 100L,
						new long[]{ 2147483549L, 2147483563L, 2147483579L, 2147483587L, 2147483629L, 2147483647L,
								2147483659L, 2147483693L, 2147483713L, 2147483743L }),
				arguments(LongPrimes.GREATEST_LONG_PRIME - 10, LongPrimes.GREATEST_LONG_PRIME, new long[0]));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetDivisors(long number, long[] expected){
		assertArrayEquals(expected, LongPrimes.getDivisors(number));
	}
	
	static Stream<Arguments> testGetDivisors(){
		return Stream.of(
				arguments(36, new long[]{ 1, 2, 4, 3, 6, 12, 9, 18, 36 }),
				arguments(1, new long[]{ 1 }),
				arguments(2, new long[]{ 1, 2 }),
				arguments(4, new long[]{ 1, 2, 4 }),
				arguments(49, new long[]{ 1, 7, 49 }),
				arguments(8 * 49, new long[]{ 1, 2, 4, 8, 1 * 7, 2 * 7, 4 * 7, 8 * 7, 1 * 49, 2 * 49, 4 * 49, 8 * 49 }),
				arguments(9973, new long[]{ 1, 9973 }),
				arguments(4999999937L, new long[]{ 1, 4999999937L }),
				arguments(
						Long.MAX_VALUE,
						new long[]{ 1, 1L * 7L, 1L * 49L, 1L * 73, 1L * 7L * 73, 1L * 49L * 73, 1L * 127, 1L * 7L * 127,
								1L * 49L * 127, 1L * 73 * 127, 1L * 7L * 73 * 127, 1L * 49L * 73 * 127, 1L * 337,
								1L * 7L * 337, 1L * 49L * 337, 1L * 73 * 337, 1L * 7L * 73 * 337, 1L * 49L * 73 * 337,
								1L * 127 * 337, 1L * 7L * 127 * 337, 1L * 49L * 127 * 337, 1L * 73 * 127 * 337,
								1L * 7L * 73 * 127 * 337, 1L * 49L * 73 * 127 * 337, 1L * 92737, 1L * 7L * 92737,
								1L * 49L * 92737, 1L * 73 * 92737, 1L * 7L * 73 * 92737, 1L * 49L * 73 * 92737,
								1L * 127 * 92737, 1L * 7L * 127 * 92737, 1L * 49L * 127 * 92737, 1L * 73 * 127 * 92737,
								1L * 7L * 73 * 127 * 92737, 1L * 49L * 73 * 127 * 92737, 1L * 337 * 92737,
								1L * 7L * 337 * 92737, 1L * 49L * 337 * 92737, 1L * 73 * 337 * 92737,
								1L * 7L * 73 * 337 * 92737, 1L * 49L * 73 * 337 * 92737, 1L * 127 * 337 * 92737,
								1L * 7L * 127 * 337 * 92737, 1L * 49L * 127 * 337 * 92737, 1L * 73 * 127 * 337 * 92737,
								1L * 7L * 73 * 127 * 337 * 92737, 1L * 49L * 73 * 127 * 337 * 92737, 1L * 649657,
								1L * 7L * 649657, 1L * 49L * 649657, 1L * 73 * 649657, 1L * 7L * 73 * 649657,
								1L * 49L * 73 * 649657, 1L * 127 * 649657, 1L * 7L * 127 * 649657,
								1L * 49L * 127 * 649657, 1L * 73 * 127 * 649657, 1L * 7L * 73 * 127 * 649657,
								1L * 49L * 73 * 127 * 649657, 1L * 337 * 649657, 1L * 7L * 337 * 649657,
								1L * 49L * 337 * 649657, 1L * 73 * 337 * 649657, 1L * 7L * 73 * 337 * 649657,
								1L * 49L * 73 * 337 * 649657, 1L * 127 * 337 * 649657, 1L * 7L * 127 * 337 * 649657,
								1L * 49L * 127 * 337 * 649657, 1L * 73 * 127 * 337 * 649657,
								1L * 7L * 73 * 127 * 337 * 649657, 1L * 49L * 73 * 127 * 337 * 649657,
								1L * 92737 * 649657, 1L * 7L * 92737 * 649657, 1L * 49L * 92737 * 649657,
								1L * 73 * 92737 * 649657, 1L * 7L * 73 * 92737 * 649657, 1L * 49L * 73 * 92737 * 649657,
								1L * 127 * 92737 * 649657, 1L * 7L * 127 * 92737 * 649657,
								1L * 49L * 127 * 92737 * 649657, 1L * 73 * 127 * 92737 * 649657,
								1L * 7L * 73 * 127 * 92737 * 649657, 1L * 49L * 73 * 127 * 92737 * 649657,
								1L * 337 * 92737 * 649657, 1L * 7L * 337 * 92737 * 649657,
								1L * 49L * 337 * 92737 * 649657, 1L * 73 * 337 * 92737 * 649657,
								1L * 7L * 73 * 337 * 92737 * 649657, 1L * 49L * 73 * 337 * 92737 * 649657,
								1L * 127 * 337 * 92737 * 649657, 1L * 7L * 127 * 337 * 92737 * 649657,
								1L * 49L * 127 * 337 * 92737 * 649657, 1L * 73 * 127 * 337 * 92737 * 649657,
								1L * 7L * 73 * 127 * 337 * 92737 * 649657,
								1L * 49L * 73 * 127 * 337 * 92737 * 649657 }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetPrimeDivisors(long number, long[] expected){
		assertArrayEquals(expected, LongPrimes.getPrimeDivisors(number));
	}
	
	static Stream<Arguments> testGetPrimeDivisors(){
		return Stream.of(arguments(3 * 5 * 7 * 7, new long[]{ 3, 5, 7 }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetPrimeDivisorsStreamLong(long number, long[] expected){
		assertArrayEquals(expected, LongPrimes.getPrimeDivisorsStream(number).toArray());
	}
	
	static Stream<Arguments> testGetPrimeDivisorsStreamLong(){
		return Stream.of(
				arguments(1, new long[0]),
				arguments(2, new long[]{ 2 }),
				arguments(49, new long[]{ 7 }),
				arguments(27 * 13 * 13, new long[]{ 3, 13 }),
				arguments(4999999937L, new long[]{ 4999999937L }));
	}
	
	@ParameterizedTest
	@MethodSource
	void testGetPrimeDivisorsStreamBigInteger(BigInteger number, long[] expected){
		assertArrayEquals(expected, LongPrimes.getPrimeDivisorsStream(number).toArray());
	}
	
	static Stream<Arguments> testGetPrimeDivisorsStreamBigInteger(){
		return Stream.of(
				arguments(BigInteger.valueOf(1), new long[0]),
				arguments(BigInteger.valueOf(2), new long[]{ 2 }),
				arguments(BigInteger.valueOf(11 * 11 * 11 * 17 * 23), new long[]{ 11, 17, 23 }),
				arguments(BigInteger.TWO.shiftLeft(100), new long[]{ 2 }),
				arguments(BigInteger.valueOf(7 * 2147483659L), new long[]{ 7, 2147483659L }),
				arguments(BigInteger.valueOf(71L * 73 * 166667), new long[]{ 71, 73, 166667 }),
				
				// This number is close to Integer.MAX_VALUE after the first factor is eliminated
				arguments(BigInteger.valueOf(8L * 3 * 137 * 263 * 19867).pow(2), new long[]{ 2, 3, 137, 263, 19867 }),
				
				arguments(BigInteger.valueOf(997 * 5).pow(6), new long[]{ 5, 997 }),
				arguments(BigInteger.TEN.pow(45), new long[]{ 2, 5 }));
	}
}
