package fr.uco.ima.tsp.checker;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * The interface <code>TSPConstraintI</code> lists the method that the classes
 * responsible to model a constraint have to implement.
 * 
 * @author froger
 * 
 */
public interface TSPConstraintI {

	/**
	 * Checks a solution
	 * 
	 * @param solution
	 *            the solution to check
	 * @param instance
	 *            the instance associated with the solution
	 * @param output
	 *            true if it displays in the console the reason why the
	 *            constraint is violated, false otherwise
	 * @return true if the constraint verifies the solution, false otherwise
	 */
	public boolean checkConstraint(TSPSolution solution, TSPInstance instance, boolean output);

}
