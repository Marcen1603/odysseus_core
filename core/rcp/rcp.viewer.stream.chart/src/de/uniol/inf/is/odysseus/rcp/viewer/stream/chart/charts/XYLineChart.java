package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractTimeSeriesChart;

public class XYLineChart extends AbstractTimeSeriesChart {

	@Override
	protected JFreeChart createChart() {		
		JFreeChart chart = ChartFactory.createXYLineChart(getTitle(), "", "", this.dataset, PlotOrientation.VERTICAL, true, true, false);
		chart.getPlot().setBackgroundPaint(DEFAULT_BACKGROUND);			
		return chart;
	}
	
	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX+".xylinechart";
	}

}
