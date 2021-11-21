package jamato.algebra;

import java.math.BigInteger;

/**
 * This class provides methods that calculate the greatest common divisor.
 * 
 * @author JSiebel
 *
 */
public class GCD {
	
	protected GCD() {
	}
	
	/**
	 * Returns the greatest common divisor of two values. The result is always
	 * positive or zero (if both arguments are zero).
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>{@code gcd(-n, -m) == gcd(-n, m) == gcd(n, -m) == gcd(n, m)}</li>
	 * <li>{@code gcd(n, 0) == gcd(0, n) == |n|}</li>
	 * <li>{@code gcd(0, 0) == 0}</li>
	 * </ul>
	 * 
	 * @param n the first value
	 * @param m the second value
	 * @return the greatest common divisor
	 */
	public static int of(int n, int m) {
		while (m != 0) {
			int k = n % m;
			n = m;
			m = k;
		}
		return Math.abs(n);
	}
	
	/**
	 * Returns the greatest common divisor of two values. The result is always
	 * positive or zero (if both arguments are zero).
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>{@code gcd(-n, -m) == gcd(-n, m) == gcd(n, -m) == gcd(n, m)}</li>
	 * <li>{@code gcd(n, 0) == gcd(0, n) == |n|}</li>
	 * <li>{@code gcd(0, 0) == 0}</li>
	 * </ul>
	 * 
	 * @param n the first value
	 * @param m the second value
	 * @return the greatest common divisor
	 */
	public static long of(long n, long m) {
		while (m != 0) {
			long k = n % m;
			n = m;
			m = k;
		}
		return Math.abs(n);
	}
	
	/**
	 * Returns the greatest common divisor of two values. The result is always
	 * positive or zero (if both arguments are zero).
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>{@code gcd(-n, -m) == gcd(-n, m) == gcd(n, -m) == gcd(n, m)}</li>
	 * <li>{@code gcd(n, 0) == gcd(0, n) == |n|}</li>
	 * <li>{@code gcd(0, 0) == 0}</li>
	 * </ul>
	 * 
	 * @param n the first value
	 * @param m the second value
	 * @return the greatest common divisor
	 */
	public static BigInteger of(BigInteger n, BigInteger m) {
		return n.gcd(m);
	}
}
