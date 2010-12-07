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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.AbstractListParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.ComboEditingSupport;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.TextEditingSupport;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class AggregationsParameterEditor extends AbstractListParameterEditor<AggregateItem> {

	private static final String[] AGG_FUNCTIONS = {
		"AVG", "MAX", "MIN", "SUM", "COUNT"
	};
	
	private class AggItem {
		public String aggregationFunction;
		public String inputAttribute;
		public String outputAttribute;
		
		public AggItem( String input, String aggregation, String output) {
			inputAttribute = input;
			aggregationFunction = aggregation;
			outputAttribute = output;
		}
	}
	
	private List<AggItem> aggregations = new ArrayList<AggItem>();
	private TableViewer tableViewer;
	private SDFAttributeList schema;

	@Override
	public void createControl(Composite parent) {

		GridLayout gridLayout = new GridLayout();
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(gridLayout);

		Label titleLabel = new Label(container, SWT.NONE);
		titleLabel.setText(getParameter().getName());
		titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		if( loadSchema() ) {
			load();
			
			createTable(container);
			createButtons(container);
		} else {
			Label label = new Label(container, SWT.NONE);
			label.setText("No input-operator defined");
			label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}
	}
	
	private void createButtons(Composite container) {
		Composite buttonContainer = new Composite(container, SWT.NONE);
		buttonContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		buttonContainer.setLayout(gridLayout);
		
		Button addButton = new Button( buttonContainer, SWT.PUSH);
		addButton.setImage( Activator.getDefault().getImageRegistry().get("addIcon"));
		addButton.setToolTipText("Add aggregation");
		addButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewRow();
				tableViewer.refresh();
				getView().layout();
				
				save();
			}
		});
		
		Button removeButton = new Button( buttonContainer, SWT.PUSH );
		removeButton.setImage( Activator.getDefault().getImageRegistry().get("removeIcon"));
		removeButton.setToolTipText("Remove aggregation");
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
				AggItem item = (AggItem)selection.getFirstElement();
				
				aggregations.remove(item);
				
				// neu zeichnen
				tableViewer.refresh();
				getView().layout();
				
				save();
			}
		});
		
	}

	private boolean loadSchema() {
		// Schema des vorgängeroperators holen
		AbstractOperatorBuilder builder = (AbstractOperatorBuilder)getOperatorBuilder();
		
		if( builder.hasInputOperator(0) ) {
			schema = builder.getInputOperator(0).getOutputSchema();
			return true;
		}
		
		schema = null;
		return false;
	}

	// Läd die Daten aus dem Parameter
	private void load() {
		List<AggregateItem> list = getValue();

		if (list != null) {
			for( AggregateItem i : list) {
				aggregations.add(new AggItem(i.inAttribute.getAttributeName(), i.aggregateFunction.getName(), i.outAttribute.getAttributeName()));
			}
		}
	}
	
	private void save() {
		ArrayList<ArrayList<String>> valList = new ArrayList<ArrayList<String>>();
		
		for( AggItem aggItem : aggregations) {
			ArrayList<String> strings = new ArrayList<String>();
			strings.add(aggItem.aggregationFunction);
			strings.add(aggItem.inputAttribute);
			strings.add(aggItem.outputAttribute);
			
			valList.add(strings);
		}
		
		setValue(valList);
	}

	private void createTable(Composite parent) {
		// Layout
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableComposite.setLayout(tableLayout);
		tableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Tabelle erzeugen
		tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		// Spalte: Eingangsattribut
		TableViewerColumn originalNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		originalNameColumn.getColumn().setText("Input");
		tableLayout.setColumnData(originalNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		originalNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				AggItem item = (AggItem) cell.getElement();
				cell.setText(item.inputAttribute);
			}
		});
		originalNameColumn.setEditingSupport(new ComboEditingSupport(tableViewer, getAttributeList()) {

			@Override
			protected void doSetValue(Object element, Object value) {
				((AggItem)element).inputAttribute = value.toString();
				save();
			}

			@Override
			protected Object doGetValue(Object element) {
				return ((AggItem)element).inputAttribute;
			}
			
		});
		
		// Spalte: Aggregationstyp
		TableViewerColumn aggregationColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		aggregationColumn.getColumn().setText("Function");
		tableLayout.setColumnData(aggregationColumn.getColumn(), new ColumnWeightData(5, 25, true));
		aggregationColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				AggItem item = (AggItem) cell.getElement();
				cell.setText(item.aggregationFunction);
			}
		});
		aggregationColumn.setEditingSupport(new ComboEditingSupport(tableViewer, AGG_FUNCTIONS) {

			@Override
			protected void doSetValue(Object element, Object value) {
				((AggItem)element).aggregationFunction = value.toString();
				save();
			}

			@Override
			protected Object doGetValue(Object element) {
				return ((AggItem)element).aggregationFunction;
			}
			
		});

		// Spalte: Ausgangsattribut
		TableViewerColumn outputColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		outputColumn.getColumn().setText("Output");
		tableLayout.setColumnData(outputColumn.getColumn(), new ColumnWeightData(5, 25, true));
		outputColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				AggItem item = (AggItem) cell.getElement();
				cell.setText(item.outputAttribute);
			}
		});
		outputColumn.setEditingSupport(new TextEditingSupport(tableViewer) {

			@Override
			protected void doSetValue(Object element, Object value) {
				((AggItem)element).outputAttribute = value.toString();
				save();
			}

			@Override
			protected Object doGetValue(Object element) {
				return ((AggItem)element).outputAttribute;
			}
			
		});

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(aggregations);

	}
	
	private void addNewRow() {
		aggregations.add( new AggItem(schema.getAttribute(0).getAttributeName(), AGG_FUNCTIONS[0], schema.getAttribute(0).getAttributeName() + "_MAX"));
	}
	
	private String[] getAttributeList() {
		String[] list = new String[schema.getAttributeCount()];
		for( int i = 0; i < schema.getAttributeCount(); i++) {
			list[i]= schema.getAttribute(i).getAttributeName(); 
		}
		return list;
	}

	@Override
	public void close() {
		save();
	}
}
