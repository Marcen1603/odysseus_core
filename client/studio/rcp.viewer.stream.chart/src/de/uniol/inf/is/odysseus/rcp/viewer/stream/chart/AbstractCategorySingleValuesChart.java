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
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting.Type;

public abstract class AbstractCategorySingleValuesChart extends AbstractJFreeChart<Double, IMetaAttribute> {

	private DefaultCategoryDataset dcds = new DefaultCategoryDataset();

	private double max = 1.0;
	private int adjustCounter = 0;

	private int maxAdjustTimes = 100;
	private Double min = 0.0;
	private boolean autoadjust = false;

	private boolean keepUpperBound = true;

	private double maxBound = 1.0;

	protected CategoryDataset getDataset() {
		return dcds;
	}

	@Override
	protected void processElement(final List<Double> tuple, final IMetaAttribute metadata, final int port) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {

					for (int i = 0; i < getChoosenAttributes(port).size(); i++) {
						double value = tuple.get(i);
						recalcAxis(value);
						dcds.setValue(value, getChoosenAttributes(port).get(i).getName(), "");
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
		if (!autoadjust) {
			ValueAxis va = this.getChart().getCategoryPlot().getRangeAxis();
			va.setAutoRange(false);
			if (keepUpperBound) {
				maxBound  = Math.max(this.max, maxBound);
				if (value > maxBound || adjustCounter >= getMaxAdjustTimes()) {
					maxBound = value * 1.05;
					adjustCounter = 0;
				} else {
					adjustCounter++;
				}
				va.setUpperBound(maxBound);
			} else {
				va.setUpperBound(this.max);
			}
			va.setLowerBound(this.min);
		} else {
			ValueAxis va = this.getChart().getCategoryPlot().getRangeAxis();
			va.setAutoRange(true);
		}
	}

	@Override
	public void reloadChart() {
		dcds.clear();
	}

	@Override
	public String isValidSelection(Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

	@ChartSetting(name = "Lower Bound for Y-Axis", type = Type.SET)
	public void setMin(Double min) {
		this.min = min;
	}

	@ChartSetting(name = "Lower Bound for Y-Axis", type = Type.GET)
	public Double getMin() {
		return this.min;
	}

	@ChartSetting(name = "Upper Bound for Y-Axis", type = Type.SET)
	public void setMax(Double max) {
		this.max = max;
	}

	@ChartSetting(name = "Upper Bound for Y-Axis", type = Type.GET)
	public Double getMax() {
		return this.max;
	}

	@ChartSetting(name = "Autoadjust Bounds", type = Type.GET)
	public boolean isAutoadjust() {
		return autoadjust;
	}

	@ChartSetting(name = "Autoadjust Bounds", type = Type.SET)
	public void setAutoadjust(boolean autoadjust) {
		this.autoadjust = autoadjust;
	}

	@ChartSetting(name = "Keep upper bound for autoadjust", type = Type.GET)
	public boolean isKeepUpperBound() {
		return this.keepUpperBound;
	}

	@ChartSetting(name = "Keep upper bound for autoadjust", type = Type.SET)
	public void setKeepUpperBound(boolean keepUpperBound) {
		this.keepUpperBound = keepUpperBound;
	}

	@ChartSetting(name = "Keep upper bound during autoadjust only x elements", type = Type.GET)
	public int getMaxAdjustTimes() {
		return maxAdjustTimes;
	}

	@ChartSetting(name = "Keep upper bound during autoadjust only x elements", type = Type.SET)
	public void setMaxAdjustTimes(int maxAdjustTimes) {
		this.maxAdjustTimes = maxAdjustTimes;
	}

}
