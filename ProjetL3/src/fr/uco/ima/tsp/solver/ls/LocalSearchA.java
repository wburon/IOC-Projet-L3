package fr.uco.ima.tsp.solver.ls;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;
import fr.uco.ima.tsp.utilities.Timer;

/**
 * Defines the interface of local search procedures
 * 
 */
public abstract class LocalSearchA {

	/**
	 * The instance
	 */
	protected TSPInstance instance;
	/**
	 * The initial solution
	 */
	protected TSPSolution initSol;
	/**
	 * The exploration strategy
	 */
	protected ExplorationStrategy strategy;

	/**
	 * The algorithm's running time
	 */
	protected Timer timer = null;
	/**
	 * Maximum running time. The default value is {@link Long#MAX_VALUE}.
	 */
	protected long maxRunTime = Long.MAX_VALUE;
	/**
	 * Maximum number of iterations. The default value is
	 * {@link Integer#MAX_VALUE}.
	 */
	protected int maxIterations = Integer.MAX_VALUE;
	/**
	 * Current number of iterations
	 */
	protected int nbIterations = 0;

	public LocalSearchA() {
		super();
		this.timer = new Timer();
	}

	/**
	 * Sets the time limit
	 * 
	 * @param t
	 *            the new time limit (in seconds)
	 */
	public void setMaxRunTime(int t) {
		timer.setTimeOut(t);
	}

	/**
	 * Sets the maximum number of iterations
	 * 
	 * @param n
	 *            the new maximum number of iterations.
	 */
	public void setMaxNbIterations(int n) {
		maxIterations = n;
	}

	/**
	 * Checks if the algorithm needs to stop according to the current number of
	 * iterations and the solution time (in comparison to the maximum number of
	 * iterations and the maximum runtime)
	 * 
	 * @return <code>true</code> if the algorithm needs to stop,
	 *         <code>false</code> otherwise.
	 */
	public boolean stop() {
		return nbIterations > maxIterations || timer.hasTimedOut();
	}

	/**
	 * Runs the local search procedure and returns a local optimum. Implementing
	 * classes must ensure that the returned local optimum is evaluated (i.e.,
	 * its objective function is computed)
	 * 
	 * @param s
	 *            the starting solution
	 * @param strategy
	 *            the exploration strategy
	 * @return a local optimum
	 */
	public TSPSolution run(TSPInstance instance, TSPSolution s, ExplorationStrategy strategy) {
		this.instance = instance;
		this.initSol = s;
		this.strategy = strategy;
		this.nbIterations = 0;
		timer.reset();
		timer.start();
		TSPSolution best = run();
		timer.stop();
		return best;
	}

	/**
	 * Launch the algorithm for the instance defined as attribute and from the
	 * solution initSol defined as attribute
	 * 
	 * @return
	 */
	protected abstract TSPSolution run();

	/**
	 * Returns the number of iterations
	 * 
	 * @return
	 */
	public int getNbIterations() {
		return nbIterations;
	}

	/**
	 * Returns the solution time (in milliseconds)
	 * 
	 * @return
	 */
	public int getRuntime() {
		return (int) timer.readTimeMS();
	}
}
