package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultValueDataset;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

public class ThermometerChart extends AbstractChart<Double, IMetaAttribute> {

	DefaultValueDataset dataset = new DefaultValueDataset(new Double(0));
	private int selectedValue;
	private ThermometerPlot plot;

	private double mininum = 0.0d;
	private double maximum = 400.0d;	
	private double upperWarning = maximum * 0.75d;
	private double upperCritical = maximum * 0.90d;
	
	@Override
	protected void init() {	
		super.init();
		selectedValue = 0;		
		chartSettingsChanged();
		resetBounds();
	}
	
	
	private void resetBounds(){
		plot.setUpperBound(this.maximum);
		plot.setLowerBound(this.mininum);
		
		plot.setSubrange(ThermometerPlot.NORMAL, this.mininum, this.upperWarning);
		plot.setSubrange(ThermometerPlot.WARNING, this.upperWarning, this.upperCritical);
		plot.setSubrange(ThermometerPlot.CRITICAL, this.upperCritical, this.maximum);
	}

	@Override
	protected void processElement(final List<Double> tuple, IMetaAttribute metadata, int port) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					dataset.setValue(tuple.get(selectedValue));										
				} catch (SWTException e) {
					dispose();
					return;
				} catch(Exception ex){
					ex.printStackTrace(System.err);
				}
			}
		});

	}

	@Override
	public String isValidSelection(List<IViewableAttribute> selectAttributes) {
		if (selectAttributes.size() == 1) {
			return null;
		}
		return "The number of choosen attributes should be at least one!";
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
		plot.setSubrangePaint(ThermometerPlot.NORMAL, Color.GREEN);
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

	@ChartSetting(name = "Lower Bound", type = Type.GET)
	public Double getMininum() {
		return mininum;
	}
	
	@ChartSetting(name = "Lower Bound", type = Type.SET)
	public void setMininum(Double mininum) {
		this.mininum = mininum;
		resetBounds();
	}
	
	@ChartSetting(name = "Upper Bound", type = Type.GET)
	public Double getMaximum() {
		return maximum;
	}

	@ChartSetting(name = "Upper Bound", type = Type.SET)
	public void setMaximum(Double maximum) {
		this.maximum = maximum;
		resetBounds();
	}

	@ChartSetting(name = "Upper Warning Bound", type = Type.GET)
	public Double getUpperWarning() {
		return upperWarning;
	}

	@ChartSetting(name = "Upper Warning Bound", type = Type.SET)
	public void setUpperWarning(Double upperWarning) {
		this.upperWarning = upperWarning;
		resetBounds();
	}

	@ChartSetting(name = "Upper Critical Bound", type = Type.GET)
	public Double getUpperCritical() {
		return upperCritical;
	}

	@ChartSetting(name = "Upper Critical Bound", type = Type.SET)
	public void setUpperCritical(Double upperCritical) {
		this.upperCritical = upperCritical;
		resetBounds();
	}


	@Override
	public void chartSettingsChanged() {		
		
	}

}
