package fr.uco.ima.tsp.solver.ls;

import java.util.ArrayList;

import fr.uco.ima.tsp.data.TSPSolution;

public class RechercheTabou extends LocalSearchA{

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
	public RechercheTabou(NeighborhoodI neighborhood) {
		super();
		this.neighborhood = neighborhood;
	}
	
	private ArrayList<TSPSolution> listeTabou;
	
	@Override
	protected TSPSolution run() {
		TSPSolution best = initSol;
		TSPSolution s;
		listeTabou = new ArrayList<>();
		while (!stop()) {
			s = findVoisinageNonTabou(best);
			if(s.getOF() < best.getOF()){
				System.out.println("[INFO] Nouvelle meilleure solution calculée : " + s.getOF());
				best = s;
				best.setOF(s.getOF());
				listeTabou.add(s);
			} else {
				System.out.println("[INFO] Nouvelle solution calculée : " + s.getOF());
				// we can stop since there has been no improvement
				break;
			}
			nbIterations++;
		}
		return best;
	}


	private TSPSolution findVoisinageNonTabou(TSPSolution best) {
		boolean a = true;
		TSPSolution s = null;
		while(a){
			s = neighborhood.explore(instance, best.clone(), strategy);
			if(!listeTabou.contains(s))
				a = false;
		}
		return s;
	}

}
