package fr.uco.ima.tsp.data;

import java.util.ArrayList;

/**
 * Concrete implementation of a solution represented by a single permutation of
 * integers. This permutation becomes handy to represent solutions to the
 * traveling salesman problem
 * 
 */
public class TSPSolution {

	private ArrayList<Integer> permutation;
	/**
	 * The objective function
	 */
	private double of = Double.NaN;

	public TSPSolution(ArrayList<Integer> p) {
		permutation = p;
	}

	@Override
	public TSPSolution clone() {
		TSPSolution clone = new TSPSolution(permutation);
		clone.of = this.of;
		return clone;
	}

	public double getOF() {
		return this.of;
	}

	public void setOF(double of) {
		this.of = of;
	}

	public int size() {
		return this.permutation.size();
	}

	public int get(int i) {
		return this.permutation.get(i);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("OF=" + this.of);
		s.append("\n");
		s.append(permutation);
		return s.toString();
	}

	public void print() {
		System.out.println(this);
	}

}
