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

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.rcp.chart.ProbabilisticMapper;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSelectedAttributesAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSettingsAction;
import net.ericaro.surfaceplotter.JSurfacePanel;
import net.ericaro.surfaceplotter.ProgressiveSurfaceModel;

/**
 *
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
@SuppressWarnings("unused")
public class ProbabilityChart3DDashboardPart extends AbstractDashboardPart {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ProbabilityChart3DDashboardPart.class);
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

    /** The number of samples. */
    private int samples = 1000;
    /** Upper Bound for X-Axis. */
    private double xMax;
    /** Upper Bound for y-Axis. */
    private double yMax;
    /** Upper Bound for z-Axis. */
    private double zMax;
    /** Lower Bound for X-Axis. */
    private double xMin;
    /** Lower Bound for y-Axis. */
    private double yMin;
    /** Lower Bound for z-Axis. */
    private double zMin;
    /** The progressive surface model. */
    private final ProgressiveSurfaceModel surfaceModel = new ProgressiveSurfaceModel();
    /** The mapper. */
    private final ProbabilisticMapper mapper = new ProbabilisticMapper();
    /** The actions. */
    private ChangeSelectedAttributesAction<IMultivariateDistribution> changeAttributesAction;
    /** The settings action. */
    private ChangeSettingsAction changeSettingsAction;
    /** The chart. */
    private Composite chartComposite;

    private String attributeList;

    public final void setAttributeList(final String list) {
        this.attributeList = list;
    }

    public final String getAttributeList() {
        return this.attributeList;
    }

    // protected static ImageDescriptor IMG_MONITOR_EDIT =
    // ImageDescriptor.createFromURL(Activator.getBundleContext().getBundle().getEntry("icons/monitor_edit.png"));
    // protected static ImageDescriptor IMG_COG =
    // ImageDescriptor.createFromURL(Activator.getBundleContext().getBundle().getEntry("icons/cog.png"));
    /*
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
        this.chartComposite = new Composite(parent, SWT.EMBEDDED);
        this.chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        final java.awt.Frame frame = SWT_AWT.new_Frame(this.chartComposite);
        frame.add(this.createChart());

        this.createActions();
        this.contributeToActionBars();

    }

    /*
     *
     * @see
     * de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener
     * #streamElementRecieved(java.lang.Object, int)
     */
    @Override
    public final void streamElementRecieved(final IPhysicalOperator senderOperator, final IStreamObject<?> element, final int port) {
        final ProbabilisticTuple<?> probabilisticElement = (ProbabilisticTuple<?>) element;
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < ProbabilityChart3DDashboardPart.this.attributes.length; i++) {
                    final Object value = probabilisticElement.getAttribute(ProbabilityChart3DDashboardPart.this.positions[i]);
                    if (ProbabilityChart3DDashboardPart.this.isContinuous(i)) {
                        final ProbabilisticDouble continuousAttribute = (ProbabilisticDouble) value;
                        final MultivariateMixtureDistribution distribution = probabilisticElement.getDistribution(continuousAttribute.getDistribution());
                        if (distribution.getDimension() > 1) {
                            ProbabilityChart3DDashboardPart.this.getMapper().setup(distribution);
                        }
                    }
                }

                if (!ProbabilityChart3DDashboardPart.this.chartComposite.isDisposed()) {
                    // chartComposite.redraw();
                    ProbabilityChart3DDashboardPart.this.getSurfaceModel().plot().execute();
                }

            }

        });

    }

    /*
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
            ProbabilityChart3DDashboardPart.LOG.error("Probability Chart DashboardPart only supports one query!");
            throw new Exception("Probability Chart DashboardPart only supports one query!");
        }
        this.operator = physicalRoots.iterator().next();
        this.positions = ProbabilityChart3DDashboardPart.determinePositions(this.operator.getOutputSchema(), this.attributes);
        this.continuousAttributes = new Boolean[this.positions.length];
        Arrays.fill(this.continuousAttributes, false);
        for (int i = 0; i < this.positions.length; i++) {
            final SDFAttribute attribute = this.operator.getOutputSchema().get(this.positions[i]);
            if (attribute.getDatatype().getClass().equals(SDFProbabilisticDatatype.class)) {
                final SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute.getDatatype();
                this.continuousAttributes[i] = true;
            }
        }
    }

    /**
     * Checks whether the attribute at the given position is a continuous
     * probabilistic attribute.
     *
     * @param pos
     *            The position
     * @return <code>true</code> if the attribute at the given position is a
     *         continuous probabilistic attribute
     */
    private boolean isContinuous(final int pos) {
        if ((pos < 0) && (pos >= this.continuousAttributes.length)) {
            return false;
        }
        return this.continuousAttributes[pos];
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
    }

    /**
     * Gets the value of the zMin property.
     *
     * @return The zMin value
     */
    public final double getZMin() {
        return this.zMin;
    }

    /**
     * Sets the value of the zMin property.
     *
     * @param z
     *            The zMin value
     */
    public final void setZMin(final double z) {
        this.zMin = z;
    }

    /**
     * Gets the value of the zMax property.
     *
     * @return The zMax value
     */
    public final double getZMax() {
        return this.zMax;
    }

    /**
     * Sets the value of the zMax property.
     *
     * @param z
     *            The zMax value
     */
    public final void setZMax(final double z) {
        this.zMax = z;
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
     * Creates the surface panel.
     *
     * @return The chart
     */
    private JSurfacePanel createChart() {
        final JSurfacePanel chart = new JSurfacePanel();
        this.initSurfaceModel();
        chart.setModel(this.getSurfaceModel());
        chart.setConfigurationVisible(false);
        chart.setDoubleBuffered(true);
        chart.setTitleText("Probabilistic Chart (3D)");

        return chart;
    }

    /**
     * Initialize the surface model.
     */
    private void initSurfaceModel() {
        this.surfaceModel.setMapper(this.getMapper());
        this.surfaceModel.setDisplayXY(true);
        this.surfaceModel.setDisplayZ(true);
        this.surfaceModel.setDisplayGrids(true);
        this.surfaceModel.setBoxed(true);
        this.surfaceModel.setCalcDivisions(this.getSamples());
        this.surfaceModel.setDispDivisions(this.getSamples());
        // sm.setCalcDivisions(1000);
        // sm.setContourLines(1000);
        // sm.setDispDivisions(1000);

        this.surfaceModel.setXMax((float) this.getXMax());
        this.surfaceModel.setXMin((float) this.getXMin());
        this.surfaceModel.setYMax((float) this.getYMax());
        this.surfaceModel.setYMin((float) this.getYMin());
        this.surfaceModel.setZMax((float) this.getZMax());
        this.surfaceModel.setZMin((float) this.getZMin());
        this.surfaceModel.setAutoScaleZ(true);
    }

    /**
     * Gets the value of the surfaceModel property.
     *
     * @return the surface model
     */
    private ProgressiveSurfaceModel getSurfaceModel() {
        return this.surfaceModel;
    }

    /**
     * Gets the value of the mapper property.
     *
     * @return The mapper
     */
    private ProbabilisticMapper getMapper() {
        return this.mapper;
    }

    /**
     * Create the action bars for the view.
     */
    private void contributeToActionBars() {
        // IActionBars bars = getViewSite().getActionBars();
        // fillLocalMenu(bars.getMenuManager());
        // fillLocalMenu(bars.getToolBarManager());
    }

    /**
     * Register actions.
     *
     * @param manager
     *            The contribution manager
     */
    private void fillLocalMenu(final IContributionManager manager) {
        manager.add(this.changeAttributesAction);
        manager.add(this.changeSettingsAction);
    }

    /**
     * Create the actions for the view.
     */
    private void createActions() {
        // this.changeAttributesAction = new
        // ChangeSelectedAttributesAction<NormalDistributionMixture>(this.getSite().getShell(),
        // this);
        // this.changeAttributesAction.setText("Change Attributes");
        // this.changeAttributesAction.setToolTipText("Configure the attributes that will be shown by the chart");
        // this.changeAttributesAction.setImageDescriptor(IMG_MONITOR_EDIT);
        //
        // this.changeSettingsAction = new
        // ChangeSettingsAction(this.getSite().getShell(), this);
        // this.changeSettingsAction.setText("Change Settings");
        // this.changeSettingsAction.setToolTipText("Change several settings for this chart");
        // this.changeSettingsAction.setImageDescriptor(IMG_COG);

    }
}
