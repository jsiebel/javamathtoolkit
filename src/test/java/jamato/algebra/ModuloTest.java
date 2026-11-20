package jamato.algebra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ModuloTest{
	
	@ParameterizedTest
	@MethodSource
	void testMultiplyInt(int factor1, int factor2, int modulus){
		int expectedResult = BigInteger.valueOf(factor1)
				.multiply(BigInteger.valueOf(factor2))
				.mod(BigInteger.valueOf(modulus))
				.intValue();
		int actual = Modulo.multiply(factor1, factor2, modulus);
		assertEquals(expectedResult, actual);
	}
	
	private static Stream<Arguments> testMultiplyInt(){
		return Stream.of(
				arguments(5, 6, 23),
				arguments(0, Integer.MAX_VALUE, 23),
				arguments(Integer.MAX_VALUE, 0, 23),
				arguments(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE));
	}
	
	@ParameterizedTest
	@MethodSource
	void testMultiplyLong(long factor1, long factor2, long modulus){
		long expectedResult = BigInteger.valueOf(factor1)
				.multiply(BigInteger.valueOf(factor2))
				.mod(BigInteger.valueOf(modulus))
				.longValue();
		long actual = Modulo.multiply(factor1, factor2, modulus);
		assertEquals(expectedResult, actual);
	}
	
	private static Stream<Arguments> testMultiplyLong(){
		return Stream.of(
				arguments(5, 6, 23),
				arguments(0, Long.MAX_VALUE, 23),
				arguments(Long.MAX_VALUE, 0, 23),
				arguments(Long.MIN_VALUE, Long.MIN_VALUE, Long.MAX_VALUE),
				arguments(1_000_000_000_000_000_000L, 1_000_000_000_000_000_000L, 99),
				arguments(-1_000_000_000_000_000_000L, 1_000_000_000_000_000_000L, 99),
				arguments(1_000_000_000_000_000_000L, -1_000_000_000_000_000_000L, 99),
				arguments(-1_000_000_000_000_000_000L, -1_000_000_000_000_000_000L, 99),
				arguments(512313212312L, 121121111112L, 12312312323L));
	}
	
	@ParameterizedTest
	@MethodSource
	void testInvertInt(int value, int modulus, int expectedIverse){
		int actualInverse = Modulo.invert(value, modulus);
		assertEquals(expectedIverse, actualInverse);
	}
	
	private static Stream<Arguments> testInvertInt(){
		return Stream.of(
				arguments(3, 10, 7),
				arguments(-17, 10, 7),
				arguments(9972, 9973, 9972),
				arguments(9971, 9973, 9972 / 2),
				arguments(2147483646, 2147483647, 2147483646),
				arguments(2147483645, 2147483647, 2147483646 / 2),
				arguments(1, Integer.MAX_VALUE, 1),
				arguments(2, Integer.MAX_VALUE, Integer.MAX_VALUE / 2 + 1));
	}
	
	@ParameterizedTest
	@MethodSource
	void testInvertIntException(int value, int modulus){
		assertThrows(ArithmeticException.class, () -> Modulo.invert(value, modulus));
	}
	
	private static Stream<Arguments> testInvertIntException(){
		return Stream.of(
				arguments(3, -10),
				arguments(0, 10),
				arguments(-18, 10));
	}
	
	@ParameterizedTest
	@MethodSource
	void testInvertLong(long value, long modulus, long expectedIverse){
		long actualInverse = Modulo.invert(value, modulus);
		assertEquals(expectedIverse, actualInverse);
	}
	
	private static Stream<Arguments> testInvertLong(){
		return Stream.of(
				arguments(3, 10, 7),
				arguments(-17, 10, 7),
				arguments(9972, 9973, 9972),
				arguments(9971, 9973, 9972 / 2),
				arguments(9223372036854775782L, 9223372036854775783L, 9223372036854775782L),
				arguments(9223372036854775781L, 9223372036854775783L, 9223372036854775782L / 2),
				arguments(1, Long.MAX_VALUE, 1),
				arguments(2, Long.MAX_VALUE, Long.MAX_VALUE / 2 + 1));
	}
	
	@ParameterizedTest
	@MethodSource
	void testInvertLongException(long value, long modulus){
		assertThrows(ArithmeticException.class, () -> Modulo.invert(value, modulus));
	}
	
	private static Stream<Arguments> testInvertLongException(){
		return Stream.of(
				arguments(3L, -10L),
				arguments(0, -10L),
				arguments(-18L, 10L));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPowInt(int base, int exponent, int modulus, int expectedResult){
		assertEquals(expectedResult, Modulo.pow(base, exponent, modulus));
	}
	
	static Stream<Arguments> testPowInt(){
		return Stream.of(
				arguments(0, 0, 37, 1),
				arguments(-7, 3, 1000, 657),
				arguments(-7, -3, 1000, 793),
				arguments(2, Integer.MIN_VALUE, 15, 1),
				arguments(1_000_000, Integer.MIN_VALUE, Integer.MAX_VALUE, 237191231),
				arguments(1000, 371, 9, 1),
				arguments(46341, 2, 1000000, 488281));
	}
	
	@ParameterizedTest
	@MethodSource
	void testPowLong(long base, long exponent, long modulus, long expectedResult){
		assertEquals(expectedResult, Modulo.pow(base, exponent, modulus));
	}
	
	static Stream<Arguments> testPowLong(){
		return Stream.of(
				arguments(0, 0, 37, 1),
				arguments(-7, 3, 1000, 657),
				arguments(99009901, Long.MIN_VALUE, 37L, 10),
				arguments(99009901, Long.MIN_VALUE, 10_000_000_000L, 1718860801),
				arguments(1000, 371, 9, 1),
				arguments(Long.MAX_VALUE / 4, 2, Integer.MAX_VALUE, 536870912));
	}
}
