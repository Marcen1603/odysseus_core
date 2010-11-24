package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractCategoryTimeSeriesChart;

public class AreaChart extends AbstractCategoryTimeSeriesChart{

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createAreaChart(getTitle(), "", "", getDataset(), PlotOrientation.VERTICAL, true, true, false);	
		return chart;				
	}
	
	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX+".areachart";
	}	

}
