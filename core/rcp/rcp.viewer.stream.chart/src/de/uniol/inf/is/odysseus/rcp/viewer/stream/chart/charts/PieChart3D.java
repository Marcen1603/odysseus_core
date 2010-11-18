package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

public class PieChart3D extends PieChart {

	@Override
	protected JFreeChart createChart() {

		JFreeChart chart = ChartFactory.createPieChart3D(getTitle(), dataset, true, true, false);
		return chart;
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX + ".piechart3d";
	}
}
