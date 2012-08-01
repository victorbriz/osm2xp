package com.osm2xp.constants;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;

/**
 * Osm2xp Constants.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Osm2xpConstants {

	public static final String OSM2XP_VERSION = "2.0.3";
	public static final String PREFS_PATH = "." + File.separator + "ressources"
			+ File.separator + "prefs" + File.separator;
	public static final String FACADES_SETS_PATH = ResourcesPlugin
			.getWorkspace().getRoot().getLocation()
			+ File.separator + "resources" + File.separatorChar + "facades";
	public static final String ORIENTDB_PATH = ResourcesPlugin.getWorkspace()
			.getRoot().getLocation()
			+ File.separator + "resources" + File.separatorChar + "orientDB";
	public static final String JDBM_PATH = ResourcesPlugin.getWorkspace()
			.getRoot().getLocation()
			+ File.separator + "resources" + File.separatorChar + "JDBM";
	public static final String UTILS_PATH = ResourcesPlugin.getWorkspace()
			.getRoot().getLocation()
			+ File.separator + "resources" + File.separatorChar + "utils";
	public static final String OUTPUT_FORMAT_XPLANE9 = "Xplane 9";
	public static final String OUTPUT_FORMAT_XPLANE10 = "Xplane 10";
	public static final String OUTPUT_FORMAT_OSM = "OpenStreetMap (xml)";
	public static final String OUTPUT_FORMAT_CONSOLE = "Console";
	public static final String OUTPUT_FORMAT_WAVEFRONT = "WaveFront (.obj)";
	public static final String OUTPUT_FORMAT_FSX = "Flight Simulator X";
	public static final String OUTPUT_FORMAT_FLYLEGAGY = "Fly! Legacy";
	public static final String OUTPUT_FORMAT_G2XPL = "G2xpl binding file";

}
