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

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.IAttributesChangeable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ChangeAttributesDialog extends TitleAreaDialog {

	private static final String DEFAULT_MESSAGE = "Changes the attributes that are shown by the chart";
	private Table table;
	private SDFAttributeList activatedAttributes;	
	private IAttributesChangeable changeable;
	private Button okButton;

	public ChangeAttributesDialog(Shell parentShell, IAttributesChangeable changeable) {
		super(parentShell);
		this.activatedAttributes = changeable.getVisibleSchema().clone();		
		this.changeable = changeable;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle("Change Attributes");
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
		col1.setWidth(400);

		TableColumn col2 = new TableColumn(table, SWT.LEFT);
		col2.setText("visible");
		col2.setWidth(100);
		col2.setAlignment(SWT.CENTER);

		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		int i = 0;
		for (SDFAttribute a : this.changeable.getAllowedSchema()) {
			TableItem item = new TableItem(table, SWT.NONE);
			Button check = new Button(table, SWT.CHECK);
			check.setData(a);
			check.setSelection(this.activatedAttributes.contains(a));
			check.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Button thisButton = (Button) e.widget;
					SDFAttribute selAtt = (SDFAttribute) thisButton.getData();
					if (thisButton.getSelection()) {
						activatedAttributes.add(selAtt);						
					} else {
						activatedAttributes.remove(selAtt);
					}
					validate();
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

	private void validate() {
		String test = ChangeAttributesDialog.this.changeable.isValidSelection(ChangeAttributesDialog.this.activatedAttributes);		
		if (test == null) {			
			okButton.setEnabled(true);
			setMessage(DEFAULT_MESSAGE);
		} else {			
			setErrorMessage(test);
			okButton.setEnabled(false);
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

	public SDFAttributeList getSelectedAttributes() {
		return this.activatedAttributes;
	}
}
