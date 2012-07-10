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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public abstract class AbstractCategorySingleValuesChart extends
		AbstractJFreeChart<Double, IMetaAttribute> {

	private DefaultCategoryDataset dcds = new DefaultCategoryDataset();

	private double max = 0.0;
	private int adjustCounter = 0;

	private int maxAdjustTimes = 10;

	protected CategoryDataset getDataset() {
		return dcds;
	}

	@Override
	protected void processElement(final List<Double> tuple,
			final IMetaAttribute metadata, final int port) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {

					for (int i = 0; i < getChoosenAttributes(port).size(); i++) {
						double value = tuple.get(i);
						recalcAxis(value);
						dcds.setValue(value, getChoosenAttributes(port).get(i)
								.getName(), "");
					}
				} catch (SWTException e) {
					dispose();
					return;
				}
			}
		});
	}

	@Override
	protected void decorateChart(JFreeChart chart) {
		if (chart.getPlot() instanceof CategoryPlot) {
			CategoryPlot plot = chart.getCategoryPlot();
			chart.setAntiAlias(true);

			// change background colors
			chart.setBackgroundPaint(DEFAULT_BACKGROUND);

			plot.setBackgroundPaint(DEFAULT_BACKGROUND);
			plot.setRangeGridlinePaint(DEFAULT_BACKGROUND_GRID);
		} else {
			if (chart.getPlot() instanceof PiePlot) {
				PiePlot plot = (PiePlot) chart.getPlot();
				plot.setBackgroundPaint(DEFAULT_BACKGROUND);
			}
		}
	}

	private void recalcAxis(double value) {
		if (value > max || adjustCounter >= maxAdjustTimes) {
			max = value * 1.05;
			adjustCounter = 0;
		} else {
			adjustCounter++;
		}
		ValueAxis va = this.getChart().getCategoryPlot().getRangeAxis();
		va.setAutoRange(false);
		va.setUpperBound(max);
	}

	@Override
	public void chartSettingsChanged() {
		dcds.clear();
	}

	@Override
	public String isValidSelection(
			Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

}
