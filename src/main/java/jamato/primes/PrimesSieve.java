package jamato.primes;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * Creates a sieve of some prime numbers that can be used for filtering out easy composite numbers when searching
 * primes.
 *
 * @author JSiebel
 *
 */
class PrimesSieve{
	
	/** The size of the sieve. */
	final int size;
	
	/**
	 * An array storing if a number is divisible by any sieve prime. If <code>isComposite[x%size] == true</code>, where
	 * <code>x</code> is greater than all sieve primes, then <code>x</code> is a composite number.
	 */
	private final boolean[] isComposite;
	
	/**
	 * An array storing how much greater the next possible prime is for a given index: If <code>x</code> is a number
	 * greater than all sieve primes, than there are no prime numbers between <code>x</code> and
	 * <code>x+candidateStep[x%size]</code> (exclusive).
	 */
	private final int[] candidateStep;
	
	/**
	 * The number of prime candidates remaining in the sieve.
	 */
	int numberOfCandidates;
	
	/**
	 * Creates a prime sieve of the given primes.
	 * 
	 * @param primes the sieve primes
	 */
	PrimesSieve(int... primes){
		this.size = Arrays.stream(primes).reduce(1, (s, p) -> s * p);
		this.numberOfCandidates = Arrays.stream(primes).reduce(1, (s, p) -> s * (p - 1));
		
		isComposite = new boolean[size];
		for (int p : primes){
			for (int i = 0; i < size; i += p){
				isComposite[i] = true;
			}
		}
		
		candidateStep = new int[size];
		byte nextIndex = 2;
		for (int i = size - 1; i >= 0; i--){
			candidateStep[i] = nextIndex;
			if (!isComposite[i]){
				nextIndex = 0;
			}
			nextIndex++;
		}
	}
	
	/**
	 * <code>true</code> if the number is a prime candidate (that is, divisible by none of the sieve prime),
	 * <code>false</code> otherwise.
	 *
	 * @param number a number, must be greater than all sieve primes
	 * @return <code>true</code> if the number is a prime candidate
	 */
	public boolean isCandidate(int number){
		return !isComposite[number % size];
	}
	
	/**
	 * <code>true</code> if the number is a prime candidate (that is, divisible by none of the sieve prime),
	 * <code>false</code> otherwise.
	 *
	 * @param number a number, must be greater than all sieve primes
	 * @return <code>true</code> if the number is a prime candidate
	 */
	public boolean isCandidate(long number){
		return !isComposite[(int) (number % size)];
	}
	
	/**
	 * Returns the least prime candidate greater than the given number; that is the least number that is divisible by
	 * none of the sieve primes.
	 *
	 * @param number a number, must be greater than all sieve primes
	 * @return a prime candidate
	 */
	public int getNextCandidate(int number){
		return number + candidateStep[number % size];
	}
	
	public IntStream stream(int startExclusive){
		return stream(startExclusive, Integer.MAX_VALUE);
	}
	
	public IntStream stream(int startExclusive, int endInclusive){
		return StreamSupport.intStream(new PrimesSieveSpliterator(this, startExclusive, endInclusive), true);
	}
}
