package jamato.combinatorics;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import combinatorics.BinomialCoefficient;
import jamato.algebra.Ring;
import jamato.number.DoubleComplex;
import jamato.number.IntRational;
import jamato.polynomial.DoublePolynomial;

class BinomialCoefficientTest {
	
	@ParameterizedTest
	@MethodSource
	void testBinomInt(int n, int k, int result) {
		assertEquals(result, BinomialCoefficient.binom(n, k));
	}
	
	static Stream<Arguments> testBinomInt() {
		return Stream.of(
				Arguments.of(5, 2, 10),
				Arguments.of(10, 7, 10 * 9 * 8 / 6),
				Arguments.of(0, 0, 1),
				Arguments.of(-10, 2, 55),
				Arguments.of(-10, 3, -220),
				Arguments.of(-3, 6, 28),
				Arguments.of(-3, 7, -36),
				Arguments.of(15, -1, 0),
				Arguments.of(15, 0, 1),
				Arguments.of(15, 1, 15),
				Arguments.of(443, 442, 443),
				Arguments.of(443, 443, 1),
				Arguments.of(443, 444, 0),
				Arguments.of(60001, 2, 60001 * (60000 / 2)), // avoid overflow calculating n*(n-1)/2
				Arguments.of(33, 17, 1166803110),
				Arguments.of(-1, Integer.MAX_VALUE, -1),
				Arguments.of(-2, Integer.MAX_VALUE, Integer.MIN_VALUE),
				Arguments.of(Integer.MIN_VALUE, 0, 1),
				Arguments.of(Integer.MIN_VALUE, 1, Integer.MIN_VALUE));
	}
	
	@ParameterizedTest
	@MethodSource
	void testBinomLong(long n, long k, long result) {
		assertEquals(result, BinomialCoefficient.binom(n, k));
	}
	
	static Stream<Arguments> testBinomLong() {
		return Stream.of(
				Arguments.of(5, 2, 10),
				Arguments.of(10, 7, 10 * 9 * 8 / 6),
				Arguments.of(0, 0, 1),
				Arguments.of(-10, 2, 55),
				Arguments.of(-10, 3, -220),
				Arguments.of(-3, 6, 28),
				Arguments.of(-3, 7, -36),
				Arguments.of(15, -1, 0),
				Arguments.of(15, 0, 1),
				Arguments.of(15, 1, 15),
				Arguments.of(443, 442, 443),
				Arguments.of(443, 443, 1),
				Arguments.of(443, 444, 0),
				Arguments.of(4_000_000_001L, 2, 4_000_000_001L * (4_000_000_000L / 2)), // avoid overflow calculating
																						// n*(n-1)/2
				Arguments.of(33, 17, 1166803110),
				Arguments.of(66, 33, 7219428434016265740L),
				Arguments.of(99, 82, 5519611944537877494L),
				Arguments.of(-1, Long.MAX_VALUE, -1),
				Arguments.of(-2, Long.MAX_VALUE, Long.MIN_VALUE),
				Arguments.of(Long.MIN_VALUE, 0, 1),
				Arguments.of(Long.MIN_VALUE, 1, Long.MIN_VALUE));
	}
	
	@ParameterizedTest
	@MethodSource
	void testBinomDouble(double n, long k, double result) {
		double delta = Math.abs(result) * 1e-9;
		assertEquals(result, BinomialCoefficient.binom(n, k), delta);
	}
	
	static Stream<Arguments> testBinomDouble() {
		return Stream.of(
				Arguments.of(5, 2, 10),
				Arguments.of(2.5, 3, 0.3125),
				Arguments.of(13.2, 15, -0.001398636097306624),
				Arguments.of(0, 0, 1),
				Arguments.of(-10, 2, 55),
				Arguments.of(-10, 3, -220),
				Arguments.of(82.0 / 3, 5, 62894902.0 / 729),
				Arguments.of(-111.111, 11, -1.2902634339734162E15));
	}
	
	@ParameterizedTest
	@MethodSource
	void testBinomBigInteger(BigInteger n, BigInteger k, BigInteger result) {
		assertEquals(result, BinomialCoefficient.binom(n, k));
	}
	
