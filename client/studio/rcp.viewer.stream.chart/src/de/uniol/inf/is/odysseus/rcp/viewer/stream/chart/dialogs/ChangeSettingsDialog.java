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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.IChartSettingChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.MethodSetting;

public class ChangeSettingsDialog extends TitleAreaDialog {

	private static final String DEFAULT_MESSAGE = "Change the settings for this chart";
	protected static final String MAIN_PREFERENCE_NODE = "de.uniol.inf.is.odysseus.rcp.viewer";
	private IChartSettingChangeable changeable;
	private Table table;
	private Button okButton;
	private Button saveAsDefaults;
	private Button loadDefaults;
	private Map<MethodSetting, Object> currentValues = new TreeMap<MethodSetting, Object>();
	private Preferences preferences = ConfigurationScope.INSTANCE.getNode(MAIN_PREFERENCE_NODE);
	private Map<String, TableEditor> tableEditors = new HashMap<String, TableEditor>();

	public ChangeSettingsDialog(Shell parentShell, IChartSettingChangeable changeable) {
		super(parentShell);
		this.changeable = changeable;
		for (MethodSetting ms : this.changeable.getChartSettings()) {
			try {
				currentValues.put(ms, ms.getGetter().invoke(this.changeable));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle("Change Settings");
		setMessage(DEFAULT_MESSAGE);
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout());
		Composite labelTextComposite = new Composite(mainComposite, SWT.NONE);

		labelTextComposite.setLayout(new GridLayout(4, true));
		labelTextComposite.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));

		Label label1 = new Label(labelTextComposite, SWT.NONE);
		label1.setText("Please choose");

		this.saveAsDefaults = new Button(labelTextComposite, SWT.PUSH);
		this.saveAsDefaults.setText("Save current settings as defaults");
		this.saveAsDefaults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (Entry<MethodSetting, Object> entry : getCurrentValues().entrySet()) {
					String name = entry.getKey().getName();
					String value = entry.getValue().toString();
					preferences.put(name, value);					
				}
				try {
					// Forces the application to save the preferences
					preferences.flush();
					setMessage("Preferences saved!");
				} catch (BackingStoreException e2) {
					e2.printStackTrace();
				}
			}
		});

		this.loadDefaults = new Button(labelTextComposite, SWT.PUSH);
		this.loadDefaults.setText("Load defaults");
		this.loadDefaults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadTable(true);
				setMessage("Preferences loaded!");
			}
		});

		Composite tableSpan = new Composite(mainComposite, SWT.NONE);
		tableSpan.setLayout(new GridLayout());
		GridData spanData = new GridData(GridData.FILL, GridData.FILL, true, true);
		spanData.horizontalSpan = 2;
		spanData.heightHint = 400;
		tableSpan.setLayoutData(spanData);

		table = new Table(tableSpan, SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER);
		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		col1.setText("Attribute");
		col1.setWidth(450);

		TableColumn col2 = new TableColumn(table, SWT.LEFT);
		col2.setText("Value");
		col2.setWidth(300);
		col2.setAlignment(SWT.CENTER);

		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		loadTable(false);

		return parent;
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
					createDropDownField(table, item, entry);
				} else {
					if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
						createCheckBoxField(table, item, entry);
					} else {
						createTextField(table, item, entry);
					}
				}
			}
		}

	}

	/**
	 * @return
	 */
	private TableItem resolveOrCreateTableItem(String name) {
		for (TableItem ti : table.getItems()) {
			if (ti.getText().equals(name)) {
				return ti;
			}
		}
		try {
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(0, name);
			return ti;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void createDropDownField(Table table, TableItem item, final Entry<MethodSetting, Object> entry) {
		try {
			List<?> liste = (List<?>) entry.getKey().getListGetter().invoke(this.changeable);
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
					// combo.dispose();
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
		Button check = new Button(table, SWT.CHECK);
		Boolean value = Boolean.parseBoolean(item.getText(1));
		check.setData(value);
		check.setSelection(value);
		check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button thisButton = (Button) e.widget;
				entry.setValue(thisButton.getSelection());
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
						// else: string
						entry.setValue(value);
					}
				}
			}
			okButton.setEnabled(true);
			setMessage(DEFAULT_MESSAGE);
		} catch (NumberFormatException ex) {
			okButton.setEnabled(false);
			setErrorMessage("The value has to be a valid number!");
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		this.okButton = createButton(parent, IDialogConstants.OK_ID, "OK", true);

		this.okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				setReturnCode(Window.OK);
				close();
			}
		});

		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	}

	public Map<MethodSetting, Object> getCurrentValues() {
		return currentValues;
	}

}
