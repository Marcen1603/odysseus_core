package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

public abstract class AbstractTimeSeriesChart extends AbstractChart<Double, ITimeInterval> {

	protected Map<String, TimeSeries> series = new HashMap<String, TimeSeries>();

	protected TimeSeriesCollection dataset = new TimeSeriesCollection();

	private static final int DEFAULT_MAX_NUMBER_OF_ITEMS = 100;

	private int maxItems = DEFAULT_MAX_NUMBER_OF_ITEMS;

	private String dateformat = "HH:mm";

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
		NumberAxis axis = (NumberAxis) getChart().getXYPlot().getDomainAxis();
		axis.setNumberFormatOverride(new SimpleNumberToDateFormat(this.dateformat));
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
					FixedMillisecond ms = new FixedMillisecond(metadata.getStart().getMainPoint());
					for (int i = 0; i < tuple.size(); i++) {
						double value = tuple.get(i);
						series.get(getChoosenAttributes().get(i).getName()).add(ms, value);
					}

				} catch (SWTException ex) {
					// widget disposed
					dispose();
					return;
				} catch (SeriesException e) {
					System.out.println("Warn: " + e.getLocalizedMessage());
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		});
	}

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

	@ChartSetting(name = "Date Format", type = Type.GET)
	public String getDateFormat() {
		return this.dateformat;
	}

	@ChartSetting(name = "Date Format", type = Type.SET)
	public void setDateFormat(String dateFormat) {
		this.dateformat = dateFormat;
	}

}
