package fr.uco.ima.tsp.solver.milp;

/**
 * The Enumeration <code>SolverStatus</code> represents all the solver status.
 * 
 * @author froger
 * 
 */
public enum SolverStatus {
	LOADED, OPTIMAL, INFEASIBLE, INF_OR_UNBD, UNBOUNDED, ITERATION_LIMIT, NODE_LIMIT, TIME_LIMIT, SOLUTION_LIMIT, GAP_LIMIT, INTERRUPTED, NUMERIC, FEASIBLE, ERROR, UNKNOWN_STATUS
}
