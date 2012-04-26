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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

public abstract class AbstractXYChart extends
		AbstractJFreeChart<Double, IMetaAttribute> {

	private XYSeriesCollection dataset = new XYSeriesCollection();
	private int choosenXValue;
	private int choosenYValue;
	private int choosenSerie = -1;
	private Integer choosenXValuePort;
	private Integer choosenYValuePort;
	private Integer choosenSeriePort;

	@Override
	public String isValidSelection(
			Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		int sel = 0;
		for (Entry<Integer, Set<IViewableAttribute>> e : selectAttributes
				.entrySet()) {
			sel += e.getValue().size();
		}
		if (sel == 2) {
			return null;
		}
		return "The number of choosen attributes should be at least one!";
	}

	@Override
	protected void init() {
		choosenXValue = 0;
		choosenYValue = 1;
	}

	@Override
	public void chartSettingsChanged() {
	}

	@Override
	protected void processElement(final List<Double> tuple,
			IMetaAttribute metadata, int port) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					String key = "-";
					XYSeries currentserie;
					if (choosenSerie == -1) {
						key = "-";
					} else {
						key = tuple.get(choosenSerie).toString();
					}

					if (!containsSeriesWithKey(key)) {
						currentserie = new XYSeries(key);
						dataset.addSeries(currentserie);
					} else {
						currentserie = dataset.getSeries(key);
					}

					currentserie.add(tuple.get(choosenXValue),
							tuple.get(choosenYValue));
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
		return getViewableAttributes(this.choosenXValuePort).get(
				this.choosenXValue).getName();
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.SET)
	public void setXValue(String value) {
		for (Integer port : getPorts()) {
			for (int i = 0; i < getViewableAttributes(port).size(); i++) {
				if (getViewableAttributes(port).get(i).getName().equals(value)) {
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
		return getViewableAttributes(choosenYValuePort).get(this.choosenYValue)
				.getName();
	}

	@ChartSetting(name = "Value for Y-Axis", type = Type.SET)
	public void setYValue(String value) {
		for (Integer port : getPorts()) {
			for (int i = 0; i < getViewableAttributes(port).size(); i++) {
				if (getViewableAttributes(port).get(i).getName().equals(value)) {
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
			return getViewableAttributes(choosenSeriePort).get(
					this.choosenSerie).getName();
		}

		return "";
	}

	@ChartSetting(name = "Value for Series", type = Type.SET)
	public void setSeriesValue(String value) {
		if (value.equals("")) {
			this.choosenSerie = -1;
		} else {
			for (Integer port : getPorts()) {
				for (int i = 0; i < getViewableAttributes(port).size(); i++) {
					if (getViewableAttributes(port).get(i).getName()
							.equals(value)) {
						this.choosenSerie = i;
						this.choosenSeriePort = port;
						return;
					}
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

	public XYDataset getDataset() {
		return this.dataset;
	}

}
