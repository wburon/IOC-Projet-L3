package fr.uco.ima.tsp.checker;

/**
 * Class responsible to generate a constraint checker object for the TSP
 * 
 * @author froger
 *
 */
public class TSPConstraintCheckerFactory {

	/**
	 * Generates a constraint checker object for the TSP
	 * 
	 * @return
	 */
	public static TSPConstraintChecker newConstraintChecker() {
		TSPConstraintChecker ch = new TSPConstraintChecker();
		ch.addConstraint(new TSPConstraint());
		return ch;
	}

}
