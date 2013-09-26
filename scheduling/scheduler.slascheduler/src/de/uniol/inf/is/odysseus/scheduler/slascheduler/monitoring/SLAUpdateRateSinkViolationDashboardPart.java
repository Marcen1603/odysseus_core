package de.uniol.inf.is.odysseus.scheduler.slascheduler.monitoring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.AbstractChartDashboardPart;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSinkAverageConformance;

public class SLAUpdateRateSinkViolationDashboardPart extends
		AbstractChartDashboardPart {

	private static final Logger LOG = LoggerFactory
			.getLogger(SLAUpdateRateSourceVisualizationDashboardPart.class);

	private DefaultCategoryDataset dataset;
//	private List<IPhysicalOperator> operators = new ArrayList<>();
	private Map<IPhysicalOperator, Integer> operatorToViolations = new HashMap<>();

	@Override
	protected void addStreamElementToChart(IPhysicalOperator senderOperator,
			Tuple<?> element, int port) {
		if (senderOperator instanceof UpdateRateSinkAverageConformance) {
			if (!operatorToViolations.containsKey(senderOperator)) {
				if (operatorToViolations.size() > 1)
					return;
				operatorToViolations.put(senderOperator, 0);
//				dataset.setValue(0, "Number", senderOperator.getName() + " Compliance");
				dataset.setValue(0, "Number", senderOperator.getName() + " Violation");
			}
			try {
				final Tuple<?> tuple = (Tuple<?>) element;
				final boolean isViolation = tuple.getAttribute(2);
				
				if (isViolation) {
//					int oldVal = dataset.getValue("Number", senderOperator.getName() + " Violation").intValue();
					int val = operatorToViolations.get(senderOperator) + 1;
					operatorToViolations.put(senderOperator, val);
					dataset.setValue(val, "Number", senderOperator.getName() + " Violation");
				} /*else {
//					int oldVal = dataset.getValue("Number", senderOperator.getName() + " Compliance").intValue();
					int val = operatorToCompliance.get(senderOperator) + 1;
					operatorToCompliance.put(senderOperator, val);
					dataset.setValue(val, "Number", senderOperator.getName() + " Compliance");
				}*/

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
		
		BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer(); 
//		renderer.setBaseItemLabelGenerator(CategoryItemLabelGenerator generator);
//		renderer.setBaseItemLabelGenerator(
//				new StandardCategoryItemLabelGenerator()
//				 @Override
//				 public String generateLabel(CategoryDataset dataset, int row, int column) {
//				        return "Your Text" +row+","+column;
//				 }
//				);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		return chart;
	}

	@Override
	protected Dataset createDataset() {
		dataset = new DefaultCategoryDataset();

		return dataset;
	}

	@Override
	protected void decorateChart(JFreeChart chart) {
		
	}

	@Override
	protected void startChart(Collection<IPhysicalOperator> physicalRoots)
			throws Exception {
		
	}

}