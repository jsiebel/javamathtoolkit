package jamato.number;

import static jamato.number.IntRational.NAN;
import static jamato.number.IntRational.NEGATIVE_INFINITY;
import static jamato.number.IntRational.ONE;
import static jamato.number.IntRational.POSITIVE_INFINITY;
import static jamato.number.IntRational.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

public class IntRationalTest {
	
	@Test
	public void testDoubleConstructor() {
		assertEquals(new IntRational(0, 1), new IntRational(0.0));
		assertEquals(new IntRational(1, 1), new IntRational(1.0));
		assertEquals(new IntRational(1234, 1), new IntRational(1234.0));
		
		assertEquals(new IntRational(43, 20), new IntRational(2.15));
		assertEquals(new IntRational(-43, 20), new IntRational(-2.15));
		assertEquals(Math.PI, new IntRational(Math.PI).doubleValue(), 1e-9);
		double phi = Math.sqrt(5) / 2 + 0.5;
		assertEquals(phi, new IntRational(phi).doubleValue(), 1e-9);
		assertEquals(1/phi, new IntRational(1/phi).doubleValue(), 1e-9);
		

		assertEquals(new IntRational(1, 1_000_000_000), new IntRational(1e-9));
		assertEquals(new IntRational(0, 1), new IntRational(1e-10));
		
		assertEquals(new IntRational(Integer.MAX_VALUE, 1), new IntRational((double) Integer.MAX_VALUE));
		assertEquals(new IntRational(Integer.MAX_VALUE, 2), new IntRational(0.5*Integer.MAX_VALUE));
		assertEquals(new IntRational(Integer.MAX_VALUE/2+1, 1), new IntRational(0.5*Integer.MAX_VALUE + 0.5));
		assertEquals(new IntRational(Integer.MAX_VALUE/2+1, 1), new IntRational(0.5*Integer.MAX_VALUE + 0.6));
		assertEquals(new IntRational(Integer.MAX_VALUE, 1), new IntRational(1e10));

		assertEquals(new IntRational(Integer.MIN_VALUE, 1), new IntRational((double) Integer.MIN_VALUE));
		assertEquals(new IntRational(Integer.MIN_VALUE/2, 1), new IntRational(0.5 * Integer.MIN_VALUE));
		assertEquals(new IntRational(Integer.MIN_VALUE+1, 2), new IntRational(0.5 * Integer.MIN_VALUE + 0.5));
		assertEquals(new IntRational(Integer.MIN_VALUE+1, 2), new IntRational(0.5 * Integer.MIN_VALUE + 0.6));
		assertEquals(new IntRational(Integer.MIN_VALUE, 1), new IntRational((double) Integer.MIN_VALUE));

		assertEquals(NAN, new IntRational(Double.NaN));
		assertEquals(POSITIVE_INFINITY, new IntRational(Double.POSITIVE_INFINITY));
		assertEquals(NEGATIVE_INFINITY, new IntRational(Double.NEGATIVE_INFINITY));
	}
	
	@Test
	public void testNegate() {
		assertEquals(new IntRational(34, 93), new IntRational(-34, 93).negate());
		assertEquals(ZERO, ZERO.negate());
		assertEquals(NAN, NAN.negate());
		assertEquals(POSITIVE_INFINITY, new IntRational(Integer.MIN_VALUE).negate());
		assertEquals(POSITIVE_INFINITY, NEGATIVE_INFINITY.negate());
		assertEquals(NEGATIVE_INFINITY, POSITIVE_INFINITY.negate());
	}
	
	@Test
	public void testIsZero() {
		assertFalse(new IntRational(9922, 112).isZero());
		assertTrue(ZERO.isZero());
		assertFalse(ONE.isZero());
		assertFalse(NAN.isZero());
		assertFalse(POSITIVE_INFINITY.isZero());
		assertFalse(NEGATIVE_INFINITY.isZero());
	}
	