	static Stream<Arguments> testBinomBigInteger() {
		return Stream.of(
				Arguments.of(BigInteger.valueOf(5), BigInteger.valueOf(2), BigInteger.valueOf(10)),
				Arguments.of(BigInteger.valueOf(10), BigInteger.valueOf(7), BigInteger.valueOf(10 * 9 * 8 / 6)),
				Arguments.of(BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(1)),
				Arguments.of(BigInteger.valueOf(-10), BigInteger.valueOf(2), BigInteger.valueOf(55)),
				Arguments.of(BigInteger.valueOf(-10), BigInteger.valueOf(3), BigInteger.valueOf(-220)),
				Arguments.of(BigInteger.valueOf(-3), BigInteger.valueOf(6), BigInteger.valueOf(28)),
				Arguments.of(BigInteger.valueOf(-3), BigInteger.valueOf(7), BigInteger.valueOf(-36)),
				Arguments.of(BigInteger.valueOf(15), BigInteger.valueOf(0), BigInteger.valueOf(1)),
				Arguments.of(BigInteger.valueOf(15), BigInteger.valueOf(1), BigInteger.valueOf(15)),
				Arguments.of(BigInteger.valueOf(443), BigInteger.valueOf(442), BigInteger.valueOf(443)),
				Arguments.of(BigInteger.valueOf(443), BigInteger.valueOf(443), BigInteger.valueOf(1)),
				Arguments.of(BigInteger.valueOf(443), BigInteger.valueOf(444), BigInteger.valueOf(0)),
				Arguments.of(BigInteger.valueOf(60001), BigInteger.valueOf(2), BigInteger.valueOf(60001 * (60000 / 2))),
				Arguments.of(
						BigInteger.valueOf(4000),
						BigInteger.valueOf(20),
						new BigInteger("430935842846395817171619344153709943715872089267709800")),
				Arguments.of(
						BigInteger.valueOf(5000),
						BigInteger.valueOf(4900),
						new BigInteger(
								"31200712362264902158218956128032228473657743533947971322421615117159973590240524417467398634919490225388156366904500778507607366736438460753230447092622556048011898852325839203618027924154110887085798563637966200")));
	}
	
	@ParameterizedTest
	@MethodSource
	<T extends Ring<T>> void testBinomRing(T n, int k, T one, T result) {
		assertEquals(result, BinomialCoefficient.binom(n, k, one));
	}
	
	static Stream<Arguments> testBinomRing() {
		return Stream.of(
				Arguments.of(IntRational.valueOf("5/2"), 3, IntRational.ONE, IntRational.valueOf("15/48")),
				Arguments.of(DoubleComplex.valueOf("4-3i"), 3, DoubleComplex.ONE, DoubleComplex.valueOf("-9.5-8.5i")));
	}
	
	@ParameterizedTest
	@MethodSource
	void binomStreamInt(int n, int limit, IntStream result) {
		assertArrayEquals(result.toArray(), BinomialCoefficient.binomStream(n).limit(limit).toArray());
	}
	
	static Stream<Arguments> binomStreamInt() {
		return Stream.of(
				Arguments.of(0, 10, IntStream.of(1)),
				Arguments.of(1, 10, IntStream.of(1, 1)),
				Arguments.of(3, 10, IntStream.of(1, 3, 3, 1)),
				Arguments.of(-4, 10, IntStream.of(1, -4, 10, -20, 35, -56, 84, -120, 165, -220)));
	}
	
	@ParameterizedTest
	@MethodSource
	void binomStreamLong(long n, int limit, LongStream result) {
		assertArrayEquals(result.toArray(), BinomialCoefficient.binomStream(n).limit(limit).toArray());
	}
	
	static Stream<Arguments> binomStreamLong() {
		return Stream.of(
				Arguments.of(0, 10, LongStream.of(1)),
				Arguments.of(1, 10, LongStream.of(1, 1)),
				Arguments.of(3, 10, LongStream.of(1, 3, 3, 1)),
				Arguments.of(-4, 10, LongStream.of(1, -4, 10, -20, 35, -56, 84, -120, 165, -220)),
				Arguments.of(
						60,
						10,
						LongStream.of(
								1L,
								60L,
								1_770L,
								34_220L,
								487_635L,
								5_461_512L,
								50_063_860L,
								386_206_920L,
								2_558_620_845L,
								14_783_142_660L)),
				Arguments.of(
						4_000_000_001L,
						3,
						LongStream.of(1, 4_000_000_001L, 4_000_000_001L * (4_000_000_000L / 2))));
	}
	
	@ParameterizedTest
	@MethodSource
	void binomStreamDouble(double n, int limit, DoubleStream result) {
		assertArrayEquals(result.toArray(), BinomialCoefficient.binomStream(n).limit(limit).toArray(), 1e-9);
	}
	
	static Stream<Arguments> binomStreamDouble() {
		return Stream.of(
				Arguments.of(0, 10, DoubleStream.of(1)),
				Arguments.of(1, 10, DoubleStream.of(1, 1)),
				Arguments.of(3, 10, DoubleStream.of(1, 3, 3, 1)),
				Arguments.of(-4, 10, DoubleStream.of(1, -4, 10, -20, 35, -56, 84, -120, 165, -220)),
				Arguments.of(2.5, 6, DoubleStream.of(1, 2.5, 1.875, 0.3125, -0.0390625, 0.01171875)),
				Arguments.of(
						-82.0 / 3,
						4,
						DoubleStream.of(
								1,
								-82.0 / 3,
								-82.0 / 3 * -85.0 / 3 / 2,
								-82.0 / 3 * -85.0 / 3 * -88.0 / 3 / 2 / 3)));
	}
	
