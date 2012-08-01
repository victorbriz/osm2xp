package com.osm2xp.parsers;

import java.awt.Color;
import java.io.File;
import java.util.Map;

import com.osm2xp.dataProcessors.IDataSink;
import com.osm2xp.exceptions.OsmParsingException;
import com.osm2xp.translators.ITranslator;

/**
 * Parser interface.
 * 
 * @author Benjamin Blanchet
 * 
 */
public interface IParser {

	public void init(File dataFile, ITranslator translator,
			Map<Long, Color> roofsColorMap, IDataSink processor);

	public void process() throws OsmParsingException;

	public void complete();

}
