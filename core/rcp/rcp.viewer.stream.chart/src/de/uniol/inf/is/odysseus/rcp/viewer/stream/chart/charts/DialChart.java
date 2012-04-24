/** Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.util.List;

import org.eclipse.swt.SWTException;
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
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

/**
 * 
 * @author Dennis Geesen Created at: 24.04.2012
 */
public class DialChart extends AbstractChart<Double, IMetaAttribute> {

	private DefaultValueDataset dataset = new DefaultValueDataset();
	private int selectedValue = 0;
	
	private double lowerBound = -40;
	private double upperBound = 60;
	private double startAngle = -120;
	private double extent = -300;
	private double majorTickIncrement = 10.0;
	private int minorTickCount = 4;

	@Override
	public void chartSettingsChanged() {
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
	public String isValidSelection(List<IViewableAttribute> selectAttributes) {
		if (selectAttributes.size() == 1) {
			return null;
		}
		return "The number of choosen attributes should be at least one!";
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
		dialFrame.setBackgroundPaint(Color.lightGray);
		dialFrame.setForegroundPaint(Color.darkGray);
		plot.setDialFrame(dialFrame);

		GradientPaint gp = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
		DialBackground db = new DialBackground(gp);
		db.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
		plot.setBackground(db);
		StandardDialScale scale = new StandardDialScale(lowerBound, upperBound, startAngle, extent, majorTickIncrement, minorTickCount);

		scale.setTickRadius(0.88);
		scale.setTickLabelOffset(0.15);
		scale.setTickLabelFont(new Font("Dialog", Font.PLAIN, 14));
		plot.addScale(0, scale);

		DialPointer needle = new DialPointer.Pointer();
		plot.addLayer(needle);

		DialCap cap = new DialCap();
		cap.setRadius(0.05);
		plot.setCap(cap);

		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(getTitle());
		return chart;
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX + ".dialchart";
	}

	@ChartSetting(name = "Lower Bound", type = Type.GET)
	public Double getLowerBound() {
		return lowerBound;
	}

	@ChartSetting(name = "Lower Bound", type = Type.SET)
	public void setLowerBound(Double lowerBound) {
		this.lowerBound = lowerBound;
	}

	@ChartSetting(name = "Upper Bound", type = Type.GET)
	public Double getUpperBound() {
		return upperBound;
	}

	@ChartSetting(name = "Upper Bound", type = Type.SET)
	public void setUpperBound(Double upperBound) {
		this.upperBound = upperBound;
	}

	@ChartSetting(name = "Start Angle", type = Type.GET)
	public Double getStartAngle() {
		return startAngle;
	}

	@ChartSetting(name = "Start Angle", type = Type.SET)
	public void setStartAngle(Double startAngle) {
		this.startAngle = startAngle;
	}

	@ChartSetting(name = "Extent", type = Type.GET)
	public Double getExtent() {
		return extent;
	}

	@ChartSetting(name = "Extent", type = Type.SET)
	public void setExtent(Double extent) {
		this.extent = extent;
	}

	@ChartSetting(name = "Major Tick Increment", type = Type.GET)
	public Double getMajorTickIncrement() {
		return majorTickIncrement;
	}

	@ChartSetting(name = "Major Tick Increment", type = Type.SET)
	public void setMajorTickIncrement(Double majorTickIncrement) {
		this.majorTickIncrement = majorTickIncrement;
	}

	@ChartSetting(name = "Minor Tick Count", type = Type.GET)
	public Integer getMinorTickCount() {
		return minorTickCount;
	}

	@ChartSetting(name = "Minor Tick Count", type = Type.SET)
	public void setMinorTickCount(Integer minorTickCount) {
		this.minorTickCount = minorTickCount;
	}

}
