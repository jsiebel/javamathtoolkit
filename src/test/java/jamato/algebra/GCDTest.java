package jamato.algebra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GCDTest {
	
	@Test
	public void testGdc() {
		assertEquals(6, GCD.of(12, 30));
		assertEquals(9, GCD.of(-18, 45));
		assertEquals(9, GCD.of(27, -45));
		assertEquals(13, GCD.of(-78, -91));
		assertEquals(1, GCD.of(1, 3123422));
		assertEquals(1, GCD.of(3123422, 1));
		assertEquals(284, GCD.of(284, 0));
		assertEquals(284, GCD.of(0, 284));
		assertEquals(284, GCD.of(-284, 0));
		assertEquals(284, GCD.of(0, -284));
		assertEquals(0, GCD.of(0, 0));

		assertEquals(6L, GCD.of(12L, 30L));
		assertEquals(9L, GCD.of(-18L, 45L));
		assertEquals(9L, GCD.of(27L, -45L));
		assertEquals(13L, GCD.of(-78L, -91L));
		assertEquals(1L, GCD.of(1L, 3123422L));
		assertEquals(1L, GCD.of(3123422L, 1L));
		assertEquals(284L, GCD.of(284L, 0L));
		assertEquals(284L, GCD.of(0L, 284L));
		assertEquals(284L, GCD.of(-284L, 0L));
		assertEquals(284L, GCD.of(0L, -284L));
		assertEquals(0L, GCD.of(0L, 0L));
	}
}
