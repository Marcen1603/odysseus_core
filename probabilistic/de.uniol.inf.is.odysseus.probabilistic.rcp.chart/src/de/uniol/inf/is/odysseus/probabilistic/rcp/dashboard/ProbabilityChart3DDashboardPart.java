package de.uniol.inf.is.odysseus.probabilistic.rcp.dashboard;

import java.util.List;

import net.ericaro.surfaceplotter.JSurfacePanel;
import net.ericaro.surfaceplotter.ProgressiveSurfaceModel;

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

import de.offis.chart.charts.ProbabilityMapper;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSelectedAttributesAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSettingsAction;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
@SuppressWarnings("unused")
public class ProbabilityChart3DDashboardPart extends AbstractDashboardPart {
	/** Logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(ProbabilityChart3DDashboardPart.class);
	/** First sink operator in the query. */
	private IPhysicalOperator operator;

	private String[] attributes;
	private Boolean[] continuousAttributes;
	private int[] positions;

	private int samples = 1000;
	private double minX = -10.0;
	private double maxX = 10.0;
	private double minY = -10.0;
	private double maxY = 10.0;
	private final ProgressiveSurfaceModel surfaceModel = new ProgressiveSurfaceModel();
	private final ProbabilityMapper mapper = new ProbabilityMapper();

	private ChangeSelectedAttributesAction<NormalDistributionMixture> changeAttributesAction;
	private ChangeSettingsAction changeSettingsAction;
	private Composite chartComposite;

	// protected static ImageDescriptor IMG_MONITOR_EDIT =
	// ImageDescriptor.createFromURL(Activator.getBundleContext().getBundle().getEntry("icons/monitor_edit.png"));
	// protected static ImageDescriptor IMG_COG =
	// ImageDescriptor.createFromURL(Activator.getBundleContext().getBundle().getEntry("icons/cog.png"));

