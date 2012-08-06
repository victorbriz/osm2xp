package com.osm2xp.translators.impl;

import java.io.File;
import java.text.MessageFormat;

import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.OsmPolygon;
import com.osm2xp.model.osm.Relation;
import com.osm2xp.model.osm.Way;
import com.osm2xp.translators.ITranslator;
import com.osm2xp.utils.FilesUtils;
import com.osm2xp.utils.GeomUtils;
import com.osm2xp.utils.OsmUtils;
import com.osm2xp.utils.helpers.GuiOptionsHelper;
import com.osm2xp.utils.logging.Osm2xpLogger;

/**
 * FlightGear Translator implementation.
 * 
 * @author Benjamin Blanchet.
 * 
 */
public class FlightGearTranslatorImpl implements ITranslator {
	/**
	 * current lat/long tile.
	 */
	private Point2D currentTile;
	/**
	 * generated file folder path.
	 */
	private String folderPath;
	/**
	 * generated xml file.
	 */
	private File xmlFile;

	private static final String FLIGHT_GEAR_OBJECT_DECLARATION = "OBJECT_SHARED Models/{0} {1} {2} {3} {4}\n";

	/**
	 * Constuctor.
	 * 
	 * @param currentTile
	 *            current lat/long tile.
	 * @param folderPath
	 *            folder path.
	 */
	public FlightGearTranslatorImpl(Point2D currentTile, String folderPath) {
		super();
		this.currentTile = currentTile;
		this.folderPath = folderPath;
		File file = new File(GuiOptionsHelper.getOptions().getCurrentFilePath());
		String fileName = file.getName().substring(0,
				file.getName().indexOf("."));
		this.xmlFile = new File(this.folderPath + File.separator + fileName
				+ "_" + currentTile.x + "_" + currentTile.y + ".stg");

	}

	@Override
	public void processNode(Node node) throws Osm2xpBusinessException {
	}

	@Override
	public void processPolygon(OsmPolygon osmPolygon)
			throws Osm2xpBusinessException {
		LinearRing2D polygon = new LinearRing2D();

		// if the processor sent back a complete list of nodes
		// construct a polygon from those nodes

		polygon = GeomUtils.getPolygonFromOsmNodes(osmPolygon.getNodes());
		// simplify shape if checked and if necessary
		if (GuiOptionsHelper.getOptions().isSimplifyShapes()
				&& !osmPolygon.isSimplePolygon()) {
			polygon = GeomUtils.simplifyPolygon(polygon);
		}

		// if the polygon is a building, write it in the xml file.
		if (OsmUtils.isBuilding(osmPolygon.getTags())) {
			Point2D centerPoint = GeomUtils.getPolygonCenter(osmPolygon
					.getPolygon());
			String objectDeclaration = MessageFormat.format(
					FLIGHT_GEAR_OBJECT_DECLARATION, new Object[] {
							"Communications/radio-medium.xml", centerPoint.x,
							centerPoint.y, 1, 0 });
			objectDeclaration=objectDeclaration.replaceAll(",", ".");
			FilesUtils.writeTextToFile(this.xmlFile, objectDeclaration, true);

		}

	}

	@Override
	public void processRelation(Relation relation)
			throws Osm2xpBusinessException {
	}

	@Override
	public void complete() {
		FilesUtils.writeTextToFile(this.xmlFile, "END", true);
		Osm2xpLogger.info("Fly! Legacy buildings file finished.");
	}

	@Override
	public void init() {
		Osm2xpLogger.info("Starting Fly! Legacy file for tile "
				+ this.currentTile.x + "/" + this.currentTile.y + ".");
	}

	@Override
	public Boolean mustStoreNode(Node node) {
		Boolean result = true;
		if (!GuiOptionsHelper.getOptions().isSinglePass()) {
			result = GeomUtils.compareCoordinates(currentTile, node);
		}
		return result;
	}

	@Override
	public Boolean mustStoreWay(Way way) {
		return null;
	}
}
