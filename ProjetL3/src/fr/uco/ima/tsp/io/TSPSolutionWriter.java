package fr.uco.ima.tsp.io;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import fr.uco.ima.tsp.data.TSPInstance;
import fr.uco.ima.tsp.data.TSPSolution;

/**
 * Writes a solution in an XML format
 * 
 * @author froger
 *
 */
public class TSPSolutionWriter {

	/**
	 * Constructs a new SolutionWriter.
	 * 
	 * @param pathName
	 *            the pathname of the file we want to write
	 */
	public TSPSolutionWriter() {

	}

	public void write(TSPInstance instance, TSPSolution solution, String filePath, String authorName) {
		Document xmlSol = new Document();
		Element root = new Element("solution");
		root.setAttribute("instance", instance.getName());
		root.setAttribute("author", authorName);
		xmlSol.addContent(root);
		int idRoute = 0, size, nodeID;
		Element elementNode;
		Element er = new Element("route");
		er.setAttribute("id", String.valueOf(idRoute++));
		size = solution.size();
		for (int i = 0; i < size; i++) {
			nodeID = solution.get(i);
			elementNode = new Element("node");
			elementNode.setAttribute("id", String.valueOf(nodeID));
			er.addContent(elementNode);
		}
		root.addContent(er);
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutputter.output(xmlSol, new FileOutputStream(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
