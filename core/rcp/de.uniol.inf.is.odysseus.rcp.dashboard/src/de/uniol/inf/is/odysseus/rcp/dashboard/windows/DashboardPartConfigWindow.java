package de.uniol.inf.is.odysseus.rcp.dashboard.windows;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class DashboardPartConfigWindow extends TitleAreaDialog {

	private static class SettingValuePair {
		public SettingDescriptor<?> setting;
		public String value;

		public SettingValuePair(SettingDescriptor<?> setting, Object value) {
			this.setting = setting;
			this.value = value != null ? value.toString() : "";
		}
	}
	
	private static final String WINDOW_TITLE = "Configure Dashboard Part";
	private static final String DISPLAY_TITLE = "Dashboard Part settings";
	
	private final List<SettingValuePair> settings;
	private Button okButton;

	public DashboardPartConfigWindow(Shell parentShell, Configuration partConfig ) {
		super(parentShell);
		Preconditions.checkNotNull(partConfig, "Configuration of dashboard part must not be null!");
		
		this.settings = createSettingsList(partConfig);
	}

	private static List<SettingValuePair> createSettingsList(Configuration partConfig) {
		List<SettingValuePair> result = Lists.newArrayList();
		for( Setting<?> setting : partConfig.getSettings() ) {
			SettingDescriptor<?> settingDescriptor = setting.getSettingDescriptor();
			result.add(new SettingValuePair(settingDescriptor, setting.get()));
		}
		return result;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle(DISPLAY_TITLE);
		getShell().setText(WINDOW_TITLE);
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableComposite.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewer = createSettingsTableViewer(tableComposite);
		tableViewer.setInput(settings);
		
		tableComposite.pack();
		return tableComposite;
	}

	private TableViewer createSettingsTableViewer(Composite parent) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		final TableViewer tableViewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableViewerColumn settingNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		settingNameColumn.getColumn().setText("Setting");
		tableColumnLayout.setColumnData(settingNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		settingNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final SettingValuePair pair = (SettingValuePair) cell.getElement();
				final String txt = pair.setting.isOptional() ? pair.setting.getName() : pair.setting.getName() + "*";
				cell.setText(txt);
			}
		});

		final TableViewerColumn settingValueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		settingValueColumn.getColumn().setText("Value");
		tableColumnLayout.setColumnData(settingValueColumn.getColumn(), new ColumnWeightData(5, 25, true));
		settingValueColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final SettingValuePair pair = (SettingValuePair) cell.getElement();
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
				return ((SettingValuePair) element).value;
			}

			@Override
			public void modify(Object element, String property, Object value) {
				final TableItem item = (TableItem) element;
				final SettingValuePair pair = (SettingValuePair) item.getData();
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
	
	private void validateSettings() {
		for (final SettingValuePair pair : settings) {
			if (!pair.setting.isOptional() && Strings.isNullOrEmpty(pair.value)) {
				okButton.setEnabled(false);
				return;
			}
		}

		okButton.setEnabled(true);
	}
	
	public Map<String, String> getSelectedSettings() {
		final Map<String, String> settingMap = Maps.newHashMap();
		for (final SettingValuePair pair : settings) {
			settingMap.put(pair.setting.getName(), pair.value);
		}
		return settingMap;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, "OK", true);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.OK);
				close();
			}

		});
	}
}
