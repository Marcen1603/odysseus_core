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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

public abstract class AbstractTimeSeriesChart extends AbstractJFreeChart<Double, ITimeInterval> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractTimeSeriesChart.class);
	
	private Map<String, TimeSeries> series = new HashMap<String, TimeSeries>();

	protected TimeSeriesCollection dataset = new TimeSeriesCollection();

	private static final String TIME_PICO = "picoseconds";
	private static final String TIME_NANO = "nanoseconds";
	private static final String TIME_MICRO = "microseconds";
	private static final String TIME_MILLI = "milliseconds";
	private static final String TIME_SECONDS = "seconds";
	private static final String TIME_MINUTES = "minutes";

	private double max = Double.NaN;
	private double min = Double.NaN;
	private boolean autoadjust = true;
	private double margin = 0.05; // 5 percent
	private String xTitle = "";
	private String yTitle = "";

	private static final int DEFAULT_MAX_NUMBER_OF_ITEMS = 1000;
	private static final String DEFAULT_TIME_GRANULARITY = TIME_MILLI;

	private static final String CURRENT_TIME = "Current Time";
	private int maxItems = DEFAULT_MAX_NUMBER_OF_ITEMS;

	// also milli
	private String dateformat = "HH:mm:ss";
	private int choosenXValue = -1;
	private Double timefactor = 1.0;
	private String timeinputgranularity = DEFAULT_TIME_GRANULARITY;
	private Integer choosenXValuePort = 0;

	private long updateIntervalMillis = 0;
	private final List<Tuple<ITimeInterval>> bufferedTuples = Lists.newArrayList();
	private final List<Integer> bufferedPorts = Lists.newArrayList();
	private ChartUpdater chartUpdater;

	@Override
	public void reloadChart() {
		series.clear();
		this.dataset.removeAllSeries();
		for (Integer port : getPorts()) {
			for (int i = 0; i < getChoosenAttributes(port).size(); i++) {
				String name = getChoosenAttributes(port).get(i).getName();
				TimeSeries serie = new TimeSeries(name);
				serie.setMaximumItemCount(this.maxItems);
				series.put(name, serie);
				this.dataset.addSeries(serie);
			}
		}
		
		ValueAxis domainAxis = getChart().getXYPlot().getDomainAxis();
		domainAxis.setLabel(xTitle);
		if (domainAxis instanceof NumberAxis) {
			NumberAxis axis = (NumberAxis) getChart().getXYPlot().getDomainAxis();
			axis.setNumberFormatOverride(new SimpleNumberToDateFormat(this.dateformat));
		}
		if (domainAxis instanceof DateAxis) {
			DateAxis axis = (DateAxis) getChart().getXYPlot().getDomainAxis();
			axis.setDateFormatOverride(new SimpleDateFormat(this.dateformat));
		}
		
		getChart().getXYPlot().getRangeAxis().setLabel(yTitle);
		
		if (this.timeinputgranularity.equals(TIME_MILLI)) {
			this.timefactor = 1.0;
		} else if (this.timeinputgranularity.equals(TIME_MICRO)) {
			this.timefactor = 1000.0;
		} else if (this.timeinputgranularity.equals(TIME_NANO)) {
			this.timefactor = 1000000.0;
		} else if (this.timeinputgranularity.equals(TIME_PICO)) {
			this.timefactor = 1000000000.0;
		} else if (this.timeinputgranularity.equals(TIME_SECONDS)) {
			this.timefactor = 0.001;
		}else if (this.timeinputgranularity.equals(TIME_MINUTES)) {
			this.timefactor = 1.67777e-5;
		}
	}

	@Override
	public String isValidSelection(Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

	@Override
	protected void processElement(List<Double> tuple, ITimeInterval metadata, int port) {
		// this is not needed, since streamElementReceived is overwritten!
	}

	@SuppressWarnings("unchecked")
	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, final int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.warn("Stream visualization is only for relational tuple, not for {}!", element.getClass());
			return;
		}
		
		if( getChart() == null ) {
			return;
		}

		Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) element;

		if (isAsyncUpdate()) {
			addToBuffers(port, tuple);
		} else {
			processElement(port, tuple, true);
		}
	}

	private boolean isAsyncUpdate() {
		return updateIntervalMillis > 0;
	}

	private void processElement(final int port, final Tuple<ITimeInterval> tuple, final boolean update) {
		final List<Double> viewableValues = this.viewSchema.get(port).convertToViewableFormat(tuple);
		final List<?> values = this.viewSchema.get(port).convertToChoosenFormat(viewableValues);
		Display display = PlatformUI.getWorkbench().getDisplay();

		if (getChartComposite().isDisposed()) {
			return;
		}
		
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				if (getChartComposite().isDisposed()) {
					return;
				}
				
				try {
					if (choosenXValue == -1) {
						long time = tuple.getMetadata().getStart().getMainPoint();
						long millis = Math.round(time / timefactor);
						FixedMillisecond ms = new FixedMillisecond(millis);

						for (int i = 0; i < values.size(); i++) {
							double value = ((Number) values.get(i)).doubleValue();
							addToSeries(port, update, ms, i, value);
							adjust(value);
						}
					} else {
						for (int i = 0; i < values.size(); i++) {
							double value = ((Number) values.get(i)).doubleValue();
							long x = ((Number) viewableValues.get(choosenXValue)).longValue();
							FixedMillisecond ms = new FixedMillisecond(x);
							addToSeries(port, update, ms, i, value);
							adjust(value);
						}
					}
					
					

				} catch (SWTException ex) {
					LOG.error("Exception in adding data into chart", ex);
					
					dispose();
					return;
				}
			}
		});
	}
	
	private void addToSeries(int port, boolean update, FixedMillisecond ms, int i, double value) {
		TimeSeries timeSeries = series.get(getChoosenAttributes(port).get(i).getName());
		
		timeSeries.setNotify(update);
		timeSeries.addOrUpdate(ms, value);
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
		if (isAutoadjust()) {
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
		values.add(TIME_PICO);
		values.add(TIME_SECONDS);
		values.add(TIME_MINUTES);
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
		for (Integer port : getPorts()) {
			for (IViewableAttribute a : getViewableAttributes(port)) {
				values.add(a.getName());
			}
		}

		return values;
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.GET)
	public String getXValue() {
		if (this.choosenXValue == -1) {
			return CURRENT_TIME;
		}
		return getViewableAttributes(choosenXValuePort).get(this.choosenXValue).getName();
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.SET)
	public void setXValue(String value) {
		if (value.equalsIgnoreCase(CURRENT_TIME)) {
			this.choosenXValue = -1;
			return;
		}
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

	@ChartSetting(name = "Update interval (ms)", type = Type.SET)
	public void setUpdateIntervalMillis(long millis) {
		Preconditions.checkArgument(millis >= 0, "Update interval must be zero or positive!");
		
		if( millis != updateIntervalMillis ) {
			if (millis > 0 && updateIntervalMillis == 0) {
				startUpdater(millis);
			} else if (millis == 0 && updateIntervalMillis > 0) {
				stopUpdater();
			} else {
				changeUpdater(millis);
			}
			
			this.updateIntervalMillis = millis;
		}
	}

	private void startUpdater(long millis) {
		chartUpdater = new ChartUpdater(millis) {
			@Override
			protected void updateChart() {
				cleanBuffers();
			}

		};

		chartUpdater.start();
	}

	private void stopUpdater() {
		if( chartUpdater != null ) {
				chartUpdater.stopRunning();
				chartUpdater = null;
		}

		cleanBuffers();
	}

	private void addToBuffers(final int port, Tuple<ITimeInterval> tuple) {
		synchronized (bufferedTuples) {
			bufferedTuples.add(tuple);
			bufferedPorts.add(port);
		}
	}

	private void cleanBuffers() {
		synchronized (bufferedTuples) {
			if( !bufferedTuples.isEmpty() ) {
				
				while (bufferedTuples.size() > 1) {
					Tuple<ITimeInterval> tuple = bufferedTuples.remove(0);
					Integer port = bufferedPorts.remove(0);
	
					processElement(port, tuple, false);
				}
	
				processElement(bufferedPorts.remove(0), bufferedTuples.remove(0), true);
			}
		}
	}

	private void changeUpdater(long millis) {
		stopUpdater();
		startUpdater(millis);
	}

	@ChartSetting(name = "Update interval (ms)", type = Type.GET)
	public long getUpdateIntervalMillis() {
		return updateIntervalMillis;
	}
	
	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);
		
		startUpdaterIfNeeded();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		stopUpdater();
	}
	
	@Override
	public void onUnpause() {
		super.onUnpause();
		
		startUpdaterIfNeeded();
	}

	private void startUpdaterIfNeeded() {
		if( isAsyncUpdate() ) {
			startUpdater(updateIntervalMillis);
		}
	}
	
	@Override
	public void onStop() {
		stopUpdater();
		
		super.onStop();
	}
	
	@ChartSetting(name = "X-Axis title", type=Type.SET)
	public void setXTitle( String xTitle ) {
		if( getChart() != null ) {
			getChart().getXYPlot().getDomainAxis().setLabel(xTitle);
		}
		this.xTitle = xTitle;
	}
	
	@ChartSetting(name = "X-Axis title", type=Type.GET)
	public String getXTitle() {
		return xTitle;
	}
	
	@ChartSetting(name = "Y-Axis title", type=Type.SET)
	public void setYTitle( String yTitle ) {
		if( getChart() != null ) {
			getChart().getXYPlot().getRangeAxis().setLabel(yTitle);
		}
		this.yTitle = yTitle;
	}
	
	@ChartSetting(name = "Y-Axis title", type=Type.GET)
	public String getYTitle() {
		return yTitle;
	}
}
