package fr.uco.ima.tsp.solver.ls;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * Defines the interface for neighborhoods
 *
 */
public interface NeighborhoodI {

	/**
	 * Explores the neighborhood and returns a neighbor solution. Depending on
	 * the exploration strategy, the returned solution may be the best solution
	 * in the neighborhood of <code>s</code> or the first improving solution
	 * found during the exploration. Implementing classes should ensure that the
	 * returned solution is evaluated (i.e., its objective function is
	 * computed). If no improving solution is found, the method returns the
	 * initial solution <code>s</code>.
	 * 
	 * @param instance
	 *            the instance
	 * @param s
	 *            the solution from where the neighborhood exploration starts
	 * @param strategy
	 *            the exploration strategy (first or best improvement)
	 * @return an improving solution of <code>s</code> if found or the initial
	 *         solution <code>s</code> otherwise.
	 */
	public TSPSolution explore(TSPInstance instance, TSPSolution s, ExplorationStrategy strategy);

}