	@Override
	public final void createPartControl(final Composite parent,
			final ToolBar toolbar) {
		final String attributeList = this.getConfiguration().get("Attributes");
		if (Strings.isNullOrEmpty(attributeList)) {
			new Label(parent, SWT.NONE).setText("Attribute List is invalid!");
			return;
		}

		this.attributes = attributeList.trim().split(",");
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

	@Override
	public final void streamElementRecieved(final IStreamObject<?> element,
			final int port) {
		final ProbabilisticTuple<?> probabilisticElement = (ProbabilisticTuple<?>) element;
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < ProbabilityChart3DDashboardPart.this.attributes.length; i++) {
					final Object value = probabilisticElement
							.getAttribute(ProbabilityChart3DDashboardPart.this.positions[i]);
					if (ProbabilityChart3DDashboardPart.this.isContinuous(i)) {
						final ProbabilisticContinuousDouble continuousAttribute = (ProbabilisticContinuousDouble) value;
						final NormalDistributionMixture distribution = probabilisticElement
								.getDistribution(continuousAttribute
										.getDistribution());
						if (distribution.getDimension() > 1) {
							ProbabilityChart3DDashboardPart.this.getMapper()
									.setup(distribution);
						}
					} else {
						// ProbabilityChart3DDashboardPart.this.updateSerie(currentserie,
						// (AbstractProbabilisticValue<?>) value);
					}
				}

				if (!ProbabilityChart3DDashboardPart.this.chartComposite
						.isDisposed()) {
					// chartComposite.redraw();
					ProbabilityChart3DDashboardPart.this.getSurfaceModel()
							.plot().execute();
				}

			}

		});

	}

	@Override
	public void punctuationElementRecieved(final IPunctuation point,
			final int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public void securityPunctuationElementRecieved(
			final ISecurityPunctuation sp, final int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public void settingChanged(final String settingName, final Object oldValue,
			final Object newValue) {
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
	public final void onStart(final List<IPhysicalOperator> physicalRoots)
			throws Exception {
		super.onStart(physicalRoots);
		if (physicalRoots.size() > 1) {
			ProbabilityChart3DDashboardPart.LOG
					.error("Probability Chart DashboardPart only supports one query!");
			throw new Exception(
					"Probability Chart DashboardPart only supports one query!");
		}
		this.operator = physicalRoots.get(0);
		this.positions = ProbabilityChart3DDashboardPart.determinePositions(
				this.operator.getOutputSchema(), this.attributes);
		this.continuousAttributes = new Boolean[this.positions.length];
		for (int i = 0; i < this.positions.length; i++) {
			final SDFAttribute attribute = this.operator.getOutputSchema().get(
					this.positions[i]);
			if (attribute.getDatatype().getClass()
					.equals(SDFProbabilisticDatatype.class)) {
				final SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute
						.getDatatype();
				if (datatype.isContinuous()) {
					this.continuousAttributes[i] = true;
				} else {
					this.continuousAttributes[i] = false;
				}
			} else {
				this.continuousAttributes[i] = false;
			}
		}
	}

	private boolean isContinuous(final int index) {
		if ((index < 0) && (index >= this.continuousAttributes.length)) {
			return false;
		}
		return this.continuousAttributes[index];
	}

	/**
	 * Sets the value of the samples property.
	 * 
	 * @param samples
	 *            The samples value
	 */
	public final void setSamples(final int samples) {
		this.samples = samples;
	}

	/**
	 * Gets the number of samples.
	 * 
	 * @return The number of samples
	 */
	public final int getSamples() {
		return this.samples;
	}

	/**
	 * Sets the value of the minX property.
	 * 
	 * @param minX
	 *            The minX value
	 */
	public final void setMinX(final double minX) {
		this.minX = minX;
	}

	/**
	 * Gets the minimal X value.
	 * 
	 * @return The minimal X value
	 */
	public final double getMinX() {
		return this.minX;
	}

	/**
	 * Sets the value of the maxX property.
	 * 
	 * @param maxX
	 *            The maxX value
	 */
	public final void setMaxX(final double maxX) {
		this.maxX = maxX;
	}

	/**
	 * Gets the maximal X value.
	 * 
	 * @return The maximal X value
	 */
	public final double getMaxX() {
		return this.maxX;
	}

	/**
	 * Sets the value of the minY property.
	 * 
	 * @param minY
	 *            The minY value
	 */
	public final void setMinY(final double minY) {
		this.minY = minY;
	}

	/**
	 * Gets the minimal Y value.
	 * 
	 * @return The minimal Y value
	 */
	public final double getMinY() {
		return this.minY;
	}

	/**
	 * Sets the value of the maxY property.
	 * 
	 * @param maxY
	 *            The maxY value
	 */
	public final void setMaxY(final double maxY) {
		this.maxY = maxY;
	}

	/**
	 * Gets the maximal Y value.
	 * 
	 * @return The maximal Y value
	 */
	public final double getMaxY() {
		return this.maxY;
	}

	/**
	 * 
	 * @param outputSchema
	 * @param attributes
	 * @return
	 */
	private static int[] determinePositions(final SDFSchema outputSchema,
			final String[] attributes) {
		final int[] positions = new int[attributes.length];

		for (int i = 0; i < attributes.length; i++) {
			for (int j = 0; j < outputSchema.size(); j++) {
				if (outputSchema.get(j).getAttributeName()
						.equalsIgnoreCase(attributes[i])) {
					positions[i] = j;
					break;
				}
			}
		}

		return positions;
	}

	private JSurfacePanel createChart() {
		final JSurfacePanel chart = new JSurfacePanel();
		this.initSurfaceModel();
		chart.setModel(this.getSurfaceModel());
		chart.setConfigurationVisible(false);
		chart.setDoubleBuffered(true);
		chart.setTitleText("Prob Chart 3D");

		return chart;
	}

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

		this.surfaceModel.setXMax((float) this.getMaxX());
		this.surfaceModel.setXMin((float) this.getMinX());
		this.surfaceModel.setYMax((float) this.getMaxY());
		this.surfaceModel.setYMin((float) this.getMinY());
		this.surfaceModel.setZMax(1.0f);
		this.surfaceModel.setZMin(0.0f);
		this.surfaceModel.setAutoScaleZ(true);
	}

	private ProgressiveSurfaceModel getSurfaceModel() {
		return this.surfaceModel;
	}

	private ProbabilityMapper getMapper() {
		return this.mapper;
	}

	private void contributeToActionBars() {
		// IActionBars bars = getViewSite().getActionBars();
		// fillLocalMenu(bars.getMenuManager());
		// fillLocalMenu(bars.getToolBarManager());
	}

	private void fillLocalMenu(final IContributionManager manager) {
		manager.add(this.changeAttributesAction);
		manager.add(this.changeSettingsAction);
	}

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
