package jamato.number;

import static jamato.number.DoubleComplex.I;
import static jamato.number.DoubleComplex.ONE;
import static jamato.number.DoubleComplex.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DoubleComplexTest {
	
	@Test
	public void testDoubleConstructor() {
		assertEquals(DoubleComplex.ZERO, new DoubleComplex(0, 0));
		assertEquals(ONE, new DoubleComplex(1, 0));
	}
	
	@Test
	public void testPolar() {
		assertEquals(DoubleComplex.ZERO, DoubleComplex.polar(0, 22));
		assertEquals(DoubleComplex.ONE, DoubleComplex.polar(1, 0));
		assertTrue(DoubleComplex.I.distanceTo(DoubleComplex.polar(1, Math.PI/2)) < 1e-10);
	}
	
	@Test
	public void testNegate() {
		assertEquals(new DoubleComplex(33.3, 43), new DoubleComplex(-33.3, -43).negate());
	}

	@Test
	public void testIsZero() {
		assertTrue(ZERO.isZero());
		assertFalse(ONE.isZero());
		assertFalse(I.isZero());
	}
	
	@Test
	public void testAdd() {
		assertEquals(new DoubleComplex(3, 4), new DoubleComplex(2, 4).add(new DoubleComplex(1)));
		assertEquals(new DoubleComplex(4, 1), new DoubleComplex(0, 1).add(4));
	}
	
	@Test
	public void testSubtract() {
		assertEquals(new DoubleComplex(1, 4), new DoubleComplex(2, 4).subtract(new DoubleComplex(1)));
		assertEquals(new DoubleComplex(-4, 1), new DoubleComplex(0, 1).subtract(4));
	}
	
	@Test
	public void testInvert() {
		assertEquals(ONE, ONE.invert());
		assertEquals(new DoubleComplex(3.0/25, -4.0/25), new DoubleComplex(3, 4).invert());
		assertTrue(ZERO.invert().isNaN());
	}

	@Test
	public void testIsOne() {
		assertFalse(ZERO.isOne());
		assertTrue(ONE.isOne());
		assertFalse(I.isOne());
	}
	
	@Test
	public void testMultiply() {
		assertEquals(new DoubleComplex(-7, 22), new DoubleComplex(2, 3).multiply(new DoubleComplex(4, 5)));
		assertEquals(ZERO, new DoubleComplex(4, 8).multiply(0));
		assertEquals(new DoubleComplex(10.5, 4.5), new DoubleComplex(7, 3).multiply(1.5));
	}
	
	@Test
	public void testDivide() {
		assertEquals(new DoubleComplex(4, 5), new DoubleComplex(-7, 22).divide(new DoubleComplex(2, 3)));
		assertEquals(new DoubleComplex(0.5, 1), new DoubleComplex(4, 8).divide(8));
		assertEquals(new DoubleComplex(1, 2), new DoubleComplex(4, 8).divide(4.0));
	}
	
	@Test
	public void testPow() {
		assertEquals(ONE, I.pow(-4));
		assertEquals(ONE, I.pow(0));
		assertEquals(I, I.pow(1));
		assertEquals(I, I.pow(5));
		assertEquals(ZERO, ZERO.pow(31));
		assertEquals(new DoubleComplex(-119, -120), new DoubleComplex(2, 3).pow(4));
	}
	
	@Test
	public void testAbs() {
		assertEquals(0, ZERO.abs(), 0);
		assertEquals(1, ONE.abs(), 0);
		assertEquals(1, I.abs(), 0);
		assertEquals(10, new DoubleComplex(-6, 8).abs(), 0);
	}
	
	@Test
	public void testDistanceTo() {
		assertEquals(1, ZERO.distanceTo(ONE), 0);
		assertEquals(1, ZERO.distanceTo(I), 0);
		assertEquals(Math.sqrt(2), ONE.distanceTo(I), 0);
	}
	
	@Test
	public void testConjugate() {
		assertEquals(ZERO, ZERO.conjugate());
		assertEquals(ONE, ONE.conjugate());
		assertEquals(new DoubleComplex(0, -1), I.conjugate());
		assertEquals(new DoubleComplex(3.5, 2.6), new DoubleComplex(3.5, -2.6).conjugate());
	}
	
	@Test
	public void testIsFinite() {
		assertTrue(ZERO.isFinite());
		assertTrue(new DoubleComplex(7.7).isFinite());
		assertFalse(ZERO.invert().isFinite());
	}
	
	@Test
	public void testIsNaN() {
		assertFalse(ZERO.isNaN());
		assertFalse(new DoubleComplex(7.7).isNaN());
		assertTrue(ZERO.invert().isNaN());
	}
	
	@Test
	public void testToString() {
		assertEquals("0.0", ZERO.toString());
		assertEquals("i", I.toString());
		assertEquals("-i", new DoubleComplex(0, -1).toString());
		assertEquals("47.0-i", new DoubleComplex(47, -1).toString());
		assertEquals("3.0-2.5i", new DoubleComplex(3, -2.5).toString());
	}
	
	@Test
	public void testValueOf() {
		assertEquals(ONE, DoubleComplex.valueOf("1"));
		assertEquals(ZERO, DoubleComplex.valueOf("0i"));
		assertEquals(I, DoubleComplex.valueOf("0+1i"));
		assertEquals(I, DoubleComplex.valueOf("i"));
		assertEquals(new DoubleComplex(0, -1), DoubleComplex.valueOf("-i"));
		assertEquals(new DoubleComplex(2, 1), DoubleComplex.valueOf("2+i"));
		assertEquals(new DoubleComplex(2, -1), DoubleComplex.valueOf("2-i"));
		assertEquals(new DoubleComplex(1.1, 1.2), DoubleComplex.valueOf("1.1+1.2i"));
		assertEquals(new DoubleComplex(-1.3, 1.4), DoubleComplex.valueOf("-1.3+1.4i"));
		assertEquals(new DoubleComplex(1.5, -1.6), DoubleComplex.valueOf("1.5-1.6i"));
		assertEquals(new DoubleComplex(-1.7, -1.8), DoubleComplex.valueOf("-1.7-1.8i"));
	}
}
