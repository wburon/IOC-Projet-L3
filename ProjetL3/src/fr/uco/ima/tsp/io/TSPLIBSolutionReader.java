package fr.uco.ima.tsp.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import fr.uco.ima.tsp.data.TSPSolution;

/**
 * Reads a solution
 * 
 * @author froger
 *
 */
public class TSPLIBSolutionReader {

	// The file holding the instance data
	private BufferedReader reader;
	private String instanceName;

	public TSPSolution readSolution(String filePath) {
		assert (filePath != null);
		// start the input stream
		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		boolean readingHeader = true;
		boolean readingSolution = false;
		String headerName, headerValue;
		String[] items;
		int nodeID;
		ArrayList<Integer> permutation = new ArrayList<>();
		try {
			String line = reader.readLine().trim();
			while (!line.isEmpty() && !line.equals("EOF")) {
				if (readingHeader) {
					items = line.split(":");
					headerName = items[0].trim().toUpperCase();
					headerValue = items.length > 1 ? items[1].trim() : "";
					if (headerName.equals("TOUR_SECTION")) {
						readingHeader = false;
						readingSolution = true;
					}
					if (headerName.equals("NAME")) {
						instanceName = headerValue.substring(0, headerValue.indexOf("."));
					}
				} else if (readingSolution) {
					nodeID = Integer.valueOf(line);
					if (nodeID == -1) {
						break;
					}
					permutation.add(nodeID - 1);
				}
				line = reader.readLine().trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		TSPSolution instance = new TSPSolution(permutation);
		return instance;
	}

	public String getAssociatedInstance() {
		return instanceName;
	}
}
