package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.AbstractListParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.activator.Activator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class SchemaParameterEditor extends AbstractListParameterEditor<SDFAttribute> {

	private static final String SCHEMA_TEXT = "SCHEMA";
	private static final String NAME_TEXT = "Name";
	private static final String TYPE_TEXT = "Type";
	private static final String ADD_TEXT = "Add attribute";
	private static final String REMOVE_TEXT = "Remove attribute";
	
	private TableViewer tableViewer;
	
	@Override
	public void createControl(final Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		
		Text schemaLabel = new Text(parent, SWT.CENTER);
		schemaLabel.setText(SCHEMA_TEXT);
		schemaLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		schemaLabel.setLayoutData(data);
		
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableComposite.setLayout(tableLayout);
		data = new GridData( GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		tableComposite.setLayoutData(data);
		
		tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		// Name
		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		nameColumn.getColumn().setText(NAME_TEXT);
		tableLayout.setColumnData( nameColumn.getColumn(), new ColumnWeightData(5,25,true));
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				SDFAttribute attr = ((SDFAttribute)cell.getElement());
				cell.setText(attr.getSourceName() + "." + attr.getAttributeName());
			}
		});
		
		// Type
		TableViewerColumn typeColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		typeColumn.getColumn().setText(TYPE_TEXT);
		tableLayout.setColumnData( typeColumn.getColumn(), new ColumnWeightData(5,25,true));
		typeColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				SDFAttribute attr = ((SDFAttribute)cell.getElement());
				cell.setText(attr.getDatatype().getURIWithoutQualName());
			}
		});
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		Button addButton = new Button( parent, SWT.PUSH);
		addButton.setImage( Activator.getDefault().getImageRegistry().get("addIcon"));
		addButton.setToolTipText(ADD_TEXT);
		addButton.addSelectionListener( new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Liste aller Namen holen
				List<String> names = new ArrayList<String>();
				for( SDFAttribute a : ((List<SDFAttribute>)tableViewer.getInput()) ) {
					names.add(a.getSourceName() + "." + a.getURIWithoutQualName());
				}
				
				// Dialog starten
				final AddAttributeWindow attWnd = new AddAttributeWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), names);
						
				attWnd.show(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent e) {
						
						// erstelltes Attribut vom Fenster holen und ggfs. einsetzen
						SDFAttribute att = attWnd.getAttribute();
						if( att != null ) {
							List<SDFAttribute> attributes = (List<SDFAttribute>)tableViewer.getInput();
							attributes.add(att);
							
							// neu zeichnen
							tableViewer.refresh();
							getView().layout();
							
							// speichern
							saveTable();
						}
					}
				});
			}
		});
		
		Button removeButton = new Button( parent, SWT.PUSH );
		removeButton.setImage( Activator.getDefault().getImageRegistry().get("removeIcon"));
		removeButton.setToolTipText(REMOVE_TEXT);
		removeButton.addSelectionListener(new SelectionAdapter() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				// selection ist ein SDFAttribute
				SDFAttribute selection = getSelection();
				
				if( selection != null ) {
					// aus der Liste entfernen
					List<SDFAttribute> attributes = (List<SDFAttribute>)tableViewer.getInput();
					attributes.remove(selection);
					
					// neu zeichnen
					tableViewer.refresh();
					getView().layout();
					
					// speichern
					saveTable();
				}
			}
		});
		
		loadTable();
	}

	@Override
	public void close() {
		saveTable();
	}
	
	private SDFAttribute getSelection() {
		IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
		return (SDFAttribute)selection.getFirstElement();
	}

	private void loadTable() {
		List<SDFAttribute> attributes = getValue();
		if( attributes == null ) 
			attributes = new ArrayList<SDFAttribute>();
				
		tableViewer.setInput(attributes);
		
		tableViewer.getTable().getParent().layout();
	}
	
	@SuppressWarnings("unchecked")
	private void saveTable() {
		List<SDFAttribute> attributes = (List<SDFAttribute>)tableViewer.getInput();
		if( attributes.size() != 0 ) {
		
			ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
			
			for( SDFAttribute a : attributes ) {
				ArrayList<String> s = new ArrayList<String>();
				s.add(a.getSourceName() + "." + a.getAttributeName());
				s.add(a.getDatatype().getURIWithoutQualName());
				
				list.add(s);
			}
			
			setValue(list);
		} else {
			setValue(null);
		}
	}
}
