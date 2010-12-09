package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.awt.Color;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractCategoryTimeSeriesChart extends AbstractChart {

	private DefaultCategoryDataset dcds = new DefaultCategoryDataset();

	private double max = 0.0;

	private static final int DEFAULT_MAX_NUMBER_OF_ITEMS = 10;

	private int maxItems = DEFAULT_MAX_NUMBER_OF_ITEMS;

	protected CategoryDataset getDataset() {
		return dcds;
	}

	@Override
	protected void processElement(final RelationalTuple<? extends ITimeInterval> tuple, int port) {
		final SDFAttributeList currentSchema = super.getSchema();
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				// dcds.clear();
				try {
					int i = 0;
					for (SDFAttribute a : currentSchema) {
						if (getVisibleSchema().contains(currentSchema.get(i))) {
							double value = Double.parseDouble(tuple.getAttribute(i).toString());
							recalcAxis(value);
							dcds.addValue(value, a.toString(), tuple.getMetadata().getStart());

						}
						i++;
					}
				} catch (SWTException e) {
					dispose();
					return;
				}
			}
		});
		if (dcds.getColumnCount() > this.maxItems) {
			dcds.removeColumn(0);
		}
	}

	protected void decorateChart(JFreeChart chart) {
		CategoryPlot plot = chart.getCategoryPlot();
		chart.setAntiAlias(true);

		// change background colors
		chart.setBackgroundPaint(Color.WHITE);

		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
	}

	private void recalcAxis(double value) {
		if (value > max) {
			max = value * 1.05;
		}
		ValueAxis va = this.getChart().getCategoryPlot().getRangeAxis();
		va.setAutoRange(false);
		va.setUpperBound(max);
	}

	@Override
	public void chartSettingsChanged() {
	}

	@Override
	public String isValidSelection(SDFAttributeList selectAttributes) {
		if (selectAttributes.size() > 0) {
			return null;
		}
		return "The number of choosen attributes should be at least one!";
	}	

}
