package fr.uco.ima.tsp.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * Writes HTML files to visualize a TSP instance or TSP solution
 * 
 * @author froger
 *
 */
public class TSPViewer {

	/**
	 * Use NORMAL for small size instances, LARGE for large size instances
	 * (n>200)
	 * 
	 * @author froger
	 *
	 */
	public enum ViewerType {
		NORMAL, LARGE;
	}

	private static final int X_NORMAL = 1500;
	private static final int Y_NORMAL = 720;

	private static final int X_LARGE = 7500;
	private static final int Y_LARGE = 3600;

	public void write(TSPInstance instance, String filePath, ViewerType type) {
		int size = instance.size();
		double xMin = Double.MAX_VALUE, xMax = -Double.MAX_VALUE, yMin = Double.MAX_VALUE, yMax = -Double.MAX_VALUE;
		for (int i = 0; i < size; i++) {
			if (instance.getX(i) < xMin) {
				xMin = instance.getX(i);
			}
			if (instance.getX(i) > xMax) {
				xMax = instance.getX(i);
			}
			if (instance.getY(i) < yMin) {
				yMin = instance.getY(i);
			}
			if (instance.getY(i) > yMax) {
				yMax = instance.getY(i);
			}
		}
		double scaleX;
		double scaleY;
		switch (type) {
		case NORMAL:
			scaleX = (xMax - xMin) / X_NORMAL;
			scaleY = (yMax - yMin) / Y_NORMAL;
			break;
		case LARGE:
			scaleX = (xMax - xMin) / X_LARGE;
			scaleY = (yMax - yMin) / Y_LARGE;
			break;
		default:
			throw new IllegalArgumentException("The type is unknown -> " + type);
		}

		double scale = Math.min(scaleX, scaleY);
		try {
			FileWriter fw = new FileWriter(filePath, false);
			BufferedWriter output = new BufferedWriter(fw);
			output.write("<html>\n");
			output.write("\n<head>\n");
			output.write("\t<title>TSP instance - " + instance.getName() + "</title>\n");
			output.write(
					"\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/cytoscape/3.2.9/cytoscape.min.js\"></script>\n");
			output.write("</head>\n");
			output.write("\n<style>\n");
			output.write("\t#cy {\n");
			output.write("\t\twidth: 100%;\n");
			output.write("\t\theight: 100%;\n");
			output.write("\t\tposition: absolute;\n");
			output.write("\t\ttop: 0px;\n");
			output.write("\t\tleft: 0px;\n");
			output.write("\t}\n");
			output.write("</style>\n");
			output.write("\n<body>\n");
			// output.write("\t<div id=\"cy\"><h3> Instance : " +
			// instance.getName() + "</h3></div>\n");
			output.write("\t<div id=\"cy\"></div>\n");
			output.write("\t<script>\n");
			output.write("\t\tvar cy = cytoscape({\n");
			output.write("\t\t\tcontainer: document.getElementById('cy'),\n");
			output.write("\t\t\tstyle: [\n");
			output.write("\t\t\t{\n");
			output.write("\t\t\t\tselector: 'node',\n");
			output.write("\t\t\t\tcss: {\n");
			output.write("\t\t\t\t\t'content': 'data(id)',\n");
			output.write("\t\t\t\t\t'text-valign': 'center',\n");
			output.write("\t\t\t\t\t'text-halign': 'center'\n");
			output.write("\t\t\t\t}\n");
			output.write("\t\t\t}\n");
			output.write("\t\t\t],\n");
			output.write("\t\t\telements: [\n");
			for (int i = 0; i < size; i++) {
				output.write("{ data: { id: '" + i + "' }, position : { x: " + (int) (instance.getX(i) / scale)
						+ ", y: " + (int) (instance.getY(i) / scale)
						+ "}, selectable: false, locked: true, grabbable: false},\n");
			}
			output.write("\t\t\t]\n");
			output.write("\t\t});\n");
			output.write("\t\t</script>\n");
			output.write("\t</body>\n");
			output.write("\t</html>\n");
			output.flush();
			output.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(
					"Error when writing the instance viewer file " + instance.getName() + " \n");
		}
	}

