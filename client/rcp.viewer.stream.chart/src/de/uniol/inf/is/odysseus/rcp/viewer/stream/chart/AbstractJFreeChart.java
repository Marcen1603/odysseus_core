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
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardActionBarContributor;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSelectedAttributesAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSettingsAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.SaveImageAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.ChartSetting.Type;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.IChartSettingChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.MethodSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewSchema;

public abstract class AbstractJFreeChart<T, M extends IMetaAttribute> extends
		AbstractChart<T, M> implements IChartSettingChangeable, IDashboardPart {

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

	private JFreeChart chart;
	private ChangeSelectedAttributesAction<T> changeAttributesAction;
	private ChangeSettingsAction changeSettingsAction;
    /** Save action.*/
    private SaveImageAction saveImageAction;
	private final Map<Integer, List<String>> loadedChoosenAttributes = Maps
			.newHashMap();
	private final Map<Integer, List<String>> loadedGroupedAttributes = Maps
			.newHashMap();

	private List<IDashboardPartListener> listener = new ArrayList<>();
	private IDashboardPartQueryTextProvider queryTextProvider;
	private boolean opened = false;
	
	private String sinkNames;
	private boolean sinkSynced;
	private Map<String, String> contextMap = Maps.newHashMap();

	private ChartComposite chartComposite;
	private IWorkbenchPart workbenchpart;
	private IFile dashboardPartFile;
	private IProject containingProject;
	private boolean isStarted;

	private static int currentUniqueSecondIdentifer = 0;

	private String chartTitle = "";

	public static synchronized String getUniqueSecondIdentifier(String prefix) {
		currentUniqueSecondIdentifer++;
		return prefix + "_" + currentUniqueSecondIdentifer;
	}

	@Override
	public void init(IFile dashboardFile, IProject containingProject,
			IWorkbenchPart containingPart) {
		this.dashboardPartFile = dashboardFile;
		this.containingProject = containingProject;
		this.workbenchpart = containingPart;
	}

	@Override
	public void setProject(IProject project) {
		this.containingProject = project;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		initComposite(parent);
		contributeToActionBars();
	}

	private void initComposite(Composite parent) {
		this.chart = createChart();
		this.chart.setTitle(chartTitle);
		LegendTitle l = this.chart.getLegend();
		for (String name : GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames()) {
			if (name.equalsIgnoreCase("Arial Narrow")) {
				Font f = new Font("Arial Narrow", Font.PLAIN, 10);
				l.setItemFont(f);
				break;
			}
		}
		
		decorateChart(this.chart);		
		ChartPanel panel = new ChartPanel(chart);
		panel.setPopupMenu(null);
		chartComposite = new StreamChartComposite(parent, SWT.NONE, this.chart);
		chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));		
		createActions(parent.getShell());
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalMenu(bars.getMenuManager());
		fillLocalMenu(bars.getToolBarManager());
	}

	private void fillLocalMenu(IContributionManager manager) {
	    manager.add(saveImageAction);
		manager.add(changeAttributesAction);
		manager.add(changeSettingsAction);
	}

	private void createActions(Shell shell) {
        this.saveImageAction = new SaveImageAction(shell, this);
        this.saveImageAction.setText("Save Image");
        this.saveImageAction.setToolTipText("Save the chart as PNG image");
        ImageDescriptor imgImage = ImageDescriptor
                .createFromURL(Activator.getBundleContext().getBundle()
                        .getEntry("icons/chart_curve.png"));
        this.saveImageAction.setImageDescriptor(imgImage);

		this.changeAttributesAction = new ChangeSelectedAttributesAction<T>(
				shell, this);
		this.changeAttributesAction.setText("Change Attributes");
		this.changeAttributesAction
				.setToolTipText("Configure the attributes that will be shown by the chart");
		ImageDescriptor imgDescMonitor = ImageDescriptor
				.createFromURL(Activator.getBundleContext().getBundle()
						.getEntry("icons/monitor_edit.png"));
		this.changeAttributesAction.setImageDescriptor(imgDescMonitor);

		this.changeSettingsAction = new ChangeSettingsAction(shell, this);
		this.changeSettingsAction.setText("Change Settings");
		this.changeSettingsAction
				.setToolTipText("Change several settings for this chart");
		ImageDescriptor imgDescCog = ImageDescriptor.createFromURL(Activator
				.getBundleContext().getBundle().getEntry("icons/cog.png"));
		this.changeSettingsAction.setImageDescriptor(imgDescCog);

	}

	protected abstract void decorateChart(JFreeChart thechart);

	protected abstract JFreeChart createChart();

	@Override
	public void chartSettingsChanged() {
		fireDashboardChangedEvent();
		reloadChartImpl();
	}

	private void fireDashboardChangedEvent() {
		for (IDashboardPartListener l : this.listener) {
			l.dashboardPartChanged(this);
		}
	}

	@Override
	public void setFocus() {

	}

	public JFreeChart getChart() {
		return chart;
	}

	public ChartComposite getChartComposite() {
		return chartComposite;
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
		Method[] methods = theclass.getMethods();
		for (Method m : methods) {
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

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		initComposite(parent);
		addToToolbar(toolbar, saveImageAction);
		addToToolbar(toolbar, changeAttributesAction);
		addToToolbar(toolbar, changeSettingsAction);
	}

	@Override
	public void setSinkNames(String sinkNames) {
		this.sinkNames = sinkNames;
	}

	@Override
	public String getSinkNames() {
		return sinkNames;
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
	public IDashboardPartQueryTextProvider getQueryTextProvider() {
		return queryTextProvider;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void onPause() {
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> toSave = new HashMap<>();

		if (opened) {
			Set<Integer> ports = getPorts();
			for (Integer port : ports) {
				List<IViewableAttribute> choosenAttributes = getChoosenAttributes(port);

				for (IViewableAttribute choosenAttribute : choosenAttributes) {
					String key = port + "#" + choosenAttribute.getName();
					toSave.put(key, "choosenAttribute");
				}
				for (IViewableAttribute groupedAttribute : getGroupByAttributes(port)) {
					String key = "GROUPED#" + port + "#"
							+ groupedAttribute.getName();
					toSave.put(key, "groupedAttribute");
				}
			}

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
	public void onSaveXML( Document document, Element element ) {
		
		
	}

	@Override
	public void saveImage(File path) throws IOException {
	    BufferedImage image = this.chart.createBufferedImage(this.chartComposite.getSize().x, this.chartComposite.getSize().y);
	    ImageIO.write(image, "png", path);
	}
	
	@Override
	public void onLoad(Map<String, String> saved) {
		loadedChoosenAttributes.clear();
		loadedGroupedAttributes.clear();

		for (Entry<String, String> values : saved.entrySet()) {

			String key = values.getKey();

			if (key.startsWith("GROUPED")) {
				String[] splittedKey = key.split("#");

				Integer port = Integer.valueOf(splittedKey[1]);
				String groupedAttribute = splittedKey[2];

				if (!loadedGroupedAttributes.containsKey(port)) {
					loadedGroupedAttributes.put(port,
							Lists.<String> newArrayList());
				}
				loadedGroupedAttributes.get(port).add(groupedAttribute);
			} else if (key.contains("#")) {
				String[] splittedKey = key.split("#");

				Integer port = Integer.valueOf(splittedKey[0]);
				String choosenAttribute = splittedKey[1];

				if (!loadedChoosenAttributes.containsKey(port)) {
					loadedChoosenAttributes.put(port,
							Lists.<String> newArrayList());
				}
				loadedChoosenAttributes.get(port).add(choosenAttribute);
			}

			String methodName = values.getKey();
			String value = values.getValue();
			try {
				for (MethodSetting ms : getChartSettings()) {
					if (ms.getName().equals(methodName)) {
						Class<?> paramType = ms.getSetter().getParameterTypes()[0];
						if (paramType.equals(Double.class)
								|| paramType.equals(double.class)) {
							Double d = Double.parseDouble(value);
							ms.getSetter().invoke(this, d);
						} else if (paramType.equals(Integer.class)
								|| paramType.equals(int.class)) {
							Integer d = Integer.parseInt(value);
							ms.getSetter().invoke(this, d);
						} else if (paramType.equals(Boolean.class)
								|| paramType.equals(boolean.class)) {
							Boolean d = Boolean.getBoolean(value);
							ms.getSetter().invoke(this, d);
						} else if (paramType.equals(Long.class)
								|| paramType.equals(long.class)) {
							Long d = Long.parseLong(value);
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
	public void onLoadXML( Document document, Element element ) {
	}
	
	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots)
			throws Exception {
		initWithOperator(physicalRoots.iterator().next());
		opened = true;
		isStarted = true;
	}

	@Override
	protected void initConnection(
			IStreamConnection<IStreamObject<?>> streamConnection) {

		for (ISubscription<? extends ISource<?>,?> s : streamConnection
				.getSubscriptions()) {
			int port = s.getSinkInPort();
			List<String> preChoosenAttributesOfPort = loadedChoosenAttributes
					.get(port);
			List<String> preGroupingAttributesOfPort = loadedGroupedAttributes
					.get(port);


			this.viewSchema
					.put(port,
							new ViewSchema<T>(
									s.getSchema(),
									s.getSource().getMetaAttributeSchema(),
									port,
									preChoosenAttributesOfPort != null ? preChoosenAttributesOfPort
											: Lists.<String> newArrayList(),
									preGroupingAttributesOfPort != null ? preGroupingAttributesOfPort
											: Lists.<String> newArrayList()));
		}

		if (validate()) {
			streamConnection.addStreamElementListener(this);
			streamConnection.connect();
			reloadChartImpl();
			init();
		}
	}

	@Override
	public void onStop() {
		isStarted = false;
	}

	public boolean isStarted() {
		return isStarted;
	}

	@Override
	public void onUnpause() {
	}

	@Override
	public void onLock() {

	}

	@Override
	public void onUnlock() {

	}
	
	public void open() {
		this.opened = true;
	}

	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		this.queryTextProvider = Objects.requireNonNull(provider,	"QueryTextProvider for DashboardPart must not be null!");
	}

	@Override
	public void addListener(IDashboardPartListener listener) {
		this.listener.add(listener);
	}

	@Override
	public void removeListener(IDashboardPartListener listener) {
		this.listener.remove(listener);
	}

	public final IWorkbenchPart getWorkbenchPart() {
		return this.workbenchpart;
	}

	public final IFile getDashboardPartFile() {
		return dashboardPartFile;
	}

	public final IProject getProject() {
		return containingProject;
	}

	@Override
	public IDashboardActionBarContributor getEditorActionBarContributor() {
		return null;
	}

	@ChartSetting(name = "Chart title", type = Type.SET)
	public void setChartTitle(String title) {
		if (getChart() != null) {
			getChart().setTitle(title);
		}

		chartTitle = title;
	}

	@ChartSetting(name = "Chart title", type = Type.GET)
	public String getChartTitle() {
		return chartTitle;
	}

	@Override
	public Point getPreferredSize() {
		return new Point(500, 300);
	}

	@Override
	public boolean isSinkSynchronized() {
		return sinkSynced;
	}

	@Override
	public void setSinkSynchronized(boolean doSync) {
		sinkSynced = doSync;
	}
	
	@Override
	public void addContext( String key, String value ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key for context map must not be null or empty!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "value for context map must not be null or empty!");
		
		contextMap.put(key,  value);
	}
	
	@Override
	public Optional<String> getContextValue( String key ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key for context map must not be null or empty!");
		
		return Optional.fromNullable(contextMap.get(key));
	}
	
	@Override
	public ImmutableCollection<String> getContextKeys() {
		return ImmutableSet.copyOf(contextMap.keySet());
	}
	
	@Override
	public void removeContext( String key ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key for context map must not be null or empty!");
		
		contextMap.remove(key);
	}
}
