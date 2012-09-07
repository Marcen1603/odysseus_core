/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.editor.text.pql.windows;

import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;

class OutputSchemaTableViewer {

	private static final Logger LOG = LoggerFactory.getLogger(OutputSchemaTableViewer.class);
	
	private final TableViewer tableViewer;
	private final List<AttributeTypePair> attributes = Lists.newArrayList();
	
	private final List<String> dataTypes;
	
	public OutputSchemaTableViewer( Composite parent ) {
		dataTypes = determineAttributeTypes();		
		
		parent.setLayout(new GridLayout());

		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();

		Composite outputSchemaTableButtonComposite = new Composite(parent, SWT.NONE);
		insertTableButtons(outputSchemaTableButtonComposite, tableViewer, attributes, AttributeTypePair.class);

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn indexColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		indexColumn.getColumn().setText("#");
		tableColumnLayout.setColumnData(indexColumn.getColumn(), new ColumnWeightData(1, 10, true));
		indexColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				AttributeTypePair attributeTypePair = (AttributeTypePair) cell.getElement();
				cell.setText(String.valueOf(attributes.indexOf(attributeTypePair) + 1));
			}
		});
		
		TableViewerColumn attributeNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		attributeNameColumn.getColumn().setText("Attribute");
		tableColumnLayout.setColumnData(attributeNameColumn.getColumn(), new ColumnWeightData(7, 25, true));
		attributeNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				AttributeTypePair attributeTypePair = (AttributeTypePair) cell.getElement();
				cell.setText(attributeTypePair.getAttributeName());
			}
		});

		TableViewerColumn typeColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		typeColumn.getColumn().setText("Type");
		tableColumnLayout.setColumnData(typeColumn.getColumn(), new ColumnWeightData(7, 25, true));
		typeColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				AttributeTypePair attributeTypePair = (AttributeTypePair) cell.getElement();
				cell.setText(dataTypes.get(attributeTypePair.getTypeIndex()));
			}
		});

		tableViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return !"index".equalsIgnoreCase(property);
			}

			@Override
			public Object getValue(Object element, String property) {
				switch (property) {
				case "attribute":
					return ((AttributeTypePair) element).getAttributeName();

				case "type":
					return ((AttributeTypePair) element).getTypeIndex();

				default:
					throw new RuntimeException("Property " + property + " not known to get value!");
				}
			}

			@Override
			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem) element;
				AttributeTypePair setting = (AttributeTypePair) item.getData();
				switch (property) {
				case "attribute":
					setting.setAttributeName((String) value);
					break;

				case "type":
					int index = (Integer)value;
					if( index >= 0 && index < dataTypes.size()) {
						setting.setTypeIndex(index);
					}
					break;
					
				default:
					throw new RuntimeException("Property " + property + " not known to modify!");
				}
				tableViewer.refresh();
			}

		});

		tableViewer.setColumnProperties(new String[] { "index", "attribute", "type" });
		tableViewer.setCellEditors(new CellEditor[] { null, new TextCellEditor(tableViewer.getTable()), new ComboBoxCellEditor(tableViewer.getTable(), dataTypes.toArray(new String[0]), SWT.READ_ONLY) });
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		tableViewer.setInput(attributes);		
	}
	
	public TableViewer getTableViewer() {
		return tableViewer;
	}
	
	public List<AttributeTypePair> getData() {
		return attributes;
	}
	
	public String getDataType( int index ) {
		return dataTypes.get(index);
	}
	
	private static <T> void insertTableButtons(Composite outputSchemaTableButtonComposite, final TableViewer tableViewer, final List<T> inputList, final Class<T> inputClass) {
		outputSchemaTableButtonComposite.setLayout(new GridLayout(2, true));
		
		Button addButton = createButton(outputSchemaTableButtonComposite, "Add");
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					T obj = inputClass.newInstance();
					inputList.add(obj);
					tableViewer.refresh();
				} catch (InstantiationException ex) {
					LOG.error("Could not create instance of class {} to insert in table.", inputClass, ex);
				} catch (IllegalAccessException ex) {
					LOG.error("Could not create instance of class {} to insert in table.", inputClass, ex);
				}
			}
		});
		
		final Button removeButton = createButton(outputSchemaTableButtonComposite, "Remove");
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( tableViewer.getSelection() != null ) {
					IStructuredSelection structSelection = (IStructuredSelection)tableViewer.getSelection();
					inputList.remove(structSelection.getFirstElement());
					tableViewer.refresh();
					
					if( inputList.isEmpty() ) {
						removeButton.setEnabled(false);
					}
				}
			}
		});
		
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				removeButton.setEnabled( event.getSelection() != null );
			}
		});
	}
	
	private static List<String> determineAttributeTypes() {
		IDataDictionary dd = PQLEditorTextPlugIn.getDataDictionary();
		if( dd == null ) {
			return Lists.newArrayList();
		}
		
		return ImmutableList.copyOf(dd.getDatatypes());
	}
	
	private static Button createButton(Composite composite, String text) {
		Button button = new Button(composite, SWT.PUSH);
		button.setText(text);
		return button;
	}
	
}
