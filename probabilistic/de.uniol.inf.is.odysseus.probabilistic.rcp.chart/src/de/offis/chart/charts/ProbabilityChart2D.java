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
package de.offis.chart.charts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.offis.chart.charts.datatype.ProbabilisticViewSchema;
import de.offis.chart.charts.datatype.ProbabilisticViewableSDFAttribute;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

public class ProbabilityChart2D extends AbstractJFreeChart<Object, IMetaAttribute> {

    private final XYSeriesCollection dataset = new XYSeriesCollection();

    private double maxX = 100.0;
    private double maxY = 1.0;
    private double minX = -100.0;
    private double minY = 0.0;

    private int samples = 1000;

    private boolean autoadjust = false;
    private final double margin = 0.05; // 5 percent

    private int redrawEach = 0;
    private int count = 0;

    @Override
    public String getViewID() {
        return "de.offis.chart.charts.probabilitychart2d";
    }

    @Override
    public void chartSettingsChanged() {
        if (!this.autoadjust) {
            if (!Double.isNaN(this.minY)) {
                this.getChart().getXYPlot().getRangeAxis().setLowerBound(this.minY * (1.0 - this.margin));
            }
            if (!Double.isNaN(this.maxY)) {
                this.getChart().getXYPlot().getRangeAxis().setUpperBound(this.maxY * (1.0 + this.margin));
            }
            if (!Double.isNaN(this.minX)) {
                this.getChart().getXYPlot().getDomainAxis().setLowerBound(this.minX * (1.0 - this.margin));
            }
            if (!Double.isNaN(this.maxX)) {
                this.getChart().getXYPlot().getDomainAxis().setUpperBound(this.maxX * (1.0 + this.margin));
            }
        }

        this.dataset.removeAllSeries();
    }

    @Override
    public String isValidSelection(final Map<Integer, Set<IViewableAttribute>> selectAttributes) {
        int sel = 0;
        for (final Entry<Integer, Set<IViewableAttribute>> e : selectAttributes.entrySet()) {
            final Set<IViewableAttribute> attributes = e.getValue();
            for (final IViewableAttribute attribute : attributes) {
                final SDFDatatype type = attribute.getSDFDatatype();
                if (type.getClass().equals(SDFProbabilisticDatatype.class)) {
                    final SDFProbabilisticDatatype probType = (SDFProbabilisticDatatype) type;
                    if (probType.isContinuous()) {
                        sel++;
                    } else if (probType.isDiscrete()) {
                        sel++;
                    }
                }
            }
        }
        if (sel > 0) {
            return null;
        }
        return "The number of choosen attributes should be at least one!";
    }

    public XYDataset getDataset() {
        return this.dataset;
    }

    @ChartSetting(name = "Upper Bound for X-Axis", type = Type.GET)
    public double getMaxX() {
        return this.maxX;
    }

    @ChartSetting(name = "Upper Bound for X-Axis", type = Type.SET)
    public void setMaxX(final double maxX) {
        this.maxX = maxX;
    }

    @ChartSetting(name = "Upper Bound for Y-Axis", type = Type.GET)
    public double getMaxY() {
        return this.maxY;
    }

    @ChartSetting(name = "Upper Bound for Y-Axis", type = Type.SET)
    public void setMaxY(final double maxY) {
        if ((maxY >= 0.0) && (maxY <= 1.0)) {
            this.maxY = maxY;
        }
    }

    @ChartSetting(name = "Lower Bound for X-Axis", type = Type.GET)
    public double getMinX() {
        return this.minX;
    }

    @ChartSetting(name = "Lower Bound for X-Axis", type = Type.SET)
    public void setMinX(final double minX) {
        this.minX = minX;
    }

    @ChartSetting(name = "Lower Bound for Y-Axis", type = Type.GET)
    public double getMinY() {
        return this.minY;
    }

    @ChartSetting(name = "Lower Bound for Y-Axis", type = Type.SET)
    public void setMinY(final double minY) {
        if ((minY >= 0.0) && (minY <= 1.0)) {
            this.minY = minY;
        }
    }

    @ChartSetting(name = "Autoadjust Bounds", type = Type.GET)
    public Boolean isAutoadjust() {
        return this.autoadjust;
    }

    @ChartSetting(name = "Autoadjust Bounds", type = Type.SET)
    public void setAutoadjust(final Boolean autoadjust) {
        this.autoadjust = autoadjust;
    }

    @ChartSetting(name = "Number of samples", type = Type.GET)
    public int getSamples() {
        return this.samples;
    }

    @ChartSetting(name = "Number of samples", type = Type.SET)
    public void setSamples(final int samples) {
        this.samples = samples;
    }

    @ChartSetting(name = "Redraw after each", type = Type.GET)
    public int getRedrawEach() {
        return this.redrawEach;
    }

    @ChartSetting(name = "Redraw after each", type = Type.SET)
    public void setRedrawEach(final int redrawEach) {
        this.redrawEach = redrawEach;
    }

    @Override
    protected void init() {

    }

    @Override
    protected JFreeChart createChart() {
        final JFreeChart chart = ChartFactory.createXYLineChart(this.getTitle(), "", "", this.getDataset(), PlotOrientation.VERTICAL, true, true, false);
        chart.getPlot().setBackgroundPaint(AbstractJFreeChart.DEFAULT_BACKGROUND);
        chart.getLegend().setBackgroundPaint(AbstractJFreeChart.DEFAULT_BACKGROUND_GRID);
        chart.getXYPlot().setRenderer(new XYSplineRenderer());
        return chart;
    }

    @Override
    protected void decorateChart(final JFreeChart thechart) {

    }

