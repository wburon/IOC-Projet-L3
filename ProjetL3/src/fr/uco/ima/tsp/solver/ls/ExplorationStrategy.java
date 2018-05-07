package fr.uco.ima.tsp.solver.ls;

/**
 * Enumerates the neighborhood exploration strategies
 * 
 */
public enum ExplorationStrategy {
	/**
	 * Neighborhoods are explored using a fist improvement strategy (that is,
	 * the exploration ends as soon as an improving solution is found)
	 */
	FIRST_IMPROVEMENT,
	/**
	 * Neighborhoods are explored using a best improvement strategy (that is,
	 * the exploration returns the best improving neighbor solution)
	 */
	BEST_IMPROVEMENT,
	/**
	 * Neighborhoods are explored using a best strategy (that is, the
	 * exploration returns the best neighbor solution)
	 */
	BEST,
	/**
	 * A solution of the neighborhood is randomly drawn.
	 */
	RANDOM;

}
