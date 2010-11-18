package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractCategorySingleValuesChart extends AbstractChart {

	

	private DefaultCategoryDataset dcds = new DefaultCategoryDataset();

	private double max = 0.0;
	private int adjustCounter = 0;

	private int maxAdjustTimes = 10;
	
	public void init(IPhysicalOperator observingOperator) {
		super.init(observingOperator);		
	}
	
	protected CategoryDataset getDataset() {		
		return dcds;
	}

	@Override
	protected void processElement(final RelationalTuple<? extends ITimeInterval> tuple, int port) {
		final SDFAttributeList currentSchema = super.getSchema(); 
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
			//	dcds.clear();
				int i = 0;
				for (SDFAttribute a : currentSchema) {
					if (currentVisibleAttributes[i]) {
						double value = Double.parseDouble(tuple.getAttribute(i).toString());
						recalcAxis(value);
						dcds.setValue(value, a.toString(), "");	
						
					}
					i++;
				}
			}
		});
	}

	protected void decorateChart(JFreeChart chart) {		
		CategoryPlot plot = chart.getCategoryPlot();  
        chart.setAntiAlias(true);
        
     // change background colors
		chart.setBackgroundPaint(Color.WHITE);
		
		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);				
	}
	
	private void recalcAxis(double value){
		if(value>max || adjustCounter>=maxAdjustTimes ){
			max = value*1.05;
			adjustCounter = 0;
		}else{
			adjustCounter++;
		}
		ValueAxis va = this.getChart().getCategoryPlot().getRangeAxis();
		va.setAutoRange(false);
		va.setUpperBound(max);		
	}
	
	@Override
	public void visibleAttributesChanged() {
			dcds.clear();
	}

}
