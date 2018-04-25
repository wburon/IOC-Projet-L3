package fr.uco.ima.tsp.io;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;

import fr.uco.ima.tsp.data.TSPSolution;

/**
 * Reads a solution in an XML format
 * 
 * @author froger
 *
 */
public class TSPSolutionParser {

	private Document mXMLData = null;

	/**
	 * Constructs a new SolutionParser.
	 * 
	 * @param pathName
	 *            the pathname of the file holding the data
	 */
	public TSPSolutionParser() {

	}

	public TSPSolution read(String pathName) {
		mXMLData = XMLParser.parse(pathName);
		if (mXMLData == null) {
			return null;
			// throw new IllegalStateException("The instance file cannot be read
			// -> " + pathName);
		}
		// Data on the different nodes
		Element root = mXMLData.getRootElement();
		Iterator<Element> itRoutes = root.getChildren().iterator();
		Iterator<Element> itNodesInRoute;
		ArrayList<Integer> list = new ArrayList<>();
		Element eRoute, eNode;
		int nodeID;
		eRoute = itRoutes.next();
		itNodesInRoute = eRoute.getChildren().iterator();
		while (itNodesInRoute.hasNext()) {
			eNode = itNodesInRoute.next();
			try {
				nodeID = eNode.getAttribute("id").getIntValue();
				list.add(nodeID);
			} catch (DataConversionException | NumberFormatException e) {
				e.printStackTrace();
			}
		}
		TSPSolution solution = new TSPSolution(list);
		return solution;
	}

	public String getAssociatedInstance() {
		if (mXMLData == null) {
			throw new IllegalStateException("Call read() before");
		}
		Element root = mXMLData.getRootElement();
		String instanceName = root.getAttributeValue("instance");
		return instanceName;
	}

}
