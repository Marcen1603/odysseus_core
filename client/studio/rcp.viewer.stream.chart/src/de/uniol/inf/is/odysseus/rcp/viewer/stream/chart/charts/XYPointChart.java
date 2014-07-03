/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractXYChart;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class XYPointChart extends AbstractXYChart {

    @Override
    protected JFreeChart createChart() {
        final JFreeChart chart = ChartFactory.createXYLineChart(this.getTitle(), "", "", super.getDataset(), PlotOrientation.VERTICAL, true, true, false);
        chart.getPlot().setBackgroundPaint(AbstractJFreeChart.DEFAULT_BACKGROUND);
        return chart;
    }

    @Override
    protected void decorateChart(final JFreeChart thechart) {
        super.decorateChart(thechart);
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
        thechart.getXYPlot().setRenderer(renderer);
        thechart.getXYPlot().getDomainAxis().setAutoRange(true);
        thechart.getXYPlot().getRangeAxis().setAutoRange(true);
        thechart.getXYPlot().setBackgroundPaint(AbstractJFreeChart.DEFAULT_BACKGROUND);
        thechart.getXYPlot().setRangeGridlinePaint(AbstractJFreeChart.DEFAULT_BACKGROUND_GRID);
    }

    @Override
    public String getViewID() {
        return AbstractJFreeChart.VIEW_ID_PREFIX + ".xypointchart";
    }

}
