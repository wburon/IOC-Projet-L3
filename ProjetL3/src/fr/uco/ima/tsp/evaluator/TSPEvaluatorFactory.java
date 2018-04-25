package fr.uco.ima.tsp.evaluator;

/**
 * Class responsible to build the solution evaluator for the TSP
 * 
 * @author froger
 *
 */
public class TSPEvaluatorFactory {

	public static TSPEvaluator newEvaluator() {
		return new TSPEvaluator();
	}

}
