package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.AbstractViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.IChartSettingChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.config.MethodSetting;

@SuppressWarnings("rawtypes")
public class ChartConfigurer extends AbstractDashboardPartConfigurer<AbstractJFreeChart> {

	private static final Logger LOG = LoggerFactory.getLogger(ChartConfigurer.class);
	
	// SETTINGS
	private static final String MAIN_PREFERENCE_NODE = "de.uniol.inf.is.odysseus.rcp.viewer";
	
	private final Map<MethodSetting, Object> currentValues = new TreeMap<MethodSetting, Object>();
	private final Preferences preferences = ConfigurationScope.INSTANCE.getNode(MAIN_PREFERENCE_NODE);
	private final Map<String, TableEditor> tableEditors = new HashMap<String, TableEditor>();

	private AbstractJFreeChart dashboardPartChart;
	private boolean startedByThis;
	
	private Button saveAsDefaults;
	private Button loadDefaults;
	private Table settingsTable;
	
	// ATTRIBUTES
	private final List<IViewableAttribute> activatedAttributes = new ArrayList<IViewableAttribute>();
	private final List<IViewableAttribute> groupdByAttributes = new ArrayList<IViewableAttribute>();
	private final Map<IViewableAttribute, Button> tableChecks = new HashMap<>();
	private final Map<IViewableAttribute,Button> groupByChecks = new HashMap<>();

	private Button selectAllButton;
	private Button deselectAllButton;
	private Table table;

