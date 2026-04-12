package jamato.primes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Tests the {@link MillerRabinTest} class. */
class MillerRabinTestTest{
	
	@ParameterizedTest
	@MethodSource
	void test(int n, int base, boolean expectedResult){
		boolean actualResult = MillerRabinTest.test(n, base);
		assertEquals(expectedResult, actualResult);
	}
	
	static Stream<Arguments> test(){
		return Stream.of(
				arguments(3, 2, true),
				arguments(5, 2, true),
				arguments(7, 2, true),
				arguments(9, 2, false),
				arguments(11, 2, true),
				arguments(13, 2, true),
				arguments(15, 2, false),
				arguments(Integer.MAX_VALUE, 2, true),
				arguments(23 * 89, 2, true), // Smallest pseudoprime to base 2
				
				arguments(3, 3, true),
				arguments(5, 3, true),
				arguments(7, 3, true),
				arguments(9, 3, false),
				arguments(11, 3, true),
				arguments(13, 3, true),
				arguments(15, 3, false),
				arguments(11 * 11, 3, true), // Smallest pseudoprime to base 3
				arguments(Integer.MAX_VALUE, 3, true),
				
				arguments(3, Primes.GREATEST_INT_PRIME, true),
				arguments(5, Primes.GREATEST_INT_PRIME, true),
				arguments(7, Primes.GREATEST_INT_PRIME, true),
				arguments(9, Primes.GREATEST_INT_PRIME, true), // (base-1) is a multiple of 9, so 9 is a pseudoprime
				arguments(11, Primes.GREATEST_INT_PRIME, true),
				arguments(13, Primes.GREATEST_INT_PRIME, true),
				arguments(15, Primes.GREATEST_INT_PRIME, false),
				arguments(Integer.MAX_VALUE, Primes.GREATEST_INT_PRIME, true));
	}
}
