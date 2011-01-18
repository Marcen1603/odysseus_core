package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.util.List;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public abstract class AbstractCategorySingleValuesChart extends AbstractChart<Double, IMetaAttribute> {

	private DefaultCategoryDataset dcds = new DefaultCategoryDataset();

	private double max = 0.0;
	private int adjustCounter = 0;

	private int maxAdjustTimes = 10;

	@Override
	public void init(IPhysicalOperator observingOperator) {
		super.init(observingOperator);
	}

	protected CategoryDataset getDataset() {
		return dcds;
	}

	@Override
	protected void processElement(final List<Double> tuple, final IMetaAttribute metadata, int port){		
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					
					for(int i=0;i<getChoosenAttributes().size();i++){
						double value = tuple.get(i);
						recalcAxis(value);
						dcds.setValue(value, getChoosenAttributes().get(i).getName(), "");																
					}
				} catch (SWTException e) {				
					dispose();
					return;
				}
			}
		});
	}

	@Override
	protected void decorateChart(JFreeChart chart) {
		if (chart.getPlot() instanceof CategoryPlot) {
			CategoryPlot plot = chart.getCategoryPlot();
			chart.setAntiAlias(true);

			// change background colors
			chart.setBackgroundPaint(DEFAULT_BACKGROUND);

			plot.setBackgroundPaint(DEFAULT_BACKGROUND);
			plot.setRangeGridlinePaint(DEFAULT_BACKGROUND_GRID);
		} else {
			if (chart.getPlot() instanceof PiePlot) {
				PiePlot plot = (PiePlot) chart.getPlot();
				plot.setBackgroundPaint(DEFAULT_BACKGROUND);
			}
		}
	}

	private void recalcAxis(double value) {
		if (value > max || adjustCounter >= maxAdjustTimes) {
			max = value * 1.05;
			adjustCounter = 0;
		} else {
			adjustCounter++;
		}
		ValueAxis va = this.getChart().getCategoryPlot().getRangeAxis();
		va.setAutoRange(false);
		va.setUpperBound(max);
	}

	@Override
	public void chartSettingsChanged() {
		dcds.clear();
	}
	
	@Override
	public String isValidSelection(List<IViewableAttribute> selectAttributes) {
		if (selectAttributes.size() > 0) {
			return null;
		}
		return "The number of choosen attributes should be at least one!";
	}	


}
