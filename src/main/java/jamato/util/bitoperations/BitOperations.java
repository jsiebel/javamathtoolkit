package jamato.util.bitoperations;

/**
 * Provides methods for bit operations on numbers.
 */
public class BitOperations {
	
	private BitOperations() {
		// hidden constructor
	}
	
	/**
	 * Returns of the index of the highest one-bit below (right of) the given index. If there is no lower one-bit,
	 * <code>-1</code> is returned. Example: When calling this method on 0b10000101 with the starting index
	 * <code>6</code>, <code>2</code> is returned.
	 * 
	 * <pre>
	 * 0b10000101
	 *    ^   ^
	 *    6543210
	 * </pre>
	 * 
	 * @param value a long value
	 * @param index the bit index
	 * @return the next-lower one-bit
	 */
	public static int indexOfNextLowerOneBit(int value, int index) {
		int shiftedValue = value << (Integer.SIZE - index);
		return index == 0 || shiftedValue == 0 ? -1 : index - Integer.numberOfLeadingZeros(shiftedValue) - 1;
	}
	
	/**
	 * Returns of the index of the highest one-bit below (right of) the given index. If there is no lower one-bit,
	 * <code>-1</code> is returned. Example: When calling this method on 0b10000101 with the starting index
	 * <code>6</code>, <code>2</code> is returned.
	 * 
	 * <pre>
	 * 0b10000101
	 *    ^   ^
	 *    6543210
	 * </pre>
	 * 
	 * @param value a long value
	 * @param index the bit index
	 * @return the next-lower one-bit
	 */
	public static int indexOfNextLowerOneBit(long value, int index) {
		long shiftedValue = value << (Long.SIZE - index);
		return index == 0 || shiftedValue == 0 ? -1 : index - Long.numberOfLeadingZeros(shiftedValue) - 1;
	}
	
	/**
	 * Returns of the index of the lowest one-bit above (left of) the given index. If there is no higher one-bit,
	 * <code>32</code> is returned. Example: When calling this method on 0b10100001 with the starting index
	 * <code>2</code>, <code>5</code> is returned.
	 * 
	 * <pre>
	 * 0b10100001
	 *     ^  ^
	 *     543210
	 * </pre>
	 * 
	 * @param value a long value
	 * @param index the bit index
	 * @return the next-higher one-bit
	 */
	public static int indexOfNextHigherOneBit(int value, int index) {
		int shiftedValue = value >> (index + 1);
		return index == Integer.SIZE - 1 || shiftedValue == 0 ? 32
				: index + Integer.numberOfTrailingZeros(shiftedValue) + 1;
	}
	
	/**
	 * Returns of the index of the lowest one-bit above (left of) the given index. If there is no higher one-bit,
	 * <code>64</code> is returned. Example: When calling this method on 0b10100001 with the starting index
	 * <code>2</code>, <code>5</code> is returned.
	 * 
	 * <pre>
	 * 0b10100001
	 *     ^  ^
	 *     543210
	 * </pre>
	 * 
	 * @param value a long value
	 * @param index the bit index
	 * @return the next-higher one-bit
	 */
	public static int indexOfNextHigherOneBit(long value, int index) {
		long shiftedValue = value >> (index + 1);
		return index == Long.SIZE - 1 || shiftedValue == 0 ? 64 : index + Long.numberOfTrailingZeros(shiftedValue) + 1;
	}
	
	/**
	 * Returns the offset of the n-th set bit in the given value, starting at <code>n=0</code> for the lowest set bit.
	 * If no bits are set (i.e. <code>value=0</code>) or there are less than <code>n+1</code> bits in the value, 32 is
	 * returned. For example, <code>offsetOfNthBit(0b01100101, 2) = 5</code>.
	 * 
	 * <pre>
	 * 0b01100101
	 *    ^^  ^ ^
	 *    65  2 0
	 * </pre>
	 * 
	 * @param value an integer value
	 * @param n the
	 * @return the offset of the n-th set bit
	 */
	public static int offsetOfNthBit(int value, int n) {
		int remainder = value;
		for (int i = 0; i < n && remainder != 0; i++) {
			remainder &= ~Integer.lowestOneBit(remainder);
		}
		return Integer.numberOfTrailingZeros(remainder);
	}
	
	/**
	 * Returns the offset of the n-th set bit in the given value, starting at <code>n=0</code> for the lowest set bit.
	 * If no bits are set (i.e. <code>value=0</code>) or there are less than <code>n+1</code> bits in the value, 32 is
	 * returned. For example, <code>offsetOfNthBit(0b01100101L, 2) = 5</code>.
	 * 
	 * <pre>
	 * 0b01100101
	 *    ^^  ^ ^
	 *    65  2 0
	 * </pre>
	 * 
	 * @param value an integer value
	 * @param n the
	 * @return the offset of the n-th set bit
	 */
	public static int offsetOfNthBit(long value, int n) {
		long remainder = value;
		for (int i = 0; i < n && remainder != 0; i++) {
			remainder &= ~Long.lowestOneBit(remainder);
		}
		return Long.numberOfTrailingZeros(remainder);
	}
	
}
