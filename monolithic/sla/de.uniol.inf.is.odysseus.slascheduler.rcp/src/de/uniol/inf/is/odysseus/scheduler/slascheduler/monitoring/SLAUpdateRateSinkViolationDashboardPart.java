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
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSinkAverageConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSinkNumberConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSinkRateConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance.UpdateRateSinkSingleConformance;

public class SLAUpdateRateSinkViolationDashboardPart extends
		AbstractChartDashboardPart {

	private DefaultCategoryDataset dataset;
	private List<Integer> queriesToVisualize= new ArrayList<>();

	@Override
	protected void addStreamElementToChart(IPhysicalOperator senderOperator,
			Tuple<?> element, int port) {
		if (senderOperator instanceof UpdateRateSinkAverageConformance 
				|| senderOperator instanceof UpdateRateSinkNumberConformance 
				|| senderOperator instanceof UpdateRateSinkRateConformance 
				|| senderOperator instanceof UpdateRateSinkSingleConformance) {
			
			if (SLAViolationCounter.hasChangedUpRaSi()) {
				int queryID;
				int number;
				for (Map.Entry<Integer, Integer> entry : SLAViolationCounter.getNumberOfViolationsUpRaSi().entrySet()) {
					queryID = entry.getKey();
					number = entry.getValue();

					@SuppressWarnings("rawtypes")
					String sinkName = ((AbstractSLAPipeConformance)senderOperator).getAssociatedWith().getName();
					
					if (!queriesToVisualize.contains(queryID)) {
//						if (queriesToVisualize.size() > 4)
//							return;
						queriesToVisualize.add(queryID);
						dataset.setValue(0, "Number", "Query" + queryID + "_" + sinkName);
					} else
						dataset.setValue(number, "Number", "Query" + queryID + "_" + sinkName);
				}
			}
		}
	}

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createBarChart(
				"SLA UpdateRateSink Violation Monitoring", null, "Number of violations", dataset,
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

		private static final long serialVersionUID = 1131224651786239284L;

		public CustomRenderer() {
		}

		@Override
		public Paint getItemPaint(final int row, final int column) {
			// return (row > 200) ? Color.blue : Color.yellow ;
			return new Color(245, 34, 52);
		}
	}

}