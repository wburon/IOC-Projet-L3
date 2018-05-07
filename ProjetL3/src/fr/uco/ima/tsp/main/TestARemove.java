package fr.uco.ima.tsp.main;

import java.util.ArrayList;
import java.util.Random;

public class TestARemove {

	public static void main(String[] args){
		
		System.out.println("affichage ?");
		
		ArrayList<Integer> liste=new ArrayList<>();
		Random alea=new Random();
		
		for(int i=0; i<6; i++){
			do{
				i=alea.nextInt(6);
			}while(liste.contains(i));
				
			liste.add(i);
		}
		
		int a=4, b=2;
		
		int posA, posB;
		
		posA = liste.indexOf(a);
		posB = liste.indexOf(b);
		System.out.println(liste.toString());
		
		liste.set(posA, b);
		liste.set(posB, a);
		System.out.println(liste.toString());
		
	}
}
