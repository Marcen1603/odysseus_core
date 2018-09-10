package de.uniol.inf.is.odysseus.rcp.dashboard.windrose.parts;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.DialChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting.Type;

/**
 * This method is a raw prototype of a Windrose. Actually it uses the code of
 * the DialChart with some changed variables to create the look of a windrose.
 * in Future there is the possibility to adjust this class, so it will be look
 * more like a Windrose or to show two or more Pointers, to show multiple values
 * per time
 * 
 * @author Mark Milster, but the code is mostly equal to the
 *         de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.DialChart
 */
public class WindRose extends DialChart {

	private DefaultValueDataset dataset = new DefaultValueDataset();
	private int selectedValue = 0;

	private double lowerBound = 0;
	private double upperBound = 360;
	private double startAngle = 90;
	private double extent = -360;
	private double majorTickIncrement = 10.0;
	private int minorTickCount = 4;

	@Override
	public void reloadChart() {
		DialPlot plot = (DialPlot) getChart().getPlot();
		StandardDialScale scale = (StandardDialScale) plot.getScale(0);
		scale.setLowerBound(lowerBound);
		scale.setUpperBound(upperBound);
		scale.setStartAngle(startAngle);
		scale.setExtent(extent);
		scale.setMajorTickIncrement(majorTickIncrement);
		scale.setMinorTickCount(minorTickCount);
	}

	@Override
	public String isValidSelection(
			Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

	@Override
	protected void processElement(final List<Double> tuple,
			IMetaAttribute metadata, int port) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					dataset.setValue(tuple.get(selectedValue));
				} catch (SWTException e) {
					dispose();
					return;
				} catch (Exception ex) {
					ex.printStackTrace(System.err);
				}
			}
		});

	}

	@Override
	protected void decorateChart(JFreeChart thechart) {

	}

	@Override
	protected JFreeChart createChart() {
		DialPlot plot = new DialPlot();
		plot.setView(0.0, 0.0, 1.0, 1.0);
		plot.setDataset(this.dataset);
		StandardDialFrame dialFrame = new StandardDialFrame();
		dialFrame.setBackgroundPaint(Color.WHITE);
		dialFrame.setForegroundPaint(Color.BLACK);
		plot.setDialFrame(dialFrame);
		plot.setBackgroundPaint(DEFAULT_BACKGROUND);
		GradientPaint gp = new GradientPaint(new Point(), new Color(255, 255,
				255), new Point(), new Color(170, 170, 220));
		DialBackground db = new DialBackground(gp);
		db.setGradientPaintTransformer(new StandardGradientPaintTransformer(
				GradientPaintTransformType.VERTICAL));
		plot.setBackground(db);
		StandardDialScale scale = new StandardDialScale(lowerBound, upperBound,
				startAngle, extent, majorTickIncrement, minorTickCount);

		scale.setTickRadius(0.88);
		scale.setTickLabelOffset(0.15);
		scale.setTickLabelFont(new Font("Dialog", Font.PLAIN, 14));
		plot.addScale(0, scale);

		DialPointer needle = new DialPointer.Pointer();
		plot.addLayer(needle);

		DialPointer needle2 = new DialPointer.Pointer();
		plot.addLayer(needle2);

		DialCap cap = new DialCap();
		cap.setRadius(0.05);
		plot.setCap(cap);

		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(getTitle());
		chart.setBackgroundPaint(DEFAULT_BACKGROUND);
		return chart;
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX + ".dialchart";
	}

	@Override
	@ChartSetting(name = "Lower Bound", type = Type.GET)
	public Double getLowerBound() {
		return lowerBound;
	}

	@Override
	@ChartSetting(name = "Lower Bound", type = Type.SET)
	public void setLowerBound(Double lowerBound) {
		this.lowerBound = lowerBound;
	}

	@Override
	@ChartSetting(name = "Upper Bound", type = Type.GET)
	public Double getUpperBound() {
		return upperBound;
	}

	@Override
	@ChartSetting(name = "Upper Bound", type = Type.SET)
	public void setUpperBound(Double upperBound) {
		this.upperBound = upperBound;
	}

	@Override
	@ChartSetting(name = "Start Angle", type = Type.GET)
	public Double getStartAngle() {
		return startAngle;
	}

	@Override
	@ChartSetting(name = "Start Angle", type = Type.SET)
	public void setStartAngle(Double startAngle) {
		this.startAngle = startAngle;
	}

	@Override
	@ChartSetting(name = "Extent", type = Type.GET)
	public Double getExtent() {
		return extent;
	}

	@Override
	@ChartSetting(name = "Extent", type = Type.SET)
	public void setExtent(Double extent) {
		this.extent = extent;
	}

	@Override
	@ChartSetting(name = "Major Tick Increment", type = Type.GET)
	public Double getMajorTickIncrement() {
		return majorTickIncrement;
	}

	@Override
	@ChartSetting(name = "Major Tick Increment", type = Type.SET)
	public void setMajorTickIncrement(Double majorTickIncrement) {
		this.majorTickIncrement = majorTickIncrement;
	}

	@Override
	@ChartSetting(name = "Minor Tick Count", type = Type.GET)
	public Integer getMinorTickCount() {
		return minorTickCount;
	}

	@Override
	@ChartSetting(name = "Minor Tick Count", type = Type.SET)
	public void setMinorTickCount(Integer minorTickCount) {
		this.minorTickCount = minorTickCount;
	}

}
