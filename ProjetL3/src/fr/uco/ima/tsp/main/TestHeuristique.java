package fr.uco.ima.tsp.main;

import fr.uco.ima.tsp.checker.TSPConstraintChecker;
import fr.uco.ima.tsp.checker.TSPConstraintCheckerFactory;
import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPInstanceFactory;
import fr.uco.ima.tsp.data.TSPInstanceFactory.InstanceName;
import fr.uco.ima.tsp.data.TSPSolution;
import fr.uco.ima.tsp.evaluator.TSPEvaluator;
import fr.uco.ima.tsp.evaluator.TSPEvaluatorFactory;
import fr.uco.ima.tsp.io.TSPSolutionWriter;
import fr.uco.ima.tsp.io.TSPViewer;
import fr.uco.ima.tsp.io.TSPViewer.ViewerType;
import fr.uco.ima.tsp.solver.TSPSolverI;
import fr.uco.ima.tsp.solver.ls.ClarkandWrightHeuristic;
import fr.uco.ima.tsp.solver.ls.HeuristicCercle;
import fr.uco.ima.tsp.solver.ls.HeuristicFarthestInsertionOne;
import fr.uco.ima.tsp.solver.milp.TSPSolverMILP;
import gurobi.GRBException;

public class TestHeuristique {

	public static void main(String[] args) {
		// Construire l'instance
		TSPInstance instance = TSPInstanceFactory.getInstance(InstanceName.wi29);

		// Créer l'objet responsable d'écrire les fichiers de visulalisation des
		// instances et des solutions
		TSPViewer viewer = new TSPViewer();

		// Ecrire un fichier HTML pour visualiser l'instance
		// viewer.write(instance, "./display/instances/" + instance.getName() +
		// ".html",
		// ViewerType.LARGE);

		// Créer le vérificateur de solution
		TSPConstraintChecker checker = TSPConstraintCheckerFactory.newConstraintChecker();
		// Créer le calculeur de fonction objectif
		TSPEvaluator evaluator = TSPEvaluatorFactory.newEvaluator();

		
		
		TSPSolverI solverH = new ClarkandWrightHeuristic();
		 TSPSolution solution = solverH.solve(instance);
		 boolean feasible = checker.checkConstraints(solution, instance,
		 true);
		 int evaluation = evaluator.getEvaluation(solution, instance);
		 System.out.println("Feasible = " + feasible);
		 System.out.println("Evaluation = " + evaluation);

		if (feasible) {
			// Ecrire la solution dans un fichier XML
			TSPSolutionWriter writer = new TSPSolutionWriter();
			writer.write(instance, solution, "./solutions/solution_" + instance.getName() + ".xml", "HeuristiqueFI1BaronBuron");

			// Ecrire un fichier HTML pour visualiser l'instance
			viewer.write(solution, instance, "./display/solutions/" + instance.getName() + "_sol" + ".html",
					ViewerType.NORMAL);

		}


	}

}
