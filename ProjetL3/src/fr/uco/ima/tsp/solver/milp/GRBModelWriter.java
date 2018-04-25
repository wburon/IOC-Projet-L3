/**
 *
 */
package fr.uco.ima.tsp.solver.milp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import gurobi.GRB;
import gurobi.GRB.DoubleAttr;
import gurobi.GRB.StringAttr;
import gurobi.GRBException;
import gurobi.GRBModel;
import gurobi.GRBVar;

/**
 * The class <code>GRBModelWriter</code> is a utility class that provides
 * methods to write instances of {@link GRBModel} into a compresses file
 * 
 * @author froger
 */
public class GRBModelWriter {

	/** The logger. */
	protected static final Logger LOGGER = Logger.getLogger(GRBModelWriter.class.getName());

	private static final int BUFFER = 1048576;

	/**
	 * The date in String format
	 */
	private final String mDate;
	/**
	 * The path of the destination directory
	 */
	private final String mPath;
	/**
	 * Writes or not in a zip folder.
	 */
	private boolean mWriteToZip = true;

	private final File mZipFile;
	private final File mZipTmpFile;

	/**
	 * Creates a new <code>GRBModelWriter</code>
	 * 
	 * @param path
	 *            the path of the destination directory
	 * @param zipName
	 *            the name of the compressed archive (without extension)
	 * @throws IOException
	 */
	public GRBModelWriter(String path, String zipName) throws IOException {
		mDate = String.format("%1$ty%1$tm%1$td_%1$tH-%1$tM_%1$tS", new Date());
		mPath = path;
		if (mWriteToZip) {
			mZipFile = new File(String.format("%s/%s_%s.zip", path, zipName, mDate));
			mZipTmpFile = new File(String.format("%s/%s_%s_tmp.zip", path, zipName, mDate));
		} else {
			mZipFile = null;
			mZipTmpFile = null;
		}
	}

	public void setWriteToZip(boolean writeToZip) {
		this.mWriteToZip = writeToZip;
	}

	/**
	 * Write a model to a .mps file and add it to the compressed archive if
	 * <code>mWriteToZip=true</code>.
	 * 
	 * @param model
	 *            the model to be written
	 * @param name
	 *            the name of the model
	 * @throws IOException
	 */
	public void writeMPS(GRBModel model, String name) throws IOException {
		if (mWriteToZip) {
			writeToZIP(model, name, ".mps");
		} else {
			writePlain(model, name, ".mps");
		}
	}

	/**
	 * Write a model to a .lp file and add it to the compressed archive if
	 * <code>mWriteToZip=true</code>.
	 * 
	 * @param model
	 *            the model to be written
	 * @param name
	 *            the name of the model
	 * @throws IOException
	 */
	public void writeLP(GRBModel model, String name) throws IOException {
		if (mWriteToZip) {
			writeToZIP(model, name, ".lp");
		} else {
			writePlain(model, name, ".lp");
		}
	}

	/**
	 * Write a model to two files (.mst and <code>extension</code>)
	 * 
	 * @param model
	 *            the model to be written
	 * @param name
	 *            the name of the files in which the model will be written (date
	 *            tag and extensions are added automatically)
	 * @param extension
	 *            the extension of the file (<code>lp</code> or <code>mps</code>
	 *            ).
	 * @throws IOException
	 */
	private void writePlain(GRBModel model, String name, String extension) throws IOException {
		File lockFile = new File(String.format("%s/writing.lock", mPath));
		lockFile.createNewFile();

		try {
			String fileName = String.format("%s/%s_%s", mPath, name, mDate);

			File mstFile = null;
			try {
				mstFile = writeMST(model, fileName + "_" + extension);
			} catch (GRBException e) {
				e.printStackTrace();
			}

			if (mstFile != null) {
				try {
					model.write(fileName + extension);
				} catch (GRBException e) {
					e.printStackTrace();
					mstFile.delete();
				}
			}
		} catch (IOException e) {
			lockFile.delete();
			throw e;
		} finally {
			lockFile.delete();
		}
	}

