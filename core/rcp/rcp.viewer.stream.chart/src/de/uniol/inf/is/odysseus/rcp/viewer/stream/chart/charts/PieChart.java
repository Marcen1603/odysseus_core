package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PieChart extends AbstractChart{

	DefaultPieDataset dataset = new DefaultPieDataset();
	
	@Override
	protected JFreeChart createChart() {
		
		JFreeChart chart = ChartFactory.createPieChart(getTitle(), dataset, true, true, false);
				
		
		return chart;	
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX+".piechart";
	}

	@Override
	public void visibleAttributesChanged() {
		
	}

	@Override
	protected void processElement(final RelationalTuple<? extends ITimeInterval> tuple, int port) {
		final SDFAttributeList currentSchema = super.getSchema();		
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				for (SDFAttribute a : currentSchema) {
					if (currentVisibleAttributes[i]) {
						double value = Double.parseDouble(tuple.getAttribute(i).toString());
						dataset.setValue(a.toString(), value);												
					}
					i++;
				}
			}
		});
		
	}

	@Override
	protected void decorateChart(JFreeChart thechart) {	
		thechart.getPlot().setBackgroundPaint(DEFAULT_BACKGROUND);
		
	}
	
}
