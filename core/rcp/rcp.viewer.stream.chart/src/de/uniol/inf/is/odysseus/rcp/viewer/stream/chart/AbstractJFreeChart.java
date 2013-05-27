/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSelectedAttributesAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSettingsAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.IChartSettingChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.MethodSetting;

public abstract class AbstractJFreeChart<T, M extends IMetaAttribute> extends AbstractChart<T, M> implements IChartSettingChangeable, IDashboardPart {

	Logger logger = LoggerFactory.getLogger(AbstractJFreeChart.class);

	public AbstractJFreeChart() {
		// we need this!
	}

	// protected static ImageDescriptor IMG_MONITOR_EDIT = ImageDescriptor
	// .createFromURL(Activator.getBundleContext().getBundle()
	// .getEntry("icons/monitor_edit.png"));
	// protected static ImageDescriptor IMG_COG = ImageDescriptor
	// .createFromURL(Activator.getBundleContext().getBundle()
	// .getEntry("icons/cog.png"));

	protected final static Color DEFAULT_BACKGROUND = Color.WHITE;
	protected final static Color DEFAULT_BACKGROUND_GRID = Color.GRAY;
	protected final static String VIEW_ID_PREFIX = "de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts";
	private static final String QUERY_FILE_NAME = "QueryFileName";

	private JFreeChart chart;
	private ChangeSelectedAttributesAction<T> changeAttributesAction;
	private ChangeSettingsAction changeSettingsAction;
	private String queryFileName = null;
	
	private List<IDashboardPartListener> listener = new ArrayList<>();

	private static int currentUniqueSecondIdentifer = 0;

	public static synchronized String getUniqueSecondIdentifier(String prefix) {
		currentUniqueSecondIdentifer++;
		return prefix + "_" + currentUniqueSecondIdentifer;
	}

	@Override
	public void createPartControl(Composite parent) {
		initComposite(parent);
		contributeToActionBars();
	}

	private void initComposite(Composite parent) {
		this.chart = createChart();
		// if(this.chart.getPlot()!=null){
		// this.chart.getPlot().setBackgroundPaint(DEFAULT_BACKGROUND);
		// }
		decorateChart(this.chart);

		ChartComposite chartComposite = new ChartComposite(parent, SWT.NONE, this.chart, true);
		chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createActions(parent.getShell());
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		if (memento != null) {
			this.queryFileName = memento.getString(QUERY_FILE_NAME);
			if (this.queryFileName != null && !this.queryFileName.isEmpty()) {
				List<ISource<?>> sources = ScriptExecutor.loadAndExecuteQueryScript(this.queryFileName);
				this.initWithOperator(sources.get(0));
			}
		}
	}

	@Override
	public void saveState(IMemento memento) {
		memento.putString(QUERY_FILE_NAME, queryFileName);
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

	private void createActions(Shell shell) {
		this.changeAttributesAction = new ChangeSelectedAttributesAction<T>(shell, this);
		this.changeAttributesAction.setText("Change Attributes");
		this.changeAttributesAction.setToolTipText("Configure the attributes that will be shown by the chart");
		ImageDescriptor imgDescMonitor = ImageDescriptor.createFromURL(Activator.getBundleContext().getBundle().getEntry("icons/monitor_edit.png"));
		this.changeAttributesAction.setImageDescriptor(imgDescMonitor);

		this.changeSettingsAction = new ChangeSettingsAction(shell, this);
		this.changeSettingsAction.setText("Change Settings");
		this.changeSettingsAction.setToolTipText("Change several settings for this chart");
		ImageDescriptor imgDescCog = ImageDescriptor.createFromURL(Activator.getBundleContext().getBundle().getEntry("icons/cog.png"));
		this.changeSettingsAction.setImageDescriptor(imgDescCog);

	}

	protected abstract void decorateChart(JFreeChart thechart);

	protected abstract JFreeChart createChart();

	@Override
	public final void chartSettingsChanged() {
		fireDashboardChangedEvent();
		reloadChart();	
	}

	private void fireDashboardChangedEvent() {
		for(IDashboardPartListener l : this.listener){
			l.dashboardPartChanged();
		}		
	}	

	@Override
	public void setFocus() {

	}

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

	protected int getSelectedValueCount(boolean[] values) {
		int count = 0;
		for (boolean b : values) {
			if (b) {
				count++;
			}
		}
		return count;
	}

	@Override
	public List<MethodSetting> getChartSettings() {
		List<MethodSetting> settings = new ArrayList<MethodSetting>();
		settings.addAll(getAnnotatedSettings(this.getClass()));
		// settings.addAll(getAnnotatedSettings())
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
						MethodSetting ms = new MethodSetting(us.name(), m, other);
						ms.setListGetter(getListMethod(us));
						settings.add(ms);
					} else {
						System.out.println("WARN: setting can not be loaded because there is no getter/setter pair");
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

	public void setFileName(String queryFile) {
		this.queryFileName = queryFile;
	}

	/**
	 * DASHBOARD IMPLEMENTATIONS
	 */

	private Configuration configuration;
	private IDashboardPartQueryTextProvider queryTextProvider;
	private boolean opened = false;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		initComposite(parent);
		addToToolbar(toolbar, changeAttributesAction);
		addToToolbar(toolbar, changeSettingsAction);
	}

	private void addToToolbar(ToolBar tb, final Action action) {
		final ToolItem toolItem = new ToolItem(tb, SWT.PUSH);
		toolItem.setImage(action.getImageDescriptor().createImage());
		toolItem.setToolTipText("");
		toolItem.setWidth(100);
		toolItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				action.run();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				action.run();

			}
		});
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public IDashboardPartQueryTextProvider getQueryTextProvider() {
		return queryTextProvider;
	}

	@Override
	public boolean init(Configuration configuration) {
		this.configuration = configuration;
		this.configuration.addListener(this);

		return true;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (this.configuration != null) {
			this.configuration.removeListener(this);
		}
	}

	@Override
	public void onPause() {
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> toSave = new HashMap<>();
		if (opened) {
			for (MethodSetting ms : getChartSettings()) {
				Object value = null;
				try {
					value = ms.getGetter().invoke(this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (value != null) {
					toSave.put(ms.getName(), value.toString());
				}
			}
		}
		return toSave;
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		for (Entry<String, String> values : saved.entrySet()) {
			String methodName = values.getKey();
			String value = values.getValue();
			try {
				for (MethodSetting ms : getChartSettings()) {
					if (ms.getName().equals(methodName)) {
						Class<?> paramType = ms.getSetter().getParameterTypes()[0];
						if (paramType.equals(Double.class) || paramType.equals(double.class)) {
							Double d = Double.parseDouble(value);
							ms.getSetter().invoke(this, d);
						} else if (paramType.equals(Integer.class) || paramType.equals(int.class)) {
							Integer d = Integer.parseInt(value);
							ms.getSetter().invoke(this, d);
						} else if (paramType.equals(Boolean.class) || paramType.equals(boolean.class)) {
							Boolean d = Boolean.getBoolean(value);
							ms.getSetter().invoke(this, d);
						} else {
							ms.getSetter().invoke(this, value);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
		initWithOperator(physicalRoots.get(0));
		opened = true;
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onUnpause() {
	}

	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		this.queryTextProvider = Preconditions.checkNotNull(provider, "QueryTextProvider for DashboardPart must not be null!");
	}


	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {

	}
	
	@Override
	public void addListener(IDashboardPartListener listener) {
		this.listener.add(listener);
	}
	
	@Override
	public void removeListener(IDashboardPartListener listener) {
		this.listener.remove(listener);
	}

}
