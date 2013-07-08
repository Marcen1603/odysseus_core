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

public class ProbabilityChart3D extends
		AbstractProbabilityChart<NormalDistributionMixture, IMetaAttribute>
		implements IChartSettingChangeable {

	public static final String VIEW_ID = "de.offis.chart.charts.probabilitychart3d";
	private final JSurfacePanel chart = new JSurfacePanel();
	private final ProgressiveSurfaceModel sm = new ProgressiveSurfaceModel();
	private final ProbabilityMapper m = new ProbabilityMapper();

	private ChangeSelectedAttributesAction<NormalDistributionMixture> changeAttributesAction;
	private ChangeSettingsAction changeSettingsAction;

	protected static ImageDescriptor IMG_MONITOR_EDIT = ImageDescriptor
			.createFromURL(Activator.getBundleContext().getBundle()
					.getEntry("icons/monitor_edit.png"));
	protected static ImageDescriptor IMG_COG = ImageDescriptor
			.createFromURL(Activator.getBundleContext().getBundle()
					.getEntry("icons/cog.png"));

	public ProbabilityChart3D() {
	}

	private void updateChart(final NormalDistributionMixture mix) {
		if (mix.getDimension() < 2) {
			return; // keine 3D Normaldistribution
		}

		this.m.setup(mix);

		this.sm.plot().execute();
	}

	@Override
	public void createPartControl(final Composite parent) {
		final Composite swtFrame = new Composite(parent, SWT.EMBEDDED);
		final java.awt.Frame frame = SWT_AWT.new_Frame(swtFrame);
		frame.add(this.chart);

		this.initChart();

		this.createActions();
		this.contributeToActionBars();
	}

	@Override
	protected void processElement(final List<NormalDistributionMixture> tuple,
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
						final SDFProbabilisticDatatype probType = (SDFProbabilisticDatatype) type;
						if (probType.isContinuous()) {
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

	private void initChart() {
		this.sm.setMapper(this.m);
		this.sm.setDisplayXY(true);
		this.sm.setDisplayZ(true);
		this.sm.setDisplayGrids(true);
		this.sm.setBoxed(true);

		// sm.setCalcDivisions(1000);
		// sm.setContourLines(1000);
		// sm.setDispDivisions(1000);

		this.sm.setXMax(15f);
		this.sm.setXMin(-15f);
		this.sm.setYMax(15f);
		this.sm.setYMin(-15f);
		this.sm.setZMax(0.3f);
		this.sm.setZMin(0f);
		this.sm.setAutoScaleZ(false);

		this.chart.setModel(this.sm);
		this.chart.setConfigurationVisible(false);
		this.chart.setDoubleBuffered(true);
		this.chart.setTitleText("");

	}

	private void contributeToActionBars() {
		final IActionBars bars = this.getViewSite().getActionBars();
		this.fillLocalMenu(bars.getMenuManager());
		this.fillLocalMenu(bars.getToolBarManager());
	}

	private void fillLocalMenu(final IContributionManager manager) {
		manager.add(this.changeAttributesAction);
		manager.add(this.changeSettingsAction);
	}

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

	@Override
	public List<MethodSetting> getChartSettings() {
		final List<MethodSetting> settings = new ArrayList<MethodSetting>();
		settings.addAll(this.getAnnotatedSettings(this.getClass()));
		return settings;
	}

	public List<MethodSetting> getAnnotatedSettings(final Class<?> theclass) {
		final List<MethodSetting> settings = new ArrayList<MethodSetting>();
		for (final Method m : theclass.getMethods()) {
			final ChartSetting us = m.getAnnotation(ChartSetting.class);
			if (us != null) {
				if (us.type().equals(Type.GET)) {
					final Method other = this.getAccordingType(us);
					if (other != null) {
						final MethodSetting ms = new MethodSetting(us.name(),
								m, other);
						ms.setListGetter(this.getListMethod(us));
						settings.add(ms);
					} else {
						System.out
								.println("WARN: setting can not be loaded because there is no getter/setter pair");
						continue;
					}
				}
			}
		}
		return settings;
	}

	private Method getListMethod(final ChartSetting otherMethod) {
		return this.findMethod(otherMethod.name(), Type.OPTIONS);
	}

	private Method findMethod(final String name, final Type searchForTyp) {
		for (final Method m : this.getClass().getMethods()) {
			final ChartSetting us = m.getAnnotation(ChartSetting.class);
			if (us != null) {
				if (us.name().equals(name)) {
					if (us.type().equals(searchForTyp)) {
						return m;
					}
				}
			}
		}
		return null;
	}

	private Method getAccordingType(final ChartSetting otherMethod) {
		if (otherMethod.type().equals(Type.SET)) {
			return this.findMethod(otherMethod.name(), Type.GET);
		}
		return this.findMethod(otherMethod.name(), Type.SET);
	}

	// @ChartSetting(name = "Time Input Granularity", type = Type.OPTIONS)
	// public List<String> getTimeInputGranularityValues() {
	// List<String> values = new ArrayList<String>();
	// values.add(TIME_MILLI);
	// values.add(TIME_MICRO);
	// values.add(TIME_NANO);
	// values.add(TIME_PICO);
	// return values;
	// }
	//
	// @ChartSetting(name = "Time Input Granularity", type = Type.GET)
	// public String getTimeInputGranularity() {
	// return timeinputgranularity;
	// }
	//
	// @ChartSetting(name = "Time Input Granularity", type = Type.SET)
	// public void setTimeInputGranularity(String value) {
	// this.timeinputgranularity = value;
	// }

	@ChartSetting(name = "X-Max", type = Type.GET)
	public String getXMaxValue() {
		return this.sm.getXMax() + "";
	}

	@ChartSetting(name = "X-Max", type = Type.SET)
	public void setXMaxValue(final String value) {
		this.sm.setXMax(Float.parseFloat(value));
	}

	@ChartSetting(name = "X-Min", type = Type.GET)
	public String getXMinValue() {
		return this.sm.getXMin() + "";
	}

	@ChartSetting(name = "X-Min", type = Type.SET)
	public void setXMinValue(final String value) {
		this.sm.setXMin(Float.parseFloat(value));
	}

	@ChartSetting(name = "Y-Max", type = Type.GET)
	public String getYMaxValue() {
		return this.sm.getYMax() + "";
	}

	@ChartSetting(name = "Y-Max", type = Type.SET)
	public void setYMaxValue(final String value) {
		this.sm.setYMax(Float.parseFloat(value));
	}

	@ChartSetting(name = "Y-Min", type = Type.GET)
	public String getYMinValue() {
		return this.sm.getYMin() + "";
	}

	@ChartSetting(name = "Y-Min", type = Type.SET)
	public void setYMinValue(final String value) {
		this.sm.setYMin(Float.parseFloat(value));
	}

	@ChartSetting(name = "Z-Max", type = Type.GET)
	public String getZMaxValue() {
		return this.sm.getZMax() + "";
	}

	@ChartSetting(name = "Z-Max", type = Type.SET)
	public void setZMaxValue(final String value) {
		this.sm.setZMax(Float.parseFloat(value));
	}

	@ChartSetting(name = "Z-Min", type = Type.GET)
	public String getZMinValue() {
		return this.sm.getZMin() + "";
	}

	@ChartSetting(name = "Z-Min", type = Type.SET)
	public void setZMinValue(final String value) {
		this.sm.setZMin(Float.parseFloat(value));
	}

	@Override
	public void chartSettingsChanged() {
		this.sm.plot().execute();
	}

	@Override
	public void setFocus() {

	}

	@Override
	public String getViewID() {
		return ProbabilityChart3D.VIEW_ID;
	}

	@Override
	public String isValidSelection(
			final Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return this.checkAtLeastOneSelectedAttribute(selectAttributes);
	}

	@Override
	protected void reloadChart() {

	}
}
