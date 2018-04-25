package fr.uco.ima.tsp.solver.milp;

import java.io.IOException;
import java.util.ArrayList;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;
import fr.uco.ima.tsp.solver.TSPSolverI;
import gurobi.GRB;
import gurobi.GRB.DoubleAttr;
import gurobi.GRB.DoubleParam;
import gurobi.GRB.IntAttr;
import gurobi.GRB.IntParam;
import gurobi.GRB.StringAttr;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

/**
 * The class <code>TSPSolverMILP</code> solves an mixed integer linear
 * programming model for the TSP
 * 
 * @author froger
 *
 */
public class TSPSolverMILP implements TSPSolverI {

	/** The gurobi solver */
	private final GRBEnv mGRBEnv;
	private GRBModel mGRBModel;

	/** The instance */
	private TSPInstance mInstance;

	/** The variables */
	private GRBVar[][] x;
	private int n;
	private GRBVar[] pi;
	private int[][] dij;

	/**
	 * Constructor
	 * 
	 * @param output
	 *            true if it shows the GUROBI display in the console
	 * @param timeLimit
	 *            time limit (in secondes)
	 * @throws GRBException
	 */
	public TSPSolverMILP(boolean output, int timeLimit) throws GRBException {
		// Création de l'environnement Gurobi
		mGRBEnv = new GRBEnv();
		if (output) {
			mGRBEnv.set(IntParam.OutputFlag, 1);
		} else {
			mGRBEnv.set(IntParam.OutputFlag, 0);
		}
		mGRBEnv.set(DoubleParam.TimeLimit, timeLimit);

	}

	/**
	 * Solves the MIP model.
	 * 
	 * @throws GRBException
	 */
	@Override
	public TSPSolution solve(TSPInstance instance) {
		this.mInstance = instance;
		dij = this.mInstance.getDistanceMatrix();
		n = this.mInstance.size();
		try {
			// 1 - create the model (variables, constraints, objective function)
			this.createModel();

			// TODO
			// 2 - optimize the model (launch the solver)
			this.mGRBModel.optimize();
			// Récupérer la valeur de la fonction objectif
			double of = this.mGRBModel.get(DoubleAttr.ObjVal);
			// Récupérer la valeur du gap (différence relative entre la
			// meilleure solution trouvée et la meilleure borne calculée)
			double gap = this.mGRBModel.get(DoubleAttr.MIPGap);
			// Récupérer le temps de résolution (en secondes)
			double time = this.mGRBModel.get(DoubleAttr.Runtime);
			// Récupérer le nombre de solution trouvée
			int n = this.mGRBModel.get(IntAttr.SolCount);

			// 3 - build the solution (from the values of the variables)
			return computeSolution();
		} catch (GRBException e) {
			e.printStackTrace();
			throw new IllegalStateException("Error when solving " + instance.getName());
		}
	}

	private void createModel() throws GRBException {
		// 1 - Create the Gurobi model
		this.mGRBModel = new GRBModel(this.mGRBEnv);

		// 2 - Create the variables
		createVariables();

		// 3 - Create the variables
		createConstraints();

		// 4 - Create the objective function
		createObjectiveFunction();
	}

