package fr.uco.ima.tsp.solver.ls;

import java.util.ArrayList;
import java.util.Random;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

public class Swap implements NeighborhoodI{

	@Override
	public TSPSolution explore(TSPInstance instance, TSPSolution s, ExplorationStrategy strategy) {

		TSPSolution solution;
		int i,j;
		Random alea = new Random();
		double deltaMin=Double.MAX_VALUE,delta;
		int bestI=-1,bestJ=-1;
		
			
		if(strategy.equals(ExplorationStrategy.RANDOM)){
			i=alea.nextInt();
			j=alea.nextInt();
			deltaMin = getEval(i,j,s,instance);
		}else{
			for(i=0; i<s.size(); i++){
				for(j=0; j<s.size(); j++){
					if(i!=j){
						delta = getEval(i,j,s,instance);
						if(delta<deltaMin){
							deltaMin=delta;
							bestI=i;
							bestJ=j;
						}
					}
				}if(deltaMin<0 && strategy.equals(ExplorationStrategy.FIRST_IMPROVEMENT))
					break;
				
			}
		}
		
		if(strategy.equals(ExplorationStrategy.BEST))
			solution = BuildSol(bestI, bestJ, s, deltaMin);
		else{
			if(deltaMin<0)
				solution = BuildSol(bestI,bestJ,s,deltaMin);
			else
				solution = s;
		}
		
		
		return solution;
	}
	
	/**
	 * Méthode permettant de construire la liste de sommets pour créer un nouvel 
	 * objet TSPSolution
	 * @param i 1er sommet sélectionné pour effectuer la permutation
	 * @param j 2eme sommet sélectionné pour effectuer la permutation
	 * @param s la solution initiale à partir de laquelle on construit une nouvelle solution
	 * @param delta la différence de coût entre la solution s et la nouvelle
	 * @return newSolution
	 */
	public TSPSolution BuildSol(int i,int j, TSPSolution s, double delta){
		ArrayList<Integer> newPermutation = new ArrayList<>();
		
		if(i<j)
			newPermutation = swap(s,i,j);
		else
			newPermutation = swap(s,j,i);
		
		TSPSolution newSolution = new TSPSolution(newPermutation);
		double newOf = s.getOF()+delta;
		newSolution.setOF(newOf);
		return newSolution;
	}
	
	/**
	 * Méthode permettant d'évaluer le coût en cas de permutation entre i et j
	 * @param i 1er sommet sélectionné pour effectuer la permutation
	 * @param j 2eme sommet sélectionné pour effectuer la permutation
	 * @param s solution sur laquelle on calcul le cout
	 * @param instance objet TSPInstance qui nous permet d'avoir la distance entre les sommets
	 * @return delta=cout-saving la différence entre le nouveau coût et l'ancien
	 */
	public double getEval(int i, int j, TSPSolution s, TSPInstance instance){
		int iN1;
		int iN2 = s.get(i);
		int iN3;
		int jN1;
		int jN2 = s.get(j);
		int jN3;
		
		double saving, cout;
		
		if(i==0){
			iN1=s.get(s.size()-1);
			iN3=s.get(i+1);
		}else if(i==s.size()-1){
			iN1=s.get(i-1);
			iN3=s.get(0);
		}else{
			iN1=s.get(i-1);
			iN3=s.get(i+1);
		}
		
		if(j==0){
			jN1=s.get(s.size()-1);
			jN3=s.get(j+1);
		}else if(j==s.size()-1){
			jN1=s.get(j-1);
			jN3=s.get(0);
		}else{
			jN1=s.get(j-1);
			jN3=s.get(j+1);
		}
		
		if(j==i+1 || (j==0 && i==s.size()-1)){
			saving = instance.getDistance(iN1, iN2)+instance.getDistance(iN2, iN3)+instance.getDistance(jN2, jN3);
			cout=instance.getDistance(iN1, jN2)+instance.getDistance(jN2, iN2)+instance.getDistance(iN2, jN3);
		}else if(j==i-1 || (j==s.size()-1 && i==0)){
			saving = instance.getDistance(iN1, iN2)+instance.getDistance(iN2, iN3)+instance.getDistance(jN1, jN2);
			cout = instance.getDistance(jN2, iN3)+instance.getDistance(jN1, iN2)+instance.getDistance(iN2, jN2);
		}else{
			saving = instance.getDistance(iN1, iN2)+instance.getDistance(iN2, iN3)+instance.getDistance(jN1, jN2)+instance.getDistance(jN2, jN3);
			cout = instance.getDistance(iN1, jN2)+instance.getDistance(jN2, iN3)+instance.getDistance(jN1, iN2)+instance.getDistance(iN2, jN3);
		}
		return cout-saving;
		
	}
	
	/**
	 * Méthode permutant le Ième et Jème sommet d'une solution s avec i<j
	 * @param s 
	 * @param i
	 * @param j
	 * @return une liste qu'on utilisera lors de la création de la solution dans BuildSol
	 */
	private ArrayList<Integer> swap(TSPSolution s, int i, int j){
		ArrayList<Integer> list = new ArrayList<>();
		int l;
		for(l=0; l<i; l++)
			list.add(s.get(l));
		list.add(s.get(j));
		for(l=i+1; l<j; l++)
			list.add(s.get(l));
		list.add(s.get(i));
		for(l=j+1; l<s.size(); l++)
			list.add(s.get(l));
		return list;
	}

}
