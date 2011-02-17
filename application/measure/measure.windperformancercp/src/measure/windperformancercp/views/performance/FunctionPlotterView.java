/** Copyright [2011] [The Odysseus Team]
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
package measure.windperformancercp.views.performance;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

public class FunctionPlotterView extends ViewPart {
	public static final String ID = "measure.windperformancercp.functionPlotterView";

	String title = "Test";
	String xLabel = "Windspeed";
	String yLabel = "Power";
	
    
	@Override
	public void createPartControl(Composite parent) {
		
		XYSeriesCollection dataset = new XYSeriesCollection();

		XYSeries series1 = new XYSeries("Series 1");
		series1.add(1.0, 4.5);
		series1.add(4.4, 3.2);
		dataset.addSeries(series1);

		XYSeries series2 = new XYSeries("Series 2");
		series2.add(3.2, 8.5);
		series2.add(4.9, 3.7);
		dataset.addSeries(series2); 
		
		JFreeChart chart = createScatterPlot(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		
		new ChartComposite(parent, SWT.NONE,
				chart, true);

		
	}
	
	
	public JFreeChart createScatterPlot(XYSeriesCollection dataset){
		JFreeChart chart = ChartFactory.createScatterPlot("Test", 
				"Windspeed", 
				"Power", 
				dataset, 
				PlotOrientation.VERTICAL, 
				true, 
				false, 
				false);
		return chart;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