	@Test
	public void testAdd() {
		assertEquals(new IntRational(3, 4), new IntRational(2, 4).add(new IntRational(2, 8)));
		assertEquals(new IntRational(0, 4), new IntRational(0, 1).add(new IntRational(0, 3400)));
		assertEquals(new IntRational(1, 2), new IntRational(13, 30).add(new IntRational(2, 30)));
		assertEquals(ZERO, new IntRational(1, Integer.MAX_VALUE).add(new IntRational(-1, Integer.MAX_VALUE)));
		assertEquals(ZERO, new IntRational(Integer.MAX_VALUE).add(new IntRational(-Integer.MAX_VALUE)));
		assertEquals(ZERO, ZERO.add(ZERO));
		assertEquals(NAN, ONE.add(NAN));
		assertEquals(NAN, NAN.add(ONE));
		assertEquals(NAN, POSITIVE_INFINITY.add(NEGATIVE_INFINITY));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.add(POSITIVE_INFINITY));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.add(NEGATIVE_INFINITY));
		assertEquals(POSITIVE_INFINITY, new IntRational(1_000_000_000).add(new IntRational(2_000_000_000)));
		assertEquals(new IntRational(2, 1_000_000_000).doubleValue(), new IntRational(1, 1_000_000_000).add(new IntRational(1, 1_000_000_001)).doubleValue(), 1e-9);

		assertEquals(new IntRational(-325, 18), new IntRational(17, 18).add(-19));
		assertEquals(NAN, NAN.add(17));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.add(217));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.add(9973));
	}
	
	@Test
	public void testSubtract() {
		assertEquals(new IntRational(1, 4), new IntRational(2, 4).subtract(new IntRational(2, 8)));
		assertEquals(new IntRational(0, 4), new IntRational(0, 1).subtract(new IntRational(0, 3400)));
		assertEquals(new IntRational(1, 2), new IntRational(27, 50).subtract(new IntRational(2, 50)));
		assertEquals(ZERO, new IntRational(1, Integer.MAX_VALUE).subtract(new IntRational(1, Integer.MAX_VALUE)));
		assertEquals(ZERO, new IntRational(Integer.MAX_VALUE).subtract(new IntRational(Integer.MAX_VALUE)));
		assertEquals(NAN, ONE.subtract(NAN));
		assertEquals(NAN, NAN.subtract(ONE));
		assertEquals(NAN, POSITIVE_INFINITY.subtract(POSITIVE_INFINITY));
		assertEquals(NAN, NEGATIVE_INFINITY.subtract(NEGATIVE_INFINITY));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.subtract(NEGATIVE_INFINITY));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.subtract(POSITIVE_INFINITY));
		assertEquals(NEGATIVE_INFINITY, new IntRational(-1_000_000_000).subtract(new IntRational(2_000_000_000)));
		assertEquals(new IntRational(-2, 2_000_000_000).doubleValue(), new IntRational(-1, 2_000_000_000).subtract(new IntRational(1, 2_000_000_001)).doubleValue(), 1e-9);

		assertEquals(new IntRational(-21, 11), new IntRational(100, 11).subtract(11));
		assertEquals(NAN, NAN.subtract(17));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.subtract(217));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.subtract(9973));
	}
	
	@Test
	public void testInvert() {
		assertEquals(new IntRational(26, 557), new IntRational(557, 26).invert());
		assertEquals(POSITIVE_INFINITY, ZERO.invert());
		assertEquals(NAN, NAN.invert());
		assertEquals(ZERO, POSITIVE_INFINITY.invert());
		assertEquals(ZERO, NEGATIVE_INFINITY.invert());
	}
	
	@Test
	public void testIsOne() {
		assertFalse(new IntRational(992, 1012).isOne());
		assertFalse(ZERO.isOne());
		assertTrue(ONE.isOne());
		assertFalse(NAN.isOne());
		assertFalse(POSITIVE_INFINITY.isOne());
		assertFalse(NEGATIVE_INFINITY.isOne());
	}
	
	@Test
	public void testMultiply() {
		assertEquals(new IntRational(1, 4), new IntRational(3, 8).multiply(new IntRational(4, 6)));
		assertEquals(ZERO, new IntRational(4, 8).multiply(0));
		assertEquals(new IntRational(50, 3), new IntRational(25, 15).multiply(10));
		assertEquals(ZERO, new IntRational(253, -7).multiply(0));
		
		assertEquals(NAN, NAN.multiply(5));
		assertEquals(NAN, NAN.multiply(ONE));
		assertEquals(NAN, POSITIVE_INFINITY.multiply(NAN));
		
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.multiply(ONE));
		assertEquals(POSITIVE_INFINITY, NEGATIVE_INFINITY.multiply(-35));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.multiply(POSITIVE_INFINITY));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.multiply(ONE));
		assertEquals(NEGATIVE_INFINITY, POSITIVE_INFINITY.multiply(-31));
		assertEquals(POSITIVE_INFINITY, NEGATIVE_INFINITY.multiply(NEGATIVE_INFINITY));
	}
	
	@Test
	public void testDivide() {
		assertEquals(new IntRational(5, 64), new IntRational(3, 8).divide(new IntRational(24, 5)));
		assertEquals(new IntRational(1, 6), new IntRational(4, 8).divide(3));
		
		assertEquals(NAN, NAN.divide(5));
		assertEquals(NAN, NAN.divide(ONE));
		assertEquals(NAN, POSITIVE_INFINITY.divide(NAN));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.divide(ONE));
		assertEquals(POSITIVE_INFINITY, NEGATIVE_INFINITY.divide(-5));
		assertEquals(NAN, POSITIVE_INFINITY.divide(POSITIVE_INFINITY));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.divide(ONE));
		assertEquals(NEGATIVE_INFINITY, POSITIVE_INFINITY.divide(-11456));
		assertEquals(NAN, NEGATIVE_INFINITY.divide(NEGATIVE_INFINITY));
		assertEquals(ZERO, ONE.divide(NEGATIVE_INFINITY));
	}
	
	@Test
	public void testPow() {
		assertEquals(new IntRational(25, 81), new IntRational(-5, 9).pow(2));
		assertEquals(new IntRational(27, 512), new IntRational(8, 3).pow(-3));
		assertEquals(ONE, new IntRational(831).pow(0));
		assertEquals(NAN, NAN.pow(72));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.pow(93));
		assertEquals(POSITIVE_INFINITY, NEGATIVE_INFINITY.pow(94));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.pow(95));
		assertEquals(POSITIVE_INFINITY, ZERO.pow(-96));
		assertEquals(ZERO, ZERO.pow(97));
	}
	
	@Test
	public void testSignum() {
		assertEquals(1, new IntRational(-417, -211).signum());
		assertEquals(-1, new IntRational(7190, -44582).signum());
		assertEquals(-1, NEGATIVE_INFINITY.signum());
		assertEquals(0, ZERO.signum());
		assertEquals(0, NAN.signum());
		assertEquals(1, POSITIVE_INFINITY.signum());
	}
	
	@Test
	public void testAbsolute() {
		assertEquals(new IntRational(319, 12), new IntRational(319, -12).absolute());
		assertEquals(new IntRational(320, 13), new IntRational(320, 13).absolute());
		assertEquals(POSITIVE_INFINITY, NEGATIVE_INFINITY.absolute());
		assertEquals(ZERO, ZERO.absolute());
		assertEquals(NAN, NAN.absolute());
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.absolute());
	}
	
	@Test
	public void testRound() {
		assertEquals(0, new IntRational(0).round());
		assertEquals(1000, new IntRational(1000).round());
		assertEquals(-1000, new IntRational(-1000).round());
		
		assertEquals(-6, new IntRational(-6.75).ceil());
		assertEquals(-6, new IntRational(-6.75).round(RoundingMode.DOWN));
		assertEquals(-7, new IntRational(-6.75).floor());
		assertEquals(-7, new IntRational(-6.75).round(RoundingMode.HALF_DOWN));
		assertEquals(-7, new IntRational(-6.75).round(RoundingMode.HALF_EVEN));
		assertEquals(-7, new IntRational(-6.75).round());
		assertEquals(-7, new IntRational(-6.75).round(RoundingMode.UP));
		
		assertEquals(-2, new IntRational(-2.5).ceil());
		assertEquals(-2, new IntRational(-2.5).round(RoundingMode.DOWN));
		assertEquals(-3, new IntRational(-2.5).floor());
		assertEquals(-2, new IntRational(-2.5).round(RoundingMode.HALF_DOWN));
		assertEquals(-2, new IntRational(-2.5).round(RoundingMode.HALF_EVEN));
		assertEquals(-3, new IntRational(-2.5).round());
		assertEquals(-3, new IntRational(-2.5).round(RoundingMode.UP));
		
		assertEquals(3, new IntRational(2.5).ceil());
		assertEquals(2, new IntRational(2.5).round(RoundingMode.DOWN));
		assertEquals(2, new IntRational(2.5).floor());
		assertEquals(2, new IntRational(2.5).round(RoundingMode.HALF_DOWN));
		assertEquals(2, new IntRational(2.5).round(RoundingMode.HALF_EVEN));
		assertEquals(3, new IntRational(2.5).round());
		assertEquals(3, new IntRational(2.5).round(RoundingMode.UP));
		
		assertEquals(-4, new IntRational(-3.5).round(RoundingMode.HALF_EVEN));
		assertEquals(0, NAN.round(RoundingMode.HALF_EVEN));
	}
	
	@Test
	public void testDoubleValue() {
		assertEquals(430.0/37000, new IntRational(430, 37000).doubleValue(), 1e-9);
		assertEquals(Double.NaN, NAN.doubleValue(), 1e-9);
		assertEquals(Double.POSITIVE_INFINITY, POSITIVE_INFINITY.doubleValue(), 1e-9);
		assertEquals(Double.NEGATIVE_INFINITY, NEGATIVE_INFINITY.doubleValue(), 1e-9);
	}

	@Test
	public void testFloatValue() {
		assertEquals(45000.0/37, new IntRational(45000, 37).floatValue(), 1e-3);
		assertEquals(Float.NaN, NAN.floatValue(), 1e-3);
		assertEquals(Float.POSITIVE_INFINITY, POSITIVE_INFINITY.floatValue(), 1e-3);
		assertEquals(Float.NEGATIVE_INFINITY, NEGATIVE_INFINITY.floatValue(), 1e-3);
	}

	@Test
	public void testIntValue() {
		assertEquals(103/9, new IntRational(103, 9).intValue());
		assertEquals(0, new IntRational(-7, 101).intValue());
	}

	@Test
	public void testLongValue() {
		assertEquals(101L/7, new IntRational(101, 7).longValue());
		assertEquals(0L, new IntRational(-7, 101).longValue());
	}
	
	@Test
	public void testCompareTo() {
		assertEquals(1, new IntRational(-3, 99).compareTo(new IntRational(-4, 100)));
		assertEquals(-1,
				new IntRational(Integer.MAX_VALUE, Integer.MAX_VALUE-1)
				.compareTo(new IntRational(Integer.MAX_VALUE-1, Integer.MAX_VALUE-2)));
		assertEquals(0, ZERO.compareTo(ZERO));
		assertEquals(-1, ZERO.compareTo(ONE));
		assertEquals(1, ONE.compareTo(ZERO));
		assertEquals(-1, ONE.compareTo(POSITIVE_INFINITY));
		assertEquals(1, ONE.compareTo(NEGATIVE_INFINITY));
		assertEquals(-1, ONE.compareTo(NAN));
		assertEquals(1, NAN.compareTo(NEGATIVE_INFINITY));
		assertEquals(-1, POSITIVE_INFINITY.compareTo(NAN));
		assertEquals(1, POSITIVE_INFINITY.compareTo(ONE));
	}
}
