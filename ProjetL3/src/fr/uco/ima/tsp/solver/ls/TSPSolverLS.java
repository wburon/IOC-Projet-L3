package fr.uco.ima.tsp.solver.ls;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;
import fr.uco.ima.tsp.evaluator.TSPEvaluator;
import fr.uco.ima.tsp.solver.TSPSolverI;

public class TSPSolverLS implements TSPSolverI {

	private final LocalSearchA localSearch;
	private final SolutionGeneratorI solGenerator;
	private final ExplorationStrategy strategy;
	private final TSPEvaluator evaluator;

	public TSPSolverLS(LocalSearchA localSearch, SolutionGeneratorI solGenerator, ExplorationStrategy strategy,
			TSPEvaluator evaluator) {
		super();
		this.localSearch = localSearch;
		this.solGenerator = solGenerator;
		this.strategy = strategy;
		this.evaluator = evaluator;
	}

	@Override
	public TSPSolution solve(TSPInstance instance) {
		// TODO Auto-generated method stub
		TSPSolution initSolution = solGenerator.generateSolution(instance);
		// Compute the value of the objective function for the initial solution
		double of = evaluator.getEvaluation(initSolution, instance);
		// Set the value of the objective function for the initial solution
		initSolution.setOF(of);
		System.out.println("[INFO] Solution initiale calculée : " + of);
		// launch the local search
		return localSearch.run(instance, initSolution, strategy);
	}

}
