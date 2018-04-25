package fr.uco.ima.tsp.data;

/**
 * Provides a concrete implementation of a TSP instance
 * 
 * @author froger
 *
 */
public class TSPInstance {

	/**
	 * Nom de l'instance.
	 */
	private String name;
	/**
	 * La matrice des distances
	 */
	private final int[][] matrix;
	/**
	 * Coordonnées des noeuds
	 */
	private final double[][] coordinates;

	/**
	 * Constructs a new TSP instance
	 * 
	 * @param n
	 *            number of nodes in the underlying graph
	 */
	public TSPInstance(String name, double[][] coordinates, int[][] matrix) {
		this.name = name;
		this.coordinates = coordinates;
		this.matrix = matrix;
	}

	public double getX(int i) {
		return coordinates[i][0];
	}

	public double getY(int i) {
		return coordinates[i][1];
	}

	/**
	 * 
	 * @param i
	 *            the first node
	 * @param j
	 *            the second node
	 * @return the distance between node <code>i</code> and node <code>j</code>
	 */
	public int getDistance(int i, int j) {
		return this.matrix[i][j];
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the number of nodes in the instance
	 */
	public int size() {
		return matrix.length;
	}

	/**
	 * 
	 * @return a copy of the distance matrix
	 */
	public int[][] getDistanceMatrix() {
		return this.matrix.clone();
	}

	/**
	 * Prints the distance matrix to the standard output
	 * 
	 */
	public void printDistanceMatrix() {
		for (int i = 0; i < this.matrix.length; i++) {
			for (int j = 0; j < this.matrix[0].length; j++) {
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}

}
