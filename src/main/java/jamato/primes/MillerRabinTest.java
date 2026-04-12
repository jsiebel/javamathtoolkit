package jamato.primes;

import jamato.algebra.Exponentiation;

/**
 * This utility class provides methods for performing the Miller-Rabin primality test.
 *
 * @author JSiebel
 *
 */
public final class MillerRabinTest{
	
	private MillerRabinTest(){
		// Utility class
	}
	
	/**
	 * Checks if an odd number is a prime according to the Miller-Rabin test with the given base. The Miller-Rabin test
	 * correctly identifies any odd prime number, but is wrong on some composite numbers (pseudoprimes). Multiple tests
	 * on different bases can be combined to reduce the number of false positives.
	 *
	 * @param n the number to be checked, must be an odd number and greater than 2
	 * @param base the base used for the check
	 * @return <code>true</code> if the number is a prime or a pseudoprime, <code>false</code> otherwise
	 * @see "https://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test"
	 */
	public static boolean test(int n, int base){
		if (base % n == 0){
			// A base cannot be used to check the primality of a number that it is a multiple of (or to check itself).
			return true;
		}
		int s = Integer.numberOfTrailingZeros(n - 1);
		int d = (n - 1) >>> s;
		int p = Exponentiation.powMod(base, d, n);
		if (p == 1 || p == n - 1){
			// base ^ d ≡ 1 or base ^ d ≡ -1 (mod n)
			return true;
		}else{
			for (int r = 1; r < s; r++){
				p = (int) ((long) p * p % n);
				if (p == n - 1){
					// base ^ (2^r * d) ≡ -1 (mod n)
					return true;
				}
			}
			// base ^ (2^s * d) = base ^ (n-1) ≢ 1
			return false;
		}
	}
}
