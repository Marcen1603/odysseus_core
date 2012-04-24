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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

public abstract class AbstractTimeSeriesChart extends AbstractChart<Double, ITimeInterval> {

	protected Map<String, TimeSeries> series = new HashMap<String, TimeSeries>();

	protected TimeSeriesCollection dataset = new TimeSeriesCollection();

	private static final String TIME_NANO = "nanoseconds";
	private static final String TIME_MICRO = "microseconds";
	private static final String TIME_MILLI = "milliseconds";

	private double max = Double.NaN;
	private double min = Double.NaN;
	private boolean autoadjust = true;
	private double margin = 0.05; // 5 percent

	private static final int DEFAULT_MAX_NUMBER_OF_ITEMS = 100;
	private static final String DEFAULT_TIME_GRANULARITY = TIME_MILLI;

	private static final String CURRENT_TIME = "Current Time";
	private int maxItems = DEFAULT_MAX_NUMBER_OF_ITEMS;

	// also milli
	private String dateformat = "HH:mm:ss";

	private int choosenXValue = -1;

	private Integer timefactor = 1;

	private String timeinputgranularity = DEFAULT_TIME_GRANULARITY;

	@Override
	public void chartSettingsChanged() {
		series.clear();
		this.dataset.removeAllSeries();
		for (int i = 0; i < getChoosenAttributes().size(); i++) {
			String name = getChoosenAttributes().get(i).getName();
			TimeSeries serie = new TimeSeries(name);
			serie.setMaximumItemCount(this.maxItems);
			series.put(name, serie);
			this.dataset.addSeries(serie);
		}
		ValueAxis domainAxis = getChart().getXYPlot().getDomainAxis();
		if (domainAxis instanceof NumberAxis) {
			NumberAxis axis = (NumberAxis) getChart().getXYPlot().getDomainAxis();
			axis.setNumberFormatOverride(new SimpleNumberToDateFormat(this.dateformat));
		}
		if(domainAxis instanceof DateAxis){
			DateAxis axis = (DateAxis) getChart().getXYPlot().getDomainAxis();
			axis.setDateFormatOverride(new SimpleDateFormat(this.dateformat));
		}
		if (this.timeinputgranularity.equals(TIME_MILLI)) {
			this.timefactor = 1;
		} else if (this.timeinputgranularity.equals(TIME_MICRO)) {
			this.timefactor = 1000;
		} else if (this.timeinputgranularity.equals(TIME_NANO)) {
			this.timefactor = 1000000;
		}

		min = Double.NaN;
		max = Double.NaN;
	}

	@Override
	public String isValidSelection(List<IViewableAttribute> selectAttributes) {
		if (selectAttributes.size() > 0) {
			return null;
		}
		return "The number of choosen attributes should be at least one!";
	}

	@Override
	protected void processElement(final List<Double> tuple, final ITimeInterval metadata, int port) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					if (choosenXValue == -1) {
						long time = metadata.getStart().getMainPoint();
						long millis = time / timefactor;
						FixedMillisecond ms = new FixedMillisecond(millis);

						for (int i = 0; i < tuple.size(); i++) {
							double value = tuple.get(i);
							series.get(getChoosenAttributes().get(i).getName()).add(ms, value);
							adjust(value);
						}
					} else {
						for (int i = 0; i < tuple.size(); i++) {
							double value = tuple.get(i);
							long x = tuple.get(choosenXValue).longValue();
							FixedMillisecond ms = new FixedMillisecond(x);
							series.get(getChoosenAttributes().get(i).getName()).add(ms, value);
							adjust(value);
						}
					}

				} catch (SWTException ex) {
					// widget disposed
					dispose();
					return;
				} catch (SeriesException e) {
					// System.out.println("Warn: " + e.getLocalizedMessage());
				} catch (Exception e) {
					e.printStackTrace();

				}
			}

		});
	}

	private void adjust(double value) {
		if (Double.isNaN(max)) {
			max = value;
		}
		if (Double.isNaN(min)) {
			min = value;
		}
		if (value > max) {
			max = value;
		} else if (value < min) {
			min = value;
		}
		if (autoadjust) {
			getChart().getXYPlot().getRangeAxis().setLowerBound(min * (1.0 - margin));
			getChart().getXYPlot().getRangeAxis().setUpperBound(max * (1.0 + margin));
		}

	}

	@Override
	protected void decorateChart(JFreeChart thechart) {
		thechart.setBackgroundPaint(DEFAULT_BACKGROUND);
		thechart.getXYPlot().setBackgroundPaint(DEFAULT_BACKGROUND);
		thechart.getXYPlot().setRangeGridlinePaint(DEFAULT_BACKGROUND_GRID);
	}

	@ChartSetting(name = "Max Shown Items", type = Type.GET)
	public Integer getMaxItems() {
		return maxItems;
	}

	@ChartSetting(name = "Max Shown Items", type = Type.SET)
	public void setMaxItems(Integer maxItems) {
		this.maxItems = maxItems;
	}

	@ChartSetting(name = "Date Time Format", type = Type.GET)
	public String getDateFormat() {
		return this.dateformat;
	}

	@ChartSetting(name = "Date Time Format", type = Type.SET)
	public void setDateFormat(String dateFormat) {
		this.dateformat = dateFormat;
	}

	@ChartSetting(name = "Time Input Granularity", type = Type.OPTIONS)
	public List<String> getTimeInputGranularityValues() {
		List<String> values = new ArrayList<String>();
		values.add(TIME_MILLI);
		values.add(TIME_MICRO);
		values.add(TIME_NANO);
		return values;
	}

	@ChartSetting(name = "Time Input Granularity", type = Type.GET)
	public String getTimeInputGranularity() {
		return timeinputgranularity;
	}

	@ChartSetting(name = "Time Input Granularity", type = Type.SET)
	public void setTimeInputGranularity(String value) {
		this.timeinputgranularity = value;
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.OPTIONS)
	public List<String> getXValues() {
		return getValues();
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(CURRENT_TIME);
		for (IViewableAttribute a : getViewableAttributes()) {
			values.add(a.getName());
		}

		return values;
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.GET)
	public String getXValue() {
		if (this.choosenXValue == -1) {
			return CURRENT_TIME;
		}
		return getViewableAttributes().get(this.choosenXValue).getName();
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.SET)
	public void setXValue(String value) {
		if (value.equalsIgnoreCase(CURRENT_TIME)) {
			this.choosenXValue = -1;
			return;
		}
		for (int i = 0; i < getViewableAttributes().size(); i++) {
			if (getViewableAttributes().get(i).getName().equals(value)) {
				this.choosenXValue = i;
				return;
			}
		}
	}
	
	@ChartSetting(name = "Lower Bound for Y-Axis", type = Type.SET)
	public void setMin(Double min){
		this.min = min;
	}
	@ChartSetting(name = "Lower Bound for Y-Axis", type = Type.GET)
	public Double getMin(){
		return this.min;
	}
	

	@ChartSetting(name = "Upper Bound for Y-Axis", type = Type.SET)
	public void setMax(Double max){
		this.max = max;
	}
	
	@ChartSetting(name = "Upper Bound for Y-Axis", type = Type.GET)
	public Double getMax(){
		return this.max;
	}

}
