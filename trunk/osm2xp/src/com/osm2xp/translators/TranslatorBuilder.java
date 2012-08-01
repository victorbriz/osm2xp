package com.osm2xp.translators;

import java.io.File;
import java.util.List;

import math.geom2d.Point2D;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.facades.FacadeSet;
import com.osm2xp.model.osm.Relation;
import com.osm2xp.model.stats.GenerationStats;
import com.osm2xp.translators.impl.ConsoleTranslatorImpl;
import com.osm2xp.translators.impl.FsxBgTranslatorImpl;
import com.osm2xp.translators.impl.G2xplTranslatorImpl;
import com.osm2xp.translators.impl.OsmTranslatorImpl;
import com.osm2xp.translators.impl.WavefrontTranslatorImpl;
import com.osm2xp.translators.impl.Xplane10TranslatorImpl;
import com.osm2xp.translators.impl.Xplane9TranslatorImpl;
import com.osm2xp.translators.impl.flyLegacyTranslatorImpl;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.helpers.FacadeSetHelper;
import com.osm2xp.utils.helpers.GuiOptionsHelper;
import com.osm2xp.utils.helpers.StatsHelper;
import com.osm2xp.utils.helpers.WavefrontOptionsHelper;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;
import com.osm2xp.writers.IWriter;
import com.osm2xp.writers.impl.BglWriterImpl;
import com.osm2xp.writers.impl.DsfWriterImpl;
import com.osm2xp.writers.impl.OsmWriterImpl;

/**
 * TranslatorBuilder.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class TranslatorBuilder {

	public static ITranslator getTranslator(File currentFile,
			Point2D currentTile, String folderPath, List<Relation> relationsList) {

		// XPLANE 9 DSF implementation
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Osm2xpConstants.OUTPUT_FORMAT_XPLANE9)) {
			return buildXplane9Translator(currentFile, currentTile, folderPath,
					relationsList);
		}
		// XPLANE 10 DSF implementation
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Osm2xpConstants.OUTPUT_FORMAT_XPLANE10)) {
			return buildXplane10Translator(currentFile, currentTile, folderPath);
		}
		// OSM implementation
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Osm2xpConstants.OUTPUT_FORMAT_OSM)) {
			return buildOsmTranslator(currentTile, folderPath);
		}
		// DEBUG CONSOLE
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Osm2xpConstants.OUTPUT_FORMAT_CONSOLE)) {
			return buildConsoleTranslator(currentTile);
		}

		// WAVEFRONT OBJECT
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Osm2xpConstants.OUTPUT_FORMAT_WAVEFRONT)) {
			return buildWavefrontTranslator(currentTile, folderPath);
		}

		// FSX TRANSLATOR
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Osm2xpConstants.OUTPUT_FORMAT_FSX)) {
			return buildFsxTranslator(currentFile, currentTile, folderPath);
		}

		// G2XPL TRANSLATOR
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Osm2xpConstants.OUTPUT_FORMAT_G2XPL)) {
			return buildG2xplTranslator(currentTile, folderPath);
		}

		// FLY! LEGACY TRANSLATOR
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Osm2xpConstants.OUTPUT_FORMAT_FLYLEGAGY)) {
			return buildFlyLegacyTranslator(currentTile, folderPath);
		}

		return null;
	}

	/**
	 * @param currentFile
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 */
	private static ITranslator buildFsxTranslator(File currentFile,
			Point2D currentTile, String folderPath) {
		IWriter writer = new BglWriterImpl(folderPath);
		GenerationStats stats = StatsHelper.initStats(currentFile, currentTile);
		return new FsxBgTranslatorImpl(stats, writer, currentTile, folderPath);
	}

	/**
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 */
	private static ITranslator buildWavefrontTranslator(Point2D currentTile,
			String folderPath) {
		return new WavefrontTranslatorImpl(folderPath, currentTile,
				WavefrontOptionsHelper.getOptions()
						.isWaveFrontExportSingleObject());
	}

	/**
	 * @param currentTile
	 * @param processor
	 * @return
	 */
	private static ITranslator buildConsoleTranslator(Point2D currentTile) {
		return new ConsoleTranslatorImpl(currentTile);
	}

	/**
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 */
	private static ITranslator buildOsmTranslator(Point2D currentTile,
			String folderPath) {
		IWriter writer = new OsmWriterImpl(folderPath);
		return new OsmTranslatorImpl(writer, currentTile);
	}

	/**
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 */
	private static ITranslator buildG2xplTranslator(Point2D currentTile,
			String folderPath) {
		return new G2xplTranslatorImpl(currentTile, folderPath);
	}

	/**
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 */
	private static ITranslator buildFlyLegacyTranslator(Point2D currentTile,
			String folderPath) {
		return new flyLegacyTranslatorImpl(currentTile, folderPath);
	}

	/**
	 * @param currentFile
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 * @throws Osm2xpBusinessException
	 */
	private static ITranslator buildXplane10Translator(File currentFile,
			Point2D currentTile, String folderPath) {

		GenerationStats stats = StatsHelper.initStats(currentFile, currentTile);
		String FacadeSetPath = Osm2xpConstants.FACADES_SETS_PATH
				+ File.separator
				+ XplaneOptionsHelper.getOptions().getFacadeSet();
		FacadeSet facadeSet = FacadeSetHelper.getFacadeSet(FacadeSetPath);
		DsfObjectsProvider dsfObjectsProvider = new DsfObjectsProvider(
				facadeSet);
		IWriter writer = new DsfWriterImpl(folderPath, dsfObjectsProvider);
		return new Xplane10TranslatorImpl(stats, writer, currentTile,
				folderPath, dsfObjectsProvider);
	}

	/**
	 * @param currentFile
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @param relationsList
	 * @return
	 * @throws Osm2xpBusinessException
	 */
	private static ITranslator buildXplane9Translator(File currentFile,
			Point2D currentTile, String folderPath, List<Relation> relationsList) {

		GenerationStats stats = StatsHelper.initStats(currentFile, currentTile);
		String FacadeSetPath = Osm2xpConstants.FACADES_SETS_PATH
				+ File.separator
				+ XplaneOptionsHelper.getOptions().getFacadeSet();
		FacadeSet facadeSet = FacadeSetHelper.getFacadeSet(FacadeSetPath);
		DsfObjectsProvider dsfObjectsProvider = new DsfObjectsProvider(
				facadeSet);
		IWriter writer = new DsfWriterImpl(folderPath, dsfObjectsProvider);
		return new Xplane9TranslatorImpl(stats, writer, currentTile,
				folderPath, dsfObjectsProvider);
	}
}
