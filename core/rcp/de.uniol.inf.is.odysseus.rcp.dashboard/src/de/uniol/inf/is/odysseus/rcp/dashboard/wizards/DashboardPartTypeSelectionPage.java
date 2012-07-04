package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class DashboardPartTypeSelectionPage extends WizardPage {

	private static class SettingValuePair {
		public SettingDescriptor<?> setting;
		public String value;

		public SettingValuePair(SettingDescriptor<?> setting) {
			this.setting = setting;
			reset();
		}
		
		public void reset() {
			this.value = setting.getDefaultValue() != null ? setting.getDefaultValue().toString() : "";
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartTypeSelectionPage.class);
	private static final int DESCRIPTION_LABEL_HEIGHT = 60;

	private final List<String> dashboardPartNames;

	private Label partDescriptionLabel;
	private Combo choosePartNameCombo;
	private Label settingDescriptionLabel;

	private TableViewer settingsTable;
	private List<SettingValuePair> settings;

	public DashboardPartTypeSelectionPage(String pageName) {
		super(pageName);

		setTitle("Choose type of DashboardPart");
		setDescription("Choose one type of DashboardPart and configure it.");

		dashboardPartNames = determineDashboardPartNames();
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL)));
		rootComposite.setLayout(new GridLayout(1, true));

		Composite choosePartNameComposite = new Composite(rootComposite, SWT.NONE);
		choosePartNameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		choosePartNameComposite.setLayout(new GridLayout(2, false));

		Label choosePartNameLabel = new Label(choosePartNameComposite, SWT.NONE);
		choosePartNameLabel.setText("Type");

		choosePartNameCombo = new Combo(choosePartNameComposite, SWT.BORDER | SWT.READ_ONLY);
		choosePartNameCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		choosePartNameCombo.setItems(dashboardPartNames.toArray(new String[0]));

		partDescriptionLabel = new Label(rootComposite, SWT.BORDER | SWT.WRAP);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = DESCRIPTION_LABEL_HEIGHT;
		partDescriptionLabel.setLayoutData(gd);
		partDescriptionLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		partDescriptionLabel.setText("");
		choosePartNameCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectDashboardPart(choosePartNameCombo.getSelectionIndex());
			}

		});

		settingsTable = createSettingsTableViewer(rootComposite);
		settingsTable.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				selectSetting(settingsTable.getSelection());
			}
			
		});
		
		Composite settingDescriptionComposite = new Composite(rootComposite, SWT.NONE);
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		gd2.heightHint = DESCRIPTION_LABEL_HEIGHT;
		settingDescriptionComposite.setLayoutData(gd2);
		settingDescriptionComposite.setLayout(new GridLayout(2, false));

		settingDescriptionLabel = new Label(settingDescriptionComposite, SWT.BORDER | SWT.WRAP);
		settingDescriptionLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
		settingDescriptionLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		settingDescriptionLabel.setText("");
		
		Button resetSettingsButton = new Button(settingDescriptionComposite, SWT.PUSH);
		resetSettingsButton.setText("Reset settings");
		resetSettingsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetSettings();
			}
		});

		selectDashboardPart(0);
		finishCreation(rootComposite);
	}
	
	public String getSelectedDashboardPartName() {
		return choosePartNameCombo.getText();
	}
	
	public Map<String, String> getSelectedSettings() {
		Map<String, String> settingMap = Maps.newHashMap();
		for( SettingValuePair pair : settings ) {
			settingMap.put(pair.setting.getName(), pair.value);
		}
		return settingMap;
	}
	
	private void finishCreation(Composite rootComposite) {
		setErrorMessage(null);
		setMessage(null);
		setControl(rootComposite);
		setPageComplete(true);
	}

	private void selectDashboardPart(int index) {
		choosePartNameCombo.select(index);
		String dashboardName = getDashboardPartName(index);
		refreshPartDescriptionLabel(dashboardName);
		refreshSettingsTable(dashboardName);
		validateSettings();
	}
	
	private void selectSetting(ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection)selection;
		SettingValuePair pair = (SettingValuePair)structuredSelection.getFirstElement();
		settingDescriptionLabel.setText(pair != null ? pair.setting.getDescription() : "");
	}

	private void refreshSettingsTable(String dashboardName) {
		settings = getSettings(dashboardName);
		
		settingsTable.setInput(settings);
		settingsTable.refresh();
	}

	private void resetSettings() {
		for( SettingValuePair pair : settings ) {
			pair.reset();
		}
		
		settingsTable.refresh();
		validateSettings();
	}

	private void refreshPartDescriptionLabel(String dashboardPartName) {
		partDescriptionLabel.setText(getDescription(dashboardPartName));
	}
	
	private String getDashboardPartName(int index) {
		return dashboardPartNames.get(index);
	}

	private String getDescription(String dashboardPartName) {
		Optional<DashboardPartDescriptor> optDescriptor = DashboardPartRegistry.getDashboardPartDescriptor(dashboardPartName);
		if (optDescriptor.isPresent()) {
			DashboardPartDescriptor descriptor = optDescriptor.get();
			return descriptor.getDescription();
		} else {
			LOG.error("Could not find DashboardPartDescriptor for {}.", dashboardPartName);
			return "";
		}
	}

	private static List<SettingValuePair> getSettings(String dashboardPartName) {
		Optional<DashboardPartDescriptor> optDescriptor = DashboardPartRegistry.getDashboardPartDescriptor(dashboardPartName);
		List<SettingValuePair> result = Lists.newArrayList();
		if( optDescriptor.isPresent() ) {
			DashboardPartDescriptor descriptor = optDescriptor.get();
			for( String settingDescriptorName : descriptor.getSettingDescriptorNames() ) {
				Optional<SettingDescriptor<?>> optSettingDescriptor = descriptor.getSettingDescriptor(settingDescriptorName);
				if( optSettingDescriptor.isPresent() ) {
					SettingDescriptor<?> settingDescriptor = optSettingDescriptor.get();
					result.add(new SettingValuePair(settingDescriptor));
				} else {
					LOG.error("Could not find SettingDescriptor {} in DashboardPart {}", settingDescriptorName, dashboardPartName);
				}
			}
		} else {
			LOG.error("Could not find DashboardPartDescriptor for {}.", dashboardPartName);
		}
		
		return result;
	}

	private TableViewer createSettingsTableViewer(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		final TableViewer tableViewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn settingNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		settingNameColumn.getColumn().setText("Setting");
		tableColumnLayout.setColumnData(settingNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		settingNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				SettingValuePair pair = (SettingValuePair)cell.getElement();
				String txt = pair.setting.isOptional() ? pair.setting.getName() : pair.setting.getName() + "*";
				cell.setText(txt);
			}
		});
		
		TableViewerColumn settingValueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		settingValueColumn.getColumn().setText("Value");
		tableColumnLayout.setColumnData(settingValueColumn.getColumn(), new ColumnWeightData(5, 25, true));
		settingValueColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				SettingValuePair pair = (SettingValuePair)cell.getElement();
				cell.setText(pair.value);
			}
		});

		tableViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return "settingValue".equals(property);
			}

			@Override
			public Object getValue(Object element, String property) {
				return ((SettingValuePair)element).value;
			}

			@Override
			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem)element;
				SettingValuePair pair = (SettingValuePair)item.getData();
				pair.value = value.toString();
				tableViewer.update(item.getData(), null);
				
				validateSettings();
			}
			
		});
		
		tableViewer.setColumnProperties(new String[] { "setting", "settingValue" });
		tableViewer.setCellEditors(new CellEditor[] { null, new TextCellEditor(tableViewer.getTable()) });
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		return tableViewer;
	}

	private static List<String> determineDashboardPartNames() {
		return DashboardPartRegistry.getDashboardPartNames();
	}
	
	private void validateSettings() {
		for( SettingValuePair pair : settings ) {
			if(!pair.setting.isOptional() && Strings.isNullOrEmpty(pair.value)) {
				setPageComplete(false);
				return;
			}
		}
		
		setPageComplete(true);
	}
}
