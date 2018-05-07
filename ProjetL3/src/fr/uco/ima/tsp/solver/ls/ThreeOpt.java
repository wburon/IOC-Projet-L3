package fr.uco.ima.tsp.solver.ls;

import java.util.ArrayList;
import java.util.Random;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * Implements the 3-opt neighborhood
 *
 */
public class ThreeOpt implements NeighborhoodI {

	@Override
	public TSPSolution explore(TSPInstance instance, TSPSolution s, ExplorationStrategy strategy) {
		// Get the number of nodes (cities) in the instance
		int nbNodes = s.size();
		double deltaMin = Double.MAX_VALUE, delta;
		int i, j, k;
		// To store the best triplet
		int bestI = -1, bestJ = -1, bestK = -1;
		if (strategy.equals(ExplorationStrategy.RANDOM)) {
			// we generate a random move
			Random rnd = new Random();
			bestI = (int) Math.round(1 + rnd.nextDouble() * (nbNodes - 5));
			bestJ = (int) Math.round(bestI + rnd.nextDouble() * (nbNodes - 3 - bestI));
			bestK = (int) Math.round(bestJ + rnd.nextDouble() * (nbNodes - 1 - bestJ));
			deltaMin = getEval(instance, s, bestI, bestJ, bestK);
		} else {
			// Builds the iterator
			ThreeOptIterator iterator = new ThreeOptIterator(nbNodes);
			// we iterate over all the triplets (i,j,k)
			while (iterator.next()) {
				i = iterator.getI();
				j = iterator.getJ();
				k = iterator.getK();
				// evaluate the move
				delta = getEval(instance, s, i, j, k);
				if (delta < deltaMin) {
					deltaMin = delta;
					bestI = i;
					bestJ = j;
					bestK = k;
					if (delta < 0 && strategy.equals(ExplorationStrategy.FIRST_IMPROVEMENT)) {
						// stop the exploration of the neighborhood as soon as
						// an
						// improving solution has been found
						break;
					}
				}

			}
		}
		// The
		TSPSolution solution = null;
		// build the new solution only if we need to build the best solution of
		// the neighborhood or if an improving solution has been found
		if (strategy.equals(ExplorationStrategy.BEST)) {
			// we build the best solution found
			solution = buildSol(instance, s, bestI, bestJ, bestK, deltaMin);
		} else {
			// we need to build the solution if it is an improving solution
			if (deltaMin < 0) {
				// the solution is improving the initial solution s
				solution = buildSol(instance, s, bestI, bestJ, bestK, deltaMin);
			} else {
				// the solution is not improving the initial solution s (we
				// return s)
				solution = s;
			}
		}
		return solution;
	}

	private TSPSolution buildSol(TSPInstance instance, TSPSolution s, int i, int j, int k, double delta) {
		ArrayList<Integer> newPermutation = new ArrayList<>();
		// add the nodes between positions 0 and i-1
		for (int l = 0; l < i; l++) {
			newPermutation.add(s.get(l));
		}
		// add the nodes between positions j and k-1
		for (int l = j; l < k; l++) {
			newPermutation.add(s.get(l));
		}
		// add the nodes between positions i and j-1
		for (int l = i; l < j; l++) {
			newPermutation.add(s.get(l));
		}
		// add the nodes between positions k and the last position
		int nbNodes = s.size();
		for (int l = k; l < nbNodes; l++) {
			newPermutation.add(s.get(l));
		}
		// build the new solution
		TSPSolution newSolution = new TSPSolution(newPermutation);
		// compute the new OF
		double newOF = s.getOF() + delta;
		// IMPORTANT : do not forget this step
		// it is used in the local search classes
		newSolution.setOF(newOF);
		return newSolution;
	}

	/**
	 * Computes the difference on the objective function on the generating
	 * solution if a relocate move is applied to it
	 * 
	 * @param i
	 *            the extraction position
	 * @param j
	 *            the insertion position
	 * @param type
	 * @return the difference on the objective function if a relocate move is
	 *         applied to the generating solution
	 * @throws ArrayIndexOutOfBoundsException
	 *             if indices i or j are not valid indices for the permutation
	 */
	private double getEval(TSPInstance instance, TSPSolution s, int i, int j, int k) {
		if (i >= j) {
			throw new IllegalArgumentException(i + " < " + j);
		}
		// Gets the origin and the destination of the 3 arcs that should be
		// removed
		int iN1 = s.get(i - 1);
		int iN2 = s.get(i);
		int jN1 = s.get(j - 1);
		int jN2 = s.get(j);
		int kN1 = s.get(k - 1);
		int kN2;
		if (k == s.size()) {
			// If we remove the arc to return to the first node, the successor
			// is the node at position 0
			kN2 = s.get(0);
		} else {
			kN2 = s.get(k);
		}
		// the saving related to the removal of arcs (iN1, iN2), (jN1, jN2), and
		// (kN1,kN2)
		double saving = instance.getDistance(iN1, iN2) + instance.getDistance(jN1, jN2)
				+ instance.getDistance(kN1, kN2);
		// the cost related to the insertion of the arcs (iN1, jN2), (kN1,iN2)
		// and (jN1,kN2)
		double cost = instance.getDistance(iN1, jN2) + instance.getDistance(kN1, iN2) + instance.getDistance(jN1, kN2);

		// System.out.println("saving = " + saving + "\t cost = " + cost);
		// delta is equal to the cost minus the saving
		double delta = cost - saving;
		return delta;
	}

}
