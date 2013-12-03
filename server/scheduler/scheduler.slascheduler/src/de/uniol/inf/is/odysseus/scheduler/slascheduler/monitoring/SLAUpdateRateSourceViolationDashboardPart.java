package de.uniol.inf.is.odysseus.scheduler.slascheduler.monitoring;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.AbstractChartDashboardPart;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAViolationCounter;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.AbstractSLAPipeConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceAverageConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceNumberConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceRateConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSourceSingleConformance;

public class SLAUpdateRateSourceViolationDashboardPart extends
		AbstractChartDashboardPart {

	private DefaultCategoryDataset dataset;
	private List<Integer> queriesToVisualize= new ArrayList<>();

	@Override
	protected void addStreamElementToChart(IPhysicalOperator senderOperator,
			Tuple<?> element, int port) {
		if (senderOperator instanceof UpdateRateSourceAverageConformance 
				|| senderOperator instanceof UpdateRateSourceNumberConformance 
				|| senderOperator instanceof UpdateRateSourceRateConformance 
				|| senderOperator instanceof UpdateRateSourceSingleConformance) {
			if (SLAViolationCounter.hasChangedUpRaSo()) {
				int queryID;
				int number;
				for (Map.Entry<Integer, Integer> entry : SLAViolationCounter.getNumberOfViolationsUpRaSo().entrySet()) {
					queryID = entry.getKey();
					number = entry.getValue();

					@SuppressWarnings("rawtypes")
					String sourceName = ((AbstractSLAPipeConformance)senderOperator).getAssociatedWith().getName();
					
					if (!queriesToVisualize.contains(queryID)) {
//						if (queriesToVisualize.size() > 4)
//							return;
						queriesToVisualize.add(queryID);
						dataset.setValue(0, "Number", "Query" + queryID + "_" + sourceName);
					} else
						dataset.setValue(number, "Number", "Query" + queryID + "_" + sourceName);
				}
			}
			
			/*
			if (!operatorToViolations.containsKey(senderOperator)) {
				if (operatorToViolations.size() > 4)
					return;
				operatorToViolations.put(senderOperator, 0);
				// dataset.setValue(0, "Number", senderOperator.getName() + " Compliance");
				// dataset.setValue(0, "Number", senderOperator.getName() + " Violation");
				dataset.setValue(0, "Number", "Query" + senderOperator.getOwner().get(0).getID());
			}
			try {
				final Tuple<?> tuple = (Tuple<?>) element;
				final boolean isViolation = tuple.getAttribute(2);
				
				if (isViolation) {
					// int oldVal = dataset.getValue("Number", senderOperator.getName() + " Violation").intValue();
					int val = operatorToViolations.get(senderOperator) + 1;
					operatorToViolations.put(senderOperator, val);
					dataset.setValue(val, "Number", "Query" + senderOperator.getOwner().get(0).getID());
				} //else {
					//int oldVal = dataset.getValue("Number", senderOperator.getName() + " Compliance").intValue();
					//int val = operatorToCompliance.get(senderOperator) + 1;
					//operatorToCompliance.put(senderOperator, val);
					//dataset.setValue(val, "Number", senderOperator.getName() + " Compliance");
				//}

			} catch (final Throwable t) {
				LOG.error("Could not process Tuple {}!", element, t);
			}*/
		}
	}

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createBarChart(
				"SLA UpdateRateSource Violation Monitoring", null, "Number of violations", dataset,
				PlotOrientation.VERTICAL, false, false, false);
		
		CategoryPlot plot = chart.getCategoryPlot();
		CategoryItemRenderer renderer = new CustomRenderer();
		plot.setRenderer(renderer);
		((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
		
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

	class CustomRenderer extends BarRenderer {

		private static final long serialVersionUID = 26202274630003921L;

		public CustomRenderer() {
		}

		@Override
		public Paint getItemPaint(final int row, final int column) {
			// return (row > 200) ? Color.blue : Color.yellow ;
			return new Color(245, 34, 52);//Color.red;
		}
	}
}