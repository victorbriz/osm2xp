package com.osm2xp.parsers.relationsLister;

import java.io.File;

import com.osm2xp.utils.helpers.GuiOptionsHelper;

/**
 * RelationsListerFactory.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class RelationsListerFactory {

	/**
	 * @param file
	 * @return
	 */
	public static RelationsLister getRelationsLister(File file) {
		RelationsLister result = null;
		if (GuiOptionsHelper.getOptions().getCurrentFilePath().toLowerCase()
				.contains(".pbf")) {
			result = new PbfRelationsLister(file);
		} else if (GuiOptionsHelper.getOptions().getCurrentFilePath()
				.toLowerCase().contains(".osm")) {
			result = new XmlRelationsLister(file);
		}
		return result;
	}
}
