package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractCategorySingleValuesChart;

public class BarChart extends AbstractCategorySingleValuesChart {

	
	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createBarChart(getTitle(), "", "", getDataset(), PlotOrientation.VERTICAL, true, true, false);		
		return chart;		
	}
	
	@Override
	protected void decorateChart(JFreeChart chart) {	
		super.decorateChart(chart);
		// disable outline and set normal bar painter
		BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
		renderer.setDrawBarOutline(true);
		renderer.setBarPainter(new StandardBarPainter());
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX+".barchart";
	}

}
