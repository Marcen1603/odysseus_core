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
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractCategorySingleValuesChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

public class BarChart extends AbstractCategorySingleValuesChart {

	
	private Boolean autoadjust = true;


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
	
	@ChartSetting(name = "Autoadjust Axis", type=Type.GET)
	public Boolean getAutoAdjust(){
		return this.autoadjust;
	}
	
	@ChartSetting(name = "Autoadjust Axis", type=Type.SET)
	public void setAutoAdjust(Boolean autoadjust){
		this.autoadjust = autoadjust;		
	}
	
	@Override
	public void chartSettingsChanged() {	
		super.chartSettingsChanged();
		getChart().getCategoryPlot().getRangeAxis().setAutoRange(this.autoadjust);
	}

}
