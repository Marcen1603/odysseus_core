/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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