	@SuppressWarnings("unchecked")
	@Override
	public void init(AbstractJFreeChart dashboardPartToConfigure, Collection<IPhysicalOperator> roots ) {
		dashboardPartChart = dashboardPartToConfigure;
		
		for (MethodSetting ms : ((IChartSettingChangeable)this.dashboardPartChart).getChartSettings()) {
			try {
				Object value = ms.getGetter().invoke(this.dashboardPartChart);
				currentValues.put(ms, value);
			} catch (Exception e) {
				LOG.error("Could not determine value", e);
			}
		}
		
		for (Integer port : getPorts()) {
			for (IViewableAttribute att : getChoosenAttributes(port)) {
				activatedAttributes.add(att);
			}
			for (IViewableAttribute att : getGroupByAttributes(port)) {
				groupdByAttributes.add(att);
			}
		}
		
		try {
			if( !dashboardPartChart.isStarted() ) {
				dashboardPartChart.onStart(roots);
				startedByThis = true;
			}
		} catch (Exception ex) {
			LOG.error("Exception during starting dashboardpart chart", ex);
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	private List<IViewableAttribute> getChoosenAttributes(int port) {
		return dashboardPartChart.getChoosenAttributes(port);
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<IViewableAttribute> getGroupByAttributes(int port) {
		return dashboardPartChart.getGroupByAttributes(port);
	}
	
	@SuppressWarnings({ "unchecked" })
	private Set<Integer> getPorts() {
		return dashboardPartChart.getPorts();
	}
	
	@SuppressWarnings("unchecked")
	private List<IViewableAttribute> getViewableAttributes(int port) {
		return dashboardPartChart.getViewableAttributes(port);
	}
	
	protected final IChartSettingChangeable getChangeable() {
		return dashboardPartChart;
	}

	@Override
	public void createPartControl(Composite parent) {
		createSettingsControls(parent);
		createAttributesControls(parent);
	}

	private void createAttributesControls(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout());
		Composite labelTextComposite = new Composite(mainComposite, SWT.NONE);
	
		labelTextComposite.setLayout(new GridLayout(4, true));
		labelTextComposite.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));

		Label label1 = new Label(labelTextComposite, SWT.NONE);
		label1.setText("Please choose the attributes");

		selectAllButton = new Button(labelTextComposite, SWT.PUSH);
		selectAllButton.setText("Select all");
		deselectAllButton = new Button(labelTextComposite, SWT.PUSH);
		deselectAllButton.setText("Deselect all");

		selectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeAttributeSelection(true);
			}
		});

		deselectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeAttributeSelection(false);
			}
		});

		Composite tableSpan = new Composite(mainComposite, SWT.NONE);
		tableSpan.setLayout(new GridLayout());
		GridData spanData = new GridData(GridData.FILL, GridData.FILL, true, true);
		spanData.horizontalSpan = 2;
		spanData.heightHint = 300;
		tableSpan.setLayoutData(spanData);

		table = new Table(tableSpan, SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER);
		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		col1.setText("Attribute");
		col1.setWidth(416);

		TableColumn col2 = new TableColumn(table, SWT.CENTER);
		col2.setText("Visible");
		col2.setWidth(200);

		TableColumn col3 = new TableColumn(table, SWT.CENTER);
		col3.setText("groupBy");
		col3.setWidth(200);
		
		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tableChecks.clear();
		for (Integer port : getPorts()) {
			for (IViewableAttribute a : getViewableAttributes(port)) {
				TableItem item = new TableItem(table, SWT.NONE);
				Button check = new Button(table, SWT.CHECK);
				check.setData(a);
				boolean isSelected = this.activatedAttributes.contains(a);
				check.setSelection(isSelected);
				check.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Button thisButton = (Button) e.widget;
						IViewableAttribute selAtt = (IViewableAttribute) thisButton.getData();
						if (thisButton.getSelection()) {
							activatedAttributes.add(selAtt);
							groupdByAttributes.remove(selAtt);
							if (groupByChecks.get(selAtt) != null){
								groupByChecks.get(selAtt).setSelection(false);
							}
						} else {
							activatedAttributes.remove(selAtt);
						}
						updateSelectedAttributes();
					}
				});
				tableChecks.put(a, check);
				Button groupByCheck = new Button(table, SWT.CHECK);
				groupByCheck.setData(a);
				isSelected = this.groupdByAttributes.contains(a);
				groupByCheck.setSelection(isSelected);
				groupByCheck.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Button thisButton = (Button) e.widget;
						IViewableAttribute selAtt = (IViewableAttribute) thisButton.getData();
						if (thisButton.getSelection()) {
							groupdByAttributes.add(selAtt);
							activatedAttributes.remove(selAtt);
							if (tableChecks.get(selAtt) != null){
								tableChecks.get(selAtt).setSelection(false);
							}
						} else {
							groupdByAttributes.remove(selAtt);
						}
						updateSelectedAttributes();
					}
				});
				
				groupByChecks.put(a, groupByCheck);
				
				TableEditor tbl_editor = new TableEditor(table);
				tbl_editor.grabHorizontal = true;
				tbl_editor.minimumHeight = check.getSize().x;
				tbl_editor.minimumWidth = check.getSize().y;
				tbl_editor.setEditor(check, item, 1);
				
				tbl_editor = new TableEditor(table);
				tbl_editor.grabHorizontal = true;
				tbl_editor.minimumHeight = groupByCheck.getSize().x;
				tbl_editor.minimumWidth = groupByCheck.getSize().y;
				tbl_editor.setEditor(groupByCheck, item, 2);
				item.setText(0, a.toString());
			}
		}
		
		mainComposite.layout();
	}

	@SuppressWarnings("unchecked")
	private void updateSelectedAttributes() {
		Map<Integer, List<IViewableAttribute>> attr = AbstractViewableAttribute.getAttributesAsPortMapList(activatedAttributes);
		for (Entry<Integer, List<IViewableAttribute>> e : attr.entrySet()) {
			dashboardPartChart.setChoosenAttributes(e.getKey(), e.getValue());
		}
		attr = AbstractViewableAttribute.getAttributesAsPortMapList(groupdByAttributes);
		for (Entry<Integer, List<IViewableAttribute>> e : attr.entrySet()) {
			dashboardPartChart.setGroupByAttributes(e.getKey(), e.getValue());
		}
		dashboardPartChart.chartSettingsChanged();				
	}

	private void changeAttributeSelection(boolean select) {
		activatedAttributes.clear();
		for (Button b : tableChecks.values()) {
			b.setSelection(select);
			IViewableAttribute selAtt = (IViewableAttribute) b.getData();
			if (select) {
				activatedAttributes.add(selAtt);
				groupdByAttributes.remove(selAtt);
				if (groupByChecks.get(selAtt) != null){
					groupByChecks.get(selAtt).setSelection(false);
				}
			}
		}
	}

	private void createSettingsControls(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout());
		Composite labelTextComposite = new Composite(mainComposite, SWT.NONE);

		labelTextComposite.setLayout(new GridLayout(4, true));
		labelTextComposite.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));

		Label label1 = new Label(labelTextComposite, SWT.NONE);
		label1.setText("Please choose");

		saveAsDefaults = new Button(labelTextComposite, SWT.PUSH);
		saveAsDefaults.setText("Save current settings as defaults");
		saveAsDefaults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (Entry<MethodSetting, Object> entry : getCurrentValues().entrySet()) {
					String name = entry.getKey().getName();
					String value = entry.getValue().toString();
					preferences.put(name, value);					
				}
				try {
					preferences.flush();
				} catch (BackingStoreException e2) {
					e2.printStackTrace();
				}
			}
		});

		loadDefaults = new Button(labelTextComposite, SWT.PUSH);
		loadDefaults.setText("Load defaults");
		loadDefaults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadTable(true);
			}
		});

		Composite tableSpan = new Composite(mainComposite, SWT.NONE);
		tableSpan.setLayout(new GridLayout());
		GridData spanData = new GridData(GridData.FILL, GridData.FILL, true, true);
		spanData.horizontalSpan = 2;
		tableSpan.setLayoutData(spanData);

		settingsTable = new Table(tableSpan, SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER);
		TableColumn col1 = new TableColumn(settingsTable, SWT.LEFT);
		col1.setText("Attribute");
		col1.setWidth(300);

		TableColumn col2 = new TableColumn(settingsTable, SWT.LEFT);
		col2.setText("Value");
		col2.setWidth(300);
		col2.setAlignment(SWT.CENTER);

		settingsTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		settingsTable.setHeaderVisible(true);
		settingsTable.setLinesVisible(true);

		loadTable(false);	
		
		mainComposite.layout();
	}

	private void loadTable(boolean fromPreferences) {
		for (final Entry<MethodSetting, Object> entry : this.currentValues.entrySet()) {
			String name = entry.getKey().getName();
			String value = entry.getValue().toString();
			if (fromPreferences) {
				value = preferences.get(name, value);
			}
			TableItem item = resolveOrCreateTableItem(name);
			item.setText(1, value);
			if (fromPreferences) {
				Control te = this.tableEditors.get(name).getEditor();
				if (te instanceof CCombo) {
					CCombo combo = (CCombo) te;
					combo.select(combo.indexOf(value));
				} else if (te instanceof Button) {
					boolean val = Boolean.parseBoolean(value);
					Button check = (Button) te;
					check.setData(val);
					check.setSelection(val);
				} else if (te instanceof Text) {
					Text t = (Text) te;
					t.setText(value);
				}
				setValueType(entry, value);
			} else {

				Class<?> type = entry.getKey().getGetterValueType();
				if (entry.getKey().getListGetter() != null) {
					createDropDownField(settingsTable, item, entry);
				} else {
					if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
						createCheckBoxField(settingsTable, item, entry);
					} else {
						createTextField(settingsTable, item, entry);
					}
				}
			}
		}
	}
	
	private void createDropDownField(Table table, TableItem item, final Entry<MethodSetting, Object> entry) {
		try {
			List<?> liste = (List<?>) entry.getKey().getListGetter().invoke(this.dashboardPartChart);
			String name = item.getText();
			final CCombo combo = new CCombo(table, SWT.READ_ONLY);
			combo.computeSize(SWT.DEFAULT, table.getItemHeight());
			for (int i = 0; i < liste.size(); i++) {
				combo.add(liste.get(i).toString());
			}
			combo.select(combo.indexOf(item.getText(1)));
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					setValueType(entry, combo.getText());
				}
			});

			TableEditor tbl_editor = new TableEditor(table);
			tbl_editor.grabHorizontal = true;
			tbl_editor.minimumHeight = combo.getSize().x;
			tbl_editor.minimumWidth = combo.getSize().y;
			tbl_editor.setEditor(combo, item, 1);
			this.tableEditors.put(name, tbl_editor);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createTextField(Table table, TableItem item, final Entry<MethodSetting, Object> entry) {
		final Text textEditor = new Text(table, SWT.NONE);
		String thetext = item.getText(1);
		String name = item.getText(0);
		textEditor.setText(thetext);
		textEditor.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setValueType(entry, textEditor.getText());
			}
		});
		TableEditor tbl_editor = new TableEditor(table);
		tbl_editor.grabHorizontal = true;
		tbl_editor.minimumHeight = textEditor.getSize().x;
		tbl_editor.minimumWidth = textEditor.getSize().y;
		tbl_editor.setEditor(textEditor, item, 1);
		this.tableEditors.put(name, tbl_editor);
	}

	private void createCheckBoxField(Table table, TableItem item, final Entry<MethodSetting, Object> entry) {
		final Button check = new Button(table, SWT.CHECK);
		Boolean value = Boolean.parseBoolean(item.getText(1));
		check.setData(value);
		check.setSelection(value);
		check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setValueType(entry, String.valueOf(check.getSelection()));
			}
		});
		TableEditor tbl_editor = new TableEditor(table);
		tbl_editor.grabHorizontal = true;
		tbl_editor.minimumHeight = check.getSize().x;
		tbl_editor.minimumWidth = check.getSize().y;
		tbl_editor.setEditor(check, item, 1);
		String name = item.getText();
		this.tableEditors.put(name, tbl_editor);
	}

	private void setValueType(Entry<MethodSetting, Object> entry, String value) {
		try {
			Class<?> type = entry.getKey().getGetterValueType();
			if (Integer.class.isAssignableFrom(type)) {
				entry.setValue(Integer.parseInt(value));
			} else {
				if (Double.class.isAssignableFrom(type)) {
					entry.setValue(Double.parseDouble(value));
				} else {
					if (Float.class.isAssignableFrom(type)) {
						entry.setValue(Float.parseFloat(value));
					} else {
						if( Boolean.class.isAssignableFrom(type)) {
							entry.setValue(Boolean.parseBoolean(value));
						} else {
							if( Long.class.isAssignableFrom(type)) {
								entry.setValue(Long.parseLong(value));
							} else {
								entry.setValue(value);
							}
						}
					}
				}
			}
			invokeForType(entry.getKey(), entry.getValue());
			fireListener();
			
		} catch (Throwable ex) {
			LOG.error("Could not set value", ex);
		}
	}
	
	private void invokeForType(MethodSetting ms, Object ob) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (ob instanceof String) {
			Class<?> paramType = ms.getSetter().getParameterTypes()[0];
			String val = ob.toString();			
			
			if(paramType.equals(Double.class) || paramType.equals(double.class)){
				Double d = Double.parseDouble(val);
				ms.getSetter().invoke(this.dashboardPartChart, d);
			}else if(paramType.equals(Integer.class) || paramType.equals(int.class)){
				Integer d = Integer.parseInt(val);
				ms.getSetter().invoke(this.dashboardPartChart, d);
			}else if(paramType.equals(Boolean.class) || paramType.equals(boolean.class)){
				Boolean d = Boolean.getBoolean(val);
				ms.getSetter().invoke(this.dashboardPartChart, d);
			}else if(paramType.equals(Long.class) || paramType.equals(long.class)){
				Long d = Long.parseLong(val);
				ms.getSetter().invoke(this.dashboardPartChart, d);
			}else{
				ms.getSetter().invoke(this.dashboardPartChart, val);
			}
		} else {
			ms.getSetter().invoke(this.dashboardPartChart, ob);
		}
	}
	
	private TableItem resolveOrCreateTableItem(String name) {
		for (TableItem ti : settingsTable.getItems()) {
			if (ti.getText().equals(name)) {
				return ti;
			}
		}
		try {
			TableItem ti = new TableItem(settingsTable, SWT.NONE);
			ti.setText(0, name);
			return ti;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	@Override
	public void dispose() {
		if( startedByThis ) {
			dashboardPartChart.onStop();
		}
	}

	public Map<MethodSetting, Object> getCurrentValues() {
		return currentValues;
	}

}
