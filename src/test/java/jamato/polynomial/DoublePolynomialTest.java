package jamato.polynomial;

import static jamato.polynomial.DoublePolynomial.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DoublePolynomialTest {

	@Test
	public void testIntConstructor() {
		DoublePolynomial polynomial1 = new DoublePolynomial(0);
		assertEquals(0, polynomial1.getDegree());
		assertEquals(0, polynomial1.getCoefficient(0), 0);
		assertEquals(0, polynomial1.getCoefficient(1), 0);
		
		DoublePolynomial polynomial2 = new DoublePolynomial(1, 2, 4);
		assertEquals(2, polynomial2.getDegree());
		assertEquals(1, polynomial2.getCoefficient(0), 0);
		assertEquals(2, polynomial2.getCoefficient(1), 0);
		assertEquals(4, polynomial2.getCoefficient(2), 0);
		assertEquals(0, polynomial2.getCoefficient(3), 0);

		DoublePolynomial polynomial3 = new DoublePolynomial(1, 2, 4, 0, 0);
		assertEquals(2, polynomial3.getDegree());
		assertEquals(2, polynomial3.getDegree());

		DoublePolynomial polynomial4 = new DoublePolynomial(1, 0);
		assertEquals(0, polynomial4.getDegree());
		assertEquals(0, polynomial4.getDegree());

		DoublePolynomial polynomial5 = new DoublePolynomial(0, 0);
		assertEquals(0, polynomial5.getDegree());
		assertEquals(polynomial1, polynomial5);
	}
	
	@Test
	public void testdoubleConstructor() {
		DoublePolynomial polynomial1 = new DoublePolynomial(0);
		assertEquals(0, polynomial1.getDegree());
		
		DoublePolynomial polynomial2 = new DoublePolynomial(0, 0.5);
		assertEquals(1, polynomial2.getDegree());
		
		DoublePolynomial polynomial3 = new DoublePolynomial(0, 0);
		assertEquals(0, polynomial3.getDegree());
		
		DoublePolynomial polynomial4 = new DoublePolynomial(0, 4, 0.5, 0.5);
		assertEquals(3, polynomial4.getDegree());
	}
	
	@Test
	public void testStringConstructor() {
		assertEquals(new DoublePolynomial(-0.123), valueOf("-0.123"));
		assertEquals(new DoublePolynomial(1, -2), valueOf("-2x+1"));
		assertEquals(new DoublePolynomial(-3, 0, 5), valueOf("5x^2-3"));
		assertEquals(new DoublePolynomial(0.5, 0, 4, 0.5), valueOf("0.5A³+0.5+4x²"));
		assertEquals(new DoublePolynomial(0), valueOf("-0.25 t² + 0.75 t^2 + 0 - 0.5 t² + 0t^15"));
	}
	
	@Test
	public void testIsZero() {
		assertTrue(DoublePolynomial.ZERO.isZero());
		assertFalse(new DoublePolynomial(-4).isZero());
		assertFalse(new DoublePolynomial(0, 117).isZero());
	}
	
	@Test
	public void testNegate() {
		assertEquals(DoublePolynomial.ZERO, DoublePolynomial.ZERO.negate());
		assertEquals(new DoublePolynomial(3, -11, 0, 2.25), new DoublePolynomial(-3, 11, 0, -2.25).negate());
	}
	
	@Test
	public void testAdd() {
		assertEquals(
				valueOf("6"),
				valueOf("2x+4").add(valueOf("-2x+2"))
				);
		assertEquals(
				new DoublePolynomial(1),
				new DoublePolynomial(0.5).add(new DoublePolynomial(0.5))
				);
		assertEquals(
				new DoublePolynomial(0),
				valueOf("4x²+3x+2").add(valueOf("-4x²-3x-2"))
				);
		assertEquals(
				valueOf("3x²+7x+5"),
				valueOf("3x²+2x+1").add(valueOf("5x+4"))
				);
		assertEquals(
				valueOf("5x+5"),
				new DoublePolynomial(1).add(valueOf("5x+4"))
				);
	}
	
	@Test
	public void testSubtract() {
		assertEquals(
				valueOf("4x+2"),
				valueOf("2x+4").subtract(valueOf("-2x+2"))
				);
		assertEquals(
				new DoublePolynomial(0),
				new DoublePolynomial(0.5).subtract(new DoublePolynomial(0.5))
				);
		assertEquals(
				new DoublePolynomial(0),
				valueOf("4x²+3x+2").subtract(valueOf("4x²+3x+2"))
				);
		assertEquals(
				valueOf("3x²-3x-3"),
				valueOf("3x²+2x+1").subtract(valueOf("5x+4"))
				);
		assertEquals(
				valueOf("-5x-3"),
				new DoublePolynomial(1).subtract(valueOf("5x+4"))
				);
	}
	
	@Test
	public void testIsOne() {
		assertTrue(DoublePolynomial.ONE.isOne());
		assertFalse(new DoublePolynomial(-4).isOne());
		assertFalse(new DoublePolynomial(0, 117).isOne());
	}
	
	@Test
	public void testMultiply() {
		assertEquals(
				new DoublePolynomial(0),
				valueOf("4x³+3x²+2x+1").multiply(valueOf("0x+0"))
				);
		assertEquals(
				new DoublePolynomial(0.5, 1, 0.5),
				new DoublePolynomial(0.5, 0.5).multiply(new DoublePolynomial(1, 1))
				);
		assertEquals(
				new DoublePolynomial(0),
				valueOf("5x^4+4x³+3x²+2x+1").multiply(0)
				);
		assertEquals(
				new DoublePolynomial(0.5, 1, 0.5, 1),
				new DoublePolynomial(1, 2, 1, 2).multiply(new DoublePolynomial(0.5))
				);
		assertEquals(
				new DoublePolynomial(0),
				valueOf("6x^5+5x^4+4x³+3x²+2x+1").multiply(0)
				);
		assertEquals(
				valueOf("-1200x³-1000x²-800x-600"),
				valueOf("6x³+5x²+4x+3").multiply(-200)
				);
		assertEquals(
				new DoublePolynomial(0),
				valueOf("4x^5-33").multiply(0)
				);
		assertEquals(
				new DoublePolynomial(Double.MIN_VALUE),
				new DoublePolynomial(1, 0, Double.MIN_VALUE).multiply(Double.MIN_VALUE)
				);
	}
	
	@Test
	public void testDivide() {
		assertEquals(
				valueOf("10x³+10x²+20x+10"),
				valueOf("110x³+110x²+220x+110").divide(11)
				);
		assertEquals(
				valueOf("8x³+6x²+4x+2"),
				valueOf("4x³+3x²+2x+1").divide(new DoublePolynomial(0.5))
				);
		assertEquals(
				valueOf("x+1"),
				valueOf("x²+2x+1").divide(valueOf("x+1"))
				);
		assertEquals(
				new DoublePolynomial(0),
				valueOf("5x³+9x²+7x+3").divide(valueOf("x^4+5x³+6x²+2"))
				);
		assertEquals(
				valueOf("-5x^7+4.43x"),
				valueOf("5x^7-4.43x").divide(-1)
				);
		assertEquals(
				new DoublePolynomial(1.0/7.13),
				new DoublePolynomial(1, 0, Double.MIN_VALUE).divide(7.13)
				);
		assertEquals(
				new DoublePolynomial(Double.NaN),
				new DoublePolynomial(0).divide(0)
				);
	}
	
	@Test
	public void testSubstitute() {
		assertEquals(
				valueOf("x+2"),
				valueOf("x+1").substitute(valueOf("x+1"))
				);
		double[] coefficients = { 0.5, 2 };
		
		// (2x+1/2)² + 2(2x+1/2) + 4 = 4x² + 6x + 21/4
		assertEquals(
				valueOf("4x²+6x+5.25"),
				valueOf("x²+2x+4").substitute(new DoublePolynomial(coefficients))
				);
		
		assertEquals(
				valueOf("3"),
				valueOf("3").substitute(valueOf("34.4x^33-2.910x^14"))
				);
		double[] coefficients1 = { 0 };
		
		assertEquals(
				valueOf("97"),
				valueOf("33.2x^4+0.001x^2+97").substitute(new DoublePolynomial(coefficients1))
				);
		double[] coefficients2 = { 1 };
		
		assertEquals(
				valueOf("40"),
				valueOf("10x^5+28.5x+1.5").substitute(new DoublePolynomial(coefficients2))
				);
	}
	
	@Test
	public void testPow() {
		assertEquals(
				valueOf("1"),
				valueOf("x³+6x²+12x+8").pow(0)
				);
		assertEquals(
				valueOf("1"),
				valueOf("1").pow(Integer.MAX_VALUE)
				);
		assertEquals(
				valueOf("x³+6x²+12x+8"),
				valueOf("x+2").pow(3)
				);
	}
	
	@Test
	public void testDerivative() {
		assertEquals(
				new DoublePolynomial(0 ),
				new DoublePolynomial(0 ).derivative()
				);
		assertEquals(
				new DoublePolynomial(0),
				new DoublePolynomial(1.22 ).derivative()
				);
		assertEquals(
				valueOf("15x^9+4x+15"),
				valueOf("1.5x^10+2x²+15x+0.121").derivative()
				);
	}
	
	@Test
	public void testApply() {
		assertEquals(
				0,
				valueOf("33x³").applyAsDouble(0),
				0
				);
		assertEquals(
				-1,
				valueOf("0.25x^3-1.25").applyAsDouble(1),
				0
				);
		assertEquals(
				3333,
				valueOf("3x³+3x²+3x+3").applyAsDouble(10),
				0
				);
	}
	
	@Test
	public void testToString() {
		assertEquals("0.0", valueOf("0").toString());
		assertEquals("0.5", valueOf("0.5").toString());
		assertEquals("-1.0", valueOf("-1").toString());
		assertEquals("x", valueOf("x").toString());
		assertEquals("x + 1.0", valueOf("x+1").toString());
		assertEquals("x - 2.0", valueOf("-2+x").toString());
		assertEquals("-x + 3.0", valueOf("3-x").toString());
		assertEquals("-x - 4.0", valueOf("-x-4").toString());
		assertEquals("2.0 x² + x - 1.0", valueOf("-1+x+2x^2").toString());
		assertEquals("-5.0 x^4 - 4.0 x³ + 3.0 x² - x + 1.0", valueOf("-5 x^4 - 4 x³ + 3 x² - x + 1").toString());
	}
}
