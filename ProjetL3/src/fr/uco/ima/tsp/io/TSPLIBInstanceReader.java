package fr.uco.ima.tsp.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import fr.uco.ima.tsp.data.TSPInstance;

/**
 * Reads an instance
 * 
 * @author froger
 *
 */
public class TSPLIBInstanceReader {

	// The file holding the instance data
	private BufferedReader reader;

	public TSPInstance readInstance(String filePath) {
		assert (filePath != null);
		// start the input stream
		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		boolean readingHeader = true;
		boolean readingCoordinates = false;
		int i = 0, dimension;
		double[][] coordinates = null;
		String headerName, headerValue, type = "", instanceName = "";
		String[] items;
		try {
			String line = reader.readLine().trim();
			while (!line.isEmpty() && !line.equals("EOF")) {
				if (readingHeader) {
					items = line.split(":");
					headerName = items[0].trim().toUpperCase();
					headerValue = items.length > 1 ? items[1].trim() : "";
					if (headerName.equals("DIMENSION")) {
						dimension = Integer.parseInt(headerValue);
						coordinates = new double[dimension][2];
					}
					if (headerName.equals("NODE_COORD_SECTION")) {
						readingHeader = false;
						readingCoordinates = true;
					}
					if (headerName.equals("NAME")) {
						instanceName = headerValue;
					}
					if (headerName.equals("EDGE_WEIGHT_TYPE")) {
						type = headerValue;
					}
				} else if (readingCoordinates) {
					items = line.split("(\\s)+");
					coordinates[i][0] = Double.parseDouble(items[1]);
					coordinates[i][1] = Double.parseDouble(items[2]);
					i++;
				}
				line = reader.readLine().trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		TSPInstance instance = null;
		switch (type) {
		case "EUC_2D":
			instance = new TSPInstance(instanceName, coordinates, calcEuc2D(coordinates));
			break;
		case "GEO":
			instance = new TSPInstance(instanceName, coordinates, calcGeo(coordinates));
			break;
		}
		return instance;
	}

	public int calcEuc2D(double cx1, double cy1, double cx2, double cy2) {
		double value = Math.sqrt(Math.pow((cx1 - cx2), 2) + Math.pow((cy1 - cy2), 2));
		int distance = (int) (value + 0.5);
		return distance;

	}

	public int[][] calcEuc2D(double[][] coordinates) {
		if (coordinates[0].length != 2)
			throw new IllegalArgumentException(
					"argument coordinates must be a matrix with 2 columns and an open number of files");

		int[][] matrix = new int[coordinates.length][coordinates.length];
		for (int i = 0; i < coordinates.length; i++) {
			for (int j = i + 1; j < coordinates.length; j++) {
				matrix[i][j] = calcEuc2D(coordinates[i][0], coordinates[i][1], coordinates[j][0], coordinates[j][1]);
				matrix[j][i] = matrix[i][j];
			}
		}
		return matrix;
	}

	public int calcGeo(double cx1, double cy1, double cx2, double cy2) {
		double q1 = Math.cos(cy1 - cy2);
		double q2 = Math.cos(cx1 - cx2);
		double q3 = Math.cos(cx1 + cx2);
		int distance = (int) (6378.388 * Math.acos(0.5 * ((1.0 + q1) * q2 - (1.0 - q1) * q3)) + 1.0);
		return distance;

	}

	public int[][] calcGeo(double[][] coordinates) {
		double[][] newCoordinates = translateCoordinates(coordinates);
		if (newCoordinates[0].length != 2)
			throw new IllegalArgumentException(
					"argument coordinates must be a matrix with 2 columns and an open number of files");

		int[][] matrix = new int[newCoordinates.length][newCoordinates.length];
		for (int i = 0; i < newCoordinates.length; i++) {
			for (int j = i + 1; j < newCoordinates.length; j++) {
				matrix[i][j] = calcGeo(newCoordinates[i][0], newCoordinates[i][1], newCoordinates[j][0],
						newCoordinates[j][1]);
				matrix[j][i] = matrix[i][j];
			}
		}
		return matrix;
	}

	private double[][] translateCoordinates(double[][] coordinates) {
		double[][] newCoordinates = new double[coordinates.length][2];
		double pi = 3.141592;
		for (int i = 0; i < coordinates.length; i++) {
			int degX = (int) (coordinates[i][0] + 0.5);
			double minX = coordinates[i][0] - degX;
			newCoordinates[i][0] = pi * (degX + 5.0 * minX / 3.0) / 180.0;
			int degY = (int) (coordinates[i][1] + 0.5);
			double minY = coordinates[i][1] - degY;
			newCoordinates[i][1] = pi * (degY + 5.0 * minY / 3.0) / 180.0;
		}
		return newCoordinates;
	}

}
