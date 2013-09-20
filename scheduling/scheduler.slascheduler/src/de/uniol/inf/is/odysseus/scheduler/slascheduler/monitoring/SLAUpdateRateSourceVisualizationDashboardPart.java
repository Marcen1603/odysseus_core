package de.uniol.inf.is.odysseus.scheduler.slascheduler.monitoring;

import java.util.Collection;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.AbstractSLAPipeConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceAverageConformance;

public class SLAUpdateRateSourceVisualizationDashboardPart extends
		AbstractChartDashboardPart {

	private static final Logger LOG = LoggerFactory
			.getLogger(SLAUpdateRateSourceVisualizationDashboardPart.class);

	// XYDataset xyDataset;
	XYSeries xySeries;
	XYSeries xySeriesThreshold;
	XYSeriesCollection datasetCollection;
	Plot plot;

	@SuppressWarnings("rawtypes")
	@Override
	protected void addStreamElementToChart(IPhysicalOperator senderOperator,
			Tuple<?> element, int port) {
		if (senderOperator instanceof UpdateRateSourceAverageConformance) {
			try {
				final Tuple<?> tuple = (Tuple<?>) element;
				// final int queryID = tuple.getAttribute(0);
				final double updaterate = tuple.getAttribute(1);
				// final boolean isViolation = tuple.getAttribute(2);

				if (updaterate >= 0) {
					// long updaterateSec = updaterate / 1000; //ms to seconds
					xySeries.add(System.currentTimeMillis(), updaterate);
				}

				xySeriesThreshold.add(System.currentTimeMillis(),
						((AbstractSLAPipeConformance) senderOperator)
								.getMetricValue());

			} catch (final Throwable t) {
				LOG.error("Could not process Tuple {}!", element, t);
			}
		}
	}

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createXYLineChart(
				"SLA Violation Monitoring", "Time", "Updaterate in ms",
				datasetCollection, PlotOrientation.VERTICAL, true, true, false);
		plot = chart.getPlot();
		return chart;
	}

	@Override
	protected Dataset createDataset() {
		xySeries = new XYSeries("SLAViolationMonitoring");
		xySeriesThreshold = new XYSeries("SLAViolationThreshold");
		// xyDataset = new XYSeriesCollection(xySeries);

		datasetCollection = new XYSeriesCollection();
		datasetCollection.addSeries(xySeries);
		// datasetCollection.addSeries(xySeriesThreshold);

		return datasetCollection;
	}

	@Override
	protected void decorateChart(JFreeChart chart) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void startChart(Collection<IPhysicalOperator> physicalRoots)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