    @Override
    protected void initConnection(final IStreamConnection<IStreamObject<?>> streamConnection) {
        for (final ISubscription<? extends ISource<?>> s : streamConnection.getSubscriptions()) {
            this.viewSchema.put(s.getSinkInPort(), new ProbabilisticViewSchema<Object>(s.getSchema(), s.getTarget().getMetaAttributeSchema(), s.getSinkInPort()));
        }
        if (this.validate()) {
            streamConnection.addStreamElementListener(this);
            streamConnection.connect();
            this.chartSettingsChanged();
            this.init();
        }
    }

    @Override
    protected void processElement(final List<Object> tuple, final IMetaAttribute metadata, final int port) {
        this.getSite().getShell().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                int serie = 0;
                for (final int port : ProbabilityChart2D.this.getPorts()) {
                    final Iterator<IViewableAttribute> iter = ProbabilityChart2D.this.getChoosenAttributes(port).iterator();
                    while (iter.hasNext()) {
                        final ProbabilisticViewableSDFAttribute attribute = (ProbabilisticViewableSDFAttribute) iter.next();
                        final String key = attribute.getName();
                        final SDFProbabilisticDatatype type = (SDFProbabilisticDatatype) attribute.getSDFDatatype();
                        final int index = attribute.getIndex();
                        boolean isContinuous = false;
                        if (type.isContinuous()) {
                            isContinuous = true;
                        }
                        XYSeries currentserie;
                        if (!ProbabilityChart2D.this.containsSeriesWithKey(key)) {
                            currentserie = new XYSeries(key);
                            currentserie.setMaximumItemCount(ProbabilityChart2D.this.samples);
                            ProbabilityChart2D.this.dataset.addSeries(currentserie);
                        } else {
                            currentserie = ProbabilityChart2D.this.dataset.getSeries(key);
                        }

                        final Object x = tuple.get(serie);
                        currentserie.clear();
                        if (isContinuous) {
                            ProbabilityChart2D.this.updateSerie(currentserie, (NormalDistributionMixture) x, index);
                        } else {
                            ProbabilityChart2D.this.updateSerie(currentserie, (AbstractProbabilisticValue<?>) x);
                        }

                        ProbabilityChart2D.this.count++;
                        if (ProbabilityChart2D.this.count > ProbabilityChart2D.this.redrawEach) {
                            ProbabilityChart2D.this.count = 0;
                            currentserie.setNotify(true);
                            ProbabilityChart2D.this.adjust(currentserie.getMaxX(), currentserie.getMaxY());
                        } else {
                            currentserie.setNotify(false);
                        }
                    }
                    serie++;
                }
            }
        });
    }

    private void adjust(final double valueX, final double valueY) {
        // for X
        if (Double.isNaN(this.maxX)) {
            this.maxX = valueX;
        }
        if (Double.isNaN(this.minX)) {
            this.minX = valueX;
        }
        if (valueX > this.maxX) {
            this.maxX = valueX;
        } else if (valueX < this.minX) {
            this.minX = valueX;
        }

        if (this.autoadjust) {
            this.getChart().getXYPlot().getRangeAxis().setLowerBound(this.minY * (1.0 - this.margin));
            this.getChart().getXYPlot().getRangeAxis().setUpperBound(this.maxY * (1.0 + this.margin));

            this.getChart().getXYPlot().getDomainAxis().setLowerBound(this.minX * (1.0 - this.margin));
            this.getChart().getXYPlot().getDomainAxis().setUpperBound(this.maxX * (1.0 + this.margin));
        }

    }

    private boolean containsSeriesWithKey(final Comparable<?> key) {
        for (final Object o : this.dataset.getSeries()) {
            final XYSeries s = (XYSeries) o;
            if (s.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    private void updateSerie(final XYSeries series, final AbstractProbabilisticValue<?> value) {
        for (final Entry<?, Double> e : value.getValues().entrySet()) {
            series.add(((Number) e.getKey()).doubleValue() - Double.MIN_VALUE, 0.0);
            series.add(((Number) e.getKey()).doubleValue(), e.getValue());
            series.add(((Number) e.getKey()).doubleValue() + Double.MIN_VALUE, 0.0);
        }
    }

    private void updateSerie(final XYSeries series, final NormalDistributionMixture mix, final int index) {
        if (mix.getDimension() < 1) {
            return; // no dimension
        }

        final HashMap<NormalDistributionFunction2D, Double> funcs = new HashMap<>();
        final int dimension;
        int d = 0;
        while (d < mix.getDimension()) {
            if (mix.getAttribute(d) == index) {
                break;
            }
            d++;
        }
        dimension = d;
        for (final Entry<NormalDistribution, Double> e : mix.getMixtures().entrySet()) {
            final double means = e.getKey().getMean()[dimension];
            final double m = e.getKey().getCovarianceMatrix().getMatrix().getEntry(dimension, dimension);
            funcs.put(new NormalDistributionFunction2D(means, m), e.getValue());
        }
        final Interval[] interval = mix.getSupport();
        final double scale = mix.getScale();

        final Function2D f = new Function2D() {

            @Override
            public double getValue(final double x) {
                if ((x < interval[dimension].inf()) || (x > interval[dimension].sup())) {
                    return 0.0;
                }

                double sum = 0;
                for (final Entry<NormalDistributionFunction2D, Double> func : funcs.entrySet()) {
                    sum += func.getKey().getValue(x) * func.getValue();
                }
                return sum * scale;
            }
        };
        @SuppressWarnings("unchecked")
        final List<XYDataItem> items = DatasetUtilities.sampleFunction2DToSeries(f, this.getMinX(), this.getMaxX(), this.getSamples(), series.getKey()).getItems();
        for (final XYDataItem item : items) {
            series.add(item);
        }
    }

	@Override
	protected void reloadChart() {
		
	}
}
