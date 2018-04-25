package fr.uco.ima.tsp.evaluator;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * The class <code>TSPEvaluator</code> computes the objective function of a
 * given TSP solution.
 * 
 * @author froger
 * 
 */
public class TSPEvaluator {

	/**
	 * Creates a new instance of {@link TSPEvaluator}.
	 */
	public TSPEvaluator() {

	}

	/**
	 * Evaluates the objective function of the given <code>solution</code>.
	 * 
	 * @param solution
	 *            a solution.
	 * @param instance
	 *            an instance.
	 * @return the evaluation of the given <code>solution</code>.
	 */
	public int getEvaluation(TSPSolution solution, TSPInstance instance) {
		if (solution == null) {
			return Integer.MAX_VALUE;
		}
		int sizeRoute = solution.size();
		int distance = 0;
		int nodeID1, nodeID2;
		for (int i = 0; i < sizeRoute - 1; i++) {
			nodeID1 = solution.get(i);
			nodeID2 = solution.get(i + 1);
			distance += instance.getDistance(nodeID1, nodeID2);
		}
		int firstNodeID = solution.get(0);
		int lastNodeID = solution.get(sizeRoute - 1);
		distance += instance.getDistance(lastNodeID, firstNodeID);
		return distance;
	}

	/**
	 * Checks if the given cost is the correct cost for the given solution.
	 * 
	 * @param solution
	 *            a solution.
	 * @param instance
	 *            an instance.
	 * @param cost
	 *            the cost associated with the solution.
	 * @return <code>true</code> if <code>cost</code> is the correct evaluation
	 *         of the given <code>solution</code>, <code>false</code>
	 *         otherwise..
	 */
	public boolean checkEvaluation(TSPSolution solution, TSPInstance instance, int cost) {
		return getEvaluation(solution, instance) == cost;
	}

}
