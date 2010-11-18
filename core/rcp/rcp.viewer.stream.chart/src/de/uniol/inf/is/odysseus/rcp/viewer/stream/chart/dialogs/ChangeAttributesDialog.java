package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ChangeAttributesDialog extends TitleAreaDialog {

	private Table table;
	private SDFAttributeList schema;
	private boolean[] activatedAttributes;

	public ChangeAttributesDialog(Shell parentShell, SDFAttributeList schema, boolean[] activatedAttributes) {
		super(parentShell);
		this.schema = schema.clone();
		this.activatedAttributes = activatedAttributes;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle("Change Attributes");
		setMessage("Changes the attributes that will be shown by the chart");
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
		col1.setWidth(400);

		TableColumn col2 = new TableColumn(table, SWT.LEFT);
		col2.setText("visible");
		col2.setWidth(100);
		col2.setAlignment(SWT.CENTER);

		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// table.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// LoadWorldDialog.this.item = (TableItem) e.item;
		// LoadWorldDialog.this.name = LoadWorldDialog.this.item
		// .getText(0);
		// }
		// });

		int i = 0;
		for (SDFAttribute a : this.schema) {
			TableItem item = new TableItem(table, SWT.NONE);
			Button check = new Button(table, SWT.CHECK);
			check.setData(i);
			check.setSelection(activatedAttributes[i]);
			check.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Button thisButton = (Button) e.widget;
					int zeilennummer = (Integer) thisButton.getData();
					if (thisButton.getSelection()) {
						activatedAttributes[zeilennummer] = true;
					} else {
						activatedAttributes[zeilennummer] = false;
					}
				}
			});
			TableEditor tbl_editor = new TableEditor(table);
			tbl_editor.grabHorizontal = true;
			tbl_editor.minimumHeight = check.getSize().x;
			tbl_editor.minimumWidth = check.getSize().y;
			tbl_editor.setEditor(check, item, 1);
			item.setText(0, a.toString());
			i++;
		}

		return parent;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, "OK", true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("SAVE.....");
				setReturnCode(Window.OK);
				close();
			}
		});

		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	}

	public boolean[] getSelectedAttributes() {
		return this.activatedAttributes;
	}
}
