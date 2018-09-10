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

import java.util.ArrayList;
import java.util.HashMap;
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
	private List<IViewableAttribute> groupByAttributes = new ArrayList<IViewableAttribute>();
	private IAttributesChangeable<T> changeable;
	private Button okButton;
	private Button selectAllButton;
	private Button deselectAllButton;
	private Map<IViewableAttribute,Button> tableChecks = new HashMap<>();
	private Map<IViewableAttribute,Button> tableGroupBy = new HashMap<>();

	
	public ChangeAttributesDialog(Shell parentShell, IAttributesChangeable<T> changeable) {
		super(parentShell);
		// Collections.copy(this.activatedAttributes,
		// changeable.getChoosenAttributes());
		for (Integer port : changeable.getPorts()) {
			for (IViewableAttribute att : changeable.getChoosenAttributes(port)) {
				this.activatedAttributes.add(att);
			}
			for (IViewableAttribute att: changeable.getGroupByAttributes(port)){
				this.groupByAttributes.add(att);
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
		
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout());
		Composite labelTextComposite = new Composite(mainComposite, SWT.NONE);
	
		labelTextComposite.setLayout(new GridLayout(4, true));
		labelTextComposite.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));

		Label label1 = new Label(labelTextComposite, SWT.NONE);
		label1.setText("Please choose the attributes");

		selectAllButton = createButton(labelTextComposite, IDialogConstants.SELECT_ALL_ID, "Select all", true);
		deselectAllButton = createButton(labelTextComposite, IDialogConstants.DESELECT_ALL_ID, "Deselect all", true);

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
		col2.setText("visible");
		col2.setWidth(200);

		TableColumn col3 = new TableColumn(table, SWT.CENTER);
		col3.setText("groupBy");
		col3.setWidth(200);

		
		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tableChecks.clear();
		tableGroupBy.clear();
		for (Integer port : this.changeable.getPorts()) {
			for (IViewableAttribute a : this.changeable.getViewableAttributes(port)) {
				
				TableItem item = new TableItem(table, SWT.NONE);
				Button check = new Button(table, SWT.CHECK);
				Button groupBy = new Button(table, SWT.CHECK);
				check.setData(a);
				groupBy.setData(a);
				check.setSelection(this.activatedAttributes.contains(a));
				groupBy.setSelection(this.groupByAttributes.contains(a));
				check.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Button thisButton = (Button) e.widget;
						IViewableAttribute selAtt = (IViewableAttribute) thisButton.getData();
						if (thisButton.getSelection()) {
							activatedAttributes.add(selAtt);
							groupByAttributes.remove(selAtt);
							if (tableGroupBy.containsKey(selAtt)){
								tableGroupBy.get(selAtt).setSelection(false);
							}
						} else {
							activatedAttributes.remove(selAtt);
						}
						validate();
					}
				});
				groupBy.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Button thisButton = (Button) e.widget;
						IViewableAttribute selAtt = (IViewableAttribute) thisButton.getData();
						if (thisButton.getSelection()) {
							groupByAttributes.add(selAtt);
							activatedAttributes.remove(selAtt);
							if (tableChecks.containsKey(selAtt)){
								tableChecks.get(selAtt).setSelection(false);
							}
						} else {
							groupByAttributes.remove(selAtt);
						}
						validate();
					}
				});
				tableChecks.put(a,check);
				tableGroupBy.put(a, groupBy);
				TableEditor tbl_editor = new TableEditor(table);
				tbl_editor.grabHorizontal = true;
				tbl_editor.minimumHeight = check.getSize().x;
				tbl_editor.minimumWidth = check.getSize().y;
				tbl_editor.setEditor(check, item, 1);
				tbl_editor = new TableEditor(table);
				tbl_editor.grabHorizontal = true;
				tbl_editor.minimumHeight = groupBy.getSize().x;
				tbl_editor.minimumWidth = groupBy.getSize().y;
				tbl_editor.setEditor(groupBy, item, 2);

				item.setText(0, a.toString());
			}
		}
		return parent;
	}

	private void validate() {
		Map<Integer, Set<IViewableAttribute>> attribs = AbstractViewableAttribute.getAttributesAsPortMapSet(ChangeAttributesDialog.this.activatedAttributes);
		String test = ChangeAttributesDialog.this.changeable.isValidSelection(attribs);
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

	private void changeAttributeSelection(boolean select) {
		activatedAttributes.clear();
		for (Button b : tableChecks.values()) {
			b.setSelection(select);
			IViewableAttribute selAtt = (IViewableAttribute) b.getData();
			if (select) {
				activatedAttributes.add(selAtt);
				if (tableGroupBy.containsKey(selAtt)){
					tableGroupBy.get(selAtt).setSelection(false);
				}
			}
		}
		validate();
	}

	public List<IViewableAttribute> getSelectedAttributes() {
		return this.activatedAttributes;
	}
	
	public List<IViewableAttribute> getGroupByAttributes() {
		return groupByAttributes;
	}
	
	
}
