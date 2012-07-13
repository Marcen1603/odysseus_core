/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2012 The Odysseus Team
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

/**
 * 
 * @author Dennis Geesen Created at: 18.04.2012
 */
public class LinearFunctionChart extends AbstractJFreeChart<Double, IMetaAttribute> {

	private XYSeriesCollection dataset = new XYSeriesCollection();
	private int slope = 0;
	private int intercept = 1;
	private Integer choosenSlopePort = 0;
	private Integer choosenInterceptPort = 0;
	double lowerY = -1;
	double upperY = 3;
	double lowerX = -1;
	double upperX = 3;

	@Override
	public void chartSettingsChanged() {
		getChart().getXYPlot().getRangeAxis().setAutoRange(false);
		getChart().getXYPlot().getDomainAxis().setAutoRange(false);
		resetRanges();
	}
	
	private void resetRanges(){
		getChart().getXYPlot().getRangeAxis().setLowerBound(lowerY);
		getChart().getXYPlot().getRangeAxis().setUpperBound(upperY);
		getChart().getXYPlot().getDomainAxis().setLowerBound(lowerX);
		getChart().getXYPlot().getDomainAxis().setUpperBound(upperX);			
	}

	private void calcRanges(double x1, double y1, double x2, double y2){				
		// positive slope		
		if(y1>=y2){
			lowerY = y2;
			upperY = y1;
		}else{
			lowerY = y2;
			upperY = y1;
		}
		lowerX = x1;
		upperX = x2;		
		System.out.println("\t x \tLower: "+lowerX);
		System.out.println("\t x \tUpper: "+upperX);
		System.out.println("\t y \tLower: "+lowerY);
		System.out.println("\t y \tUpper: "+upperY);
		
	}
	
	@Override
	public String isValidSelection(
			Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

	@Override
	protected void processElement(final List<Double> tuple, IMetaAttribute metadata, int port) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					dataset.removeAllSeries();
					double m = tuple.get(slope);
					double b = tuple.get(intercept);
					XYSeries series = new XYSeries("f");					
					
					double x1 = 0;
					double y1 = b;
					double x2 = Math.abs(m); 
					double y2 = m+x2+b;
					series.add(x1, y1);					
					series.add(x2, y2);
					calcRanges(x1, y1, x2, y2);					
					dataset.addSeries(series);
					resetRanges();
				} catch (SWTException e) {
					dispose();
					return;
				}
			}
		});

	}

	@Override
	protected void decorateChart(JFreeChart thechart) {

	}

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createXYLineChart(getTitle(), "", "", this.dataset, PlotOrientation.VERTICAL, true, true, false);
		chart.getPlot().setBackgroundPaint(DEFAULT_BACKGROUND);
		return chart;
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX + ".linearFunctionChart";
	}
	
	
	@ChartSetting(name = "Value for slope", type = Type.OPTIONS)
	public List<String> getSlopeValues() {
		return getValues();
	}

	@ChartSetting(name = "Value for slope", type = Type.GET)
	public String getSlopeValue() {
		return getViewableAttributes(this.choosenSlopePort).get(this.slope).getName();
	}

	@ChartSetting(name = "Value for slope", type = Type.SET)
	public void setSlopeValue(String value) {
		for (Integer port : getPorts()) {
			for (int i = 0; i < getViewableAttributes(port).size(); i++) {
				if (getViewableAttributes(port).get(i).getName().equals(value)) {
					this.slope = i;
					this.choosenSlopePort = port;
					return;
				}
			}
		}
	}

	@ChartSetting(name = "Value for intercept", type = Type.OPTIONS)
	public List<String> getInterceptValues() {
		return getValues();
	}

	@ChartSetting(name = "Value for intercept", type = Type.GET)
	public String getInterceptValue() {
		return getViewableAttributes(choosenInterceptPort).get(this.intercept).getName();
	}

	@ChartSetting(name = "Value for intercept", type = Type.SET)
	public void setInterceptValue(String value) {
		for (Integer port : getPorts()) {
			for (int i = 0; i < getViewableAttributes(port).size(); i++) {
				if (getViewableAttributes(port).get(i).getName().equals(value)) {
					this.intercept = i;
					this.choosenInterceptPort = port;
					return;
				}
			}
		}
	}
	
	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		for (Integer port : getPorts()) {
			for (IViewableAttribute a : getViewableAttributes(port)) {
				values.add(a.getName());
			}
		}
		return values;
	}


}