	/**
	 * Write a model in a file (.mps) and add it to the compressed archive
	 * 
	 * @param model
	 *            the model to be written
	 * @param name
	 *            the name of the file in which the model will be written (date
	 *            tag and extensions are added automatically)
	 * @param extension
	 *            the extension of the file (<code>lp</code> or <code>mps</code>
	 *            ).
	 * @throws IOException
	 */
	private void writeToZIP(GRBModel model, String name, String extension) throws IOException {
		byte data[] = new byte[BUFFER];
		if (mZipFile.exists())
			mZipFile.renameTo(mZipTmpFile);

		mZipFile.createNewFile();

		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(mZipFile)));
		zos.setMethod(ZipOutputStream.DEFLATED);

		if (mZipTmpFile.exists()) {
			// Copy the existing file
			synchronized (zos) {
				ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(mZipTmpFile)));
				ZipEntry entry = zis.getNextEntry();
				while (entry != null) {
					zos.putNextEntry(entry);
					int count = zis.read(data, 0, BUFFER);
					while (count != -1) {
						zos.write(data, 0, count);
						count = zis.read(data, 0, BUFFER);
					}
					zos.closeEntry();
					entry = zis.getNextEntry();
				}
				zis.close();
			}
			mZipTmpFile.delete();
		}

		// Write the model in two files
		String fileName = name + "_" + String.format("_(%1$ty%1$tm%1$td_%1$tH-%1$tM_%1$tS_%1tL)", new Date());
		File mpsFile = new File(fileName + extension);

		File mstFile = null;
		try {
			mstFile = writeMST(model, fileName + "_" + extension);
		} catch (GRBException e) {
			LOGGER.log(Level.WARNING, "Error in writeToZOP file " + mstFile);
			mstFile = null;
		}
		try {
			model.write(mpsFile.getAbsolutePath());
		} catch (GRBException e) {
			LOGGER.log(Level.WARNING, "Error writeToZOP file " + mpsFile);
			mpsFile = null;
		}

		File[] files = mpsFile != null && mstFile != null ? new File[] { mstFile, mpsFile } : new File[] {};

		for (File file : files) {
			synchronized (zos) {
				// Compress files
				FileInputStream fi = new FileInputStream(file);
				BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);
				int count = origin.read(data, 0, BUFFER);
				while (count != -1) {
					zos.write(data, 0, count);
					count = origin.read(data, 0, BUFFER);
				}
				origin.close();
				zos.closeEntry();
			}
			file.delete();
		}

		// Write
		zos.flush();
		zos.close();
	}

	/**
	 * Returns the path to the zip file.
	 * 
	 * @return the path to the zip file.
	 */
	public String getZipPath() {
		return mZipFile.getPath();
	}

	/**
	 * Write the {@link DoubleAttr#Start} attribute of all the variables of a
	 * {@link GRBModel model}
	 * 
	 * @param model
	 *            the model from which the start values will be extracted
	 * @param file
	 *            the path of the destination file
	 * @returns the written {@link File}
	 * @throws IOException
	 * @throws GRBException
	 */
	private static File writeMST(GRBModel model, String file) throws IOException, GRBException {
		if (!file.endsWith(".mst"))
			file = file + ".mst";

		File mst = new File(file);
		if (!mst.exists())
			mst.createNewFile();

		BufferedWriter out = new BufferedWriter(new FileWriter(mst, false));

		GRBVar[] vars = model.getVars();
		double[] start = model.get(DoubleAttr.Start, vars);
		String[] names = model.get(StringAttr.VarName, vars);

		String modelName = null;
		try {
			modelName = model.get(StringAttr.ModelName);
		} catch (GRBException e) {
			// Ignore
		} finally {
			if (modelName == null)
				modelName = file;
		}
		out.write("# MIP start for ");
		out.write(modelName);
		out.newLine();

		for (int i = 0; i < start.length; i++) {
			if (start[i] != GRB.UNDEFINED) {
				out.append(names[i]);
				out.write("  ");
				out.write(start[i] + "");
				out.newLine();
			}
		}

		out.flush();
		out.close();

		return mst;
	}

}
