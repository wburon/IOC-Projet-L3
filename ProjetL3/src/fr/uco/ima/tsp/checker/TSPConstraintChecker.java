package fr.uco.ima.tsp.checker;

import java.util.ArrayList;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * The class <code>TSPConstraintChecker</code> manages the constraints
 * implementing the interface {@link fr.uco.ima.tsp.checker.TSPConstraintI
 * TSPConstraintI}. It is responsible to check if a given solution is feasible
 * or not according to the constraints added.
 * 
 * @author froger
 * 
 */
public class TSPConstraintChecker {
	/**
	 * Hold the constraints
	 */
	private ArrayList<TSPConstraintI> constraints = new ArrayList<>();

	/**
	 * Adds a constraint
	 * 
	 * @param c
	 *            the constraint to add
	 */
	public void addConstraint(TSPConstraintI c) {
		this.constraints.add(c);
	}

	/**
	 * Checks the constraints
	 * 
	 * @param s
	 *            the solution to check
	 * @return true if the solution checks all constraints, false otherwise
	 */
	public boolean checkConstraints(TSPSolution s, TSPInstance i, boolean output) {
		if (s == null) {
			return false;
		}
		boolean check = true;
		for (TSPConstraintI c : constraints) {
			if (!c.checkConstraint(s, i, output)) {
				if (output) {
					System.out.println("constraint on " + c.getClass().getName() + " violated");
				}
				return false;
			}
		}
		return check;
	}

}
