package fr.uco.ima.tsp.solver.milp;

import gurobi.GRB;
import gurobi.GRB.DoubleAttr;
import gurobi.GRB.IntAttr;
import gurobi.GRB.StringAttr;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

/**
 * The class <code>GRBUtilities</code> groups useful methods when a Gurobi
 * solver is used.
 * 
 * @author froger
 * 
 */
public class GRBUtilities {

	/**
	 * Convert Gurobi solver status to {@link SolverStatus}.
	 * 
	 * @param status
	 *            the Gurobi solver status.
	 * @return the converted status.
	 */
	public static SolverStatus convertGurobiStatus(int status) {
		switch (status) {
		case GRB.LOADED:
			return SolverStatus.LOADED;
		case GRB.OPTIMAL:
			return SolverStatus.OPTIMAL;
		case GRB.INFEASIBLE:
			return SolverStatus.INFEASIBLE;
		case GRB.INF_OR_UNBD:
			return SolverStatus.INF_OR_UNBD;
		case GRB.UNBOUNDED:
			return SolverStatus.UNBOUNDED;
		case GRB.ITERATION_LIMIT:
			return SolverStatus.ITERATION_LIMIT;
		case GRB.NODE_LIMIT:
			return SolverStatus.NODE_LIMIT;
		case GRB.TIME_LIMIT:
			return SolverStatus.TIME_LIMIT;
		case GRB.SOLUTION_LIMIT:
			return SolverStatus.SOLUTION_LIMIT;
		case GRB.INTERRUPTED:
			return SolverStatus.INTERRUPTED;
		case GRB.NUMERIC:
			return SolverStatus.NUMERIC;
		default:
			return SolverStatus.UNKNOWN_STATUS;
		}
	}

	/**
	 * Gets a string with the status info associated to the given model.
	 * 
	 * @return "Status:<code>s</code> SolCount:<code>sc</code> Objective:
	 *         <code>o</code>" where
	 *         <ul>
	 *         <li><code>s</code> : solver status</li>
	 *         <li><code>sc</code> : number of solutions found</li>
	 *         <li><code>o</code>: value of the objective function</li>
	 *         </ul>
	 */
	public static String solverStatusInfo(GRBModel model) {
		int status = -1;
		int solCount = -1;
		double obj = Double.NaN;
		try {
			status = model.get(IntAttr.Status);
		} catch (GRBException e) {
		}
		try {
			solCount = model.get(IntAttr.SolCount);
		} catch (GRBException e) {
		}
		try {
			obj = model.get(DoubleAttr.ObjVal);
		} catch (GRBException e) {
		}
		return String.format("Status:%s SolCount:%s Objective:%s", status, solCount, obj);
	}

	/**
	 * Display the given linear expression <code>lin</code>.
	 * 
	 * @param lin
	 *            a linear expression.
	 * @param all
	 *            <code>true</code> if the variables with 0 coefficient have to
	 *            be displayed.
	 */
	public static void displayLinExpr(GRBLinExpr lin, boolean all) {
		try {
			GRBVar var;
			double coeff;
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < lin.size(); i++) {
				var = lin.getVar(i);
				coeff = lin.getCoeff(i);
				if (!all && coeff == 0.0) {
					continue;
				}
				if (coeff >= 0.0) {
					s.append(" + ");
				} else {
					s.append(" - ");
				}
				s.append(Math.abs(coeff) + " " + var.get(StringAttr.VarName) + "\n");
			}
			double cons = lin.getConstant();
			if (cons != 0) {
				if (cons > 0.0) {
					s.append(" + ");
				} else {
					s.append(" - ");
				}
				s.append(Math.abs(cons));
			}
			System.out.println(s.toString());
		} catch (GRBException e) {
			e.printStackTrace();
		}

	}

}
