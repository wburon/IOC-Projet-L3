package fr.uco.ima.tsp.solver.ls;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * Defines the interface to solution generators
 * 
 *
 */
public interface SolutionGeneratorI {

	/**
	 * Builds and return a solution
	 * 
	 * @return the solution
	 */
	public TSPSolution generateSolution(TSPInstance instance);

}
