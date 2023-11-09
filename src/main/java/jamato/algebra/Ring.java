package jamato.algebra;

/**
 * A ring is an algebraic structure with two operations, addition and
 * multiplication. It has the following properties:
 * 
 * <ul>
 * <li>Associativity of addition and multiplication (within numerical
 * limits)</li>
 * <li>Commutativity of addition</li>
 * <li>Additive identity</li>
 * <li>Additive inverses</li>
 * <li>Distributivity of multiplication over addition</li>
 * </ul>
 * 
 * @author JSiebel
 *
 * @param <SELF> the implementing class itself (F-bounded type)
 */
public interface Ring<SELF extends Ring<SELF>> {
	
	/**
	 * Returns the additive inverse of this.
	 *
	 * @return the additive inverse of this
	 */
	public SELF negate();
	
	/**
	 * Returns <code>true</code> if this is zero. The zero element is the neutral
	 * element of the addition, so {@code a + 0 == a} and {@code 0 + a == a} for any
	 * a.
	 * 
	 * @return <code>true</code> if this is zero
	 */
	public boolean isZero();
	
	/**
	 * Returns the sum {@code (this + summand)}.
	 * 
	 * @param summand value to be added to this object
	 * @return {@code (this + summand)}
	 */
	public SELF add(SELF summand);
	
	/**
	 * Returns the value {@code (this - subtrahend)}.
	 * 
	 * @param subtrahend value to be subtracted from this T
	 * @return {@code (this - subtrahend)}
	 */
	public default SELF subtract(SELF subtrahend) {
		return add(subtrahend.negate());
	}
	
	/**
	 * Returns the multiplicative inverse of this element.
	 *
	 * @return the multiplicative inverse of this element
	 * @throws UnsupportedOperationException if this ring has no multiplicative
	 *                                       inverse
	 * @throws ArithmeticException           if this is zero
	 */
	public default SELF invert() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Returns <code>true</code> if this is one. The one element is the neutral
	 * element of the multiplication, so {@code a * 1 == a} and {@code 1 * a == a}
	 * for any a. Not all rings have a one element.
	 * 
	 * @return <code>true</code> if this is one
	 */
	public default boolean isOne() {
		return false;
	}
	
	/**
	 * Returns the value {@code (this * factor)}.
	 * 
	 * @param factor value to be multiplied by this T
	 * @return {@code (this * factor)}
	 */
	public SELF multiply(SELF factor);
	
	/**
	 * Returns a multiple of this.
	 * 
	 * @param factor an integer factor
	 * @return {@code (this * factor)}
	 */
	public default SELF multiply(long factor) {
		/*
		 * This cast is safe for classes where the parameter SELF is the class itself.
		 */
		@SuppressWarnings("unchecked")
		SELF t = (SELF) this;
		if (factor == 0) {
			return t.subtract(t);
		}
		if (factor < 0) {
			t = t.negate();
			factor = -factor;
		}
		SELF result = null;
		while (factor != 0) {
			if (factor % 2 != 0) {
				result = result == null ? t : result.add(t);
			}
			t = t.add(t);
			factor /= 2;
		}
		return result;
	}
	
	/**
	 * Returns the value {@code (this / divisor)}. In division rings, the
	 * result is the a value for which {@code result * divisor == this}. Other rings
	 * may throw an exception or implement a remainder division where
	 * {@code result * divisor + remainder == this} for some remainder.
	 * 
	 * @param divisor value by which this BigRational is to be divided.
	 * @return {@code (this / divisor)}
	 * @throws UnsupportedOperationException if this ring has no multiplicative
	 *                                       inverse
	 * @throws ArithmeticException           if the divisor is zero
	 */
	public default SELF divide(SELF divisor) {
		return multiply(divisor.invert());
	}
	
	/**
	 * Returns the value {@code (this / divisor)}. In division rings, the
	 * result is the a value for which {@code result * divisor == this}. Other rings
	 * may throw an exception or implement a remainder division where
	 * {@code result * divisor + remainder == this} for some remainder.
	 * 
	 * @param divisor value by which this is to be divided.
	 * @return {@code (this / divisor)}
	 * @throws UnsupportedOperationException if this ring has no multiplicative
	 *                                       inverse
	 * @throws ArithmeticException           if the divisor is zero
	 */
	public default SELF divide(long divisor) {
		if (isZero() && divisor != 0) {
			/*
			 * This cast is safe for classes where the parameter SELF is the class itself.
			 */
			@SuppressWarnings("unchecked")
			SELF t = (SELF) this;
			return t;
		} else {
			return this.invert().multiply(divisor).invert();
		}
	}
	
	/**
	 * Returns the value <code>(this<sup>exponent</sup>)</code>.
	 * 
	 * @param exponent the exponent of the exponentiation, must be positive if T has no multiplicative inverse
	 * @return <code>this<sup>exponent</sup></code>
	 * @throws UnsupportedOperationException if the exponent is negative or zero, and this ring has no multiplicative
	 * inverse
	 * @throws ArithmeticException if the exponent is negative or zero and the {@code this} is zero
	 */
	public default SELF pow(int exponent) {
		/*
		 * This cast is safe for classes where the parameter SELF is the class itself.
		 */
		@SuppressWarnings("unchecked")
		SELF base = (SELF) this;
		return Exponentiation.pow(base, exponent);
	}
}