	public void write(TSPSolution solution, TSPInstance instance, String filePath, ViewerType type) {
		int size = instance.size();
		double xMin = Double.MAX_VALUE, xMax = -Double.MAX_VALUE, yMin = Double.MAX_VALUE, yMax = -Double.MAX_VALUE;
		for (int i = 0; i < size; i++) {
			if (instance.getX(i) < xMin) {
				xMin = instance.getX(i);
			}
			if (instance.getX(i) > xMax) {
				xMax = instance.getX(i);
			}
			if (instance.getY(i) < yMin) {
				yMin = instance.getY(i);
			}
			if (instance.getY(i) > yMax) {
				yMax = instance.getY(i);
			}
		}
		double scaleX;
		double scaleY;
		switch (type) {
		case NORMAL:
			scaleX = (xMax - xMin) / X_NORMAL;
			scaleY = (yMax - yMin) / Y_NORMAL;
			break;
		case LARGE:
			scaleX = (xMax - xMin) / X_LARGE;
			scaleY = (yMax - yMin) / Y_LARGE;
			break;
		default:
			throw new IllegalArgumentException("The type is unknown -> " + type);
		}
		double scale = Math.min(scaleX, scaleY);
		try {
			FileWriter fw = new FileWriter(filePath, false);
			BufferedWriter output = new BufferedWriter(fw);
			output.write("<html>\n");
			output.write("\n<head>\n");
			output.write("\t<title>TSP solution - " + instance.getName() + "</title>\n");
			output.write(
					"\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/cytoscape/3.2.9/cytoscape.min.js\"></script>\n");
			output.write("</head>\n");
			output.write("\n<style>\n");
			output.write("\t#cy {\n");
			output.write("\t\twidth: 100%;\n");
			output.write("\t\theight: 100%;\n");
			output.write("\t\tposition: absolute;\n");
			output.write("\t\ttop: 0px;\n");
			output.write("\t\tleft: 0px;\n");
			output.write("\t}\n");
			output.write("</style>\n");
			output.write("\n<body>\n");
			// output.write("\t<div id=\"cy\"><h3> Instance : " +
			// instance.getName() + "</h3></div>\n");
			output.write("\t<div id=\"cy\"></div>\n");
			output.write("\t<script>\n");
			output.write("\t\tvar cy = cytoscape({\n");
			output.write("\t\t\tcontainer: document.getElementById('cy'),\n");
			output.write("\t\t\tstyle: [\n");
			output.write("\t\t\t{\n");
			output.write("\t\t\t\tselector: 'node',\n");
			output.write("\t\t\t\tcss: {\n");
			output.write("\t\t\t\t\t'content': 'data(id)',\n");
			output.write("\t\t\t\t\t'text-valign': 'center',\n");
			output.write("\t\t\t\t\t'text-halign': 'center'\n");
			output.write("\t\t\t\t}\n");
			output.write("\t\t\t}\n");
			output.write("\t\t\t],\n");
			output.write("\t\t\telements: {\n");
			output.write("\t\t\tnodes: [\n");
			for (int i = 0; i < size; i++) {
				output.write("{ data: { id: '" + i + "' }, position : { x: " + (int) (instance.getX(i) / scale)
						+ ", y: " + (int) (instance.getY(i) / scale)
						+ "}, selectable: false, locked: true, grabbable: false},\n");
			}
			output.write("\t\t\t],\n");
			output.write("\t\t\tedges: [\n");
			int solutionSize = solution.size();
			for (int i = 0; i < size - 1; i++) {
				output.write(
						"{ data: { id: '" + solution.get(i) + "_" + solution.get(i + 1) + "' , weight: 1, source: '"
								+ solution.get(i) + "', target: '" + solution.get(i + 1) + "' } },\n");
			}
			output.write("{ data: { id: '" + solution.get(solutionSize - 1) + "_" + solution.get(0)
					+ "' , weight: 1, source: '" + solution.get(solutionSize - 1) + "', target: '" + solution.get(0)
					+ "' } },\n");
			output.write("\t\t\t]\n");
			output.write("\t\t}\n");
			output.write("\t\t});\n");
			output.write("\t\t</script>\n");
			output.write("\t</body>\n");
			output.write("\t</html>\n");
			output.flush();
			output.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(
					"Error when writing the instance viewer file " + instance.getName() + " \n");
		}
	}

}
