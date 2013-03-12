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
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSelectedAttributesAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSettingsAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.IChartSettingChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.MethodSetting;

public class ProbabilityChart3D extends AbstractProbabilityChart<NormalDistributionMixture, IMetaAttribute> implements IChartSettingChangeable {
	
	public static final String VIEW_ID = "de.offis.chart.charts.probabilitychart3d";
	private JSurfacePanel chart = new JSurfacePanel();
	private ProgressiveSurfaceModel sm = new ProgressiveSurfaceModel();
	private ProbabilityMapper m = new ProbabilityMapper();
		
	private ChangeSelectedAttributesAction<NormalDistributionMixture> changeAttributesAction;
	private ChangeSettingsAction changeSettingsAction;

	protected static ImageDescriptor IMG_MONITOR_EDIT = ImageDescriptor
			.createFromURL(Activator.getBundleContext().getBundle()
					.getEntry("icons/monitor_edit.png"));
	protected static ImageDescriptor IMG_COG = ImageDescriptor
			.createFromURL(Activator.getBundleContext().getBundle()
					.getEntry("icons/cog.png"));
	
	public ProbabilityChart3D() {}
	
	private void updateChart(NormalDistributionMixture mix){
		if(mix.getDimension() < 2)
			return; // keine 3D Normaldistribution
		
		m.setup(mix);
		
		sm.plot().execute();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite swtFrame = new Composite(parent, SWT.EMBEDDED);
		java.awt.Frame frame = SWT_AWT.new_Frame( swtFrame );		
		frame.add(chart);		

		initChart();
		
		createActions();
		contributeToActionBars();		
	}
	


