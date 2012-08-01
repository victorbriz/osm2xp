package com.osm2xp.writers.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

import math.geom2d.Point2D;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.exceptions.Osm2xpTechnicalException;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.DsfUtils;
import com.osm2xp.utils.FilesUtils;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;
import com.osm2xp.utils.logging.Osm2xpLogger;
import com.osm2xp.writers.IWriter;

/**
 * DsfWriterSinglePassImpl.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class DsfWriterSinglePassImpl implements IWriter {

	private static final String DEFAULT_FACADE_LOD = "LOD 0.000000 25000.000000";
	private String sceneFolder;
	private FileWriter writer = null;
	private BufferedWriter output;
	private String pathFichierTxt;
	private DsfObjectsProvider dsfObjectsProvider;

	public DsfWriterSinglePassImpl(String sceneFolder,
			DsfObjectsProvider dsfObjectsProvider) {
		this.sceneFolder = sceneFolder;
		this.dsfObjectsProvider = dsfObjectsProvider;
	}

	public void writeLibraryFile() {
		BufferedWriter outputlibrary = null;
		try {

			FileWriter writerLibrary = new FileWriter(sceneFolder
					+ File.separatorChar + "library.txt", false);
			outputlibrary = new BufferedWriter(writerLibrary);

			outputlibrary.write("I" + "\n");
			outputlibrary.write("800" + "\n");
			outputlibrary.write("LIBRARY" + "\n");
			for (String facade : dsfObjectsProvider.getFacadesList()) {
				outputlibrary.write("EXPORT \\lib\\osm2xp\\facades\\" + facade
						+ " .." + File.separator + "osm2xpFacades"
						+ File.separator
						+ XplaneOptionsHelper.getOptions().getFacadeSet()
						+ File.separator + facade + "\n");

			}
			outputlibrary.flush();
		} catch (IOException e) {
			throw new Osm2xpTechnicalException(e.getMessage());
		}
	}

	public void write(Object data) {
		if (data != null) {
			try {
				output.write((String) data);
				output.flush();

			} catch (IOException e) {
				Osm2xpLogger.error("Error writing data", e);
			}
		}
	}

	@Override
	public void complete() {
		// close the writter and compile the current text file to dsf
		try {
			writer.close();
		} catch (IOException e) {
			Osm2xpLogger.error("Error closing writer", e);
		}
		DsfUtils.textToDsf(new File(pathFichierTxt + ".dsf.txt"), new File(
				pathFichierTxt + ".dsf"));
	}

	@Override
	public void init(Point2D coordinates) {
		// compute folder and file name , following xplane specifications
		String[] folderAndFileNames = DsfUtils
				.getFolderAndFileNames(coordinates);
		String nomFichier = folderAndFileNames[1];
		String nomRepertoire = folderAndFileNames[0];
		String repertoireDsf = sceneFolder + File.separatorChar
				+ "Earth nav data" + File.separatorChar + nomRepertoire;
		new File(repertoireDsf).mkdirs();

		// create stats folder
		if (XplaneOptionsHelper.getOptions().isGeneratePdfStats()
				|| XplaneOptionsHelper.getOptions().isGenerateXmlStats()) {
			new File(sceneFolder + File.separatorChar + "stats").mkdirs();
		}
		if (XplaneOptionsHelper.getOptions().isPackageFacades()) {
			copyFacadeSet();
		}
		String nomFichierTuile = repertoireDsf + File.separatorChar
				+ nomFichier;
		// create the file writer
		try {
			writer = new FileWriter(nomFichierTuile + ".dsf.txt", false);
			// delete the text file on exit
			new File(nomFichierTuile + ".dsf.txt").deleteOnExit();
			output = new BufferedWriter(writer);
			pathFichierTxt = nomFichierTuile;
		} catch (IOException e) {
			throw new Osm2xpTechnicalException(e.getMessage());
		}
		// write the libraty file if needed
		if (!XplaneOptionsHelper.getOptions().isPackageFacades()) {
			writeLibraryFile();
		}
		// write the dsf header
		write(DsfUtils.getDsfHeader(coordinates, dsfObjectsProvider));

	}

	/**
	 * Copy facades files from workspace to scene folder. Also apply lod and
	 * hard wall settings
	 * 
	 * @throws Osm2xpBusinessException
	 */
	private void copyFacadeSet() {
		if (XplaneOptionsHelper.getOptions().isPackageFacades()
				&& !new File(sceneFolder + File.separatorChar + "facades")
						.exists()) {
			File from = new File(Osm2xpConstants.FACADES_SETS_PATH
					+ File.separatorChar
					+ XplaneOptionsHelper.getOptions().getFacadeSet());
			File to = new File(sceneFolder + File.separatorChar + "facades");

			try {
				FilesUtils.copyDirectory(from, to);
				applyFacadeLod(to);
				if (!XplaneOptionsHelper.getOptions().isHardBuildings()) {
					removeConcreteRoofsAndWalls(to);
				}
			} catch (FileNotFoundException e) {
				throw new Osm2xpTechnicalException(e);
			} catch (IOException e) {
				throw new Osm2xpTechnicalException(e);
			}

		}
	}

	private void applyFacadeLod(File to) throws FileNotFoundException,
			IOException {

		File[] filesList = to.listFiles();
		if (filesList != null) {
			for (int cpt = 0; cpt < filesList.length; cpt++) {
				if (filesList[cpt].getName().toLowerCase().contains(".fac")) {
					File facade = filesList[cpt];
					String line = null;
					String facadeContent = new String();
					RandomAccessFile reader = new RandomAccessFile(facade, "rw");
					while ((line = reader.readLine()) != null) {
						facadeContent += line + "\r\n";
					}
					facadeContent = facadeContent.replaceAll(
							DEFAULT_FACADE_LOD, "LOD 0.000000 "
									+ XplaneOptionsHelper.getOptions()
											.getFacadeLod() + ".000000");
					FileWriter writer = new FileWriter(facade.getAbsolutePath());
					writer.write(facadeContent);
					writer.close();

				}
			}
		}
	}

	private void removeConcreteRoofsAndWalls(File to)
			throws FileNotFoundException, IOException {

		File[] filesList = to.listFiles();
		if (filesList != null) {
			for (int cpt = 0; cpt < filesList.length; cpt++) {
				if (filesList[cpt].getName().toLowerCase().contains(".fac")) {
					File facade = filesList[cpt];
					String line = null;
					String facadeContent = new String();
					RandomAccessFile reader = new RandomAccessFile(facade, "rw");
					while ((line = reader.readLine()) != null) {
						facadeContent += line + "\r\n";
					}
					facadeContent = facadeContent.replaceAll(
							"HARD_ROOF concrete", "");
					facadeContent = facadeContent.replaceAll(
							"HARD_WALL concrete", "");
					FileWriter writer = new FileWriter(facade.getAbsolutePath());
					writer.write(facadeContent);
					writer.close();

				}
			}
		}
	}

	@Override
	public void write(Object data, Point2D coordinates) {
		// TODO Auto-generated method stub

	}

}
