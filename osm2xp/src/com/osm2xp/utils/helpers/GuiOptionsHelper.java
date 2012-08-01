package com.osm2xp.utils.helpers;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.options.GuiOptions;
import com.osm2xp.model.osm.Tag;
import com.osm2xp.utils.logging.Osm2xpLogger;

/**
 * GuiOptionsHelper.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class GuiOptionsHelper {

	private static GuiOptions options;
	private static String sceneName;
	private static Tag shapefileTag;
	private static File roofColorFile;

	private static final String INTERFACE_OPTIONS_FILE_PATH = ResourcesPlugin
			.getWorkspace().getRoot().getLocation()
			+ File.separator + "GuiOptions.xml";

	static {
		if (new File(INTERFACE_OPTIONS_FILE_PATH).exists()) {
			try {
				options = (GuiOptions) XmlHelper.loadFileFromXml(new File(
						INTERFACE_OPTIONS_FILE_PATH), GuiOptions.class);
			} catch (Osm2xpBusinessException e) {
				Osm2xpLogger.error("Error initializing GUI options helper", e);
			}
		} else {
			options = createNewGuiOptionsBean();
		}
	}

	/**
	 * return true if the choosen output mode is generating files
	 * 
	 * @return boolean
	 */
	public static boolean isOutputFormatAFileGenerator() {
		return (getOptions().getOutputFormat().equalsIgnoreCase(
				Osm2xpConstants.OUTPUT_FORMAT_FLYLEGAGY)
				|| getOptions().getOutputFormat().equalsIgnoreCase(
						Osm2xpConstants.OUTPUT_FORMAT_XPLANE10)
				|| getOptions().getOutputFormat().equalsIgnoreCase(
						Osm2xpConstants.OUTPUT_FORMAT_XPLANE9)
				|| getOptions().getOutputFormat().equalsIgnoreCase(
						Osm2xpConstants.OUTPUT_FORMAT_WAVEFRONT)
				|| getOptions().getOutputFormat().equalsIgnoreCase(
						Osm2xpConstants.OUTPUT_FORMAT_G2XPL) || getOptions()
				.getOutputFormat().equalsIgnoreCase(
						Osm2xpConstants.OUTPUT_FORMAT_OSM));
	}

	/**
	 * @return
	 */
	private static GuiOptions createNewGuiOptionsBean() {
		GuiOptions result = new GuiOptions(false, false, false, true, null,
				Osm2xpConstants.OUTPUT_FORMAT_XPLANE9, false,
				new ArrayList<String>());
		return result;
	}

	public static GuiOptions getOptions() {
		return options;
	}

	public static void setSceneName(String computeSceneName) {
		sceneName = computeSceneName;

	}

	public static String getSceneName() {
		return sceneName;
	}

	public static void saveOptions() throws Osm2xpBusinessException {
		XmlHelper
				.saveToXml(getOptions(), new File(INTERFACE_OPTIONS_FILE_PATH));
	}

	public static void addUsedFile(String fileName) {
		if (options.getLastFiles().indexOf(fileName) == -1) {
			options.getLastFiles().add(fileName);
		}

	}

	public static void askShapeFileNature(Shell shell) {
		MessageDialog messageDialog = new MessageDialog(shell,
				"shapefile nature", null, "What is the nature of "
						+ new File(getOptions().getCurrentFilePath()).getName()
						+ "?", MessageDialog.QUESTION, new String[] {
						"Buildings", "Forests" }, 1);
		if (messageDialog.open() == 0) {
			shapefileTag = new Tag("yes", "building");
		} else {
			shapefileTag = new Tag("forest", "landuse");
		}

	}

	public static Tag getShapefileTag() {
		return shapefileTag;
	}

	public static void setShapefileTag(Tag shapefileTag) {
		GuiOptionsHelper.shapefileTag = shapefileTag;
	}

	public static File getRoofColorFile() {
		return roofColorFile;
	}

	public static void setRoofColorFile(File roofColorFile) {
		GuiOptionsHelper.roofColorFile = roofColorFile;
	}
}
