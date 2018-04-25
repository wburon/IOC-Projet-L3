package fr.uco.ima.tsp.checker;

import java.util.HashMap;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * The class <code>TSPConstraint</code> implements the constraint of the TSP
 * (all the nodes need to be visited once).
 * 
 * @author froger
 *
 */
public class TSPConstraint implements TSPConstraintI {

	@Override
	public boolean checkConstraint(TSPSolution solution, TSPInstance instance, boolean output) {
		int nbVisits;
		HashMap<Integer, Integer> mapCustNbVisits = new HashMap<>();
		int nodeID, sizeRoute, sizeInstance;
		Integer key;
		sizeRoute = solution.size();
		sizeInstance = instance.size();
		for (int i = 0; i < sizeRoute; i++) {
			nodeID = solution.get(i);
			if (nodeID < 0 || nodeID >= sizeInstance) {
				if (output) {
					System.out.println("Node " + nodeID + " does not belong to the instance");
				}
				return false;
			}
			key = mapCustNbVisits.get(nodeID);
			if (key == null) {
				mapCustNbVisits.put(nodeID, 1);
			} else {
				mapCustNbVisits.put(nodeID, key.intValue() + 1);
			}
		}
		for (int i = 0; i < sizeInstance; i++) {
			key = mapCustNbVisits.get(i);
			if (key == null) {
				if (output) {
					System.out.println("Node " + i + " is not visited in the solution");
				}
				return false;
			} else {
				nbVisits = key.intValue();
				if (nbVisits > 1) {
					if (output) {
						System.out.println("Node " + i + " is visited " + nbVisits + " times");
					}
					return false;
				}
			}
		}
		return true;
	}

}
