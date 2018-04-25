package fr.uco.ima.tsp.solver;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * The interface <code>TSPSolverI</code> lists the method that classes
 * responsible for computing a TSP solution should implement
 * 
 * @author froger
 *
 */
public interface TSPSolverI {

	public TSPSolution solve(TSPInstance instance);

}
