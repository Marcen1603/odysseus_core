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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting.Type;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractPolarChart extends AbstractJFreeChart<Double, ITimeInterval> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTimeSeriesChart.class);
    private static final int DEFAULT_MAX_NUMBER_OF_ITEMS = 1000;

    private final XYSeriesCollection dataset = new XYSeriesCollection();
    private final Map<Integer, Map<Long, Map<String, XYSeries>>> series = new HashMap<>();
    private final Map<Long, String> groupNames = new HashMap<>();
    private final boolean useShortNames = true;
    private final double margin = 0.05; // 5 percent

    private double max = Double.NaN;
    private double min = Double.NaN;
    private boolean autoadjust = true;
    private String rTitle = "";

    private int maxItems = AbstractPolarChart.DEFAULT_MAX_NUMBER_OF_ITEMS;
    private boolean setVerticalTickLabels = false;

    private int choosenRValue = 0;
    private int choosenThetaValue = 1;

    private Integer choosenRValuePort = 0;
    private Integer choosenThetaValuePort = 0;

    private long updateIntervalMillis = 0;
    private final List<Tuple<ITimeInterval>> bufferedTuples = Lists.newArrayList();
    private final List<Integer> bufferedPorts = Lists.newArrayList();
    private ChartUpdater chartUpdater;

    @Override
    public void reloadChart() {
        final PolarPlot plot = (PolarPlot) this.getChart().getPlot();

        if (!this.autoadjust) {
            if (!Double.isNaN(this.max)) {
                plot.getAxis().setUpperBound(this.max * (1.0 + this.margin));
            }
            if (!Double.isNaN(this.min)) {
                plot.getAxis().setLowerBound(this.min * (1.0 - this.margin));
            }
        }

        this.series.clear();
        this.dataset.removeAllSeries();

        if (this.setVerticalTickLabels) {
            plot.getAxis().setVerticalTickLabels(true);
            // getChart().getXYPlot().getDomainAxis().setVerticalTickLabels(true);
        }
        plot.getAxis().setLabel(this.rTitle);

    }

    public XYDataset getDataset() {
        return this.dataset;
    }

    @Override
    public String isValidSelection(final Map<Integer, Set<IViewableAttribute>> selectAttributes) {
        int sum = 0;
        for (final Entry<Integer, Set<IViewableAttribute>> e : selectAttributes.entrySet()) {
            if (e.getValue().size() > 0) {
                sum += e.getValue().size();
            }
        }
        if (sum >= 2) {
            return null;
        }
        return "The number of choosen attributes should be at least two!";
    }

    @Override
    protected void processElement(final List<Double> tuple, final ITimeInterval metadata, final int port) {
        // this is not needed, since streamElementReceived is overwritten!
    }

    @SuppressWarnings("unchecked")
    @Override
    public void streamElementReceived(final IPhysicalOperator senderOperator, final IStreamObject<?> element, final int port) {
        if (!(element instanceof Tuple<?>)) {
            AbstractPolarChart.LOG.warn("Stream visualization is only for relational tuple, not for {}!", element.getClass());
            return;
        }

        if (this.getChart() == null) {
            return;
        }

        final Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>) element;

        if (this.isAsyncUpdate()) {
            this.addToBuffers(port, tuple);
        }
        else {
            this.processElement(port, tuple, true);
        }
    }

    private boolean isAsyncUpdate() {
        return this.updateIntervalMillis > 0;
    }

    private void processElement(final int port, final Tuple<ITimeInterval> tuple, final boolean update) {
        final List<Double> viewableValues = this.viewSchema.get(port).convertToViewableFormat(tuple);
        final int[] gRestrict = this.viewSchema.get(port).getGroupRestrictList();
        final Display display = PlatformUI.getWorkbench().getDisplay();

        if (this.getChartComposite().isDisposed()) {
            return;
        }

        display.asyncExec(new Runnable() {

            @Override
            public void run() {
                if (AbstractPolarChart.this.getChartComposite().isDisposed()) {
                    return;
                }

                Long groupId = 0L;
                String groupName = "";
                if (gRestrict != null) {
                    groupId = (long) tuple.restrictedHashCode(gRestrict);
                    groupName = AbstractPolarChart.this.groupNames.get(groupId);
                    if (groupName == null) {
                        groupName = tuple.toString(gRestrict);
                        AbstractPolarChart.this.groupNames.put(groupId, groupName);
                    }
                }

                try {
                    if ((AbstractPolarChart.this.choosenRValue != -1) && (AbstractPolarChart.this.choosenThetaValue != -1)) {
                        final double r = ((Number) viewableValues.get(AbstractPolarChart.this.choosenRValue)).doubleValue();
                        final double theta = ((Number) viewableValues.get(AbstractPolarChart.this.choosenThetaValue)).doubleValue();

                        AbstractPolarChart.this.addToSeries(port, update, r, theta, groupId, groupName);
                        AbstractPolarChart.this.adjust(r);

                    }
                }
                catch (final SWTException ex) {
                    AbstractPolarChart.LOG.error("Exception in adding data into chart", ex);

                    AbstractPolarChart.this.dispose();
                    return;
                }
            }
        });
    }

    private void addToSeries(final int port, final boolean update, final double r, final double theta, final Long groupID, final String groupName) {
        XYSeries xySeries = null;

        Map<Long, Map<String, XYSeries>> pgSerie = this.series.get(port);

        if (pgSerie == null) {
            pgSerie = new HashMap<>();
            this.series.put(port, pgSerie);
        }

        Map<String, XYSeries> gSerie = pgSerie.get(groupID);

        if (gSerie == null) {
            gSerie = new HashMap<>();
            pgSerie.put(groupID, gSerie);
        }

        final String name = this.getChoosenAttributes(port).get(this.choosenRValue).getName() + " " + groupName + "(" + port + ")";

        xySeries = gSerie.get(name);
        if (xySeries == null) {

            if (this.useShortNames) {
                xySeries = new XYSeries(this.getChoosenAttributes(port).get(this.choosenRValue).getAttributeName() + " " + groupName + "(" + port + ")");
            }
            else {
                xySeries = new XYSeries(name);
            }
            if (this.maxItems > 0) {
                xySeries.setMaximumItemCount(this.maxItems);
            }

            gSerie.put(name, xySeries);
            this.dataset.addSeries(xySeries);
        }

        xySeries.setNotify(update);
        xySeries.add(theta, r);
    }

    private void adjust(final double value) {
        if (Double.isNaN(this.max)) {
            this.max = value;
        }
        if (Double.isNaN(this.min)) {
            this.min = value;
        }
        if (this.autoadjust && (this.getChart() != null)) {
            if (value > this.max) {
                this.max = value;
            }
            else if (value < this.min) {
                this.min = value;
            }
            final PolarPlot plot = (PolarPlot) this.getChart().getPlot();
            plot.getAxis().setUpperBound(this.max * (1.0 + this.margin));
            plot.getAxis().setLowerBound(this.min * (1.0 - this.margin));

        }
    }

    @Override
    protected void decorateChart(final JFreeChart chart) {
        chart.setBackgroundPaint(AbstractJFreeChart.DEFAULT_BACKGROUND);
        final PolarPlot plot = (PolarPlot) chart.getPlot();
        plot.setBackgroundPaint(AbstractJFreeChart.DEFAULT_BACKGROUND);
        plot.setAngleGridlinePaint(Color.BLACK);
        plot.setRadiusGridlinePaint(Color.LIGHT_GRAY);
        DefaultPolarItemRenderer r = (DefaultPolarItemRenderer) plot.getRenderer();
        r.setFillComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        r.setShapesVisible(false);
        r.setDrawOutlineWhenFilled(true);
    }

    @ChartSetting(name = "Max Shown Items", type = Type.GET)
    public Integer getMaxItems() {
        return this.maxItems;
    }

    @ChartSetting(name = "Max Shown Items", type = Type.SET)
    public void setMaxItems(final Integer maxItems) {
        this.maxItems = maxItems;
    }

    @ChartSetting(name = "Show vertical Tick Labels ", type = Type.GET)
    public boolean isSetVerticalTickLabels() {
        return this.setVerticalTickLabels;
    }

    @ChartSetting(name = "Show vertical Tick Labels ", type = Type.SET)
    public void setSetVerticalTickLabels(final boolean setVerticalTickLabels) {
        this.setVerticalTickLabels = setVerticalTickLabels;
    }

    private List<String> getValues() {
        final List<String> values = new ArrayList<String>();
        for (final Integer port : this.getPorts()) {
            for (final IViewableAttribute a : this.getViewableAttributes(port)) {
                values.add(a.getName());
            }
        }

        return values;
    }

    @ChartSetting(name = "Value for Theta", type = Type.OPTIONS)
    public List<String> getThetaValues() {
        return this.getValues();
    }

    @ChartSetting(name = "Value for Theta", type = Type.GET)
    public String getThetaValue() {
        return this.getViewableAttributes(this.choosenThetaValuePort).get(this.choosenThetaValue).getName();
    }

    @ChartSetting(name = "Value for Theta", type = Type.SET)
    public void setThetaValue(final String value) {
        for (final Integer port : this.getPorts()) {
            for (int i = 0; i < this.getViewableAttributes(port).size(); i++) {
                if (this.getViewableAttributes(port).get(i).getName().equals(value)) {
                    this.choosenThetaValue = i;
                    this.choosenThetaValuePort = port;
                    return;
                }
            }
        }
    }

    @ChartSetting(name = "Value for R-Axis", type = Type.OPTIONS)
    public List<String> getRValues() {
        return this.getValues();
    }

    @ChartSetting(name = "Value for R-Axis", type = Type.GET)
    public String getRValue() {
        return this.getViewableAttributes(this.choosenRValuePort).get(this.choosenRValue).getName();
    }

    @ChartSetting(name = "Value for R-Axis", type = Type.SET)
    public void setRValue(final String value) {
        for (final Integer port : this.getPorts()) {
            for (int i = 0; i < this.getViewableAttributes(port).size(); i++) {
                if (this.getViewableAttributes(port).get(i).getName().equals(value)) {
                    this.choosenRValue = i;
                    this.choosenRValuePort = port;
                    return;
                }
            }
        }
    }

    @ChartSetting(name = "Upper Bound for R-Axis", type = Type.SET)
    public void setMax(final Double max) {
        this.max = max;
    }

    @ChartSetting(name = "Lower Bound for R-Axis", type = Type.GET)
    public Double getMin() {
        return this.min;
    }

    @ChartSetting(name = "Lower Bound for R-Axis", type = Type.SET)
    public void setMin(final Double min) {
        this.min = min;
    }

    @ChartSetting(name = "Upper Bound for R-Axis", type = Type.GET)
    public Double getMax() {
        return this.max;
    }

    @ChartSetting(name = "Autoadjust Bounds", type = Type.GET)
    public boolean isAutoadjust() {
        return this.autoadjust;
    }

    @ChartSetting(name = "Autoadjust Bounds", type = Type.SET)
    public void setAutoadjust(final boolean autoadjust) {
        this.autoadjust = autoadjust;
    }

    @ChartSetting(name = "Update interval (ms)", type = Type.SET)
    public void setUpdateIntervalMillis(final long millis) {
        // Preconditions.checkArgument(millis >= 0, "Update interval must be zero or positive!");

        if (millis != this.updateIntervalMillis) {
            if ((millis > 0) && (this.updateIntervalMillis == 0)) {
                this.startUpdater(millis);
            }
            else if ((millis == 0) && (this.updateIntervalMillis > 0)) {
                this.stopUpdater();
            }
            else {
                this.changeUpdater(millis);
            }

            this.updateIntervalMillis = millis;
        }
    }

    private void startUpdater(final long millis) {
        this.chartUpdater = new ChartUpdater(millis) {
            @Override
            protected void updateChart() {
                AbstractPolarChart.this.cleanBuffers();
            }

        };

        this.chartUpdater.start();
    }

    private void stopUpdater() {
        if (this.chartUpdater != null) {
            this.chartUpdater.stopRunning();
            this.chartUpdater = null;
        }

        this.cleanBuffers();
    }

    private void addToBuffers(final int port, final Tuple<ITimeInterval> tuple) {
        synchronized (this.bufferedTuples) {
            this.bufferedTuples.add(tuple);
            this.bufferedPorts.add(port);
        }
    }

    private void cleanBuffers() {
        synchronized (this.bufferedTuples) {
            if (!this.bufferedTuples.isEmpty()) {

                while (this.bufferedTuples.size() > 1) {
                    final Tuple<ITimeInterval> tuple = this.bufferedTuples.remove(0);
                    final Integer port = this.bufferedPorts.remove(0);

                    this.processElement(port, tuple, false);
                }

                this.processElement(this.bufferedPorts.remove(0), this.bufferedTuples.remove(0), true);
            }
        }
    }

    private void changeUpdater(final long millis) {
        this.stopUpdater();
        this.startUpdater(millis);
    }

    @ChartSetting(name = "Update interval (ms)", type = Type.GET)
    public long getUpdateIntervalMillis() {
        return this.updateIntervalMillis;
    }

    @Override
    public void onStart(final Collection<IPhysicalOperator> physicalRoots) throws Exception {
        super.onStart(physicalRoots);

        this.startUpdaterIfNeeded();
    }

    @Override
    public void onPause() {
        super.onPause();

        this.stopUpdater();
    }

    @Override
    public void onUnpause() {
        super.onUnpause();

        this.startUpdaterIfNeeded();
    }

    private void startUpdaterIfNeeded() {
        if (this.isAsyncUpdate()) {
            this.startUpdater(this.updateIntervalMillis);
        }
    }

    @Override
    public void onStop() {
        this.stopUpdater();

        super.onStop();
    }

    @ChartSetting(name = "R-Axis title", type = Type.SET)
    public void setXTitle(final String rTitle) {
        if (this.getChart() != null) {
            final PolarPlot plot = (PolarPlot) this.getChart().getPlot();
            plot.getAxis().setLabel(rTitle);
        }
        this.rTitle = rTitle;
    }

    @ChartSetting(name = "R-Axis title", type = Type.GET)
    public String getXTitle() {
        return this.rTitle;
    }

}
