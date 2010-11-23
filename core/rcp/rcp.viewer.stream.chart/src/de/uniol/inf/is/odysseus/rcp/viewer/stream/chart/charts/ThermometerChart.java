package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import java.awt.BasicStroke;
import java.awt.Color;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultValueDataset;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.UserSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.UserSetting.Type;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class ThermometerChart extends AbstractChart {

	DefaultValueDataset dataset = new DefaultValueDataset(new Double(0));
	private int selectedValue;
	private ThermometerPlot plot;

	@Override
	public void chartPropertiesChanged() {
		for (int i = 0; i < super.currentVisibleAttributes.length; i++)
			if (super.currentVisibleAttributes[i]) {
				selectedValue = i;
			}
	}

	@Override
	protected void processElement(final RelationalTuple<? extends ITimeInterval> tuple, int port) {		
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					double value = Double.parseDouble(tuple.getAttribute(selectedValue).toString());
					dataset.setValue(value);
				} catch (SWTException e) {										
					dispose();
					return;
				}
			}
		});

	}

	@Override
	public String isValidSelection(boolean[] selectAttributes) {
		if (getSelectedValueCount(selectAttributes) == 1) {
			return null;
		}
		return "You can choose just one attribute for this chart!";
	}

	@Override
	protected void decorateChart(JFreeChart thechart) {

	}

	@Override
	protected JFreeChart createChart() {
		plot = new ThermometerPlot(dataset);
		plot.setThermometerStroke(new BasicStroke(1.0f));
		plot.setThermometerPaint(Color.DARK_GRAY);
		plot.setUseSubrangePaint(true);

		// change subranges
		plot.setSubrange(ThermometerPlot.NORMAL, Double.MAX_VALUE, Double.MAX_VALUE);
		plot.setSubrange(ThermometerPlot.WARNING, Double.MAX_VALUE, Double.MAX_VALUE);
		plot.setSubrange(ThermometerPlot.CRITICAL, Double.MAX_VALUE, Double.MAX_VALUE);

		// change mercury colors
		plot.setMercuryPaint(Color.GREEN);
		plot.setSubrangePaint(ThermometerPlot.NORMAL, Color.RED);
		plot.setSubrangePaint(ThermometerPlot.WARNING, Color.ORANGE);
		plot.setSubrangePaint(ThermometerPlot.CRITICAL, Color.RED);

		// change background color		
		plot.setBackgroundPaint(DEFAULT_BACKGROUND);

		plot.setUnits(ThermometerPlot.UNITS_NONE);
		plot.setUpperBound(400);
		JFreeChart chart = new JFreeChart(getTitle(), // chart title
				JFreeChart.DEFAULT_TITLE_FONT, plot, // plot
				true); // include legend

		chart.setBackgroundPaint(DEFAULT_BACKGROUND);
		chart.setBorderVisible(false);
		
		return chart;
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX + ".thermometerchart";
	}
	
	@UserSetting(name = "Upper Bound", type=Type.GET)
	public Object getUpperBound(){
		return this.plot.getUpperBound();
	}
	
	@UserSetting(name = "Upper Bound", type=Type.SET)
	public void setUpperBound(String newUpperBound){
		this.plot.setUpperBound(Double.parseDouble(newUpperBound));
	}

}
