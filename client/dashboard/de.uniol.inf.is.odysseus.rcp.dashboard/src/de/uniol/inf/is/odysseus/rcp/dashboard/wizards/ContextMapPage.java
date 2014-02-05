/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ContextMapPage extends WizardPage {

	private static class TableEntry {
		public String key;
		public String value;
		public boolean fixed;

		public TableEntry(String key, String value, boolean fixed) {
			this.key = key;
			this.value = value;
			this.fixed = fixed;
		}
	}

	private final QueryFileSelectionPage queryPage;
	
	private TableViewer tableViewer;
	private final List<TableEntry> tableEntries = Lists.newLinkedList();

	public ContextMapPage(String pageName, QueryFileSelectionPage queryFilePage) {
		super(pageName);

		setTitle("Context of the query");
		setDescription("Configure the context of the query with key-value-pairs.");
		
		this.queryPage = queryFilePage;
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		rootComposite.setLayout(new GridLayout(1, false));
		
		Composite tableComposite = new Composite(rootComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		TableViewerColumn keyColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		keyColumn.getColumn().setText("Key");
		tableColumnLayout.setColumnData(keyColumn.getColumn(), new ColumnWeightData(5, 25, true));
		keyColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				TableEntry entry = (TableEntry) cell.getElement();
				cell.setText(entry.key);
			}
		});

		TableViewerColumn valueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		valueColumn.getColumn().setText("Value");
		tableColumnLayout.setColumnData(valueColumn.getColumn(), new ColumnWeightData(5, 25, true));
		valueColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				TableEntry entry = (TableEntry) cell.getElement();
				cell.setText(entry.value);
			}
		});
		
		tableViewer.setCellEditors(new CellEditor[] { new TextCellEditor(tableViewer.getTable()), new TextCellEditor(tableViewer.getTable()) });
		tableViewer.setColumnProperties(new String[] {"key", "value"});
		tableViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				TableEntry entry = (TableEntry)element;
				if( "key".equals(property)) {
					return entry.key;
				}
				return entry.value;
			}

			@Override
			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem)element;
				TableEntry entry = (TableEntry)tableItem.getData();
				
				if( "key".equals(property)) {
					entry.key = (String)value;
					tableItem.setText(0, entry.key);
				} else {
					entry.value = (String)value;
					tableItem.setText(1, entry.value);
				}
				
				checkKeys();
			}
		});

		tableViewer.setInput(tableEntries);
		
		Composite buttonComposite = new Composite(rootComposite, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonComposite.setLayout(new GridLayout(2, true));
		
		Button addButton = new Button(buttonComposite, SWT.PUSH);
		addButton.setText("Add");
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableEntries.add(generateNewTableEntry());
				tableViewer.refresh();
				
				checkKeys();
			}
		});
		
		Button removeButton = new Button(buttonComposite, SWT.PUSH);
		removeButton.setText("Remove");
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
				for( Object selectedObject : selection.toArray() ) {
					TableEntry entry = (TableEntry)selectedObject;
					if( !entry.fixed ) {
						tableEntries.remove(entry);
					}
				}
				
				tableViewer.refresh();
				checkKeys();
			}
		});
		
		finishCreation(rootComposite);
	}
	
	public Map<String, String> getContextMap() {
		Map<String, String> map = Maps.newHashMap();
		for( TableEntry e : tableEntries ) {
			map.put(e.key, e.value);
		}
		return map;
	}
	
	private void checkKeys() {
		for( int i = 0; i < tableEntries.size(); i++ ) {
			String key = tableEntries.get(i).key;
			
			for( int j = i + 1; j < tableEntries.size(); j++ ) {
				if( tableEntries.get(j).key.equals(key)) {
					setPageComplete(false);
					setErrorMessage("Duplicate key '" + key + "'");
					return;
				}
			}
		}
		
		for( TableEntry e : tableEntries ) {
			if( e.fixed && Strings.isNullOrEmpty(e.value)) {
				setPageComplete(false);
				setErrorMessage("Value for key '" + e.key + "' is needed to execute query!");
				return;
			}
		}
		
		setErrorMessage(null);
		setPageComplete(true);
	}
	
	private TableEntry generateNewTableEntry() {
		int i = 0;
		wh: while(true) {
			String name = "key" + i;
			for( TableEntry e : tableEntries ) {
				if(e.key.equals(name)) {
					i++;
					continue wh;
				}
			}
			
			return new TableEntry(name, "value", false);
		}
	}
	
	private void finishCreation(Composite rootComposite) {
		setErrorMessage(null);
		setMessage(null);
		setControl(rootComposite);
		setPageComplete(true);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		if( visible == true ) {
			ImmutableList<String> queryText = queryPage.getQueryTextProvider().getQueryText();
			scanForUndefinedReplacements(queryText);
			tableViewer.refresh();
			
			checkKeys();
		}
	}

	private void scanForUndefinedReplacements(ImmutableList<String> queryText) {
		List<String> undefinedReplacements = Lists.newArrayList();
		List<String> foundDefines = Lists.newArrayList();
		List<String> preDefinedKeys = Lists.newArrayList();
		preDefinedKeys.add("WORKSPACE");
		preDefinedKeys.add("WORKSPACE/");
		preDefinedKeys.add("WORKSPACE\\");
		preDefinedKeys.add("PROJECT");
		preDefinedKeys.add("PROJECTPATH");
		preDefinedKeys.add("PROJECTPATH\\");
		preDefinedKeys.add("PROJECTPATH/");
		preDefinedKeys.add("WORKSPACEPROJECT");
		preDefinedKeys.add("WORKSPACEPROJECT\\");
		preDefinedKeys.add("WORKSPACEPROJECT/");
		preDefinedKeys.add("\\");
		preDefinedKeys.add("/");

		
		for( String line : queryText ) {
			line = line.trim();
			if( line.startsWith("#DEFINE")) {
				String[] defineParts = line.split("\\ ", 3);
				if( defineParts.length > 2 ) {
					foundDefines.add(defineParts[1]);
				}
			} else {
				
				int startPos = 0; 
				startPos = line.indexOf("${");
				
				while( startPos >= 0 ) {
					int posEnd = line.indexOf("}", startPos);
					if( posEnd != -1 ) {
						String undefinedReplacement = line.substring(startPos + 2, posEnd);
						
						if( !undefinedReplacements.contains(undefinedReplacement) && !foundDefines.contains(undefinedReplacement) && !preDefinedKeys.contains(undefinedReplacement)) {
							undefinedReplacements.add(undefinedReplacement);
						}
						startPos = line.indexOf("${", posEnd);
					} else {
						break;
					}
				}
			}
		}
		
		for( String undefinedReplacement : undefinedReplacements ) {
			tableEntries.add(new TableEntry(undefinedReplacement, "", true));
		}
	}
}
