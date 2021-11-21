package jamato.number;

import static jamato.number.BigRational.NAN;
import static jamato.number.BigRational.NEGATIVE_INFINITY;
import static jamato.number.BigRational.ONE;
import static jamato.number.BigRational.POSITIVE_INFINITY;
import static jamato.number.BigRational.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigRationalTest {
	
	@Test
	public void testDoubleConstructor() {
		assertEquals(BigRational.ZERO, new BigRational(0.0));
		assertEquals(BigRational.ONE, new BigRational(1.0));
		assertEquals(new BigRational(3, 2), new BigRational(1.5));
		assertEquals(new BigRational(1, 16), new BigRational(0.0625));
		assertEquals(new BigRational(1234), new BigRational(1234.0));
		assertEquals(new BigRational(987654321), new BigRational(987654321.0));
		
		assertEquals(new BigRational(43, 20), new BigRational(2.15));
		assertEquals(new BigRational(-43, 20), new BigRational(-2.15));
		assertEquals(Math.PI, new BigRational(Math.PI).doubleValue(), 1e-9);
		double phi = Math.sqrt(5) / 2 + 0.5;
		assertEquals(phi, new BigRational(phi).doubleValue(), 1e-9);
		assertEquals(1/phi, new BigRational(1/phi).doubleValue(), 1e-9);

		double greatestNonInteger = Math.pow(2, 52) - 0.5;
		assertEquals(
				new BigRational(BigInteger.TWO.pow(53).subtract(BigInteger.ONE), BigInteger.TWO),
				new BigRational(greatestNonInteger));
		double smallestNonInteger = -Math.pow(2, 52) + 0.5;
		assertEquals(
				new BigRational(BigInteger.TWO.pow(53).subtract(BigInteger.ONE).negate(), BigInteger.TWO),
				new BigRational(smallestNonInteger));
		
		assertEquals(new BigRational(1, 1_000_000_000), new BigRational(1e-9));
		
		assertEquals(new BigRational(4503599627370497l, 4503599627370496l), new BigRational(Math.nextUp(1d)));
		assertEquals(new BigRational(BigInteger.ONE, BigInteger.TWO.pow(1074)), new BigRational(Double.MIN_VALUE));
		assertEquals(new BigRational(BigInteger.ONE.negate(), BigInteger.TWO.pow(1074)), new BigRational(-Double.MIN_VALUE));
		assertEquals(new BigRational(BigInteger.ONE, BigInteger.TWO.pow(1022)), new BigRational(Double.MIN_NORMAL));
		assertEquals(new BigRational(BigInteger.ONE.negate(), BigInteger.TWO.pow(1022)), new BigRational(-Double.MIN_NORMAL));

		assertEquals(new BigRational(
				BigInteger.TWO.pow(150)),
				new BigRational(Math.pow(2, 150)));
		assertEquals(
				new BigRational(BigInteger.ONE, BigInteger.TWO.pow(160)), 
				new BigRational(Math.pow(0.5, 160)));
		
		assertEquals(
				new BigRational(BigInteger.ONE, BigInteger.TWO.pow(180).multiply(BigInteger.valueOf(3))), 
				new BigRational(Math.pow(0.5, 180) / 3));
		
		assertEquals(
				new BigRational(BigInteger.ONE, BigInteger.valueOf(3).multiply(BigInteger.TWO.pow(Long.SIZE-2))),
				new BigRational(4.0 / 3 / Math.pow(2, Long.SIZE)));

		assertEquals(NAN, new BigRational(Double.NaN));
		assertEquals(POSITIVE_INFINITY, new BigRational(Double.POSITIVE_INFINITY));
		assertEquals(NEGATIVE_INFINITY, new BigRational(Double.NEGATIVE_INFINITY));
	}
	
	@Test
	public void testNegate() {
		assertEquals(new BigRational(34, 93), new BigRational(-34, 93).negate());
		assertEquals(ZERO, ZERO.negate());
		assertEquals(NAN, NAN.negate());
		assertEquals(POSITIVE_INFINITY, NEGATIVE_INFINITY.negate());
		assertEquals(NEGATIVE_INFINITY, POSITIVE_INFINITY.negate());
	}
	
	@Test
	public void testIsZero() {
		assertFalse(new BigRational(9922, 112).isZero());
		assertTrue(ZERO.isZero());
		assertFalse(ONE.isZero());
		assertFalse(NAN.isZero());
		assertFalse(POSITIVE_INFINITY.isZero());
		assertFalse(NEGATIVE_INFINITY.isZero());
	}
	
	@Test
	public void testAdd() {
		assertEquals(new BigRational(3, 4), new BigRational(2, 4).add(new BigRational(2, 8)));
		assertEquals(new BigRational(0, 4), new BigRational(0, 1).add(new BigRational(0, 3400)));
		assertEquals(new BigRational(1, 2), new BigRational(13, 30).add(new BigRational(2, 30)));
		assertEquals(new BigRational(new BigInteger("12345678987654322"), new BigInteger("111111111")), new BigRational(BigInteger.ONE, new BigInteger("111111111")).add(111111111L));
		assertEquals(ZERO, new BigRational(1, Integer.MAX_VALUE).add(new BigRational(-1, Integer.MAX_VALUE)));
		assertEquals(ZERO, new BigRational(Integer.MAX_VALUE).add(new BigRational(-Integer.MAX_VALUE)));
		assertEquals(ZERO, ZERO.add(ZERO));
		assertEquals(NAN, ONE.add(NAN));
		assertEquals(NAN, NAN.add(ONE));
		assertEquals(NAN, POSITIVE_INFINITY.add(NEGATIVE_INFINITY));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.add(POSITIVE_INFINITY));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.add(NEGATIVE_INFINITY));

		assertEquals(new BigRational(-325, 18), new BigRational(17, 18).add(-19));
		assertEquals(NAN, NAN.add(17));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.add(217));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.add(9973));
	}
	
	@Test
	public void testSubtract() {
		assertEquals(new BigRational(1, 4), new BigRational(2, 4).subtract(new BigRational(2, 8)));
		assertEquals(new BigRational(0, 4), new BigRational(0, 1).subtract(new BigRational(0, 3400)));
		assertEquals(new BigRational(1, 2), new BigRational(27, 50).subtract(new BigRational(2, 50)));
		assertEquals(ZERO, new BigRational(1, Integer.MAX_VALUE).subtract(new BigRational(1, Integer.MAX_VALUE)));
		assertEquals(ZERO, new BigRational(Integer.MAX_VALUE).subtract(new BigRational(Integer.MAX_VALUE)));
		assertEquals(NAN, ONE.subtract(NAN));
		assertEquals(NAN, NAN.subtract(ONE));
		assertEquals(NAN, POSITIVE_INFINITY.subtract(POSITIVE_INFINITY));
		assertEquals(NAN, NEGATIVE_INFINITY.subtract(NEGATIVE_INFINITY));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.subtract(NEGATIVE_INFINITY));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.subtract(POSITIVE_INFINITY));
		
		assertEquals(new BigRational(-21, 11), new BigRational(100, 11).subtract(11));
		assertEquals(NAN, NAN.subtract(17));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.subtract(217));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.subtract(9973));
	}
	
	@Test
	public void testInvert() {
		assertEquals(new BigRational(26, 557), new BigRational(557, 26).invert());
		assertEquals(POSITIVE_INFINITY, ZERO.invert());
		assertEquals(NAN, NAN.invert());
		assertEquals(ZERO, POSITIVE_INFINITY.invert());
		assertEquals(ZERO, NEGATIVE_INFINITY.invert());
	}
	
	@Test
	public void testIsOne() {
		assertFalse(new BigRational(992, 1012).isOne());
		assertFalse(ZERO.isOne());
		assertTrue(ONE.isOne());
		assertFalse(NAN.isOne());
		assertFalse(POSITIVE_INFINITY.isOne());
		assertFalse(NEGATIVE_INFINITY.isOne());
	}
	
	@Test
	public void testMultiply() {
		assertEquals(new BigRational(1, 4), new BigRational(3, 8).multiply(new BigRational(4, 6)));
		assertEquals(ZERO, new BigRational(4, 8).multiply(0));
		assertEquals(new BigRational(50, 3), new BigRational(25, 15).multiply(10));
		assertEquals(ZERO, new BigRational(253, -7).multiply(0));
		
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
		assertEquals(new BigRational(5, 64), new BigRational(3, 8).divide(new BigRational(24, 5)));
		assertEquals(new BigRational(1, 6), new BigRational(4, 8).divide(3));
		
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
		assertEquals(new BigRational(25, 81), new BigRational(-5, 9).pow(2));
		assertEquals(new BigRational(27, 512), new BigRational(8, 3).pow(-3));
		assertEquals(ONE, new BigRational(831).pow(0));
		assertEquals(NAN, NAN.pow(72));
		assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY.pow(93));
		assertEquals(POSITIVE_INFINITY, NEGATIVE_INFINITY.pow(94));
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.pow(95));
		assertEquals(POSITIVE_INFINITY, ZERO.pow(-96));
		assertEquals(ZERO, ZERO.pow(97));
	}
	
	@Test
	public void testSignum() {
		assertEquals(1, new BigRational(-417, -211).signum());
		assertEquals(-1, new BigRational(new BigInteger("-123456789101112131415"), new BigInteger("1441223197")).signum());
		assertEquals(-1, NEGATIVE_INFINITY.signum());
		assertEquals(0, ZERO.signum());
		assertEquals(0, NAN.signum());
		assertEquals(1, POSITIVE_INFINITY.signum());
	}
	
	@Test
	public void testAbsolute() {
		assertEquals(new BigRational(319, 12), new BigRational(319, -12).absolute());
		assertEquals(new BigRational(320, 13), new BigRational(320, 13).absolute());
		assertEquals(POSITIVE_INFINITY, NEGATIVE_INFINITY.absolute());
		assertEquals(ZERO, ZERO.absolute());
		assertEquals(NAN, NAN.absolute());
		assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY.absolute());
	}
	
	@Test
	public void testRound() {
		assertEquals(BigInteger.valueOf(0), new BigRational(0).round());
		assertEquals(BigInteger.valueOf(1000), new BigRational(1000).round());
		assertEquals(BigInteger.valueOf(-1000), new BigRational(-1000).round());
		
		assertEquals(BigInteger.valueOf(-6), new BigRational(-6.75).ceil());
		assertEquals(BigInteger.valueOf(-6), new BigRational(-6.75).round(RoundingMode.DOWN));
		assertEquals(BigInteger.valueOf(-7), new BigRational(-6.75).floor());
		assertEquals(BigInteger.valueOf(-7), new BigRational(-6.75).round(RoundingMode.HALF_DOWN));
		assertEquals(BigInteger.valueOf(-7), new BigRational(-6.75).round(RoundingMode.HALF_EVEN));
		assertEquals(BigInteger.valueOf(-7), new BigRational(-6.75).round());
		assertEquals(BigInteger.valueOf(-7), new BigRational(-6.75).round(RoundingMode.UP));
		
		assertEquals(BigInteger.valueOf(-2), new BigRational(-2.5).ceil());
		assertEquals(BigInteger.valueOf(-2), new BigRational(-2.5).round(RoundingMode.DOWN));
		assertEquals(BigInteger.valueOf(-3), new BigRational(-2.5).floor());
		assertEquals(BigInteger.valueOf(-2), new BigRational(-2.5).round(RoundingMode.HALF_DOWN));
		assertEquals(BigInteger.valueOf(-2), new BigRational(-2.5).round(RoundingMode.HALF_EVEN));
		assertEquals(BigInteger.valueOf(-3), new BigRational(-2.5).round());
		assertEquals(BigInteger.valueOf(-3), new BigRational(-2.5).round(RoundingMode.UP));
		
		assertEquals(BigInteger.valueOf(3), new BigRational(2.5).ceil());
		assertEquals(BigInteger.valueOf(2), new BigRational(2.5).round(RoundingMode.DOWN));
		assertEquals(BigInteger.valueOf(2), new BigRational(2.5).floor());
		assertEquals(BigInteger.valueOf(2), new BigRational(2.5).round(RoundingMode.HALF_DOWN));
		assertEquals(BigInteger.valueOf(2), new BigRational(2.5).round(RoundingMode.HALF_EVEN));
		assertEquals(BigInteger.valueOf(3), new BigRational(2.5).round());
		assertEquals(BigInteger.valueOf(3), new BigRational(2.5).round(RoundingMode.UP));
		
		assertEquals(BigInteger.valueOf(-4), new BigRational(-3.5).round(RoundingMode.HALF_EVEN));
		assertEquals(BigInteger.ZERO, NAN.round(RoundingMode.HALF_EVEN));
	}
	
	@Test
	public void testDoubleValue() {
		assertEquals(Double.NaN, new BigRational(BigInteger.ZERO, BigInteger.ZERO).doubleValue(), 0);
		assertEquals(Double.POSITIVE_INFINITY, new BigRational(BigInteger.ONE, BigInteger.ZERO).doubleValue(), 0);
		assertEquals(Double.NEGATIVE_INFINITY, new BigRational(BigInteger.valueOf(-341233), BigInteger.ZERO).doubleValue(), 0);
		assertEquals(9, new BigRational(BigInteger.valueOf(9)).doubleValue(), 0);
		assertEquals(9000, new BigRational(BigInteger.valueOf(9000)).doubleValue(), 0);
		assertEquals(-7, new BigRational(BigInteger.valueOf(-7)).doubleValue(), 0);
		
		BigInteger nonillionPlusOne = BigInteger.TEN.pow(30).add(BigInteger.ONE);
		BigInteger octillion = BigInteger.TEN.pow(27);
		assertEquals(1000, new BigRational(nonillionPlusOne, octillion).doubleValue(), 0);
		assertEquals(0.001, new BigRational(1, 1000).doubleValue(), 0);
		assertEquals(0.001, new BigRational(octillion, nonillionPlusOne).doubleValue(), 0);
		
		BigInteger nonillionMinusOne = BigInteger.TEN.pow(30).subtract(BigInteger.ONE);
		assertEquals(1000, new BigRational(nonillionMinusOne, octillion).doubleValue(), 0);
		assertEquals(0.001, new BigRational(octillion, nonillionMinusOne).doubleValue(), 0);
		
		assertEquals(Math.pow(2, Double.MAX_EXPONENT), new BigRational(BigInteger.TWO.pow(Double.MAX_EXPONENT)).doubleValue(), 0);
		assertEquals(Double.POSITIVE_INFINITY, new BigRational(BigInteger.TWO.pow(Double.MAX_EXPONENT+1)).doubleValue(), 0);
		assertEquals(Double.NEGATIVE_INFINITY, new BigRational(BigInteger.TWO.pow(Double.MAX_EXPONENT+1), BigInteger.valueOf(-1)).doubleValue(), 0);
		assertEquals(0, new BigRational(BigInteger.TWO, BigInteger.valueOf(-3949113).pow(50)).doubleValue(), 0);
		assertEquals(Double.MIN_VALUE, new BigRational(BigInteger.ONE, BigInteger.TWO.pow(-Double.MIN_EXPONENT+52)).doubleValue(), 0);
		assertEquals(0, new BigRational(BigInteger.ONE, BigInteger.TWO.pow(-Double.MIN_EXPONENT+52+1)).doubleValue(), 0);
	}

	@Test
	public void testFloatValue() {
		assertEquals(41000.0/37, new BigRational(41000, 37).floatValue(), 1e-3);
		assertEquals(Float.NaN, NAN.floatValue(), 1e-3);
		assertEquals(Float.POSITIVE_INFINITY, POSITIVE_INFINITY.floatValue(), 1e-3);
		assertEquals(Float.NEGATIVE_INFINITY, NEGATIVE_INFINITY.floatValue(), 1e-3);
	}

	@Test
	public void testIntValue() {
		assertEquals(103/9, new BigRational(103, 9).intValue());
		assertEquals(0, new BigRational(-7, 101).intValue());
	}

	@Test
	public void testLongValue() {
		assertEquals(103L/9, new BigRational(103, 9).longValue());
		assertEquals(0L, new BigRational(-7, 101).longValue());
	}
	
	@Test
	public void testCompareTo() {
		assertEquals(1, new BigRational(-3, 99).compareTo(new BigRational(-4, 100)));
		assertEquals(-1,
				new BigRational(Integer.MAX_VALUE, Integer.MAX_VALUE-1)
				.compareTo(new BigRational(Integer.MAX_VALUE-1, Integer.MAX_VALUE-2)));
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
