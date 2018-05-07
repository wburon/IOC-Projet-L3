package fr.uco.ima.tsp.solver.ls;

/**
 * Implements the triplet iterator used to define the 3-opt neighborhood
 *
 */
public class ThreeOptIterator {

	/**
	 * The first position
	 */
	private int i;
	/**
	 * The second position
	 */
	private int j;
	/**
	 * The third position
	 */
	private int k;
	/**
	 * The total number of nodes
	 */
	private int n;
	/**
	 * true if the current move is not the first move, false otherwise
	 */
	boolean started = false;

	/**
	 * Constructs a new inverse iterator
	 * 
	 * @param n
	 *            the number of nodes
	 */
	public ThreeOptIterator(int n) {
		this.n = n;
	}

	/**
	 * 
	 * @return the current value of i
	 */
	public int getI() {
		if (i == -1)
			throw new IllegalStateException("The iterator has reached the end");
		return this.i;
	}

	/**
	 * 
	 * @return the current value of j
	 */
	public int getJ() {
		if (j == -1)
			throw new IllegalStateException("The iterator has reached the end");
		return this.j;
	}

	/**
	 * 
	 * @return the current value of k
	 */
	public int getK() {
		if (k == -1)
			throw new IllegalStateException("The iterator has reached the end");
		return this.k;
	}

	/**
	 * Updates the second position as long as it is possible and then the first
	 * position.
	 * 
	 * @return true if there is a next move to consider in the neighborhood and
	 *         false otherwise TODO document this method more extensively
	 */
	public boolean next() {
		if (started == true) {

			// Case 0: the whole neighborhood was explored
			if (i == n - 5 && j == n - 3 && k == n - 1) {
				i = -1;
				j = -1;
				k = -1;
				return false;
			}
			// increment the third position
			k++;
			// if the third position has reached the end
			if (k >= n) {
				// increment the second position
				j++;
				if (j >= n - 2) {
					// set j to n in order to update the value of i
					j = n;
				} else {
					// reset the third position
					k = j + 2;
				}

			}
			// if the second position has reached the end
			if (j >= n) {
				// increment the first position
				i++;
				if (i >= n - 5) {
					// we can stop
					return false;
				} else {
					// reset the second and third positions
					j = i + 2;
					k = j + 2;
				}
			}
			return true;

		} else {
			// No movement if the number of nodes is less than 3
			if (n < 6) {
				return false;
			}
			// First movement
			i = 1;
			j = 3;
			k = 5;
			started = true;
			return true;
		}
	}

	public static void main(String[] args) {
		ThreeOptIterator it = new ThreeOptIterator(10);
		while (it.next()) {
			System.out.println(it.getI() + "\t" + it.getJ() + "\t" + it.getK());
		}
	}
}
