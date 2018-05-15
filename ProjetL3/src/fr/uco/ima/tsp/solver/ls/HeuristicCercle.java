package fr.uco.ima.tsp.solver.ls;

import java.util.ArrayList;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;
import fr.uco.ima.tsp.solver.TSPSolverI;

public class HeuristicCercle implements SolutionGeneratorI, TSPSolverI {

	private int[][] distance;
	private int n;
	private int po;

	private TSPInstance instance;
	private Sommet[] tabSommet;
	private ArrayList<Integer> listParcours;
	private int p3;

	@Override
	public TSPSolution solve(TSPInstance instance) {
		this.instance = instance;
		this.distance = instance.getDistanceMatrix();
		this.n = instance.size();
		this.po = barycentrePlus();

		p3 = calculP3();
		tabSommet = calculTabSommet();
		double[] tabAngle = getTabAngle();
		listParcours = triListe(tabAngle);

		return buildSolution(instance);
	}

	/**
	 * Determine le point le plus au centre du nuage
	 * @return le point le plus au centre
	 */
	private int barycentrePlus() {
		// TODO Auto-generated method stub
		int sommeX = 0, sommeY = 0;
		for (int i = 0; i < n; i++) {
			sommeX += instance.getX(i);
			sommeY += instance.getY(i);
		}
		double moyX = sommeX / n;
		double moyY = sommeY / n;

		double[] dBary = new double[n];
		double min = Double.MAX_VALUE;
		int index = 0;
		for (int i = 0; i < n; i++) {
			dBary[i] = Math.sqrt((Math.pow((instance.getX(i) - moyX), 2) + Math.pow((instance.getY(i) - moyY), 2)));
			if (min > dBary[i]) {
				min = dBary[i];
				index = i;
			}
		}
		return index;

	}

	/**
	 * Tri la liste des angles pour les ordonner dans l'ordre croissant
	 * 
	 * @param tabAngle
	 * @return liste des indices à parcourir
	 */
	private ArrayList<Integer> triListe(double[] tabAngle) {
		ArrayList<Double> listAngle = new ArrayList<Double>();
		ArrayList<Integer> listParcours = new ArrayList<Integer>();

		for (int i = 0; i < n; i++)
			listAngle.add(tabAngle[i]);

		while (listParcours.size() != n - 1) {
			int minIndex = minList(listAngle);
			listParcours.add(minIndex);
			listAngle.set(minIndex, Double.MAX_VALUE);
		}
		return listParcours;
	}

	/**
	 * recherche l'indice du minimum d'une ArrayList
	 * 
	 * @param l
	 *            Liste dont on recherche le minimum
	 * @return indice du minimum
	 */
	private int minList(ArrayList<Double> l) {
		int index = 0;
		double min = Double.MAX_VALUE;

		for (int i = 0; i < l.size(); i++) {
			if (min > l.get(i)) {
				min = l.get(i);
				index = i;
			}
		}
		return index;
	}

	/**
	 * Création tableau des sommets
	 * 
	 * @return r tableau des sommets
	 */
	private Sommet[] calculTabSommet() {
		Sommet[] r = new Sommet[n];
		for (int i = 0; i < n; i++) {
			r[i] = new Sommet(instance.getX(i), instance.getY(i));
		}
		return r;
	}

	/**
	 * Renvoie le sommet le plus proche de l'origine
	 * 
	 * @return Sommet k
	 */
	private int calculP3() {

		int min = Integer.MAX_VALUE;
		int k = 0;
		for (int j = 0; j < n; j++) {
			if (distance[po][j] < min && po != j) {
				min = distance[po][j];
				k = j;
			}
		}
		return k;
	}

	/**
	 * Création du tableau de tous les angles par rapport à p3 et po
	 * 
	 * @param p3
	 *            point le plus proche
	 * @return tableau des angles
	 */
	private double[] getTabAngle() {
		double[] tabAngle = new double[n];
		
		for (int i = 0; i < n; i++) {
//				i != p3
			if (i != po) {
//				tabAngle[i] = calculAngle(i, p3);
				tabAngle[i] = calculAngleRadian(i, p3);
			} else
				tabAngle[i] = Double.MAX_VALUE;
		}

		return tabAngle;
	}

	/**
	 * calcul d'un angle 0-i-p3
	 * 
	 * @param i
	 *            indice du sommet quelconque
	 * @param p3
	 *            point le plus proche
	 * @return un angle en double
	 */
	private double calculAngle(int i, int p3) {
		int a = distance[po][p3];
		int b = distance[po][i];
		int c = distance[p3][i];

		double x = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b);
		double angle = Math.acos(x);

		return angle;
	}
	
	private double calculAngleRadian(int i, int p3){
		double deltaX = instance.getX(po)-instance.getX(i);
		double deltaY = instance.getY(po)-instance.getY(i);
		double AngRad = Math.atan2(deltaX, deltaY);
		double deg = AngRad*(180/Math.PI);
		return deg;
	}

	@Override
	public TSPSolution generateSolution(TSPInstance instance) {
		return this.solve(instance);
	}
	
	private TSPSolution buildSolution(TSPInstance instance2) {
		ArrayList<Integer> solution = new ArrayList<Integer>();
		solution.add(po);
//		solution.add(p3);
		for (int o : this.listParcours) {
			solution.add(o);
		}
		return new TSPSolution(solution);
	}

	public class Sommet {

		private double x, y;

		public Sommet(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

	}

}
