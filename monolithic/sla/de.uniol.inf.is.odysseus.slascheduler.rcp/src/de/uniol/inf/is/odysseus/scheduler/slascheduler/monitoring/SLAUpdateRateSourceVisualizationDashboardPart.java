package de.uniol.inf.is.odysseus.scheduler.slascheduler.monitoring;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.AbstractChartDashboardPart;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.AbstractSLAPipeConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceAverageConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceNumberConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceRateConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceSingleConformance;

public class SLAUpdateRateSourceVisualizationDashboardPart extends
		AbstractChartDashboardPart {

	private static final Logger LOG = LoggerFactory
			.getLogger(SLAUpdateRateSourceVisualizationDashboardPart.class);

	private TimeSeriesCollection datasetCollection;
	private Map<IPhysicalOperator, TimeSeries> operatorToSerie = new HashMap<IPhysicalOperator, TimeSeries>();

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected void addStreamElementToChart(IPhysicalOperator senderOperator,
			Tuple<?> element, int port) {
		if (senderOperator instanceof UpdateRateSourceAverageConformance 
				|| senderOperator instanceof UpdateRateSourceNumberConformance 
				|| senderOperator instanceof UpdateRateSourceRateConformance 
				|| senderOperator instanceof UpdateRateSourceSingleConformance) {
			if (!operatorToSerie.containsKey(senderOperator)) {
//				if (operatorToSerie.size() > 4)
//					return;
//				
				String sourceName = ((AbstractSLAPipeConformance)senderOperator).getAssociatedWith().getName();
				TimeSeries xySeriesSource = new TimeSeries(sourceName);
				operatorToSerie.put(senderOperator, xySeriesSource);
				datasetCollection.addSeries(xySeriesSource);
			}
			try {
				final Tuple<?> tuple = element;
				// final int queryID = tuple.getAttribute(0);
				final double updaterate = tuple.getAttribute(1);

				if (updaterate >= 0) {
					Date date = new Date(System.currentTimeMillis());
					operatorToSerie.get(senderOperator).addOrUpdate(new Second(date), updaterate);
				}
			} catch (final Throwable t) {
				LOG.error("Could not process Tuple {}!", element, t);
			}
		}
	}

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"SLA UpdateRateSource Monitoring", "Time", "Updaterate in ms",
				datasetCollection, true, true, false);
		
		Color gray = new Color(222, 222, 222);
		chart.getPlot().setBackgroundPaint(gray);
		
		XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
		
		return chart;
	}

	@Override
	protected Dataset createDataset() {
		datasetCollection = new TimeSeriesCollection();
		
		return datasetCollection;
	}

	@Override
	protected void decorateChart(JFreeChart chart) {
		
	}

	@Override
	protected void startChart(Collection<IPhysicalOperator> physicalRoots)
			throws Exception {
		
	}

}
