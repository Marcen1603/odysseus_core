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

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting.Type;

public abstract class AbstractCategoryTimeSeriesChart extends AbstractJFreeChart<Double, ITimeInterval> {

	private DefaultCategoryDataset dcds = new DefaultCategoryDataset();

	private double max = 0.0;

	private static final int DEFAULT_MAX_NUMBER_OF_ITEMS = 10;

	private int maxItems = DEFAULT_MAX_NUMBER_OF_ITEMS;

	protected CategoryDataset getDataset() {
		return dcds;
	}

	@Override
	protected void processElement(final List<Double> tuple, final ITimeInterval metadata, final int port) {
		try {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {

						int i = 0;
						for (IViewableAttribute a : getChoosenAttributes(port)) {
							double value = tuple.get(i);
							recalcAxis(value);
							dcds.addValue(value, a.getName(), metadata.getStart());

							i++;
						}
					} catch (SWTException e) {
						// we assume that we dispose.
						dispose();
						return;
					}

					if (dcds.getColumnCount() > maxItems) {
						dcds.removeColumn(0);
					}
				}
			});
		} catch (SWTException swtex) {
			System.out.println("WARN: SWT Exception " + swtex.getMessage());
		}
	}

	@Override
	protected void decorateChart(JFreeChart chart) {
		CategoryPlot plot = chart.getCategoryPlot();
		chart.setAntiAlias(true);

		// change background colors
		chart.setBackgroundPaint(Color.WHITE);

		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
	}

	private void recalcAxis(double value) {
		if (value > max) {
			max = value * 1.05;
		}
		ValueAxis va = this.getChart().getCategoryPlot().getRangeAxis();
		va.setAutoRange(false);
		va.setUpperBound(max);
	}

	@Override
	public void reloadChart() {
	}

	@Override
	public String isValidSelection(Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

	@ChartSetting(name = "Max Shown Items", type = Type.GET)
	public int getMaxItems() {
		return maxItems;
	}

	@ChartSetting(name = "Max Shown Items", type = Type.SET)
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

}
