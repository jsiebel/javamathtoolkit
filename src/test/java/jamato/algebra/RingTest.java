package jamato.algebra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RingTest {
	
	@Test
	public void testSubtract() {
		assertEquals(new TestRing(3), new TestRing(7).subtract(new TestRing(4)));
	}
	
	@Test
	public void testMultiply() {
		assertEquals(new TestRing(7 * -2), new TestRing(7).multiply(-2));
		assertEquals(new TestRing(7 * -1), new TestRing(7).multiply(-1));
		assertEquals(new TestRing(7 * 0), new TestRing(7).multiply(0));
		assertEquals(new TestRing(7 * 1), new TestRing(7).multiply(1));
		assertEquals(new TestRing(7 * 2), new TestRing(7).multiply(2));
		assertEquals(new TestRing(7 * 3), new TestRing(7).multiply(3));
	}
	
	@Test
	public void testDivide() {
		assertEquals(new TestRing(3), new TestRing(1).divide(new TestRing(4)));
		assertEquals(new TestRing(7), new TestRing(6).divide(new TestRing(4)));
		assertEquals(new TestRing(0), new TestRing(0).divide(new TestRing(8)));

		assertEquals(new TestRing(3), new TestRing(1).divide(4));
		assertEquals(new TestRing(7), new TestRing(6).divide(4));
		assertEquals(new TestRing(0), new TestRing(0).divide(8));
	}
	
	@Test
	public void testPow() {
		assertEquals(new TestRing(0), new TestRing(0).pow(258));
		assertEquals(new TestRing(1), new TestRing(1).pow(447));
		assertEquals(new TestRing(9), new TestRing(2).pow(6));
		assertEquals(new TestRing(1), new TestRing(3).pow(-5));
		assertEquals(new TestRing(1), new TestRing(4).pow(0));
	}
	
	private static class TestRing implements Ring<TestRing>{

		static final int MODULUS = 11;
		final int value;
		
		TestRing(int value){
			this.value = Math.floorMod(value, MODULUS);
		}
		
		@Override
		public TestRing negate() {
			return new TestRing(-value);
		}

		@Override
		public boolean isZero() {
			return value == 0;
		}

		@Override
		public TestRing add(TestRing summand) {
			return new TestRing(value + summand.value);
		}

		@Override
		public TestRing invert() {
			if (value == 0) {
				throw new ArithmeticException();
			}else {
				return pow(MODULUS-2);
			}
		}
		
		@Override
		public TestRing multiply(TestRing factor) {
			return new TestRing(value * factor.value);
		}
		
		@Override
		public boolean equals(Object obj) {
			return value == ((TestRing)obj).value;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(value);
		}
		
		@Override
		public String toString() {
			return "TestRing[" + value + "]";
		}
	}
}
