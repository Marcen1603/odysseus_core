package de.uniol.inf.is.odysseus.probabilistic.rcp.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import org.jfree.data.function.NormalDistributionFunction2D;
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
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
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

    private String[] attributes;
    private Boolean[] continuousAttributes;
    private int[] positions;
    private final XYSeriesCollection dataset = new XYSeriesCollection();

    private int samples = 1000;
    private double minX = -100.0;
    private double maxX = 100.0;
    private ChartComposite chartComposite;

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart#createPartControl
     * (org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.ToolBar)
     */
    @Override
    public void createPartControl(Composite parent, ToolBar toolbar) {
        final String attributeList = getConfiguration().get("Attributes");
        if (Strings.isNullOrEmpty(attributeList)) {
            new Label(parent, SWT.NONE).setText("Attribute List is invalid!");
            return;
        }

        attributes = attributeList.trim().split(",");
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = attributes[i].trim();
        }
        this.chartComposite = new ChartComposite(parent, SWT.NONE, createChart(), true);
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
    public void streamElementRecieved(final IStreamObject<?> element, int port) {
        ProbabilisticTuple<?> probabilisticElement = (ProbabilisticTuple<?>) element;
        final ProbabilisticTuple<?> restrictedElement = probabilisticElement.restrict(positions, false);

        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < attributes.length; i++) {
                    final String key = attributes[i];
                    Object value = restrictedElement.getAttribute(i);
                    XYSeries currentserie;
                    if (!ProbabilityChart2DDashboardPart.this.containsSeriesWithKey(key)) {
                        currentserie = new XYSeries(key);
                        // currentserie.setMaximumItemCount(ProbabilityChart2D.this.samples);
                        ProbabilityChart2DDashboardPart.this.dataset.addSeries(currentserie);
                    } else {
                        currentserie = ProbabilityChart2DDashboardPart.this.dataset.getSeries(key);
                    }
                    currentserie.clear();
                    if (isContinuous(i)) {
                        ProbabilisticContinuousDouble continuousAttribute = (ProbabilisticContinuousDouble) value;
                        ProbabilityChart2DDashboardPart.this.updateSerie(currentserie, restrictedElement.getDistribution(continuousAttribute.getDistribution()), i);
                    } else {
                        ProbabilityChart2DDashboardPart.this.updateSerie(currentserie, (AbstractProbabilisticValue<?>) value);
                    }
                }

                if (!chartComposite.isDisposed()) {
                    chartComposite.forceRedraw();
                }

            }
        });

    }

    protected boolean containsSeriesWithKey(String key) {
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
    public void punctuationElementRecieved(IPunctuation point, int port) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener
     * #securityPunctuationElementRecieved
     * (de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation,
     * int)
     */
    @Override
    public void securityPunctuationElementRecieved(ISecurityPunctuation sp, int port) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.dashboard.IConfigurationListener#settingChanged
     * (java.lang.String, java.lang.Object, java.lang.Object)
     */
    @Override
    public void settingChanged(String settingName, Object oldValue, Object newValue) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart#onStart(
     * java.util.List)
     */
    @Override
    public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
        super.onStart(physicalRoots);
        if (physicalRoots.size() > 1) {
            LOG.error("Probability Chart DashboardPart only supports one query!");
            throw new Exception("Probability Chart DashboardPart only supports one query!");
        }
        operator = physicalRoots.get(0);
        positions = determinePositions(operator.getOutputSchema(), attributes);
        continuousAttributes = new Boolean[positions.length];
        for (int i = 0; i < positions.length; i++) {
            SDFAttribute attribute = operator.getOutputSchema().get(positions[i]);
            if (attribute.getDatatype().getClass().equals(SDFProbabilisticDatatype.class)) {
                SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute.getDatatype();
                if (datatype.isContinuous()) {
                    continuousAttributes[i] = true;
                } else {
                    continuousAttributes[i] = false;
                }
            } else {
                continuousAttributes[i] = false;
            }
        }
    }

    private boolean isContinuous(int index) {
        if ((index < 0) && (index >= continuousAttributes.length)) {
            return false;
        }
        return continuousAttributes[index];
    }

    /**
     * Sets the value of the samples property.
     * 
     * @param samples
     *            The samples value
     */
    public void setSamples(final int samples) {
        this.samples = samples;
    }

    /**
     * Gets the number of samples.
     * 
     * @return The number of samples
     */
    public int getSamples() {
        return samples;
    }

    /**
     * Sets the value of the minX property.
     * 
     * @param minX
     *            The minX value
     */
    public void setMinX(final double minX) {
        this.minX = minX;
    }

    /**
     * Gets the minimal X value.
     * 
     * @return The minimal X value
     */
    public double getMinX() {
        return minX;
    }

    /**
     * Sets the value of the maxX property.
     * 
     * @param maxX
     *            The maxX value
     */
    public void setMaxX(final double maxX) {
        this.maxX = maxX;
    }

    /**
     * Gets the maximal X value.
     * 
     * @return The maximal X value
     */
    public double getMaxX() {
        return maxX;
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
     * 
     * @return
     */
    private JFreeChart createChart() {
        final JFreeChart chart = ChartFactory.createXYLineChart("ProbabilisticChart2D", "", "", this.getDataset(), PlotOrientation.VERTICAL, true, true, false);
        chart.getXYPlot().setRenderer(new XYSplineRenderer());
        return chart;
    }

    /**
     * 
     * @param outputSchema
     * @param attributes
     * @return
     */
    private static int[] determinePositions(SDFSchema outputSchema, String[] attributes) {
        final int[] positions = new int[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            for (int j = 0; j < outputSchema.size(); j++) {
                if (outputSchema.get(j).getAttributeName().equalsIgnoreCase(attributes[i])) {
                    positions[i] = j;
                    break;
                }
            }
        }

        return positions;
    }

    /**
     * Update series with discrete probabilistic value.
     * 
     * @param series
     *            The series
     * @param value
     *            The discrete probabilistic value
     */
    private void updateSerie(final XYSeries series, final AbstractProbabilisticValue<?> value) {
        for (final Entry<?, Double> e : value.getValues().entrySet()) {
            series.add(((Number) e.getKey()).doubleValue() - Double.MIN_VALUE, 0.0);
            series.add(((Number) e.getKey()).doubleValue(), e.getValue());
            series.add(((Number) e.getKey()).doubleValue() + Double.MIN_VALUE, 0.0);
        }
    }

    /**
     * Update series with continuous probabilistic value.
     * 
     * @param series
     *            The series
     * @param distribution
     *            The distribution
     * @param index
     *            The dimension index
     */
    private void updateSerie(final XYSeries series, final NormalDistributionMixture distribution, final int index) {
        if (distribution.getDimension() < 1) {
            LOG.warn("Invalid dimension: " + distribution.getDimension());
            return; // no dimension
        }

        final HashMap<NormalDistributionFunction2D, Double> mixtures = new HashMap<>();
        final int dimension;
        int d = 0;
        while (d < distribution.getDimension()) {
            if (distribution.getAttribute(d) == index) {
                break;
            }
            d++;
        }
        dimension = d;
        for (final Entry<NormalDistribution, Double> mixtureEntry : distribution.getMixtures().entrySet()) {
            final double mean = mixtureEntry.getKey().getMean()[dimension];
            final double variance = mixtureEntry.getKey().getCovarianceMatrix().getMatrix().getEntry(dimension, dimension);
            mixtures.put(new NormalDistributionFunction2D(mean, variance), mixtureEntry.getValue());
        }
        final Interval[] support = distribution.getSupport();
        final double scale = distribution.getScale();

        final Function2D function = new Function2D() {

            @Override
            public double getValue(final double x) {
                if ((support[dimension] != null) && ((x < support[dimension].inf()) || (x > support[dimension].sup()))) {
                    return 0.0;
                }

                double sum = 0;
                for (final Entry<NormalDistributionFunction2D, Double> func : mixtures.entrySet()) {
                    sum += func.getKey().getValue(x) * func.getValue();
                }
                return sum * scale;
            }
        };
        @SuppressWarnings("unchecked")
        final List<XYDataItem> items = DatasetUtilities.sampleFunction2DToSeries(function, this.getMinX(), this.getMaxX(), this.getSamples(), series.getKey()).getItems();
        for (final XYDataItem item : items) {
            series.add(item);
        }
    }

}
