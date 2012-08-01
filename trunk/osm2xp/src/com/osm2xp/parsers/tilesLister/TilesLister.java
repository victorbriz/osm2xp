package com.osm2xp.parsers.tilesLister;

import java.util.Set;

import math.geom2d.Point2D;

import com.osm2xp.exceptions.Osm2xpBusinessException;

/**
 * TilesLister.
 * 
 * @author Benjamin Blanchet
 * 
 */
public interface TilesLister {

	public Set<Point2D> getTilesList();

	public void process() throws Osm2xpBusinessException;
}
