package com.osm2xp.utils.helpers;

import java.util.ArrayList;
import java.util.List;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

import com.osm2xp.model.osm.OsmPolygon;
import com.osm2xp.utils.GeomUtils;

public class XplaneExclusionsHelper {

	private List<Box2D> exclusions = new ArrayList<Box2D>();

	private static final int DISTANCE = 100;

	public void addBuildingToExclusions(OsmPolygon osmPolygon) {
		// check distance from other
		if (!exclusions.isEmpty()) {
			for (Box2D footprint : exclusions) {
				Point2D centerA = GeomUtils.getPolygonCenter(new LinearRing2D(
						footprint.getVertices()));
				Point2D centerB = GeomUtils.getPolygonCenter(osmPolygon
						.getPolygon());
				double distanceBetweenPolygons = centerA.distance(centerB);
				if (distanceBetweenPolygons < DISTANCE) {
					Box2D newFootprint = footprint.merge(osmPolygon
							.getPolygon().getBoundingBox());
					System.out.println(footprint);
					exclusions.remove(footprint);
					exclusions.add(newFootprint);
					System.out.println(newFootprint);
				}
			}

		} else {
			exclusions.add(osmPolygon.getPolygon().getBoundingBox());
		}

	}

}
