package jamato.algebra;

import static jamato.algebra.Exponentation.pow;
import static jamato.algebra.Exponentation.powMod;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import jamato.number.IntRational;

public class ExponentationTest {

	@Test
	public void testPow() {
		assertEquals(32, pow(2, 5));
		assertEquals(49, pow(-7, 2));
		assertEquals(-161051, pow(-11, 5));
		assertEquals(Integer.MAX_VALUE, pow(Integer.MAX_VALUE, 1));
		assertEquals(1, pow(Integer.MIN_VALUE, 0));
		assertEquals(1, pow(0, 0));
		assertEquals(-1, pow(-1, Integer.MAX_VALUE));
		
		assertEquals(1_000_000_000_000L, pow(100L, 6));
		
		assertEquals(IntRational.ONE, pow(IntRational.ONE, Integer.MIN_VALUE, IntRational.ONE));
	}
	@Test
	public void testPowMod() {
		assertEquals(1, powMod(0, 0, 37));
		assertEquals(-343, powMod(-7, 3, 1000));
		assertEquals(1, powMod(1000, 371, 9));
	}
}
