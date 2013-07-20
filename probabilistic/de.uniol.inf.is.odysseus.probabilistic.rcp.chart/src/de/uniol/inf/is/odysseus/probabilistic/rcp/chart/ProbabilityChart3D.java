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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ericaro.surfaceplotter.JSurfacePanel;
import net.ericaro.surfaceplotter.ProgressiveSurfaceModel;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSelectedAttributesAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSettingsAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewableSDFAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.IChartSettingChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.MethodSetting;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilityChart3D extends
		AbstractProbabilityChart<NormalDistributionMixture, IMetaAttribute>
		implements IChartSettingChangeable {
	/** Logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(ProbabilityChart3D.class);
	/** The view ID. */
	public static final String VIEW_ID = "de.offis.chart.charts.probabilitychart3d";
	/** The surface panel. */
	private final JSurfacePanel chart = new JSurfacePanel();
	/** The progressive surface model. */
	private final ProgressiveSurfaceModel surfaceModel = new ProgressiveSurfaceModel();
	/** The continuous mapper. */
	private final ProbabilisticMapper mapper = new ProbabilisticMapper();
	/** The actions. */
	private ChangeSelectedAttributesAction<NormalDistributionMixture> changeAttributesAction;
	/** The settings action. */
	private ChangeSettingsAction changeSettingsAction;

	/** The monitor edit icon. */
	protected static final ImageDescriptor IMG_MONITOR_EDIT = ImageDescriptor
			.createFromURL(Activator.getBundleContext().getBundle()
					.getEntry("icons/monitor_edit.png"));
	/** The configuration icon. */
	protected static final ImageDescriptor IMG_COG = ImageDescriptor
			.createFromURL(Activator.getBundleContext().getBundle()
					.getEntry("icons/cog.png"));

	/**
 * 
 */
	public ProbabilityChart3D() {
	}

	/**
	 * Update the chart with the given normal distribution mixture.
	 * 
	 * @param mixture
	 *            The normal distribution mixture
	 */
	private void updateChart(final NormalDistributionMixture mixture) {
		if (mixture.getDimension() < 2) {
			return; // No multivariate distribution mixture
		}

		this.mapper.setup(mixture);
		this.surfaceModel.plot().execute();
	}

	/*
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public final void createPartControl(final Composite parent) {
		final Composite swtFrame = new Composite(parent, SWT.EMBEDDED);
		final java.awt.Frame frame = SWT_AWT.new_Frame(swtFrame);
		frame.add(this.chart);

		this.initChart();

		this.createActions();
		this.contributeToActionBars();
	}

	/*
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart#processElement
	 * (java.util.List, de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute,
	 * int)
	 */
	@Override
	protected final void processElement(
			final List<NormalDistributionMixture> tuple,
			final IMetaAttribute metadata, final int port) {

		this.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					final ViewableSDFAttribute attribute = (ViewableSDFAttribute) ProbabilityChart3D.this
							.getChoosenAttributes(port).get(0);
					final String name = attribute.getName();
					final SDFDatatype type = attribute.getSDFDatatype();

					if (tuple.size() == 0) {
						// no data
						return;
					}
					ProbabilityChart3D.this.chart.setTitleText(name);
					if (type.getClass().equals(SDFProbabilisticDatatype.class)) {
						final SDFProbabilisticDatatype probabilisticType = (SDFProbabilisticDatatype) type;
						if (probabilisticType.isContinuous()) {
							ProbabilityChart3D.this.updateChart(tuple.get(0));
						}
					}
				} catch (final Exception e) {
					e.printStackTrace();
					ProbabilityChart3D.this.dispose();
					return;
				}
			}
		});
	}

	/**
 * 
 */
	private void initChart() {
		this.surfaceModel.setMapper(this.mapper);
		this.surfaceModel.setDisplayXY(true);
		this.surfaceModel.setDisplayZ(true);
		this.surfaceModel.setDisplayGrids(true);
		this.surfaceModel.setBoxed(true);

		// sm.setCalcDivisions(1000);
		// sm.setContourLines(1000);
		// sm.setDispDivisions(1000);

		this.surfaceModel.setXMax(15f);
		this.surfaceModel.setXMin(-15f);
		this.surfaceModel.setYMax(15f);
		this.surfaceModel.setYMin(-15f);
		this.surfaceModel.setZMax(1f);
		this.surfaceModel.setZMin(0f);
		this.surfaceModel.setAutoScaleZ(false);

		this.chart.setModel(this.surfaceModel);
		this.chart.setConfigurationVisible(false);
		this.chart.setDoubleBuffered(true);
		this.chart.setTitleText("");

	}

	/**
 * 
 */
	private void contributeToActionBars() {
		final IActionBars bars = this.getViewSite().getActionBars();
		this.fillLocalMenu(bars.getMenuManager());
		this.fillLocalMenu(bars.getToolBarManager());
	}

	/**
	 * 
	 * @param manager
	 *            The contribution manager
	 */
	private void fillLocalMenu(final IContributionManager manager) {
		manager.add(this.changeAttributesAction);
		manager.add(this.changeSettingsAction);
	}

	/**
	 * Creates the view actions.
	 */
	private void createActions() {
		this.changeAttributesAction = new ChangeSelectedAttributesAction<NormalDistributionMixture>(
				this.getSite().getShell(), this);
		this.changeAttributesAction.setText("Change Attributes");
		this.changeAttributesAction
				.setToolTipText("Configure the attributes that will be shown by the chart");
		this.changeAttributesAction
				.setImageDescriptor(ProbabilityChart3D.IMG_MONITOR_EDIT);

		this.changeSettingsAction = new ChangeSettingsAction(this.getSite()
				.getShell(), this);
		this.changeSettingsAction.setText("Change Settings");
		this.changeSettingsAction
				.setToolTipText("Change several settings for this chart");
		this.changeSettingsAction
				.setImageDescriptor(ProbabilityChart3D.IMG_COG);

	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.
	 * IChartSettingChangeable#getChartSettings()
	 */
	@Override
	public final List<MethodSetting> getChartSettings() {
		final List<MethodSetting> settings = new ArrayList<MethodSetting>();
		settings.addAll(this.getAnnotatedSettings(this.getClass()));
		return settings;
	}

	/**
	 * 
	 * @param theclass
	 *            The class to analyse
	 * @return A list of method settings
	 */
	public final List<MethodSetting> getAnnotatedSettings(
			final Class<?> theclass) {
		final List<MethodSetting> settings = new ArrayList<MethodSetting>();
		for (final Method method : theclass.getMethods()) {
			final ChartSetting us = method.getAnnotation(ChartSetting.class);
			if (us != null) {
				if (us.type().equals(Type.GET)) {
					final Method other = this.getAccordingType(us);
					if (other != null) {
						final MethodSetting methodSetting = new MethodSetting(
								us.name(), method, other);
						methodSetting.setListGetter(this.getListMethod(us));
						settings.add(methodSetting);
					} else {
						ProbabilityChart3D.LOG
								.warn("Setting can not be loaded because there is no getter/setter pair");
						continue;
					}
				}
			}
		}
		return settings;
	}

	/**
	 * 
	 * @param otherMethod
	 *            The chart setting method
	 * @return The method
	 */
	private Method getListMethod(final ChartSetting otherMethod) {
		return this.findMethod(otherMethod.name(), Type.OPTIONS);
	}

	/**
	 * 
	 * @param name
	 *            The name of the method
	 * @param searchForTyp
	 *            The type to search for
	 * @return The method or <code>null</code> if the method can not be found
	 */
	private Method findMethod(final String name, final Type searchForTyp) {
		for (final Method method : this.getClass().getMethods()) {
			final ChartSetting chartSetting = method
					.getAnnotation(ChartSetting.class);
			if (chartSetting != null) {
				if (chartSetting.name().equals(name)) {
					if (chartSetting.type().equals(searchForTyp)) {
						return method;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param otherMethod
	 *            The method
	 * @return The method for the according type
	 */
	private Method getAccordingType(final ChartSetting otherMethod) {
		if (otherMethod.type().equals(Type.SET)) {
			return this.findMethod(otherMethod.name(), Type.GET);
		}
		return this.findMethod(otherMethod.name(), Type.SET);
	}

	/**
	 * Gets the value of the xMax property.
	 * 
	 * @return The xMax value
	 */
	@ChartSetting(name = "X-Max", type = Type.GET)
	public final double getXMaxValue() {
		return this.surfaceModel.getXMax();
	}

	/**
	 * Sets the value of the xMax property.
	 * 
	 * @param x
	 *            The xMax value
	 */
	@ChartSetting(name = "X-Max", type = Type.SET)
	public final void setXMaxValue(final double x) {
		this.surfaceModel.setXMax((float) x);
	}

	/**
	 * Gets the value of the xMin property.
	 * 
	 * @return The xMin value
	 */
	@ChartSetting(name = "X-Min", type = Type.GET)
	public final double getXMinValue() {
		return this.surfaceModel.getXMin();
	}

	/**
	 * Sets the value of the xMin property.
	 * 
	 * @param x
	 *            The xMin value
	 */
	@ChartSetting(name = "X-Min", type = Type.SET)
	public final void setXMinValue(final double x) {
		this.surfaceModel.setXMin((float) x);
	}

	/**
	 * Gets the value of the yMax property.
	 * 
	 * @return The yMax value
	 */
	@ChartSetting(name = "Y-Max", type = Type.GET)
	public final double getYMaxValue() {
		return this.surfaceModel.getYMax();
	}

	/**
	 * Sets the value of the yMax property.
	 * 
	 * @param y
	 *            The yMax value
	 */
	@ChartSetting(name = "Y-Max", type = Type.SET)
	public final void setYMaxValue(final double y) {
		this.surfaceModel.setYMax((float) y);
	}

	/**
	 * Gets the value of the yMin property.
	 * 
	 * @return The yMin value
	 */
	@ChartSetting(name = "Y-Min", type = Type.GET)
	public final double getYMinValue() {
		return this.surfaceModel.getYMin();
	}

	/**
	 * Sets the value of the yMin property.
	 * 
	 * @param y
	 *            The yMin value
	 */
	@ChartSetting(name = "Y-Min", type = Type.SET)
	public final void setYMinValue(final double y) {
		this.surfaceModel.setYMin((float) y);
	}

	/**
	 * Gets the value of the zMax property.
	 * 
	 * @return The zMax value
	 */
	@ChartSetting(name = "Z-Max", type = Type.GET)
	public final double getZMaxValue() {
		return this.surfaceModel.getZMax();
	}

	/**
	 * Sets the value of the zMax property.
	 * 
	 * @param z
	 *            The zMax value
	 */
	@ChartSetting(name = "Z-Max", type = Type.SET)
	public final void setZMaxValue(final double z) {
		this.surfaceModel.setZMax((float) z);
	}

	/**
	 * Gets the value of the zMin property.
	 * 
	 * @return The zMin value
	 */
	@ChartSetting(name = "Z-Min", type = Type.GET)
	public final double getZMinValue() {
		return this.surfaceModel.getZMin();
	}

	/**
	 * Sets the value of the zMin property.
	 * 
	 * @param z
	 *            The zMin value
	 */
	@ChartSetting(name = "Z-Min", type = Type.SET)
	public final void setZMinValue(final double z) {
		this.surfaceModel.setZMin((float) z);
	}

	/*
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.IAttributesChangeable
	 * #chartSettingsChanged()
	 */
	@Override
	public final void chartSettingsChanged() {
		this.surfaceModel.plot().execute();
	}

	/*
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

	/*
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart#getViewID
	 * ()
	 */
	@Override
	public final String getViewID() {
		return ProbabilityChart3D.VIEW_ID;
	}

	/*
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.IAttributesChangeable
	 * #isValidSelection(java.util.Map)
	 */
	@Override
	public final String isValidSelection(
			final Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return this.checkAtLeastOneSelectedAttribute(selectAttributes);
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
