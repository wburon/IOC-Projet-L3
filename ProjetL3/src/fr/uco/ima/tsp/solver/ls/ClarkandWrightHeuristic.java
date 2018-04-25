package fr.uco.ima.tsp.solver.ls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;
import fr.uco.ima.tsp.solver.TSPSolverI;

/**
 * Defines the interface to solution generators
 * 
 *
 */
public class ClarkandWrightHeuristic implements SolutionGeneratorI, TSPSolverI {

	@Override
	public TSPSolution solve(TSPInstance instance) {
		// compute the savings
		List<Pair> listPairs = computePairs(instance);
		// sort the list according to the value of the savings
		Collections.sort(listPairs);
		// build the solution
		int nbNodes = instance.size();
		int nbSelectedPairs = 0;
		Pair selectedPair;
		int idx = 0;
		int tourI = 1, tourJ, k;

		// tour number associated with each node
		int[] tour = new int[nbNodes];
		// list of nodes visited in each tour
		LinkedList<Integer>[] tours = new LinkedList[nbNodes];
		// At the beginning, create one tour per node (except the center node)
		for (int i = 1; i < nbNodes; i++) {
			tour[i] = i;
			tours[i] = new LinkedList<>();
			tours[i].add(i);
			// successorsOfCenterNode.add(i);
		}
		// we need to select nbNodes-2 pairs
		while (nbSelectedPairs < nbNodes - 2) {
			// Search the next pair to consider (the tours associated with the
			// nodes will be merge)
			// VALID PAIR
			// 1- the first node should be the last node of a tour
			// 2- the second node should be the first node of a tour
			// 3- the two nodes should not be in the same tour
			do {
				idx++;
				selectedPair = listPairs.get(idx);
			} while (tours[tour[selectedPair.i]].getLast() != selectedPair.i
					|| tours[tour[selectedPair.j]].getFirst() != selectedPair.j
					|| tour[selectedPair.i] == tour[selectedPair.j]);
			selectedPair = listPairs.get(idx);
			// update the tours
			tourI = tour[selectedPair.i];
			tourJ = tour[selectedPair.j];
			// merge the tours
			for (int l = 0; l < tours[tourJ].size(); l++) {
				k = tours[tourJ].get(l);
				tour[k] = tourI;
				tours[tourI].addLast(k);
			}
			// remove the tour that has just been merged (set it to null)
			tours[tourJ] = null;
			// increase the number of selected pairs
			nbSelectedPairs++;
		}
		// Build the final tour
		tours[tourI].addFirst(0);
		TSPSolution solution = new TSPSolution(new ArrayList<>(tours[tourI]));
		return solution;
	}

	@Override
	public TSPSolution generateSolution(TSPInstance instance) {
		return solve(instance);
	}

	private List<Pair> computePairs(TSPInstance instance) {
		List<Pair> list = new ArrayList<>();
		int size = instance.size();
		double saving;
		for (int i = 1; i < size; i++) {
			for (int j = 1; j < size; j++) {
				if (i != j) {
					saving = instance.getDistance(i, 0) + instance.getDistance(0, j) - instance.getDistance(i, j);
					Pair pair = new Pair(i, j, saving);
					list.add(pair);
				}
			}
		}
		return list;
	}

	public class Pair implements Comparable<Pair> {

		final int i;
		final int j;
		final double saving;

		public Pair(int i, int j, double saving) {
			super();
			this.i = i;
			this.j = j;
			this.saving = saving;
		}

		@Override
		public int compareTo(Pair o) {
			return (int) Math.signum(o.saving - saving);
		}

		@Override
		public String toString() {
			return "i = " + i + " j = " + j + " saving = " + saving;
		}

	}

}
