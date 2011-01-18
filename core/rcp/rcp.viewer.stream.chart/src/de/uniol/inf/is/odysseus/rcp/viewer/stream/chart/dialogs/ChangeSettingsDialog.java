package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.IChartSettingChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.MethodSetting;

public class ChangeSettingsDialog extends TitleAreaDialog {

	private static final String DEFAULT_MESSAGE = "Change the settings for this chart";
	private IChartSettingChangeable changeable;
	private Table table;
	private Button okButton;
	private Map<MethodSetting, Object> currentValues = new HashMap<MethodSetting, Object>();

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
		Composite labelTextComposite = new Composite(parent, SWT.NONE);
		GridLayout layout2 = new GridLayout();
		layout2.numColumns = 2;
		labelTextComposite.setLayout(layout2);
		labelTextComposite.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));

		Label label1 = new Label(labelTextComposite, SWT.NONE);
		label1.setText("Please choose");

		Composite tableSpan = new Composite(labelTextComposite, SWT.NONE);
		tableSpan.setLayout(new GridLayout());
		GridData spanData = new GridData(GridData.FILL, GridData.FILL, true, true);
		spanData.horizontalSpan = 2;
		spanData.heightHint = 200;
		tableSpan.setLayoutData(spanData);

		table = new Table(tableSpan, SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER);
		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		col1.setText("Attribute");
		col1.setWidth(250);

		TableColumn col2 = new TableColumn(table, SWT.LEFT);
		col2.setText("Value");
		col2.setWidth(200);
		col2.setAlignment(SWT.CENTER);		

		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		for (final Entry<MethodSetting, Object> entry : this.currentValues.entrySet()) {
			String value = entry.getValue().toString();

			final TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, entry.getKey().getName());
			try {
				item.setText(1, value);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Class<?> type = entry.getKey().getGetterValueType();
			if (entry.getKey().getListGetter() != null) {
				createDropDownField(table, item, entry);
			} else {
				if (Boolean.class.isAssignableFrom(type)) {
					createCheckBoxField(table, item, entry);
				} else {
					createTextField(table, item, entry);
				}
			}

		}

		return parent;
	}

	private void createDropDownField(Table table, TableItem item, final Entry<MethodSetting, Object> entry) {
		try {
			List<?> liste = (List<?>) entry.getKey().getListGetter().invoke(this.changeable);

			final CCombo combo = new CCombo(table, SWT.READ_ONLY);
			combo.computeSize(SWT.DEFAULT, table.getItemHeight());
			for (int i = 0; i < liste.size(); i++) {
				combo.add(liste.get(i).toString());
			}
			combo.select(combo.indexOf(entry.getValue().toString()));

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

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createTextField(Table table, TableItem item, final Entry<MethodSetting, Object> entry) {
		final Text textEditor = new Text(table, SWT.NONE);
		textEditor.setText(entry.getValue().toString());
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
	}

	private void createCheckBoxField(Table table, TableItem item, final Entry<MethodSetting, Object> entry) {
		Button check = new Button(table, SWT.CHECK);
		Boolean value = (Boolean) entry.getValue();
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
