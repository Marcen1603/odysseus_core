package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractXYChart;

public class ScatterPlotChart extends AbstractXYChart {

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createScatterPlot(getTitle(), "", "", super.getDataset(), PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}

	@Override
	protected void decorateChart(JFreeChart thechart) {	
		super.decorateChart(thechart);
		thechart.getXYPlot().getDomainAxis().setAutoRange(true);
		thechart.getXYPlot().getRangeAxis().setAutoRange(true);
		thechart.getXYPlot().setBackgroundPaint(DEFAULT_BACKGROUND);
		thechart.getXYPlot().setRangeGridlinePaint(DEFAULT_BACKGROUND_GRID);
	}
	
	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX+".scatterplotchart";
	}		
}
