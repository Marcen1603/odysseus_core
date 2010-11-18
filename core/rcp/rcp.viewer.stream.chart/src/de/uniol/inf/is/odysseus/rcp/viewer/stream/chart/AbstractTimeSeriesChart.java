package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public abstract class AbstractTimeSeriesChart extends AbstractChart{

	protected Map<String, TimeSeries> series = new HashMap<String, TimeSeries>();

	protected TimeSeriesCollection dataset = new TimeSeriesCollection();	
	
	private static final int DEFAULT_MAX_NUMBER_OF_ITEMS = 100;
	
	private int maxItems = DEFAULT_MAX_NUMBER_OF_ITEMS;
	
	@Override
	public void visibleAttributesChanged() {
		series.clear();
		this.dataset.removeAllSeries();
		for (int i = 0; i < getSchema().getAttributeCount(); i++) {
			if (currentVisibleAttributes[i]) {
				String name = getSchema().get(i).toString();
				System.out.println("set attribute: "+name);
				TimeSeries serie = new TimeSeries(name);
				serie.setMaximumItemCount(this.maxItems);
				series.put(name, serie);
				this.dataset.addSeries(serie);
			}
		}
	}
	

	protected void processElement(final RelationalTuple<? extends ITimeInterval> tuple, int port) {		
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					FixedMillisecond ms = new FixedMillisecond(tuple.getMetadata().getStart().getMainPoint());
					for (int i = 0; i < tuple.getAttributeCount(); i++) {
						if (currentVisibleAttributes[i]) {
							double value = Double.parseDouble(tuple.getAttribute(i).toString());
							series.get(getSchema().get(i).toString()).add(ms, value);
							
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	protected void decorateChart(JFreeChart thechart){		
		thechart.getXYPlot().setRangeGridlinePaint(DEFAULT_BACKGROUND_GRID);	
	}

	

}
