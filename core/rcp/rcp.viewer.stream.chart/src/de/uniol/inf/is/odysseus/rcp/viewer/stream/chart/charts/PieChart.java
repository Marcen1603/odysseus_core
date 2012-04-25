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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public class PieChart extends AbstractChart<Double, IMetaAttribute> {

	DefaultPieDataset dataset = new DefaultPieDataset();

	@Override
	protected JFreeChart createChart() {

		JFreeChart chart = ChartFactory.createPieChart(getTitle(), dataset, true, true, false);
		return chart;
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX + ".piechart";
	}

	@Override
	public void chartSettingsChanged() {

	}

	@Override
	protected void processElement(final List<Double> tuple, IMetaAttribute metadata, final int port) {		
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {					
					for(int i=0;i<getChoosenAttributes(port).size();i++){
						double value = tuple.get(i);						
						dataset.setValue(getChoosenAttributes(port).get(i).getName(), value);																
					}										
				} catch (SWTException e) {								
					dispose();
					return;
				}
			}
		});

	}

	@Override
	protected void decorateChart(JFreeChart thechart) {
		PiePlot plot = (PiePlot) thechart.getPlot();
		plot.setBackgroundPaint(DEFAULT_BACKGROUND);
		plot.setLabelGenerator(null);

	}

	@Override
	public String isValidSelection(
			Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

}
