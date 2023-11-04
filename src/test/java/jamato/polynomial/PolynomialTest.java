package jamato.polynomial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import jamato.number.IntRational;

public class PolynomialTest {

	final IntRational FOUR = new IntRational(4);
	final IntRational TWO = new IntRational(2);
	final IntRational ONE = new IntRational(1);
	final IntRational HALF = new IntRational(1, 2);
	final IntRational ZERO = new IntRational(0);
	
	@Test
	public void testIntConstructor() {
		Polynomial<IntRational> polynomial1 = valueOf(ZERO);
		assertEquals(0, polynomial1.getDegree());
		assertEquals(ZERO, polynomial1.getCoefficient(0));
		assertEquals(ZERO, polynomial1.getCoefficient(1));
		
		Polynomial<IntRational> polynomial2 = valueOf(ONE, TWO, FOUR);
		assertEquals(2, polynomial2.getDegree());
		assertEquals(ONE, polynomial2.getCoefficient(0));
		assertEquals(TWO, polynomial2.getCoefficient(1));
		assertEquals(FOUR, polynomial2.getCoefficient(2));
		assertEquals(ZERO, polynomial2.getCoefficient(3));

		Polynomial<IntRational> polynomial3 = valueOf(ONE, TWO, FOUR, ZERO, ZERO);
		assertEquals(2, polynomial3.getDegree());

		Polynomial<IntRational> polynomial4 = valueOf(ONE, ZERO);
		assertEquals(0, polynomial4.getDegree());

		Polynomial<IntRational> polynomial5 = valueOf(ZERO, ZERO);
		assertEquals(polynomial1, polynomial5);
	}
	
	@Test
	public void testIntRationalConstructor() {
		Polynomial<IntRational> polynomial1 = valueOf(ZERO);
		assertEquals(0, polynomial1.getDegree());
		
		Polynomial<IntRational> polynomial2 = valueOf(ZERO, HALF);
		assertEquals(1, polynomial2.getDegree());
		
		Polynomial<IntRational> polynomial3 = valueOf(ZERO, ZERO);
		assertEquals(0, polynomial3.getDegree());
		
		Polynomial<IntRational> polynomial4 = valueOf(ZERO, FOUR, HALF, HALF);
		assertEquals(3, polynomial4.getDegree());
	}
	
	@Test
	public void testStringConstructor() {
		assertEquals(valueOf(new IntRational(-2, 37)), valueOf("-2/37"));
		assertEquals(valueOf(ONE, new IntRational(-2)), valueOf("-2x+1"));
		assertEquals(valueOf(new IntRational(-3), ZERO, new IntRational(5)), valueOf("5x^2-3"));
		assertEquals(valueOf(HALF, ZERO, FOUR, HALF), valueOf("1/2A³+1/2+4x²"));
		assertEquals(valueOf(ZERO), valueOf("-1/2 t² + 1/3 t^2 + 0 + 1/6 t² + 0t^15"));
	}
	
	@Test
	public void testIsZero() {
		assertTrue(valueOf("0").isZero());
		assertFalse(valueOf("-28").isZero());
		assertFalse(valueOf("-227x").isZero());
	}
	
	@Test
	public void testNegate() {
		assertEquals(valueOf("0"), valueOf("0").negate());
		assertEquals(valueOf("52x³-x+12"), valueOf("-52x³+x-12").negate());
	}
	
	@Test
	public void testAdd() {
		assertEquals(
				valueOf("6"),
				valueOf("2x+4").add(valueOf("-2x+2"))
				);
		assertEquals(
				valueOf(ONE),
				valueOf(HALF).add(valueOf(HALF))
				);
		assertEquals(
				valueOf(ZERO),
				valueOf("4x²+3x+2").add(valueOf("-4x²-3x-2"))
				);
		assertEquals(
				valueOf("3x²+7x+5"),
				valueOf("3x²+2x+1").add(valueOf("5x+4"))
				);
		assertEquals(
				valueOf("5x+5"),
				valueOf(ONE).add(valueOf("5x+4"))
				);
	}
	
	@Test
	public void testSubtract() {
		assertEquals(
				valueOf("4x+2"),
				valueOf("2x+4").subtract(valueOf("-2x+2"))
				);
		assertEquals(
				valueOf(ZERO),
				valueOf(HALF).subtract(valueOf(HALF))
				);
		assertEquals(
				valueOf(ZERO),
				valueOf("4x²+3x+2").subtract(valueOf("4x²+3x+2"))
				);
		assertEquals(
				valueOf("3x²-3x-3"),
				valueOf("3x²+2x+1").subtract(valueOf("5x+4"))
				);
		assertEquals(
				valueOf("-5x-3"),
				valueOf(ONE).subtract(valueOf("5x+4"))
				);
	}
	
	@Test
	public void testIsOne() {
		assertTrue(valueOf("1").isOne());
		assertFalse(valueOf("-1").isOne());
		assertFalse(valueOf("x+1").isOne());
	}
	
