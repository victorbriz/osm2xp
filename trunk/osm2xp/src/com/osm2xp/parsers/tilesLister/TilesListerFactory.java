package com.osm2xp.parsers.tilesLister;

import java.io.File;

import com.osm2xp.utils.helpers.GuiOptionsHelper;

/**
 * TilesListerFactory.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class TilesListerFactory {

	public static TilesLister getTilesLister(File file) {
		TilesLister result = null;
		if (GuiOptionsHelper.getOptions().getCurrentFilePath().toLowerCase()
				.contains(".pbf")) {
			result = new PbfTilesLister(file);
		} else if (GuiOptionsHelper.getOptions().getCurrentFilePath()
				.toLowerCase().contains(".osm")) {
			result = new XmlTilesLister(file);
		}
		if (GuiOptionsHelper.getOptions().getCurrentFilePath().toLowerCase()
				.contains(".shp")) {
			result = new ShapefileTilesLister(file);
		}
		return result;
	}
}