	private void createVariables() throws GRBException {
		// TODO
		// "model.addVar(borne inférieure, borne supérieure, coefficient dans la
		// fonction objectif, type de variable, nom de la variable);"

		// // Créer une variable binaire x dont le coefficient associé dans la
		// fonction objectif est 10
		// x = mGRBModel.addVar(0, 1, 10, GRB.BINARY, "x");
		// // Créer une variable entière y prenant des valeurs entre 2 et 5 et
		// n’apparaissant pas dans la fonction objectif
		// y = mGRBModel.addVar(2, 5, 0, GRB.INTEGER, "y");
		// // Créer une variable cotninue z prenant des valeurs entre 0 et 10 et
		// n’apparaissant pas dans la fonction objectif
		// z = mGRBModel.addVar(0, 10, 0, GRB.CONTINUOUS, "z");

		x = new GRBVar[n][n];
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				x[i][j] = this.mGRBModel.addVar(0, 1, 0, GRB.BINARY, "x" + i + "." + j);
			}
		}
		pi = new GRBVar[n];
		for (int i = 0; i < n; i++) {
			pi[i] = this.mGRBModel.addVar(0, n - 1, 0, GRB.INTEGER, "p" + i);
		}

	}

	private void createConstraints() throws GRBException {
		// TODO
		// // 4 - Créer les contraintes et les ajouter au modèle
		// //"model.addConstr(partie gauche, type de contrainte, partie droite
		// (constante), nom de la contrainte);"
		// // Créer la contrainte x-y+2=1 (appelée "C1")
		// GRBLinExpr expr = new GRBLinExpr();
		// expr.addTerm(1.0,x);
		// expr.addTerm(-1.0,y);
		// expr.addConstant(2);
		// mGRBModel.addConstr(expr, GRB.EQUAL, 1, "C1");
		// // Créer la contrainte 2x-z>=0 (appelée "C2")
		// GRBLinExpr expr2 = new GRBLinExpr();
		// expr.addTerm(2.0,x);
		// expr.addTerm(-1.0,z);
		// mGRBModel.addConstr(expr2, GRB.GREATER_EQUAL, 0, "C2");
		// // Créer la contrainte y-z<=1 (appelée "C3")
		// GRBLinExpr expr3 = new GRBLinExpr();
		// expr.addTerm(1,y);
		// expr.addTerm(-1,z);
		// mGRBModel.addConstr(expr3, GRB.LESS_EQUAL, 1, "C3");

		// Contrainte pour avoir un predecesseur et un successeur
		for (int i = 0; i < n; i++) {
			GRBLinExpr visitApres = new GRBLinExpr();
			for (int j = 0; j < n; j++) {
				if (i != j)
					visitApres.addTerm(1.0, x[i][j]);
			}
			this.mGRBModel.addConstr(visitApres, GRB.EQUAL, 1, "Visité Apres");
		}
		for (int j = 0; j < n; j++) {
			GRBLinExpr visitAvant = new GRBLinExpr();
			for (int i = 0; i < n; i++) {
				if (i != j)
					visitAvant.addTerm(1.0, x[i][j]);
			}
			this.mGRBModel.addConstr(visitAvant, GRB.EQUAL, 1, "Visité Avant");
		}

		// Contrainte sur le nombre d'arc du graphe
		GRBLinExpr nbArc = new GRBLinExpr();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j)
					nbArc.addTerm(1.0, x[i][j]);
			}
		}
		this.mGRBModel.addConstr(nbArc, GRB.EQUAL, n, "Nombre d'arc");

		// Contrainte bizarre
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < n; j++) {
				GRBLinExpr expr = new GRBLinExpr();
				expr.addTerm(1.0, pi[i]);
				expr.addConstant(1 - n);
				expr.addTerm(n, x[i][j]);
				this.mGRBModel.addConstr(expr, GRB.LESS_EQUAL, pi[j], "contrainte");
			}
		}

	}

	private void createObjectiveFunction() throws GRBException {
		// TODO
		// // Créer l’objectif Minimiser
		// GRBLinExpr expr = new GRBLinExpr();
		// expr.addTerm(2.0,x);
		// expr.addTerm(3.0,y);
		// expr.addConstant(5);
		// model.setObjective(expr, GRB.MINIMIZE);

		GRBLinExpr obj = new GRBLinExpr();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				obj.addTerm(dij[i][j], x[i][j]);
			}
		}
		this.mGRBModel.setObjective(obj, GRB.MINIMIZE);
	}

	private TSPSolution computeSolution() {

		// 7 - Récupérer la valeur de la meilleure solution trouvée
		ArrayList<Integer> pValue = new ArrayList<Integer>();
		int[] p = new int[n];
		try {
		if (n > 0) {
			// Récupérer la valeur
			for (int i = 0; i < n; i++) {
				p[(int) pi[i].get(DoubleAttr.X)] = i;
			}
			for(int i=0; i<n ; i++){
				pValue.add(p[i]);	
			}
		}
		} catch (GRBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new TSPSolution(pValue);

	}

	public void exportModelInLPFormat(String fileName) {
		try {
			GRBModelWriter modelWriter = new GRBModelWriter("./lp/", mInstance.getName());
			modelWriter.writeLP(mGRBModel, fileName);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Cannot export the model in the LP file -> " + fileName);
		}
	}

	/**
	 * Ends the solver.
	 * 
	 * @throws GRBException
	 */
	public void end() {
		mGRBModel.dispose();
		try {
			mGRBEnv.dispose();
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	public double getSolveTime() {
		try {
			return mGRBModel.get(DoubleAttr.Runtime);
		} catch (GRBException e) {
			e.printStackTrace();
			throw new IllegalStateException("Can't get the solve time");
		}
	}

	public double getObjectiveValue() {
		try {
			return mGRBModel.get(DoubleAttr.ObjVal);
		} catch (GRBException e) {
			e.printStackTrace();
			throw new IllegalStateException("Can't get objective value");
		}
	}

	public double getMIPGap() {
		try {
			return mGRBModel.get(DoubleAttr.MIPGap);
		} catch (GRBException e) {
			e.printStackTrace();
			throw new IllegalStateException("Can't get MIP gap");
		}
	}

	public int getNbVariables() {
		try {
			return mGRBModel.get(IntAttr.NumVars);
		} catch (GRBException e) {
			e.printStackTrace();
			throw new IllegalStateException("Can't get the number of variables");
		}
	}

	public int getNbConstraints() {
		try {
			return mGRBModel.get(IntAttr.NumConstrs);
		} catch (GRBException e) {
			e.printStackTrace();
			throw new IllegalStateException("Can't get the number of constraints");
		}
	}

	public int getSolCount() {
		try {
			return mGRBModel.get(IntAttr.SolCount);
		} catch (GRBException e) {
			e.printStackTrace();
			throw new IllegalStateException("Can't get solution count");
		}
	}

	public SolverStatus getSolverStatus() {
		try {
			int solverStatusInt = mGRBModel.get(GRB.IntAttr.Status);
			SolverStatus solverStatus = GRBUtilities.convertGurobiStatus(solverStatusInt);
			return solverStatus;
		} catch (GRBException e) {
			e.printStackTrace();
			throw new IllegalStateException("Can't get solver status");
		}
	}

	/**
	 * Prints the value of all variables.
	 */
	public void printVariables() {
		try {
			for (GRBVar var : mGRBModel.getVars()) {
				if (var.get(DoubleAttr.X) > 0.1) {
					System.out.println(var.get(StringAttr.VarName) + "\t" + var.get(DoubleAttr.X));
				}
			}

		} catch (GRBException e) {
			e.printStackTrace();
		}

	}

}