	@Test
	public void testMultiply() {
		assertEquals(
				valueOf(ZERO),
				valueOf("4x³+3x²+2x+1").multiply(valueOf("0x+0"))
				);
		assertEquals(
				valueOf(HALF, ONE, HALF),
				valueOf(HALF, HALF).multiply(valueOf(ONE, ONE))
				);
		assertEquals(
				valueOf(ZERO),
				valueOf("5x^4+4x³+3x²+2x+1").multiply(ZERO)
				);
		assertEquals(
				valueOf(HALF, ONE, HALF, ONE),
				valueOf(ONE, TWO, ONE, TWO).multiply(new IntRational(1, 2))
				);
		assertEquals(
				valueOf(ZERO),
				valueOf("6x^5+5x^4+4x³+3x²+2x+1").multiply(0)
				);
		assertEquals(
				valueOf("-1200x³-1000x²-800x-600"),
				valueOf("6x³+5x²+4x+3").multiply(-200)
				);
	}
	
	@Test
	public void testDivide() {
		assertEquals(
				valueOf("10x³+10x²+20x+10"),
				valueOf("120x³+120x²+240x+120").divide(12L)
				);
		assertEquals(
				valueOf("10x³+10x²+20x+10"),
				valueOf("110x³+110x²+220x+110").divide(new IntRational(11))
				);
		assertEquals(
				valueOf("8x³+6x²+4x+2"),
				valueOf("4x³+3x²+2x+1").divide(valueOf(HALF))
				);
		assertEquals(
				valueOf("0"),
				valueOf("0").divide(valueOf("x^4+5x³+6x²+2"))
				);
		assertEquals(
				valueOf("x+1"),
				valueOf("x²+2x+1").divide(valueOf("x+1"))
				);
		assertEquals(
				valueOf(ZERO),
				valueOf("5x³+9x²+7x+3").divide(valueOf("x^4+5x³+6x²+2"))
				);
	}
	
	@Test
	public void testSubstitute() {
		assertEquals(
				valueOf("x+2"),
				valueOf("x+1").substitute(valueOf("x+1"))
				);
		
		// (2x+1/2)² + 2(2x+1/2) + 4 = 4x² + 6x + 21/4
		assertEquals(
				valueOf("4x²+6x+21/4"),
				valueOf("x²+2x+4").substitute(valueOf(HALF, TWO))
				);
		
		assertEquals(
				valueOf("3"),
				valueOf("3").substitute(valueOf("34/4x^33-2910x^14"))
				);
		
		assertEquals(
				valueOf("97"),
				valueOf("35/4x^4+1/1000x^2+97").substitute(valueOf(ZERO))
				);
		
		assertEquals(
				valueOf("40"),
				valueOf("10x^5+57/2x+3/2").substitute(valueOf(ONE))
				);
	}
	
	@Test
	public void testPow() {
		assertEquals(
				valueOf("1"),
				valueOf("x³+6x²+12x+8").pow(0)
				);
		assertEquals(
				valueOf("-1"),
				valueOf("-1").pow(Integer.MAX_VALUE)
				);
		assertEquals(
				valueOf("x³+6x²+12x+8"),
				valueOf("x+2").pow(3)
				);
	}
	
	@Test
	public void testDerivative() {
		assertEquals(
				valueOf("0"),
				valueOf("0").derivative()
				);
		assertEquals(
				valueOf("0"),
				valueOf("1/13").derivative()
				);
		assertEquals(
				valueOf("15x^9+4x+15"),
				valueOf("3/2x^10+2x²+15x+1/7").derivative()
				);
	}
	
	@Test
	public void testApply() {
		assertEquals(
				ZERO,
				valueOf("33x³").apply(ZERO)
				);
		assertEquals(
				new IntRational(-1),
				valueOf("2/5x^3-7/5").apply(ONE)
				);
		assertEquals(
				new IntRational(3333),
				valueOf("3x³+3x²+3x+3").apply(new IntRational(10))
				);
	}
	
	@Test
	public void testToString() {
		assertEquals("0", valueOf("0").toString());
		assertEquals("1/2", valueOf("1/2").toString());
		assertEquals("-1", valueOf("-1").toString());
		assertEquals("x", valueOf("x").toString());
		assertEquals("x + 1", valueOf("x+1").toString());
		assertEquals("x - 2", valueOf("-2+x").toString());
		assertEquals("-x + 3", valueOf("3-x").toString());
		assertEquals("-x - 4", valueOf("-x-4").toString());
		assertEquals("2 x² - 1", valueOf("-1+2x^2").toString());
		assertEquals("-6 x^5 + 5 x^4 - 4 x³ + x² - x + 1", valueOf("-6 x^5 + 5 x^4 - 4 x³ + x² - x + 1").toString());
	}
	
	private static Polynomial<IntRational> valueOf(IntRational... coefficients){
		return new Polynomial<>(List.of(coefficients));
	}
	
	private static Polynomial<IntRational> valueOf(String s){
		return Polynomial.valueOf(s, IntRational::valueOf, IntRational.ZERO, IntRational.ONE);
	}
}
