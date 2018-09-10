/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package de.uniol.inf.is.odysseus.probabilistic.rcp.chart;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.rcp.chart.datatype.ProbabilisticViewSchema;
import de.uniol.inf.is.odysseus.probabilistic.rcp.chart.datatype.ProbabilisticViewableSDFAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting.Type;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilityChart2D extends AbstractJFreeChart<Object, IMetaAttribute> {
    /** The data set to draw. */
    private final XYSeriesCollection dataset = new XYSeriesCollection();
    /** Upper Bound for X-Axis. */
    private double xMax = 10.0;
    /** Upper Bound for y-Axis. */
    private double yMax = 1.0;
    /** Lower Bound for X-Axis. */
    private double xMin = -10.0;
    /** Lower Bound for y-Axis. */
    private double yMin = 0.0;
    /** Number of samples. */
    private int samples = 1000;
    /** Auto adjust the drawing area. */
    private boolean autoadjust = false;
    /** Margins to the borders. */
    private final double margin = 0.05; // 5 percent
    /** Number of data to redraw. */
    private int redrawEach = 0;
    /** Counter for redrawe. */
    private int count = 0;

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart#getViewID
     * ()
     */
    @Override
    public final String getViewID() {
        return "de.offis.chart.charts.probabilitychart2d";
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart#
     * chartSettingsChanged()
     */
    @Override
    public final void chartSettingsChanged() {
        if (!this.autoadjust) {
            if (!Double.isNaN(this.yMin)) {
                this.getChart().getXYPlot().getRangeAxis().setLowerBound(this.yMin * (1.0 - this.margin));
            }
            if (!Double.isNaN(this.yMax)) {
                this.getChart().getXYPlot().getRangeAxis().setUpperBound(this.yMax * (1.0 + this.margin));
            }
            if (!Double.isNaN(this.xMin)) {
                this.getChart().getXYPlot().getDomainAxis().setLowerBound(this.xMin * (1.0 - this.margin));
            }
            if (!Double.isNaN(this.xMax)) {
                this.getChart().getXYPlot().getDomainAxis().setUpperBound(this.xMax * (1.0 + this.margin));
            }
        }

        this.dataset.removeAllSeries();
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.IAttributesChangeable
     * #isValidSelection(java.util.Map)
     */
    @Override
    public final String isValidSelection(final Map<Integer, Set<IViewableAttribute>> selectAttributes) {
        int sel = 0;
        for (final Entry<Integer, Set<IViewableAttribute>> e : selectAttributes.entrySet()) {
            final Set<IViewableAttribute> attributes = e.getValue();
            for (final IViewableAttribute attribute : attributes) {
                final SDFDatatype type = attribute.getSDFDatatype();
                if (type.getClass().equals(SDFProbabilisticDatatype.class)) {
                    sel++;
                }
            }
        }
        if (sel > 0) {
            return null;
        }
        return "The number of choosen attributes should be at least one!";
    }

    /**
     * 
     * @return The data set
     */
    public final XYDataset getDataset() {
        return this.dataset;
    }

    /**
     * Gets the value of the xMax property.
     * 
     * @return The xMax value
     */
    @ChartSetting(name = "Upper Bound for X-Axis", type = Type.GET)
    public final double getXMax() {
        return this.xMax;
    }

    /**
     * Sets the value of the xMax property.
     * 
     * @param x
     *            The xMax value
     */
    @ChartSetting(name = "Upper Bound for X-Axis", type = Type.SET)
    public final void setXMax(final double x) {
        this.xMax = x;
        if (this.xMin > x) {
            this.xMin = x;
        }
    }

    /**
     * Gets the value of the yMax property.
     * 
     * @return The yMax value
     */
    @ChartSetting(name = "Upper Bound for Y-Axis", type = Type.GET)
    public final double getYMax() {
        return this.yMax;
    }

    /**
     * Sets the value of the yMax property.
     * 
     * @param y
     *            The yMax value
     */
    @ChartSetting(name = "Upper Bound for Y-Axis", type = Type.SET)
    public final void setYMax(final double y) {
        this.yMax = y;
        if (this.yMin > y) {
            this.yMin = y;
        }
    }

    /**
     * Gets the value of the xMin property.
     * 
     * @return The xMin value
     */
    @ChartSetting(name = "Lower Bound for X-Axis", type = Type.GET)
    public final double getXMin() {
        return this.xMin;
    }

    /**
     * Sets the value of the xMin property.
     * 
     * @param x
     *            The xMin value
     */
    @ChartSetting(name = "Lower Bound for X-Axis", type = Type.SET)
    public final void setXMin(final double x) {
        this.xMin = x;
        if (this.xMax < x) {
            this.xMax = x;
        }
    }

    /**
     * Gets the value of the yMin property.
     * 
     * @return The yMin value
     */
    @ChartSetting(name = "Lower Bound for Y-Axis", type = Type.GET)
    public final double getYMin() {
        return this.yMin;
    }

    /**
     * Sets the value of the yMin property.
     * 
     * @param y
     *            The yMin value
     */
    @ChartSetting(name = "Lower Bound for Y-Axis", type = Type.SET)
    public final void setYMin(final double y) {
        this.yMin = y;
        if (this.yMax < y) {
            this.yMax = y;
        }
    }

    /**
     * Gets the value of the autoadjust property.
     * 
     * @return The autoadjust value
     */
    @ChartSetting(name = "Autoadjust Bounds", type = Type.GET)
    public final Boolean isAutoadjust() {
        return this.autoadjust;
    }

    /**
     * Sets the value of the autoadjust property.
     * 
     * @param autoadjust
     *            Whether to enable or disable auto adjust
     */
    @ChartSetting(name = "Autoadjust Bounds", type = Type.SET)
    public final void setAutoadjust(final Boolean autoadjust) {
        this.autoadjust = autoadjust;
    }

    /**
     * Gets the value of the samples property.
     * 
     * @return The samples value
     */
    @ChartSetting(name = "Number of samples", type = Type.GET)
    public final int getSamples() {
        return this.samples;
    }

    /**
     * Sets the value of the samples property.
     * 
     * @param samples
     *            The number of samples
     */
    @ChartSetting(name = "Number of samples", type = Type.SET)
    public final void setSamples(final int samples) {
        this.samples = samples;
    }

    /**
     * Gets the value of the redrawEach property.
     * 
     * @return The redrawEach value
     */
    @ChartSetting(name = "Redraw after each", type = Type.GET)
    public final int getRedrawEach() {
        return this.redrawEach;
    }

    /**
     * Sets the value of the redrawEach property.
     * 
     * @param redrawEach
     *            The number of data
     */
    @ChartSetting(name = "Redraw after each", type = Type.SET)
    public final void setRedrawEach(final int redrawEach) {
        this.redrawEach = redrawEach;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart#init()
     */
    @Override
    protected void init() {

    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart#
     * createChart()
     */
    @Override
    protected final JFreeChart createChart() {
        final JFreeChart chart = ChartFactory.createXYLineChart(this.getTitle(), "", "", this.getDataset(), PlotOrientation.VERTICAL, true, true, false);
        chart.getPlot().setBackgroundPaint(AbstractJFreeChart.DEFAULT_BACKGROUND);
        chart.getLegend().setBackgroundPaint(AbstractJFreeChart.DEFAULT_BACKGROUND_GRID);
        return chart;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart#
     * decorateChart(org.jfree.chart.JFreeChart)
     */
    @Override
    protected void decorateChart(final JFreeChart thechart) {

    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart#initConnection
     * (de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection)
     */
    @Override
    protected final void initConnection(final IStreamConnection<IStreamObject<?>> streamConnection) {
        for (final ISubscription<? extends ISource<?>,?> s : streamConnection.getSubscriptions()) {
            this.viewSchema.put(s.getSinkInPort(), new ProbabilisticViewSchema<Object>(s.getSchema(), (s.getSource()).getMetaAttributeSchema(), s.getSinkInPort()));
        }
        if (this.validate()) {
            streamConnection.addStreamElementListener(this);
            streamConnection.connect();
            this.chartSettingsChanged();
            this.init();
        }
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart#processElement
     * (java.util.List, de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute,
     * int)
     */
    @Override
    protected final void processElement(final List<Object> tuple, final IMetaAttribute metadata, final int port) {
        this.getSite().getShell().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                int serie = 0;
                for (final int port : ProbabilityChart2D.this.getPorts()) {
                    final Iterator<IViewableAttribute> iter = ProbabilityChart2D.this.getChoosenAttributes(port).iterator();
                    while (iter.hasNext()) {
                        final ProbabilisticViewableSDFAttribute attribute = (ProbabilisticViewableSDFAttribute) iter.next();
                        final String key = attribute.getName();
                        final int index = attribute.getIndex();
                        XYSeries currentserie;
                        if (!ProbabilityChart2D.this.containsSeriesWithKey(key)) {
                            currentserie = new XYSeries(key);
                            currentserie.setMaximumItemCount(ProbabilityChart2D.this.samples);
                            ProbabilityChart2D.this.dataset.addSeries(currentserie);
                        }
                        else {
                            currentserie = ProbabilityChart2D.this.dataset.getSeries(key);
                        }

                        final Object x = tuple.get(serie);
                        ProbabilityChart2D.this.updateSerie(currentserie, (MultivariateMixtureDistribution) x, index);

                        ProbabilityChart2D.this.count++;
                        if (ProbabilityChart2D.this.count > ProbabilityChart2D.this.redrawEach) {
                            ProbabilityChart2D.this.count = 0;
                            currentserie.setNotify(true);
                            ProbabilityChart2D.this.adjust(currentserie.getMinX(), currentserie.getMaxX(), currentserie.getMinY(), currentserie.getMaxY());
                        }
                        else {
                            currentserie.setNotify(false);
                        }
                        serie++;
                    }

                }
            }
        });
    }

    /**
     * Adjust the drawing area if the area does not include the given position.
     * 
     * @param minX
     *            The x min value
     * @param maxX
     *            The x max value
     * @param minY
     *            The y min value
     * @param maxY
     *            The y max value
     */
    private void adjust(final double minX, final double maxX, final double minY, final double maxY) {
        if (Double.isNaN(this.getXMax())) {
            this.setXMax(maxX);
        }
        if (Double.isNaN(this.getXMin())) {
            this.setXMin(minX);
        }
        if (Double.isNaN(this.getYMax())) {
            this.setYMax(maxY);
        }
        if (Double.isNaN(this.getYMin())) {
            this.setYMin(minY);
        }
        if (this.autoadjust) {
            this.getChart().getXYPlot().getRangeAxis().setLowerBound(minY * (1.0 - this.margin));
            this.getChart().getXYPlot().getRangeAxis().setUpperBound(maxY * (1.0 + this.margin));

            this.getChart().getXYPlot().getDomainAxis().setLowerBound(minX * (1.0 - this.margin));
            this.getChart().getXYPlot().getDomainAxis().setUpperBound(maxX * (1.0 + this.margin));
        }
    }

    /**
     * Checks whether the data set includes the given key.
     * 
     * @param key
     *            The comparable key
     * @return <code>true</code> if the data set includes the key
     */
    private boolean containsSeriesWithKey(final String key) {
        return this.dataset.indexOf(key) >= 0;
    }

    /**
     * Updates the given series with the given continuous probabilistic value.
     * 
     * @param series
     *            The series
     * @param mixture
     *            The normal distribution mixture
     * @param dimensionIndex
     *            The dimension
     */
    private void updateSerie(final XYSeries series, final MultivariateMixtureDistribution mixture, final int dimensionIndex) {
        if (mixture.getDimension() < 1) {
            return; // no dimension
        }

        final int dimension;
        int d = 0;
        while (d < mixture.getDimension()) {
            if (mixture.getAttribute(d) == dimensionIndex) {
                break;
            }
            d++;
        }
        dimension = d;

        final MultivariateMixtureDistribution distribution = mixture;

        final Function2D function = new Function2D() {

            @Override
            public double getValue(final double x) {
                final double[] values = new double[distribution.getDimension()];
                values[dimension] = x;
                return distribution.density(values);
            }
        };
        try {
            @SuppressWarnings("unchecked")
            final List<XYDataItem> items = DatasetUtilities.sampleFunction2DToSeries(function, this.getXMin(), this.getXMax(), this.getSamples(), series.getKey()).getItems();
            series.clear();
            for (final XYDataItem item : items) {
                if (!this.getChartComposite().isDisposed()) {
                    series.add(item);
                }
            }
        }
        catch (final Exception e) {
        }
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart#reloadChart
     * ()
     */
    @Override
    protected void reloadChart() {

    }
}
