/** Copyright [2011] [The Odysseus Team]
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.AbstractViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public class ChangeAttributesDialog<T> extends TitleAreaDialog {

	private static final String DEFAULT_MESSAGE = "Changes the attributes that are shown by the chart";
	private Table table;
	private List<IViewableAttribute> activatedAttributes = new ArrayList<IViewableAttribute>();
	private IAttributesChangeable<T> changeable;
	private Button okButton;

	public ChangeAttributesDialog(Shell parentShell,
			IAttributesChangeable<T> changeable) {
		super(parentShell);
		// Collections.copy(this.activatedAttributes,
		// changeable.getChoosenAttributes());
		for (Integer port : changeable.getPorts()) {
			for (IViewableAttribute att : changeable.getChoosenAttributes(port)) {
				this.activatedAttributes.add(att);
			}
		}
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
		labelTextComposite.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, true, false));

		Label label1 = new Label(labelTextComposite, SWT.NONE);
		label1.setText("Please choose");

		Composite tableSpan = new Composite(labelTextComposite, SWT.NONE);
		tableSpan.setLayout(new GridLayout());
		GridData spanData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		spanData.horizontalSpan = 2;
		spanData.heightHint = 200;
		tableSpan.setLayoutData(spanData);

		table = new Table(tableSpan, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.V_SCROLL | SWT.BORDER);
		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		col1.setText("Attribute");
		col1.setWidth(316);

		TableColumn col2 = new TableColumn(table, SWT.CENTER);
		col2.setText("visible");
		col2.setWidth(100);

		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				true));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		for (Integer port : this.changeable.getPorts()) {
			for (IViewableAttribute a : this.changeable
					.getViewableAttributes(port)) {
				TableItem item = new TableItem(table, SWT.NONE);
				Button check = new Button(table, SWT.CHECK);
				check.setData(a);
				check.setSelection(this.activatedAttributes.contains(a));
				check.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Button thisButton = (Button) e.widget;
						IViewableAttribute selAtt = (IViewableAttribute) thisButton
								.getData();
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
			}
		}
		return parent;
	}

	private void validate() {
		Map<Integer, Set<IViewableAttribute>> attribs = AbstractViewableAttribute.getAttributesAsPortMapSet(ChangeAttributesDialog.this.activatedAttributes);
		String test = ChangeAttributesDialog.this.changeable
				.isValidSelection(attribs);
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

	public List<IViewableAttribute> getSelectedAttributes() {
		return this.activatedAttributes;
	}
}
