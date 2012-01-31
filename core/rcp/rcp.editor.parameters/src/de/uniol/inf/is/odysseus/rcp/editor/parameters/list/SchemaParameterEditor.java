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
package de.uniol.inf.is.odysseus.rcp.editor.parameters.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.ComboEditingColumnDefinition;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.SimpleColumnDefinition;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.TextEditingColumnDefinition;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;


public class SchemaParameterEditor extends AbstractTableButtonListParameterEditor<SDFAttribute, SDFAttribute, List<String>> {

	private static final String[] TYPES = {
		"Integer", 
		"Long",
		"Double",
		"String",
		"StartTimestamp",
		"EndTimestamp",
		"Date"
	};
	
	@Override
	protected SDFAttribute createNewDataRow() {
		SDFAttribute attr = new SDFAttribute(null,"attribute", SDFDatatype.INTEGER);
		return attr;
	}
	
	@Override
	protected SDFAttribute convertFrom(SDFAttribute element) {
		return element;
	}
	
	@Override
	protected List<String> convertTo(SDFAttribute element) {
		List<String> s = new ArrayList<String>();
		s.add(element.getSourceName() + "." + element.getAttributeName());
		s.add(element.getDatatype().getURIWithoutQualName());
		return s;
	}
	
	@Override
	protected List<SimpleColumnDefinition<SDFAttribute>> createColumnDefinitions() {
		List<SimpleColumnDefinition<SDFAttribute>> list = new ArrayList<SimpleColumnDefinition<SDFAttribute>>();
		
		list.add(new TextEditingColumnDefinition<SDFAttribute>("Name") {
			@Override
			protected void setValue(SDFAttribute element, String value) {
				//element.setAttributeName(value);
				throw new RuntimeException("Currently not implemented!");
			}

			@Override
			protected String getStringValue(SDFAttribute element) {
				return element.getAttributeName();
			}
		});
		
		list.add(new ComboEditingColumnDefinition<SDFAttribute>("Type", TYPES ) {

			@Override
			protected void setValue(SDFAttribute element, String value) {
				//element.setDatatype(GlobalState.getActiveDatadictionary().getDatatype(value));
				// TODO: Das Setzen des Datentypen muss verz�gert erfolgen, da der Datatype nun
				// immutable ist
			}

			@Override
			protected String getStringValue(SDFAttribute element) {
				return element.getDatatype().getURIWithoutQualName();
			}
		});
		
		return list;
	}
	
//	private TableViewer tableViewer;
//	
//	@Override
//	public void createControl(final Composite parent) {
//		GridLayout layout = new GridLayout();
//		layout.numColumns = 2;
//		parent.setLayout(layout);
//		
//		Text schemaLabel = new Text(parent, SWT.CENTER);
//		schemaLabel.setText(SCHEMA_TEXT);
//		schemaLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
//		GridData data = new GridData(GridData.FILL_HORIZONTAL);
//		data.horizontalSpan = 2;
//		schemaLabel.setLayoutData(data);
//		
//		Composite tableComposite = new Composite(parent, SWT.NONE);
//		TableColumnLayout tableLayout = new TableColumnLayout();
//		tableComposite.setLayout(tableLayout);
//		data = new GridData( GridData.FILL_HORIZONTAL);
//		data.horizontalSpan = 2;
//		tableComposite.setLayoutData(data);
//		
//		tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
//		tableViewer.getTable().setHeaderVisible(true);
//		tableViewer.getTable().setLinesVisible(true);
//		
//		// Name
//		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
//		nameColumn.getColumn().setText(NAME_TEXT);
//		tableLayout.setColumnData( nameColumn.getColumn(), new ColumnWeightData(5,25,true));
//		nameColumn.setLabelProvider(new CellLabelProvider() {
//			@Override
//			public void update(ViewerCell cell) {
//				SDFAttribute attr = ((SDFAttribute)cell.getElement());
//				cell.setText(attr.getSourceName() + "." + attr.getAttributeName());
//			}
//		});
//		
//		// Type
//		TableViewerColumn typeColumn = new TableViewerColumn(tableViewer, SWT.NONE);
//		typeColumn.getColumn().setText(TYPE_TEXT);
//		tableLayout.setColumnData( typeColumn.getColumn(), new ColumnWeightData(5,25,true));
//		typeColumn.setLabelProvider(new CellLabelProvider() {
//			@Override
//			public void update(ViewerCell cell) {
//				SDFAttribute attr = ((SDFAttribute)cell.getElement());
//				cell.setText(attr.getDatatype().getURIWithoutQualName());
//			}
//		});
//		
//		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
//		
//		Button addButton = new Button( parent, SWT.PUSH);
//		addButton.setImage( Activator.getDefault().getImageRegistry().get("addIcon"));
//		addButton.setToolTipText(ADD_TEXT);
//		addButton.addSelectionListener( new SelectionAdapter() {
//			@SuppressWarnings("unchecked")
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				// Liste aller Namen holen
//				List<String> names = new ArrayList<String>();
//				for( SDFAttribute a : ((List<SDFAttribute>)tableViewer.getInput()) ) {
//					names.add(a.getSourceName() + "." + a.getURIWithoutQualName());
//				}
//				
//				// Dialog starten
//				final AddAttributeWindow attWnd = new AddAttributeWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), names);
//						
//				attWnd.show(new DisposeListener() {
//					@Override
//					public void widgetDisposed(DisposeEvent e) {
//						
//						// erstelltes Attribut vom Fenster holen und ggfs. einsetzen
//						SDFAttribute att = attWnd.getAttribute();
//						if( att != null ) {
//							List<SDFAttribute> attributes = (List<SDFAttribute>)tableViewer.getInput();
//							attributes.add(att);
//							
//							// neu zeichnen
//							tableViewer.refresh();
//							getView().layout();
//							
//							// speichern
//							saveTable();
//						}
//					}
//				});
//			}
//		});
//		
//		Button removeButton = new Button( parent, SWT.PUSH );
//		removeButton.setImage( Activator.getDefault().getImageRegistry().get("removeIcon"));
//		removeButton.setToolTipText(REMOVE_TEXT);
//		removeButton.addSelectionListener(new SelectionAdapter() {
//			
//			@SuppressWarnings("unchecked")
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				// selection ist ein SDFAttribute
//				SDFAttribute selection = getSelection();
//				
//				if( selection != null ) {
//					// aus der Liste entfernen
//					List<SDFAttribute> attributes = (List<SDFAttribute>)tableViewer.getInput();
//					attributes.remove(selection);
//					
//					// neu zeichnen
//					tableViewer.refresh();
//					getView().layout();
//					
//					// speichern
//					saveTable();
//				}
//			}
//		});
//		
//		loadTable();
//	}
//
//	@Override
//	public void close() {
//		saveTable();
//	}
//	
//	private SDFAttribute getSelection() {
//		IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
//		return (SDFAttribute)selection.getFirstElement();
//	}
//
//	private void loadTable() {
//		List<SDFAttribute> attributes = getValue();
//		if( attributes == null ) 
//			attributes = new ArrayList<SDFAttribute>();
//				
//		tableViewer.setInput(attributes);
//		
//		tableViewer.getTable().getParent().layout();
//	}
//	
//	@SuppressWarnings("unchecked")
//	private void saveTable() {
//		List<SDFAttribute> attributes = (List<SDFAttribute>)tableViewer.getInput();
//		if( attributes.size() != 0 ) {
//		
//			ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
//			
//			for( SDFAttribute a : attributes ) {
//				ArrayList<String> s = new ArrayList<String>();
//				s.add(a.getSourceName() + "." + a.getAttributeName());
//				s.add(a.getDatatype().getURIWithoutQualName());
//				
//				list.add(s);
//			}
//			
//			setValue(list);
//		} else {
//			setValue(null);
//		}
//	}
}
