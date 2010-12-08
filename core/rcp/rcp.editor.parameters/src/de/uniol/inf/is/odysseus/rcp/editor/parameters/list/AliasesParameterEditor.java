package de.uniol.inf.is.odysseus.rcp.editor.parameters.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.AbstractListParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.AliasChangeWindow;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class AliasesParameterEditor extends AbstractListParameterEditor<String> implements IParameterEditor {
	
	private Map<String, String> aliases;
	private SDFAttributeList schema;
	
	public AliasesParameterEditor() {
	}

	@Override
	public void createControl(Composite parent) {
		
		GridLayout layout = new GridLayout();
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		
		AbstractOperatorBuilder builder = (AbstractOperatorBuilder)getOperatorBuilder();
		if( builder.hasInputOperator(0) ) {
			schema = builder.getInputOperator(0).getOutputSchema();
			if( schema != null && schema.getAttributeCount() > 0 ) {
				
				// Aliases laden
				aliases = load(schema, getValue());
				
				// Tabelle anlegen
				TableViewer viewer = createAliasesTable(container, aliases);
				
				// Buttons
				createButton( container, viewer, aliases );
				
				// gleich abspeichern
				save();
				
			} else {
				Label label = new Label(container, SWT.NONE);
				label.setText("Input-operator has no or empty attribute-schema");
				label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
			}
		} else {
			Label label = new Label(container, SWT.NONE);
			label.setText("No input-operator defined");
			label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}
	}
	
	// erstellt die Buttons
	private void createButton(Composite container, final TableViewer viewer, final Map<String, String> aliases) {
		
		final Button button = new Button(container, SWT.PUSH);
		button.setText("Change");
		button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		button.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// Liste der ungültigen Namen erstellen (damit keine Namen doppeldeutig werden)
				List<String> invalidNames = new ArrayList<String>();
				invalidNames.addAll(aliases.values());
				
				// Fenster zum Editieren des Aliases aufrufen
				IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
				Entry<String, String> sel = (Entry<String, String>)selection.getFirstElement();
				AliasChangeWindow wnd = new AliasChangeWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), sel, invalidNames, viewer);
				wnd.show(); // Die Alias-Änderung geschieht da drin :-)
			}
		});
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				button.setEnabled(viewer.getSelection() != null);
			}
		});
	}

	// läd die Aliases aus dem Operator
	private Map<String, String> load(SDFAttributeList schema, List<String> value) {
		
		HashMap<String, String> aliases = new HashMap<String, String>();
		
		for( int i = 0; i < schema.getAttributeCount(); i++) {
			SDFAttribute attribute = schema.getAttribute(i);
			String attributeName = attribute.getAttributeName();
			
			// Wenn RenameOperator einen Wert bereits besitzt, dann diesen
			// verwenden, ansonsten einen Standardwert erzeugen (der Rename 
			// beinhaltet die Identität)
			aliases.put(attributeName, value != null && value.size() > i ? value.get(i) : attributeName);
		}
		
		return aliases;
	}
	
	// Tabelle erzeugen
	private TableViewer createAliasesTable( Composite parent, Map<String, String> aliases ) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableComposite.setLayout(tableLayout);
		tableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Tabelle erzeugen
		TableViewer tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		// Spalte: Originalname
		TableViewerColumn originalNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		originalNameColumn.getColumn().setText("Original name");
		tableLayout.setColumnData( originalNameColumn.getColumn(), new ColumnWeightData(5,25,true));
		originalNameColumn.setLabelProvider(new CellLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public void update(ViewerCell cell) {
				Entry<String,String> entry = (Entry<String, String>)cell.getElement();
				cell.setText(entry.getKey());
			}
		});

		// Spalte: Neuer Name
		TableViewerColumn newNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		newNameColumn.getColumn().setText("New name");
		tableLayout.setColumnData( newNameColumn.getColumn(), new ColumnWeightData(5,25,true));
		newNameColumn.setLabelProvider(new CellLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public void update(ViewerCell cell) {
				Entry<String,String> entry = (Entry<String, String>)cell.getElement();
				cell.setText(entry.getValue());
			}
		});

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(aliases.entrySet());

		return tableViewer;
	}

	@Override
	public void close() {
		save();
	}

	// Speichert die Aliases zurück im ListParameter der OperatorBuilders
	private void save() {
		if( aliases != null && schema != null ) {
			
			// Es muss die Reihenfolge beachtet werden, die Map
			// garantiert das nicht, daher wird explizit
			// eine neue Liste erzeugt
			List<String> list = new ArrayList<String>();
			for( int i = 0; i < schema.getAttributeCount(); i++ ) {
				list.add( aliases.get(schema.getAttribute(i).getAttributeName()));
			}
			setValue(list);
		}
		else
			setValue(null);
	}

}
