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

/**
 * 
 * @author Dennis Geesen Created at: 18.04.2012
 */
public class LinearFunctionChart extends AbstractJFreeChart<Double, IMetaAttribute> {

	private XYSeriesCollection dataset = new XYSeriesCollection();
	private int choosenXValue = 0;
	private int choosenYValue = 1;
	private double min = -1;
	private double max = 3;

	@Override
	public void chartSettingsChanged() {
		getChart().getXYPlot().getRangeAxis().setAutoRange(false);
		resetRanges();

	}
	
	
	private void resetRanges(){
		getChart().getXYPlot().getRangeAxis().setLowerBound(min);
		getChart().getXYPlot().getRangeAxis().setUpperBound(max);
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
					double m = tuple.get(choosenXValue);
					double b = tuple.get(choosenYValue);
					XYSeries series = new XYSeries("f");
					if(b<min){
						min = b;
					}
					if((m+b)>max){
						max = m+b;
					}
					resetRanges();
					series.add(0, b);
					series.add(1, m + b);
					dataset.addSeries(series);
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

}
