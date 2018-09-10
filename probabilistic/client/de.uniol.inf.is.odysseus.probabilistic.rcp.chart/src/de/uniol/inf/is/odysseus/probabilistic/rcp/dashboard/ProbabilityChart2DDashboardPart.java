/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.rcp.dashboard;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

/**
 *
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class ProbabilityChart2DDashboardPart extends AbstractDashboardPart {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ProbabilityChart2DDashboardPart.class);
    /** First sink operator in the query. */
    private IPhysicalOperator operator;
    /** The selected attributes. */
    private String[] attributes;
    /** The continuous attributes marker. */
    private Boolean[] continuousAttributes;
    /** The discrete attributes marker. */
    private Boolean[] discreteAttributes;
    /** The positions to restrict to. */
    private int[] positions;
    /** The data set to draw. */
    private final XYSeriesCollection dataset = new XYSeriesCollection();
    /** Upper Bound for X-Axis. */
    private double xMax = 100.0;
    /** Upper Bound for y-Axis. */
    private double yMax = 1.0;
    /** Lower Bound for X-Axis. */
    private double xMin = -100.0;
    /** Lower Bound for y-Axis. */
    private double yMin = 0.0;
    /** Number of samples. */
    private int samples = 1000;
    /** The chart. */
    private ChartComposite chartComposite;

    private String attributeList;

    public final void setAttributeList(final String list) {
        this.attributeList = list;
    }

    public final String getAttributeList() {
        return this.attributeList;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart#createPartControl
     * (org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.ToolBar)
     */
    @Override
    public final void createPartControl(final Composite parent, final ToolBar toolbar) {
        if (Strings.isNullOrEmpty(this.attributeList)) {
            new Label(parent, SWT.NONE).setText("Attribute List is invalid!");
            return;
        }

        this.attributes = this.attributeList.trim().split(",");
        for (int i = 0; i < this.attributes.length; i++) {
            this.attributes[i] = this.attributes[i].trim();
        }
        this.chartComposite = new ChartComposite(parent, SWT.NONE, this.createChart(), true);
        this.chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener
     * #streamElementRecieved(java.lang.Object, int)
     */
    @Override
    public final void streamElementRecieved(final IPhysicalOperator senderOperator, final IStreamObject<?> element, final int port) {
        final ProbabilisticTuple<?> probabilisticElement = (ProbabilisticTuple<?>) element;
        final ProbabilisticTuple<?> restrictedElement = probabilisticElement.restrict(this.positions, false);

        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < ProbabilityChart2DDashboardPart.this.attributes.length; i++) {
                    final String key = ProbabilityChart2DDashboardPart.this.attributes[i];
                    final Object value = restrictedElement.getAttribute(i);
                    XYSeries currentserie;
                    if (!ProbabilityChart2DDashboardPart.this.containsSeriesWithKey(key)) {
                        currentserie = new XYSeries(key);
                        currentserie.setMaximumItemCount(ProbabilityChart2DDashboardPart.this.samples);
                        ProbabilityChart2DDashboardPart.this.dataset.addSeries(currentserie);
                    }
                    else {
                        currentserie = ProbabilityChart2DDashboardPart.this.dataset.getSeries(key);
                    }
                    currentserie.clear();
                    final ProbabilisticDouble continuousAttribute = (ProbabilisticDouble) value;
                    ProbabilityChart2DDashboardPart.this.updateSerie(currentserie, restrictedElement.getDistribution(continuousAttribute.getDistribution()), i);
                }

                if (!ProbabilityChart2DDashboardPart.this.chartComposite.isDisposed()) {
                    ProbabilityChart2DDashboardPart.this.chartComposite.forceRedraw();
                }

            }
        });

    }

    /**
     * Checks whether the data set includes the given key.
     *
     * @param key
     *            The comparable key
     * @return <code>true</code> if the data set includes the key
     */
    protected final boolean containsSeriesWithKey(final String key) {
        return this.dataset.indexOf(key) >= 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener
     * #punctuationElementRecieved
     * (de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation, int)
     */
    @Override
    public void punctuationElementRecieved(final IPhysicalOperator senderOperator, final IPunctuation point, final int port) {

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart#onStart(
     * java.util.List)
     */
    @Override
    public final void onStart(final Collection<IPhysicalOperator> physicalRoots) throws Exception {
        super.onStart(physicalRoots);
        if (physicalRoots.size() > 1) {
            ProbabilityChart2DDashboardPart.LOG.error("Probability Chart DashboardPart only supports one query!");
            throw new Exception("Probability Chart DashboardPart only supports one query!");
        }
        this.operator = physicalRoots.iterator().next();
        this.positions = ProbabilityChart2DDashboardPart.determinePositions(this.operator.getOutputSchema(), this.attributes);

        this.continuousAttributes = new Boolean[this.positions.length];
        Arrays.fill(this.continuousAttributes, false);

        this.discreteAttributes = new Boolean[this.positions.length];
        Arrays.fill(this.discreteAttributes, false);
        for (int i = 0; i < this.positions.length; i++) {
            final SDFAttribute attribute = this.operator.getOutputSchema().get(this.positions[i]);
            if (SchemaUtils.isProbabilisticAttribute(attribute)) {
                this.continuousAttributes[i] = true;
            }
        }
    }

    /**
     * Gets the value of the samples property.
     *
     * @return The samples value
     */
    public final int getSamples() {
        return this.samples;
    }

    /**
     * Sets the value of the samples property.
     *
     * @param samples
     *            The number of samples
     */
    public final void setSamples(final int samples) {
        this.samples = samples;
    }

    /**
     * Gets the value of the xMax property.
     *
     * @return The xMax value
     */
    public final double getXMax() {
        return this.xMax;
    }

    /**
     * Sets the value of the xMax property.
     *
     * @param x
     *            The xMax value
     */
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
    public final double getYMax() {
        return this.yMax;
    }

    /**
     * Sets the value of the yMax property.
     *
     * @param y
     *            The yMax value
     */
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
    public final double getXMin() {
        return this.xMin;
    }

    /**
     * Sets the value of the xMin property.
     *
     * @param x
     *            The xMin value
     */
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
    public final double getYMin() {
        return this.yMin;
    }

    /**
     * Sets the value of the yMin property.
     *
     * @param y
     *            The yMin value
     */
    public final void setYMin(final double y) {
        this.yMin = y;
        if (this.yMax < y) {
            this.yMax = y;
        }
    }

    /**
     * Gets the dataset.
     *
     * @return The dataset
     */
    private XYSeriesCollection getDataset() {
        return this.dataset;
    }

    /**
     * Creates the line chart.
     *
     * @return The chart
     */
    private JFreeChart createChart() {
        final JFreeChart chart = ChartFactory.createXYLineChart("Probabilistic Chart (2D)", "", "", this.getDataset(), PlotOrientation.VERTICAL, true, true, false);
        chart.getXYPlot().setRenderer(new XYSplineRenderer());
        return chart;
    }

    /**
     * Returns the indexes of the given list of attributes in the given schema.
     *
     * @param schema
     *            The schema
     * @param attributes
     *            The attributes
     * @return The positions for each attribute
     */
    private static int[] determinePositions(final SDFSchema schema, final String[] attributes) {
        final int[] positions = new int[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            for (int j = 0; j < schema.size(); j++) {
                if (schema.get(j).getAttributeName().equalsIgnoreCase(attributes[i])) {
                    positions[i] = j;
                    break;
                }
            }
        }

        return positions;
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
                final double[] values = new double[mixture.getDimension()];
                values[dimension] = x;
                final double density = distribution.density(values);
                return density;
            }
        };
        @SuppressWarnings("unchecked")
        final List<XYDataItem> items = DatasetUtilities.sampleFunction2DToSeries(function, this.getXMin(), this.getXMax(), this.getSamples(), series.getKey()).getItems();
        for (final XYDataItem item : items) {
            series.add(item);
        }

    }

}
