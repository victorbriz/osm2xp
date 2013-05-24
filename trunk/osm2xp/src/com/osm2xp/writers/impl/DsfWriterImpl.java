package com.osm2xp.writers.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import math.geom2d.Point2D;

import org.apache.commons.io.FileUtils;

import com.osm2xp.constants.XplaneConstants;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.DsfUtils;
import com.osm2xp.utils.FilesUtils;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;
import com.osm2xp.writers.IWriter;

/**
 * Dsf Writer implementation.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class DsfWriterImpl implements IWriter {

	private String sceneFolder;
	private DsfObjectsProvider dsfObjectsProvider;
	private Map<Point2D, File> dsfFiles = new HashMap<Point2D, File>();

	public DsfWriterImpl(String sceneFolder,
			DsfObjectsProvider dsfObjectsProvider) {
		this.sceneFolder = sceneFolder;
		this.dsfObjectsProvider = dsfObjectsProvider;
	}

	public void write(Object data, Point2D coordinates) {
		if (data != null) {
			File dsfFile = null;
			// look if we have a dsf file for this coordinate point
			if (dsfFiles.get(coordinates) != null) {
				dsfFile = dsfFiles.get(coordinates);
			} else {
				dsfFile = checkDsfFile(coordinates);
			}
			// write into this dsf file
			FilesUtils.writeTextToFile(dsfFile, (String) data, true);

		}
	}

	@Override
	public void complete(Object data) {
		if (data != null) {
			try {
				injectSmartExclusions((String) data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Map.Entry<Point2D, File> entry : dsfFiles.entrySet()) {
			DsfUtils.textToDsf(entry.getValue(), new File(entry.getValue()
					.getPath().replaceAll(".txt", "")));
		}

	}

	@Override
	public void init(Point2D coordinates) {

		// create stats folder
		if (XplaneOptionsHelper.getOptions().isGeneratePdfStats()
				|| XplaneOptionsHelper.getOptions().isGenerateXmlStats()) {
			new File(sceneFolder + File.separatorChar + "stats").mkdirs();
		}
		// package facade set
		if (XplaneOptionsHelper.getOptions().isPackageFacades()
				&& XplaneOptionsHelper.getOptions().isGenerateBuildings()) {
			DsfUtils.copyFacadeSet(sceneFolder);
		}

		// write the libraty file if needed
		if (!XplaneOptionsHelper.getOptions().isPackageFacades()) {
			DsfUtils.writeLibraryFile(sceneFolder, dsfObjectsProvider);
		}

	}

	public File checkDsfFile(Point2D coordinates) {
		// compute the dsf file path
		File dsfFile = DsfUtils.computeXPlaneDsfFilePath(sceneFolder,
				coordinates);
		// if file doesn't exists
		if (!dsfFile.exists()) {
			// create the parent folder file
			File parentFolder = new File(dsfFile.getParent());
			parentFolder.mkdirs();
			// write its header
			String dsfHeader = DsfUtils.getDsfHeader(coordinates,
					this.dsfObjectsProvider);
			FilesUtils.writeTextToFile(dsfFile, dsfHeader, true);
			// add the file to the local files list
			dsfFiles.put(coordinates, dsfFile);
			// delete on exist
			dsfFile.deleteOnExit();

		}

		// return the dsf file
		return dsfFile;

	}

	@Override
	public void write(Object data) {

	}

	private void injectSmartExclusions(String exclusionText) throws IOException {

		for (Map.Entry<Point2D, File> entry : dsfFiles.entrySet()) {

			// temp file
			File tempFile = new File(entry.getValue().getAbsolutePath()
					+ "_temp");
			BufferedReader br = new BufferedReader(new FileReader(
					entry.getValue()));
			String line;
			while ((line = br.readLine()) != null) {
				line = line + "\n";
				if (line.contains(XplaneConstants.EXCLUSION_PLACEHOLDER)) {
					FilesUtils.writeTextToFile(tempFile, exclusionText, true);
				} else {
					FilesUtils.writeTextToFile(tempFile, line, true);
				}
			}
			br.close();

			FileUtils.copyFile(tempFile, entry.getValue());
			tempFile.delete();
			tempFile.deleteOnExit();

		}
	}
}
