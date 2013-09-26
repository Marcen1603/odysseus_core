package de.uniol.inf.is.odysseus.scheduler.slascheduler.monitoring;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.AbstractChartDashboardPart;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSinkAverageConformance;

public class SLAUpdateRateSinkVisualizationDashboardPart extends
		AbstractChartDashboardPart {

	private static final Logger LOG = LoggerFactory
			.getLogger(SLAUpdateRateSinkVisualizationDashboardPart.class);

	private XYSeriesCollection datasetCollection;
	private Map<IPhysicalOperator, XYSeries> operatorToSerie = new HashMap<IPhysicalOperator, XYSeries>();
	private long startTestSeries = -1;

	@Override
	protected void addStreamElementToChart(IPhysicalOperator senderOperator,
			Tuple<?> element, int port) {
		if(startTestSeries == -1)
			startTestSeries = System.currentTimeMillis();
		
		if (System.currentTimeMillis() - startTestSeries > 600 * 1000)
			return;
		
		if (senderOperator instanceof UpdateRateSinkAverageConformance) {
			if (!operatorToSerie.containsKey(senderOperator)) {
				if (operatorToSerie.size() > 1)
					return;
				XYSeries xySeriesSource = new XYSeries(senderOperator.getName());
				operatorToSerie.put(senderOperator, xySeriesSource);
				datasetCollection.addSeries(xySeriesSource);
			}
			try {
				final Tuple<?> tuple = (Tuple<?>) element;
				// final int queryID = tuple.getAttribute(0);
				final long updaterate = tuple.getAttribute(1);

				if (updaterate >= 0) {
					operatorToSerie.get(senderOperator).add(System.currentTimeMillis(), updaterate);
				}

				// xySeriesSourceThreshold.add(System.currentTimeMillis(), ((AbstractSLAPipeConformance) senderOperator).getMetricValue());

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
		
		Color gray = new Color(222, 222, 222);
		chart.getPlot().setBackgroundPaint(gray);
		
		return chart;
	}

	@Override
	protected Dataset createDataset() {
		datasetCollection = new XYSeriesCollection();
		
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
