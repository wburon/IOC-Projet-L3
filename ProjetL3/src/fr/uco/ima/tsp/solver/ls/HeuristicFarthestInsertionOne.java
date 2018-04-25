package fr.uco.ima.tsp.solver.ls;

import java.util.ArrayList;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;
import fr.uco.ima.tsp.solver.TSPSolverI;

public class HeuristicFarthestInsertionOne implements SolutionGeneratorI, TSPSolverI {

	private TSPInstance instance;

	private int n;

	private ArrayList<Integer> marque;
	private ArrayList<Integer> nonMarque;

	private Arc arcMaxMin;
	private ArrayList<Arc> ListeArcMin;

	private ArrayList<Arc> ListeArcGraphe;

	@Override
	public TSPSolution solve(TSPInstance instance) {
		// TODO Auto-generated method stub
		this.instance = instance;
		this.n = instance.size();
		marque = new ArrayList<Integer>();
		nonMarque = new ArrayList<Integer>();
		ListeArcGraphe = new ArrayList<Arc>();
		ListeArcMin = new ArrayList<Arc>();
		listNonMarque();

		int[] dmax = distanceMax();
		marque.add(dmax[0]);
		marque.add(dmax[1]);
		nonMarque.remove(nonMarque.indexOf(dmax[0]));
		nonMarque.remove(nonMarque.indexOf(dmax[1]));
		ListeArcGraphe.add(new Arc(dmax[0], dmax[1]));
		ParcoursGraphe();

		return generateSolution(instance);
	}

	/**
	 * Méthode parcourant le graphe à la recherche de sommets à insérer
	 * 
	 * @return une liste pour l'instant
	 */
	private void ParcoursGraphe() {
		while (nonMarque.size() != 0) {
			ArrayList<Double> tabDistMin = tabDistMin();
			arcMaxMin = maxDuMin(tabDistMin);
			insertion(arcMaxMin.getJ());
			ListeArcMin.clear();
		}

	}

	/**
	 * Méthode qui insère un nouveau sommet dans le graphe de sommets marqués
	 * 
	 * @param insert
	 *            le sommet à insérer
	 */
	private void insertion(int insert) {
		Arc arcPlusProche = null;
		double distanceMin = Double.MAX_VALUE;

		// Parcours la liste des arcs du graphe

		for (Arc arc : this.ListeArcGraphe) {
			int i = arc.getI();
			int j = arc.getJ();
//			double[] coordVect = calculCoordVect(i, j);
//			double a = coordVect[1];
//			double b = -coordVect[0];
//			double c = -a * instance.getX(i) - b * instance.getY(i);
//			// Distance du point insert à l'arc parcouru
//			double distance = (Math.abs(a * instance.getX(insert) + b * instance.getY(insert) + c))
//					/ (Math.sqrt(Math.pow(a, 2.0) + Math.pow(b, 2.0)));
			arcPlusProche = getAPP(insert);
			
			
		}
		
		
		if(marque.size()==2){
			Arc arcNouveau = arcPlusProche.reverse();
			ListeArcGraphe.add(arcNouveau);
		}
		ListeArcGraphe.remove(ListeArcGraphe.indexOf(arcPlusProche));
		
		ListeArcGraphe.add(new Arc(arcPlusProche.getI(), insert));
		ListeArcGraphe.add(new Arc(insert, arcPlusProche.getJ()));
		marque.add(insert);
		nonMarque.remove(nonMarque.indexOf(insert));
		
	}
	
	private Arc getAPP(int insert){
		double somme = 0;
		double min = Double.MAX_VALUE;
		Arc app=null;
		for(Arc a : ListeArcGraphe){
			somme = instance.getDistance(a.getI(), insert) + instance.getDistance(insert, a.getJ()) - instance.getDistance(a.getI(), a.getJ());
			if(somme<min){
				min=somme;
				app=a;
			}
		}
		return app;
	}

	/**
	 * Calcul des coordonnées d'un vecteur
	 * 
	 * @param sommet
	 *            i
	 * @param sommet
	 *            j
	 * @return coordonnées du vecteur (ij)
	 */
	private double[] calculCoordVect(int i, int j) {
		double[] coordVect = new double[2];
		coordVect[0] = instance.getX(j) - instance.getX(i);
		coordVect[1] = instance.getY(j) - instance.getY(i);
		return coordVect;
	}

	/**
	 * Méthode instanciant la liste nonMarque
	 */
	private void listNonMarque() {
		for (int i = 0; i < n; i++) {
			nonMarque.add(i);
		}
	}

	/**
	 * Récupère la distance maximum parmi toutes les distances minimum trouver
	 * dans tabDistMin
	 * 
	 * @param tabDistMin
	 *            le tableau des distances minimales
	 * @return l'indice du tableau minimum
	 */
	private Arc maxDuMin(ArrayList<Double> tabDistMin) {

		double max = Double.MIN_VALUE;
		int maxMin = 0;
		for (int i = 0; i < tabDistMin.size(); i++) {
			if (max < tabDistMin.get(i)) {
				max = tabDistMin.get(i);
				maxMin = i;
			}
		}

		return ListeArcMin.get(maxMin);
	}

	/**
	 * Création du tableau des distances minimum d'un sommet non marqués vers
	 * les sommets marqués
	 * 
	 * @return le dit tableau
	 */
	private ArrayList<Double> tabDistMin() {
		ArrayList<Double> tabDistMin = new ArrayList<>();
		for (int i : nonMarque) {
			tabDistMin.add(minimumTsPts(i));
		}
		return tabDistMin;
	}

	/**
	 * Recherche la distance minimum d'un point parmi toutes les distances des
	 * sommets marqués
	 * 
	 * @param a
	 * @return la distance minimum
	 */
	private double minimumTsPts(int a) {
		double minimum = Double.MAX_VALUE;
		int index = 0;
		for (int i : marque)
			if (minimum > instance.getDistance(i, a)) {
				minimum = instance.getDistance(i, a);
				index = i;
			}
		Arc t = new Arc(index, a);
		ListeArcMin.add(t);
		return minimum;
	}

	/**
	 * Revoie l'arc (i,j) dont la distance est maximale
	 * 
	 * @return arc i et j
	 */
	private int[] distanceMax() {
		int max = 0;
		int[] dmax = new int[2];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (instance.getDistance(i, j) > max && i != j) {
					max = instance.getDistance(i, j);
					dmax[0] = i;
					dmax[1] = j;
				}
			}
		}
		return dmax;
	}

	public class Arc {

		private int i;
		private int j;

		public Arc(int i, int j) {
			this.i = i;
			this.j = j;
		}

		public int getI() {
			return i;
		}

		public void setI(int i) {
			this.i = i;
		}

		public int getJ() {
			return j;
		}

		public void setJ(int j) {
			this.j = j;
		}
		
		public Arc reverse(){
			return new Arc(this.j,this.i);
		}

	}

	@Override
	public TSPSolution generateSolution(TSPInstance instance) {
		ArrayList<Integer> solution = new ArrayList<>();
		int i = ListeArcGraphe.get(0).getI();
		solution.add(i);
		int k = i;
		while (solution.size() < n) {
			solution.add(findJ(k));
			System.out.println(k + "  " + findJ(k));
			k = findJ(k);
		}

		return new TSPSolution(solution);
	}

	private Integer findJ(int k) {
		for (Arc arc : ListeArcGraphe) {
			if (arc.getI() == k)
				return arc.getJ();
		}
		return null;
	}

}
