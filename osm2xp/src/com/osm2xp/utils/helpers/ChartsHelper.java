package com.osm2xp.utils.helpers;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import com.osm2xp.model.stats.BuildingStat;
import com.osm2xp.model.stats.ForestStat;
import com.osm2xp.model.stats.GenerationStats;
import com.osm2xp.model.stats.ObjectStat;

/**
 * ChartsHelper.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class ChartsHelper {

	public static JFreeChart getRecapPieChart(GenerationStats stats) {

		DefaultPieDataset dataset = new DefaultPieDataset();

		if (XplaneOptionsHelper.getOptions().isGenerateBuildings()) {
			dataset.setValue("Buildings" + "(" + stats.getBuildingsNumber()
					+ ")", stats.getBuildingsNumber());
		}
		if (XplaneOptionsHelper.getOptions().isGenerateFor()) {
			dataset.setValue("Forests" + "(" + stats.getForestsNumber() + ")",
					stats.getForestsNumber());
		}
		if (XplaneOptionsHelper.getOptions().isGenerateObj()) {
			dataset.setValue("Objects" + "(" + stats.getObjectsNumber() + ")",
					stats.getObjectsNumber());
		}
		if (XplaneOptionsHelper.getOptions().isGenerateStreetLights()) {
			dataset.setValue(
					"Street lights" + "(" + stats.getStreetlightsNumber() + ")",
					stats.getStreetlightsNumber());
		}
		JFreeChart chart = ChartFactory.createPieChart("Generated items",

		dataset, false, true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlinePaint(Color.WHITE);

		return chart;

	}

	public static JFreeChart getBuildingsPieChart(GenerationStats stats) {

		DefaultPieDataset dataset = new DefaultPieDataset();

		for (BuildingStat stat : stats.getBuildingsStats().getBuildingStat()) {
			dataset.setValue(stat.getType() + " (" + stat.getNumber() + " / "
					+ (stat.getNumber() * 100) / stats.getBuildingsNumber()
					+ "%)", stat.getNumber());
		}
		JFreeChart chart = ChartFactory.createPieChart(
				"Buildings/houses repartition",

				dataset, false, true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlinePaint(Color.WHITE);
		return chart;
	}

	public static JFreeChart getObjectsPieChart(GenerationStats stats) {

		DefaultPieDataset dataset = new DefaultPieDataset();

		for (ObjectStat stat : stats.getObjectsStats().getObjectStat()) {
			dataset.setValue(
					stat.getObjectPath() + " (" + stat.getNumber() + " / "
							+ (stat.getNumber() * 100)
							/ stats.getObjectsNumber() + "%)", stat.getNumber());
		}
		JFreeChart chart = ChartFactory.createPieChart("Objects files",
				dataset, true, true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelGenerator(null);
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlinePaint(Color.WHITE);
		return chart;
	}

	public static JFreeChart getForestsPieChart(GenerationStats stats) {

		DefaultPieDataset dataset = new DefaultPieDataset();

		for (ForestStat stat : stats.getForestsStats().getForestStat()) {
			dataset.setValue(
					stat.getForestPath() + " (" + stat.getNumber() + " / "
							+ (stat.getNumber() * 100)
							/ stats.getForestsNumber() + "%)", stat.getNumber());
		}
		JFreeChart chart = ChartFactory.createPieChart("Forests files",
				dataset, true, true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelGenerator(null);
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlinePaint(Color.WHITE);
		return chart;
	}
}