	@Override
	protected void processElement(final List<NormalDistributionMixture> tuple,
			IMetaAttribute metadata, int port) {

		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					if(tuple.size() > 0){
						updateChart(tuple.get(0));
					}													
				} catch (Exception e) {
					System.out.println(e.getMessage());
					dispose();
					return;
				}
			}
		});
	}
	
	private void initChart(){
		sm.setMapper(m);
		sm.setDisplayXY(true);
		sm.setDisplayZ(true);
		sm.setDisplayGrids(true);
		sm.setBoxed(true);
		
//		sm.setCalcDivisions(1000);
//		sm.setContourLines(1000);
//		sm.setDispDivisions(1000);
		
		sm.setXMax(15f);
		sm.setXMin(-15f);
		sm.setYMax(15f);
		sm.setYMin(-15f);
		sm.setZMax(0.3f);
		sm.setZMin(0f);
		sm.setAutoScaleZ(false);

		chart.setModel(sm);
		chart.setConfigurationVisible(false);
		chart.setDoubleBuffered(true);
		chart.setTitleText("");
		
	}
	
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalMenu(bars.getMenuManager());
		fillLocalMenu(bars.getToolBarManager());
	}

	private void fillLocalMenu(IContributionManager manager) {
		manager.add(changeAttributesAction);
		manager.add(changeSettingsAction);
	}

	private void createActions() {
		this.changeAttributesAction = new ChangeSelectedAttributesAction<NormalDistributionMixture>(
				this.getSite().getShell(), this);
		this.changeAttributesAction.setText("Change Attributes");
		this.changeAttributesAction
				.setToolTipText("Configure the attributes that will be shown by the chart");
		this.changeAttributesAction.setImageDescriptor(IMG_MONITOR_EDIT);

		this.changeSettingsAction = new ChangeSettingsAction(this.getSite()
				.getShell(), this);
		this.changeSettingsAction.setText("Change Settings");
		this.changeSettingsAction
				.setToolTipText("Change several settings for this chart");
		this.changeSettingsAction.setImageDescriptor(IMG_COG);

	}

	@Override
	public List<MethodSetting> getChartSettings() {
		List<MethodSetting> settings = new ArrayList<MethodSetting>();
		settings.addAll(getAnnotatedSettings(this.getClass()));
		return settings;
	}

	public List<MethodSetting> getAnnotatedSettings(Class<?> theclass) {
		List<MethodSetting> settings = new ArrayList<MethodSetting>();
		for (Method m : theclass.getMethods()) {
			ChartSetting us = m.getAnnotation(ChartSetting.class);
			if (us != null) {
				if (us.type().equals(Type.GET)) {
					Method other = getAccordingType(us);
					if (other != null) {
						MethodSetting ms = new MethodSetting(us.name(), m,
								other);
						ms.setListGetter(getListMethod(us));
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

	private Method getListMethod(ChartSetting otherMethod) {
		return findMethod(otherMethod.name(), Type.OPTIONS);
	}

	private Method findMethod(String name, Type searchForTyp) {
		for (Method m : this.getClass().getMethods()) {
			ChartSetting us = m.getAnnotation(ChartSetting.class);
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

	private Method getAccordingType(ChartSetting otherMethod) {
		if (otherMethod.type().equals(Type.SET)) {
			return findMethod(otherMethod.name(), Type.GET);
		}
		return findMethod(otherMethod.name(), Type.SET);
	}
	
//	@ChartSetting(name = "Time Input Granularity", type = Type.OPTIONS)
//	public List<String> getTimeInputGranularityValues() {
//		List<String> values = new ArrayList<String>();
//		values.add(TIME_MILLI);
//		values.add(TIME_MICRO);
//		values.add(TIME_NANO);
//		values.add(TIME_PICO);
//		return values;
//	}
//
//	@ChartSetting(name = "Time Input Granularity", type = Type.GET)
//	public String getTimeInputGranularity() {
//		return timeinputgranularity;
//	}
//
//	@ChartSetting(name = "Time Input Granularity", type = Type.SET)
//	public void setTimeInputGranularity(String value) {
//		this.timeinputgranularity = value;
//	}
	
	@ChartSetting(name = "X-Max", type = Type.GET)
	public String getXMaxValue() {
		return sm.getXMax()+"";
	}

	@ChartSetting(name = "X-Max", type = Type.SET)
	public void setXMaxValue(String value) {
		sm.setXMax(Float.parseFloat(value));
	}
	
	@ChartSetting(name = "X-Min", type = Type.GET)
	public String getXMinValue() {
		return sm.getXMin()+"";
	}

	@ChartSetting(name = "X-Min", type = Type.SET)
	public void setXMinValue(String value) {
		sm.setXMin(Float.parseFloat(value));
	}
	
	@ChartSetting(name = "Y-Max", type = Type.GET)
	public String getYMaxValue() {
		return sm.getYMax()+"";
	}

	@ChartSetting(name = "Y-Max", type = Type.SET)
	public void setYMaxValue(String value) {
		sm.setYMax(Float.parseFloat(value));
	}
	
	@ChartSetting(name = "Y-Min", type = Type.GET)
	public String getYMinValue() {
		return sm.getYMin()+"";
	}

	@ChartSetting(name = "Y-Min", type = Type.SET)
	public void setYMinValue(String value) {
		sm.setYMin(Float.parseFloat(value));
	}
	
	@ChartSetting(name = "Z-Max", type = Type.GET)
	public String getZMaxValue() {
		return sm.getZMax()+"";
	}

	@ChartSetting(name = "Z-Max", type = Type.SET)
	public void setZMaxValue(String value) {
		sm.setZMax(Float.parseFloat(value));
	}
	
	@ChartSetting(name = "Z-Min", type = Type.GET)
	public String getZMinValue() {
		return sm.getZMin()+"";
	}

	@ChartSetting(name = "Z-Min", type = Type.SET)
	public void setZMinValue(String value) {
		sm.setZMin(Float.parseFloat(value));
	}
	
	@Override
	public void chartSettingsChanged() {
		sm.plot().execute();
	}
	
	@Override
	public void setFocus() {

	}
	
	@Override
	public String getViewID() {
		return VIEW_ID;
	}
	
	@Override
	public String isValidSelection(
			Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}
}
