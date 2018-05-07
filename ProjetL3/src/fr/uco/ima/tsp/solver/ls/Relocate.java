package fr.uco.ima.tsp.solver.ls;

import java.util.ArrayList;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

public class Relocate implements NeighborhoodI {
	
	private TSPInstance instance;
	private int n;
	private double OC;
	private int indexIinS;
	private ArrayList<Integer> listSolutionInit;
	
	


	@Override
	public TSPSolution explore(TSPInstance instance, TSPSolution s, ExplorationStrategy strategy) {
		this.instance = instance;
		this.n = this.instance.size();
		this.OC = Double.MAX_VALUE;
		
		listSolutionInit = getListSolution(s);
		ArrayList<Integer> clone;
		ArrayList<Integer> bestClone = new ArrayList<>();
		double bestDelta=0;
		
		
		for(int i=0; i<n; i++){
			indexIinS = listSolutionInit.indexOf(i);
			double cout = calculCout(indexIinS, listSolutionInit);
			clone = (ArrayList<Integer>) listSolutionInit.clone();
			clone.remove(indexIinS);
			clone = relocate(i, clone, cout);
			double delta = cout-this.OC;
			if(clone != null && this.OC<cout && delta > bestDelta){
				bestClone = clone;
				bestDelta = delta;
			}
		}
		System.out.println("best delta : "+bestDelta);
		TSPSolution solution = new TSPSolution(bestClone);
		solution.setOF(s.getOF()-bestDelta);
		return solution;
	}

	/**
	 * calcul le cout associé au sommet i dans la solution associé
	 * @param index
	 * @param listSolutionInit
	 * @return
	 */
	private double calculCout(int index, ArrayList<Integer> listSolutionInit) {
		double cout;
		if(index > 0 && index < listSolutionInit.size()-1)
			cout =  this.instance.getDistance(listSolutionInit.get(index-1),listSolutionInit.get(index)) + this.instance.getDistance(listSolutionInit.get(index), listSolutionInit.get(index+1));
		else if(index == 0)
			cout =  this.instance.getDistance(listSolutionInit.get(this.n-1),listSolutionInit.get(index)) + this.instance.getDistance(listSolutionInit.get(index), listSolutionInit.get(index+1)); 
		else 
			cout =  this.instance.getDistance(listSolutionInit.get(index-1),listSolutionInit.get(index)) + this.instance.getDistance(listSolutionInit.get(index), listSolutionInit.get(0));
		return cout;
	}
	
	private double distanceSiIBouge(int indexI){
		double distance;
		if(indexI > 0 && indexI < n)
			distance = this.instance.getDistance(this.listSolutionInit.get(indexI-1), this.listSolutionInit.get(indexI));
		else
			distance = this.instance.getDistance(this.listSolutionInit.get(n-1), this.listSolutionInit.get(0));
		return distance;
	}

	/**
	 * Ajouter le sommet i danc clone de façon a minimiser le cout et peut-être trouver un cout inferieur a c
	 * @param i
	 * @param clone
	 * @param c
	 * @return
	 */
	private ArrayList<Integer> relocate(int i, ArrayList<Integer> clone, double c) {
		ArrayList<Integer> test, testMin = new ArrayList<>();
		double cout, coutMin = Double.MAX_VALUE;
		for(int j = 0; j<n; j++){
			test = (ArrayList<Integer>) clone.clone();
			test.add(j, i);
			cout = calculCout(j, test);
			if(this.indexIinS != testMin.indexOf(i)){
				cout = cout+distanceSiIBouge(this.indexIinS);
			}
			if(cout < coutMin){
				coutMin = cout;
				testMin = test;
			}
		}
		if(coutMin < c){
			this.OC = coutMin;
			return testMin;
		}else
			return null;
	}

	/**
	 * Recupère la liste de la solution
	 * @param s : la TSPSolution
	 * @return a : la liste de sommet
	 */
	private ArrayList<Integer> getListSolution(TSPSolution s) {
		ArrayList<Integer> a = new ArrayList<>();
		for(int i=0; i<this.n; i++)
			a.add(s.get(i));
		return a;
	}
	
	

}
