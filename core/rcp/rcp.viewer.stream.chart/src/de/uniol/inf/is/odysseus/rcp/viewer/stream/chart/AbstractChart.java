package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSelectedAttributesAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action.ChangeSettingsAction;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.IChartSettingChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.MethodSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.editor.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamElementListener;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttributeList;

public abstract class AbstractChart extends ViewPart implements IAttributesChangeable, IChartSettingChangeable, IStreamElementListener<Object> {

	protected static ImageDescriptor IMG_MONITOR_EDIT = ImageDescriptor.createFromURL(Activator.getBundleContext().getBundle().getEntry("icons/monitor_edit.png"));
	protected static ImageDescriptor IMG_COG = ImageDescriptor.createFromURL(Activator.getBundleContext().getBundle().getEntry("icons/cog.png"));

	protected final static Color DEFAULT_BACKGROUND = Color.WHITE;
	protected final static Color DEFAULT_BACKGROUND_GRID = Color.GRAY;
	protected final static String VIEW_ID_PREFIX = "de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts";

	private SDFAttributeList allowedSchema;
	private SDFAttributeList initialSchema;
	private SDFAttributeList visibleSchema;	
	private SDFMetaAttributeList metadataSchema;

	private JFreeChart chart;
	private ChangeSelectedAttributesAction changeAttributesAction;
	private ChangeSettingsAction changeSettingsAction;
	private IStreamConnection<Object> connection;

	public void init(IPhysicalOperator observingOperator) {
		this.connection = createConnection(observingOperator);
		init(connection);
	}

	public IStreamConnection<Object> createConnection(IPhysicalOperator operator) {
		final Collection<ISource<?>> sources = new ArrayList<ISource<?>>();
		if (operator instanceof ISource<?>) {
			sources.add((ISource<?>) operator);
		} else if (operator instanceof ISink<?>) {
			Collection<?> list = ((ISink<?>) operator).getSubscribedToSource();
			for (Object obj : list)
				sources.add((ISource<?>) ((PhysicalSubscription<?>) obj).getTarget());
		} else
			throw new IllegalArgumentException("could not identify type of content of node " + operator);

		IStreamConnection<Object> connection = new DefaultStreamConnection<Object>(sources);
		return connection;
	}

	public void init(IStreamConnection<Object> streamConnection) {
		// first save initial schema
		this.initialSchema = extractSchema(streamConnection);
		// get only allowed datatypes for the current schema from initial schema
		this.allowedSchema = new SDFAttributeList();
		for (int i = 0; i < initialSchema.getAttributeCount(); i++) {
			String t = initialSchema.getAttribute(i).getDatatype().getURI();
			if (isAllowedDataType(t)) {
				this.allowedSchema.add(initialSchema.getAttribute(i));
			}
		}

		this.visibleSchema = allowedSchema.clone();

		// now we need all metadata...
		
		this.metadataSchema = createSchemaFromMetadata(streamConnection); 
		this.allowedSchema.addAll(metadataSchema);

		if (validate()) {
			streamConnection.addStreamElementListener(this);
			streamConnection.connect();
			chartSettingsChanged();
			init();
		}

	}	

	private SDFMetaAttributeList createSchemaFromMetadata(IStreamConnection<Object> streamConnection) {
		SDFMetaAttributeList attributes = new SDFMetaAttributeList();
		for(ISource<?> source : streamConnection.getSources()){
			attributes = SDFMetaAttributeList.union(attributes, source.getMetaAttributeSchema());
		}
		return attributes;
	}

	protected boolean isAllowedDataType(String datatype) {

		if (datatype.equals("Double") || datatype.equals("Long") || datatype.equals("Integer") || datatype.equals("StartTimestamp") || datatype.equals("EndTimestamp")
				|| datatype.equals("Timestamp")) {
			return true;
		}
		return false;
	}

	protected boolean validate() {
		if (this.allowedSchema.size() > 0) {
			return true;
		}
		System.out.println("Chart View not validated, because there has to be at least one valid attribute");
		return false;
	}

	protected void init() {

	}

	@Override
	public void setVisibleSchema(SDFAttributeList selectedlist) {
		this.visibleSchema = selectedlist;
		chartSettingsChanged();
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof RelationalTuple<?>)) {
			System.out.println("Warning: Stream visualization is only for relational tuple!");
			return;
		}
		
		@SuppressWarnings("unchecked")
		final RelationalTuple<? extends ITimeInterval> tuple = (RelationalTuple<? extends ITimeInterval>) element;
		try {			
//			// TODO: hashmap is not sort-safe --> selected schema must not be equal to following entryset!!
//			for(Entry<SDFAttribute, Method> attribute : this.metadataSchema.entrySet()){
//				try {
//					tuple.append(attribute.getValue().invoke(tuple.getMetadata()), true);
//				} catch (Exception e) {
//					e.printStackTrace();				
//				}
//			}
			processElement(tuple, port);
		} catch (SWTException swtex) {
			System.out.println("WARN: SWT Exception " + swtex.getMessage());
		}

	}

	protected abstract void processElement(RelationalTuple<? extends ITimeInterval> tuple, int port);

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {

	}

	@Override
	public void createPartControl(Composite parent) {
		this.chart = createChart();
		decorateChart(this.chart);
		new ChartComposite(parent, SWT.NONE, this.chart, true);
		// new ChartComposite(parent, SWT.NONE, chart, 2048, 2048, 640, 480,
		// 2048, 2048, true, true, false, false, true, true);
		createActions();
		contributeToActionBars();

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
		this.changeAttributesAction = new ChangeSelectedAttributesAction(this.getSite().getShell(), this);
		this.changeAttributesAction.setText("Change Attributes");
		this.changeAttributesAction.setToolTipText("Configure the attributes that will be shown by the chart");
		this.changeAttributesAction.setImageDescriptor(IMG_MONITOR_EDIT);

		this.changeSettingsAction = new ChangeSettingsAction(this.getSite().getShell(), this);
		this.changeSettingsAction.setText("Change Settings");
		this.changeSettingsAction.setToolTipText("Change several settings for this chart");
		this.changeSettingsAction.setImageDescriptor(IMG_COG);

	}

	protected abstract void decorateChart(JFreeChart thechart);

	private SDFAttributeList extractSchema(IStreamConnection<?> streamConnection) {
		ISource<?>[] sources = streamConnection.getSources().toArray(new ISource<?>[0]);
		return sources[0].getOutputSchema();
	}

	protected abstract JFreeChart createChart();

	@Override
	public void dispose() {
		if (this.connection.isConnected()) {
			this.connection.disconnect();
		}
	}

	@Override
	public void setFocus() {

	}

	public SDFAttributeList getAllowedSchema() {
		return this.allowedSchema;
	}

	public SDFAttributeList getInitialSchema() {
		return this.initialSchema;
	}

	public String getTitle() {
		return "";
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

	public abstract String getViewID();

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
		} else {
			return findMethod(otherMethod.name(), Type.SET);
		}
	}

	@Override
	public SDFAttributeList getVisibleSchema() {
		return this.visibleSchema;
	}

	public SDFAttributeList getSchema() {
		return this.initialSchema;
	}
}
