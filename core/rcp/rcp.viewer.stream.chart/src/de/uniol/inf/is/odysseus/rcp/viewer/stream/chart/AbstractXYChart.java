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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

public abstract class AbstractXYChart extends AbstractJFreeChart<Double, IMetaAttribute> {

	private XYSeriesCollection dataset = new XYSeriesCollection();
	private int choosenXValue;
	private int choosenYValue;
	private int choosenSerie = -1;
	private Integer choosenXValuePort = 0;
	private Integer choosenYValuePort = 0;
	private Integer choosenSeriePort = 0;
	private boolean considerTimeValidity = false;
	private PointInTime lastPurge = PointInTime.getZeroTime();

	// private double maxX = Double.NaN;
	// private double maxY = Double.NaN;
	//
	// private double minX = Double.NaN;
	// private double minY = Double.NaN;

	private double maxX = 55000;
	private double maxY = 34000;

	private double minX = 0;
	private double minY = -34000;

	private int maxValues = Integer.MAX_VALUE;

	private boolean autoadjust = true;
	private double margin = 0.05; // 5 percent

	private int redrawEach = 0;
	private int count = 0;

	@Override
	public String isValidSelection(Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		int sel = 0;
		for (Entry<Integer, Set<IViewableAttribute>> e : selectAttributes.entrySet()) {
			sel += e.getValue().size();
		}
		if (sel == 2) {
			return null;
		}
		return "The number of choosen attributes should be at least one!";
	}

	private void adjust(double valueX, double valueY) {
		// for X
		if (Double.isNaN(maxX)) {
			maxX = valueX;
		}
		if (Double.isNaN(minX)) {
			minX = valueX;
		}
		if (valueX > maxX) {
			maxX = valueX;
		} else if (valueX < minX) {
			minX = valueX;
		}

		// for Y
		if (Double.isNaN(maxY)) {
			maxY = valueY;
		}
		if (Double.isNaN(minY)) {
			minY = valueY;
		}
		if (valueY > maxY) {
			maxY = valueY;
		} else if (valueY < minY) {
			minY = valueY;
		}

		if (autoadjust) {
			getChart().getXYPlot().getRangeAxis().setLowerBound(minY * (1.0 - margin));
			getChart().getXYPlot().getRangeAxis().setUpperBound(maxY * (1.0 + margin));

			getChart().getXYPlot().getDomainAxis().setLowerBound(minX * (1.0 - margin));
			getChart().getXYPlot().getDomainAxis().setUpperBound(maxX * (1.0 + margin));
		}

	}

	@Override
	protected void init() {
		choosenXValue = 0;
		choosenYValue = 1;
	}

	@Override
	public void chartSettingsChanged() {
		if (!autoadjust) {
			if (!Double.isNaN(minY)) {
				getChart().getXYPlot().getRangeAxis().setLowerBound(minY * (1.0 - margin));
			}
			if (!Double.isNaN(maxY)) {
				getChart().getXYPlot().getRangeAxis().setUpperBound(maxY * (1.0 + margin));
			}
			if (!Double.isNaN(minX)) {
				getChart().getXYPlot().getDomainAxis().setLowerBound(minX * (1.0 - margin));
			}
			if (!Double.isNaN(maxX)) {
				getChart().getXYPlot().getDomainAxis().setUpperBound(maxX * (1.0 + margin));
			}
		}

		for (int i = 0; i < this.dataset.getSeriesCount(); i++) {
			XYSeries serie = this.dataset.getSeries(i);
			while (serie.getItemCount() > this.maxValues) {
				serie.remove(0);
			}
		}

	}

