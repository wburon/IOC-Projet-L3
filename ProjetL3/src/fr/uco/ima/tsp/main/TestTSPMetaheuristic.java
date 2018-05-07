package fr.uco.ima.tsp.main;

import fr.uco.ima.tsp.checker.TSPConstraintChecker;
import fr.uco.ima.tsp.checker.TSPConstraintCheckerFactory;
import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPInstanceFactory;
import fr.uco.ima.tsp.data.TSPInstanceFactory.InstanceName;
import fr.uco.ima.tsp.data.TSPSolution;
import fr.uco.ima.tsp.evaluator.TSPEvaluator;
import fr.uco.ima.tsp.evaluator.TSPEvaluatorFactory;
import fr.uco.ima.tsp.io.TSPViewer;
import fr.uco.ima.tsp.solver.TSPSolverI;
import fr.uco.ima.tsp.solver.ls.ClarkandWrightHeuristic;
import fr.uco.ima.tsp.solver.ls.Descent1;
import fr.uco.ima.tsp.solver.ls.ExplorationStrategy;
import fr.uco.ima.tsp.solver.ls.HeuristicCercle;
import fr.uco.ima.tsp.solver.ls.NeighborhoodI;
import fr.uco.ima.tsp.solver.ls.RechercheTabou;
import fr.uco.ima.tsp.solver.ls.Relocate;
import fr.uco.ima.tsp.solver.ls.TSPSolverLS;
import fr.uco.ima.tsp.solver.ls.ThreeOpt;

public class TestTSPMetaheuristic {

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

		// Tester la métaheuristique
		// Créer le voisinage
		NeighborhoodI n = new Relocate();
		// Créer l'algorithme de descente
//		Descent1 descent = new Descent1(n);
		RechercheTabou tabou = new RechercheTabou(n);
		// Créer la métaheuristique
		TSPSolverI solverLS = new TSPSolverLS(tabou, new ClarkandWrightHeuristic(),
				ExplorationStrategy.FIRST_IMPROVEMENT, evaluator);
		TSPSolution solution = solverLS.solve(instance);
		boolean feasible = checker.checkConstraints(solution, instance, true);
		int evaluation = evaluator.getEvaluation(solution, instance);
		System.out.println("Feasible = " + feasible);
		System.out.println("Evaluation = " + evaluation);

	}

}
