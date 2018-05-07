package fr.uco.ima.tsp.solver.ls;

import fr.uco.ima.tsp.data.TSPSolution;

/**
 * Implémentation d'un algorithme de descente
 * 
 *
 */
public class Descent1 extends LocalSearchA {
	/**
	 * La liste des voisinages
	 */
	private final NeighborhoodI neighborhood;

	/**
	 * Construit un algorithme de descente
	 * 
	 * @param neighborhood
	 *            the neighborhood in position 1 of the list
	 */
	public Descent1(NeighborhoodI neighborhood) {
		super();
		this.neighborhood = neighborhood;
	}

	@Override
	protected TSPSolution run() {
		TSPSolution best = initSol;
		TSPSolution s;
		while (!stop()) {
			// TODO
			// look for the solution of the neighborhood
			s = neighborhood.explore(instance, best.clone(), strategy);
			if (s.getOF() < best.getOF()) {
				System.out.println("[INFO] Nouvelle meilleure solution calculée : " + s.getOF());
				best = s;
			} else {
				System.out.println("[INFO] Nouvelle solution calculée : " + s.getOF());
				// we can stop since there has been no improvement
				break;
			}
			nbIterations++;
		}
		return best;
	}

}