	@Override
	protected void processElement(final List<Double> tuple, final IMetaAttribute metadata, int port) {

		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					if (considerTimeValidity) {
						if (metadata instanceof TimeInterval) {
							PointInTime start = ((TimeInterval) metadata).getStart();
							PointInTime end = ((TimeInterval) metadata).getEnd();
							if (start.afterOrEquals(lastPurge)) {
								dataset.removeAllSeries();
								lastPurge = end;
							}

						}
					}
					String key = "-";
					XYSeries currentserie;
					if (choosenSerie == -1) {
						key = "-";
					} else {
						key = tuple.get(choosenSerie).toString();
					}

					if (!containsSeriesWithKey(key)) {
						currentserie = new XYSeries(key);
						currentserie.setMaximumItemCount(maxValues);
						dataset.addSeries(currentserie);
					} else {
						currentserie = dataset.getSeries(key);
					}
					double x = tuple.get(choosenXValue);
					double y = tuple.get(choosenYValue);
					count++;
					if (count > redrawEach) {
						count = 0;
						currentserie.add(x, y, true);
						adjust(x, y);
					} else {
						currentserie.add(x, y, false);
					}

				} catch (SWTException e) {
					dispose();
					return;
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		});

	}

	private boolean containsSeriesWithKey(Comparable<?> key) {
		for (Object o : dataset.getSeries()) {
			XYSeries s = (XYSeries) o;
			if (s.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void decorateChart(JFreeChart thechart) {

	}

	@Override
	protected abstract JFreeChart createChart();

	@Override
	public abstract String getViewID();

	@ChartSetting(name = "Value for X-Axis", type = Type.OPTIONS)
	public List<String> getXValues() {
		return getValues();
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.GET)
	public String getXValue() {
		return getChoosenAttributes(this.choosenXValuePort).get(this.choosenXValue).getName();
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.SET)
	public void setXValue(String value) {
		for (Integer port : getPorts()) {
			for (int i = 0; i < getChoosenAttributes(port).size(); i++) {
				if (getChoosenAttributes(port).get(i).getName().equals(value)) {
					this.choosenXValue = i;
					this.choosenXValuePort = port;
					return;
				}
			}
		}
	}

	@ChartSetting(name = "Value for Y-Axis", type = Type.OPTIONS)
	public List<String> getYValues() {
		return getValues();
	}

	@ChartSetting(name = "Value for Y-Axis", type = Type.GET)
	public String getYValue() {
		return getChoosenAttributes(choosenYValuePort).get(this.choosenYValue).getName();
	}

	@ChartSetting(name = "Value for Y-Axis", type = Type.SET)
	public void setYValue(String value) {
		for (Integer port : getPorts()) {
			for (int i = 0; i < getChoosenAttributes(port).size(); i++) {
				if (getChoosenAttributes(port).get(i).getName().equals(value)) {
					this.choosenYValue = i;
					this.choosenYValuePort = port;
					return;
				}
			}
		}
	}

	@ChartSetting(name = "Value for Series", type = Type.OPTIONS)
	public List<String> getSeriesValues() {
		List<String> values = getValues();
		values.add(0, "");
		return values;
	}

	@ChartSetting(name = "Value for Series", type = Type.GET)
	public String getSeriesValue() {
		if (this.choosenSerie > -1) {
			return getChoosenAttributes(choosenSeriePort).get(this.choosenSerie).getName();
		}

		return "";
	}

	@ChartSetting(name = "Value for Series", type = Type.SET)
	public void setSeriesValue(String value) {
		if (value.equals("")) {
			this.choosenSerie = -1;
		} else {
			for (Integer port : getPorts()) {
				for (int i = 0; i < getChoosenAttributes(port).size(); i++) {
					if (getChoosenAttributes(port).get(i).getName().equals(value)) {
						this.choosenSerie = i;
						this.choosenSeriePort = port;
						return;
					}
				}
			}
		}
	}

	@ChartSetting(name = "Consider Time Validity", type = Type.SET)
	public void setTimeValidity(Boolean value) {
		this.considerTimeValidity = value;
	}

	@ChartSetting(name = "Consider Time Validity", type = Type.GET)
	public Boolean setTimeValidity() {
		return this.considerTimeValidity;
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

	public XYDataset getDataset() {
		return this.dataset;
	}

	@ChartSetting(name = "Upper Bound for X-Axis", type = Type.GET)
	public double getMaxX() {
		return maxX;
	}

	@ChartSetting(name = "Upper Bound for X-Axis", type = Type.SET)
	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	@ChartSetting(name = "Upper Bound for Y-Axis", type = Type.GET)
	public double getMaxY() {
		return maxY;
	}

	@ChartSetting(name = "Upper Bound for Y-Axis", type = Type.SET)
	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	@ChartSetting(name = "Lower Bound for X-Axis", type = Type.GET)
	public double getMinX() {
		return minX;
	}

	@ChartSetting(name = "Lower Bound for X-Axis", type = Type.SET)
	public void setMinX(double minX) {
		this.minX = minX;
	}

	@ChartSetting(name = "Lower Bound for Y-Axis", type = Type.GET)
	public double getMinY() {
		return minY;
	}

	@ChartSetting(name = "Lower Bound for Y-Axis", type = Type.SET)
	public void setMinY(double minY) {
		this.minY = minY;
	}

	@ChartSetting(name = "Autoadjust Bounds", type = Type.GET)
	public Boolean isAutoadjust() {
		return autoadjust;
	}

	@ChartSetting(name = "Autoadjust Bounds", type = Type.SET)
	public void setAutoadjust(Boolean autoadjust) {
		this.autoadjust = autoadjust;
	}

	@ChartSetting(name = "Show maximum values", type = Type.GET)
	public int getMaxValues() {
		return maxValues;
	}

	@ChartSetting(name = "Show maximum values", type = Type.SET)
	public void setMaxValues(int maxValues) {
		this.maxValues = maxValues;
	}

	@ChartSetting(name = "Redraw after each", type = Type.GET)
	public int getRedrawEach() {
		return redrawEach;
	}

	@ChartSetting(name = "Redraw after each", type = Type.SET)
	public void setRedrawEach(int redrawEach) {
		this.redrawEach = redrawEach;
	}

}
