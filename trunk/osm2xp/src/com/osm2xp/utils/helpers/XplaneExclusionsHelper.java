package com.osm2xp.utils.helpers;

import java.util.ArrayList;
import java.util.List;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

import com.osm2xp.model.osm.OsmPolygon;
import com.osm2xp.utils.GeomUtils;

public class XplaneExclusionsHelper extends Thread {

	private List<Box2D> exclusions = new ArrayList<Box2D>();
	private List<OsmPolygon> todoPolygons = new ArrayList<OsmPolygon>();

	private static final double DISTANCE = 10;

	private void addBuildingToExclusions(OsmPolygon osmPolygon) {

		// check distance from other
		if (!exclusions.isEmpty()) {
			Boolean exclusionAdded = false;
			List<Box2D> footprintsToRemove = new ArrayList<Box2D>();
			List<Box2D> footprintsToAdd = new ArrayList<Box2D>();
			exclusionloop: for (Box2D footprint : exclusions) {
				Point2D centerA = GeomUtils.getPolygonCenter(new LinearRing2D(
						footprint.getVertices()));
				Point2D centerB = GeomUtils.getPolygonCenter(osmPolygon
						.getPolygon());
				double distanceBetweenPolygons = (centerA.distance(centerB)) * 1000;

				if (distanceBetweenPolygons < DISTANCE) {
					Box2D newFootprint = footprint.merge(osmPolygon
							.getPolygon().getBoundingBox());

					footprintsToRemove.add(footprint);
					footprintsToAdd.add(newFootprint);
					exclusionAdded = true;
					break exclusionloop;
				}

			}
			if (!exclusionAdded) {
				exclusions.add(osmPolygon.getPolygon().getBoundingBox());
			}
			if (!footprintsToRemove.isEmpty()) {
				exclusions.removeAll(footprintsToRemove);
			}
			if (!footprintsToAdd.isEmpty()) {
				exclusions.addAll(footprintsToAdd);
			}
		} else {
			exclusions.add(osmPolygon.getPolygon().getBoundingBox());
		}
		// System.out.println("add Exclusion, size:"+exclusions.size());
	}

	public void removeSmallExclusions() {
		List<Box2D> footprintsToRemove = new ArrayList<Box2D>();
		for (Box2D footprint : exclusions) {
			double area = (footprint.getHeight() * footprint.getWidth()) * 1000000;
			if (area < 1) {
				footprintsToRemove.add(footprint);
			}
		}
		exclusions.removeAll(footprintsToRemove);
	}

	public void optimiseExclusion() {
		// System.out.println("Optimise Exclusion, size:"+exclusions.size());
		List<Box2D> footprintsToRemove = new ArrayList<Box2D>();
		List<Box2D> footprintsToAdd = new ArrayList<Box2D>();
		optimisationLoop: for (Box2D footprint : exclusions) {
			for (Box2D footprint2 : exclusions) {
				if (footprint != footprint2
						&& GeomUtils.boxContainsAnotherBox(footprint,
								footprint2)) {
					Box2D newFootprint = footprint.merge(footprint2);
					footprintsToAdd.add(newFootprint);
					footprintsToRemove.add(footprint2);
					footprintsToRemove.add(footprint);
					break optimisationLoop;
				}

			}
		}
		if (!footprintsToAdd.isEmpty()) {
			exclusions.removeAll(footprintsToRemove);
			exclusions.addAll(footprintsToAdd);
			optimiseExclusion();
		}
	}

	public String exportExclusions() {
		optimiseExclusion();
		removeSmallExclusions();

		StringBuilder sbBuilder = new StringBuilder();
		for (Box2D exclusion : exclusions) {
			Double xMin = null;
			Double xMax = null;
			Double yMin = null;
			Double yMax = null;

			for (Point2D point : exclusion.getVertices()) {

				if (xMin == null || point.getX() < xMin) {
					xMin = point.getX();
				}

				if (yMin == null || point.getY() < yMin) {
					yMin = point.getY();
				}

				if (xMax == null || point.getX() > xMax) {
					xMax = point.getX();
				}

				if (yMax == null || point.getY() > yMax) {
					yMax = point.getY();
				}

			}
			sbBuilder.append("PROPERTY sim/exclude_obj " + yMin + "/" + xMin
					+ "/" + yMax + "/" + xMax + "\n");

		}
		return sbBuilder.toString();
	}

	private void workExclusion() {
		List<OsmPolygon> polyToRemove = new ArrayList<OsmPolygon>();
		for (OsmPolygon polygon : todoPolygons) {
			addBuildingToExclusions(polygon);
			polyToRemove.add(polygon);
		}
		todoPolygons.removeAll(polyToRemove);
	}

	@Override
	public void run() {
		workExclusion();
	}

	public void addTodoPolygon(OsmPolygon polygon) {
		this.todoPolygons.add(polygon);
		// workExclusion();
		// addBuildingToExclusions(polygon);
	}
}
