package fr.uco.ima.tsp.solver.ls;

import java.util.ArrayList;
import java.util.Random;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

public class Swap implements NeighborhoodI{

	@Override
	public TSPSolution explore(TSPInstance instance, TSPSolution s, ExplorationStrategy strategy) {

		int i,j;
		Random alea = new Random();
		double deltaMin=Double.MAX_VALUE,delta;
			
		if(strategy.equals(ExplorationStrategy.RANDOM)){
			i=alea.nextInt();
			j=alea.nextInt();
			deltaMin = getEval(i,j,s,instance);
		}else{
			for(i=0; i<s.size(); i++){
				for(j=0; j<s.size(); j++){
					delta = getEval(i,j,s,instance);
					if(delta<0 && strategy.equals(ExplorationStrategy.FIRST_IMPROVEMENT))
						break;
				}
			}
		}
			
		
		return null;
	}
	
	public ArrayList<Integer> BuildSol(int i,int j, TSPSolution s){
		
	}
	
	public double getEval(int i, int j, TSPSolution s, TSPInstance instance){
		int iN1;
		int iN2 = s.get(i);
		int iN3;
		int jN1;
		int jN2 = s.get(j);
		int jN3;
		
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
		
		double saving = instance.getDistance(iN1, iN2)+instance.getDistance(iN2, iN3)
			+instance.getDistance(jN1, jN2)+instance.getDistance(jN2, jN3);
		double cout = instance.getDistance(iN1, jN2)+instance.getDistance(jN2, iN3)
			+instance.getDistance(jN1, iN2)+instance.getDistance(iN2, jN3);
		
		return cout-saving;
		
	}

}