	@ParameterizedTest
	@MethodSource
	void binomStreamBigInteger(BigInteger n, int limit, Stream<BigInteger> result) {
		assertArrayEquals(result.toArray(), BinomialCoefficient.binomStream(n).limit(limit).toArray());
	}
	
	static Stream<Arguments> binomStreamBigInteger() {
		return Stream.of(
				Arguments.of(BigInteger.valueOf(0), 10, LongStream.of(1).mapToObj(BigInteger::valueOf)),
				Arguments.of(BigInteger.valueOf(1), 10, LongStream.of(1, 1).mapToObj(BigInteger::valueOf)),
				Arguments.of(BigInteger.valueOf(3), 10, LongStream.of(1, 3, 3, 1).mapToObj(BigInteger::valueOf)),
				Arguments.of(
						BigInteger.valueOf(-4),
						10,
						LongStream.of(1, -4, 10, -20, 35, -56, 84, -120, 165, -220).mapToObj(BigInteger::valueOf)),
				Arguments.of(
						BigInteger.valueOf(60),
						10,
						LongStream.of(
								1L,
								60L,
								1_770L,
								34_220L,
								487_635L,
								5_461_512L,
								50_063_860L,
								386_206_920L,
								2_558_620_845L,
								14_783_142_660L).mapToObj(BigInteger::valueOf)));
	}
	
	@ParameterizedTest
	@MethodSource
	<T extends Ring<T>> void binomStreamRing(T n, T one, int limit, Stream<T> result) {
		assertArrayEquals(result.toArray(), BinomialCoefficient.binomStream(n, one).limit(limit).toArray());
	}
	
	static Stream<Arguments> binomStreamRing() {
		return Stream.of(
				Arguments.of(
						DoublePolynomial.valueOf("3x³+1.5"),
						DoublePolynomial.ONE,
						4,
						Stream.of(
								DoublePolynomial.ONE,
								DoublePolynomial.valueOf("3x³+1.5"),
								DoublePolynomial.valueOf("4.5x^6+3x³+0.375"),
								DoublePolynomial.valueOf("4.5 x^9 + 2.25 x^6 - 0.125 x^3 - 0.0625"))));
	}
	
	@Test
	void testPascalsTriangle() {
		assertEquals(
				Stream.of(
						IntStream.of(1),
						IntStream.of(1, 1),
						IntStream.of(1, 2, 1),
						IntStream.of(1, 3, 3, 1),
						IntStream.of(1, 4, 6, 4, 1))
						.map(s -> s.mapToObj(BigInteger::valueOf))
						.map(Stream::toList)
						.toList(),
				BinomialCoefficient.getPascalsTriangle(4).stream().toList());
	}
	
	@Test
	void testPascalsTriangleModulusInt() {
		List<int[]> PascalsTriangle = BinomialCoefficient.getPascalsTriangle(5, 3);
		assertArrayEquals(new int[] { 1 }, PascalsTriangle.get(0));
		assertArrayEquals(new int[] { 1, 1 }, PascalsTriangle.get(1));
		assertArrayEquals(new int[] { 1, 2, 1 }, PascalsTriangle.get(2));
		assertArrayEquals(new int[] { 1, 0, 0, 1 }, PascalsTriangle.get(3));
		assertArrayEquals(new int[] { 1, 1, 0, 1, 1 }, PascalsTriangle.get(4));
		assertArrayEquals(new int[] { 1, 2, 1, 1, 2, 1 }, PascalsTriangle.get(5));
	}
	
	@Test
	void testPascalsTriangleModulusIntOverflow() {
		List<int[]> PascalsTriangle = BinomialCoefficient.getPascalsTriangle(60, 2_000_000_000);
		int[] result = LongStream
				.of(
						1L,
						60L,
						1_770L,
						34_220L,
						487_635L,
						5_461_512L,
						50_063_860L,
						386_206_920L,
						2_558_620_845L,
						14_783_142_660L,
						75_394_027_566L,
						342_700_125_300L,
						1_399_358_844_975L,
						5_166_863_427_600L,
						17_345_898_649_800L,
						53_194_089_192_720L)
				.mapToInt(n -> (int) (n % 2_000_000_000))
				.toArray();
		assertArrayEquals(result, Arrays.copyOf(PascalsTriangle.get(60), result.length));
	}
	
	@Test
	void testPascalsTriangleModulusBigInteger() {
		assertEquals(
				Stream.of(
						IntStream.of(1),
						IntStream.of(1, 1),
						IntStream.of(1, 2, 1),
						IntStream.of(1, 0, 0, 1),
						IntStream.of(1, 1, 0, 1, 1),
						IntStream.of(1, 2, 1, 1, 2, 1))
						.map(s -> s.mapToObj(BigInteger::valueOf))
						.map(Stream::toList)
						.toList(),
				BinomialCoefficient.getPascalsTriangle(5, BigInteger.valueOf(3)));
	}
}
