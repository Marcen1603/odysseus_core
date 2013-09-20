package de.uniol.inf.is.odysseus.scheduler.slascheduler.monitoring;

import java.util.Collection;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceAverageConformance;

public class SLAUpdateRateSourceViolationDashboardPart extends
		AbstractChartDashboardPart {

	private static final Logger LOG = LoggerFactory
			.getLogger(SLAUpdateRateSourceVisualizationDashboardPart.class);

	DefaultCategoryDataset dataset;

	@Override
	protected void addStreamElementToChart(IPhysicalOperator senderOperator,
			Tuple<?> element, int port) {
		if (senderOperator instanceof UpdateRateSourceAverageConformance) {
			try {
				final Tuple<?> tuple = (Tuple<?>) element;
				final boolean isViolation = tuple.getAttribute(2);

				if (isViolation) {
					int oldVal = dataset.getValue("Number", "Violation")
							.intValue();
					dataset.setValue(oldVal + 1, "Number", "Violation");
				} else {
					int oldVal = dataset.getValue("Number", "Compliance")
							.intValue();
					dataset.setValue(oldVal + 1, "Number", "Compliance");
				}

			} catch (final Throwable t) {
				LOG.error("Could not process Tuple {}!", element, t);
			}
		}
	}

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createBarChart(
				"SLA Violation Monitoring", null, "Number", dataset,
				PlotOrientation.VERTICAL, false, false, false);

		return chart;
	}

	@Override
	protected Dataset createDataset() {
		dataset = new DefaultCategoryDataset();
		dataset.setValue(0, "Number", "Compliance");
		dataset.setValue(0, "Number", "Violation");

		return dataset;
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