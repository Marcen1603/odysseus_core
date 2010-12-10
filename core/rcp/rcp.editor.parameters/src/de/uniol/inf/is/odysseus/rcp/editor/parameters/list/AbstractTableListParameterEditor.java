package de.uniol.inf.is.odysseus.rcp.editor.parameters.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.AbstractListParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.SimpleColumnDefinition;

// T Datentyp im ListParameter
// U Datentyp in Tabelle
// V InputTyp des ListParameter
public abstract class AbstractTableListParameterEditor<T, U, V> extends AbstractListParameterEditor<T> implements IParameterEditor {

	private List<U> data;
	private Control titleControl;
	private TableViewer tableViewer;
	private List<TableViewerColumn> tableColumns;
	private List<Button> buttons;
	
	@Override
	public void createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		Layout layout = createLayout();
		composite.setLayout(layout);
		
		setData( load(getValue()) );
		
		setTitleControl( createTitleControl(composite) );
		setTableViewer( createTableViewer(composite) );
		
		getTableViewer().getTable().addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				setValue(save(getData()));
			}
		});
		
		setButtons(createButtons(composite));
	}

	@Override
	public void close() {
		setValue(save(getData()));
	}
	
	protected TableViewer createTableViewer(Composite parent) {
		// Layout für Spalten
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableLayout = createTableColumnLayout();
		tableComposite.setLayout(tableLayout);
		tableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Table
		TableViewer viewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		
		// Columns
		setColumns(createColumns( viewer, tableLayout ));
		
		viewer.setContentProvider(createContentProvider());
		IBaseLabelProvider provider = createLabelProvider();
		if(provider != null ) 
			viewer.setLabelProvider(provider);
		
		viewer.setInput(getData());
		
		return viewer;
	}
	
	protected final TableViewer getTableViewer() {
		return tableViewer;
	}
	
	protected TableColumnLayout createTableColumnLayout() {
		return new TableColumnLayout();
	}
	
	protected List<TableViewerColumn> createColumns( TableViewer viewer, TableColumnLayout columnLayout ) {
		List<SimpleColumnDefinition<U>> defs = createColumnDefinitions();
		
		List<TableViewerColumn> columns = new ArrayList<TableViewerColumn>();
		for( int i = 0; i < defs.size(); i++ ) {
			columns.add( defs.get(i).toTableViewerColumn(viewer, columnLayout) );
		}
		
		return columns;
	}
	
	protected final List<TableViewerColumn> getColumns() {
		return Collections.unmodifiableList(tableColumns);
	}
	
	protected IContentProvider createContentProvider() {
		return ArrayContentProvider.getInstance();
	}
	
	protected IBaseLabelProvider createLabelProvider() {
		return null;
	}
	
	protected Layout createLayout() {
		return new GridLayout();
	}
	
	protected Control createTitleControl(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText(getTitleText());
		return label;
	}

	protected String getTitleText() {
		return getListParameter().getName();
	}
	
	protected final Control getTitleControl() {
		return titleControl;
	}
	
	protected final List<U> getData() {
		return data;
	}
	
	protected List<Button> createButtons( Composite parent ) {
		return null;
	}
	
	protected List<Button> getButtons() {
		return Collections.unmodifiableList(buttons);
	}
	
	public final void addDataItem( U item ) {
		getData().add(item);
	}
	
	public final void removeDataItem( U item ) {
		getData().remove(item);
	}
	
	public final void refresh() {
		getTableViewer().refresh();
		getView().layout();
	}
	
	protected List<U> load(List<T> rawData) {
		List<U> data = new ArrayList<U>();
		
		if( rawData != null ) 
			for( int i = 0; i < rawData.size(); i++ ) {
				data.add(convertFrom(rawData.get(i)));
			}
		
		return data;
	}
	
	protected List<V> save(List<U> data) {
		List<V> list = new ArrayList<V>();
		for( U u : data ) {
			list.add(convertTo(u));
		}
		
		if( list.isEmpty() ) 
			return null;//setValue(null);
		else
			return list;//setValue(list);
	}
	
	@SuppressWarnings("unchecked")
	protected final U getSelection() {
		TableViewer viewer = getTableViewer();
		ISelection selection = viewer.getSelection();
		if( selection != null ) {
			U selectedItem = (U)((IStructuredSelection)selection).getFirstElement();
			if( selectedItem != null ) {
				return selectedItem;
			}
		}
		return null;
	}

	private void setTitleControl( Control control ) {
		if( control == null ) 
			throw new IllegalArgumentException("titleControl is null");
		
		titleControl = control;
	}
	
	private void setData( List<U> data ) {
		if( data == null ) 
			throw new IllegalArgumentException("data is null");
		this.data = data;
	}
	
	private void setTableViewer( TableViewer table ) {
		if( table == null ) 
			throw new IllegalArgumentException("table is null");
		this.tableViewer = table;
	}
	
	private void setColumns( List<TableViewerColumn> columns ) {
		if( columns == null || columns.size() == 0 )
			throw new IllegalArgumentException("columns is invalid");
		this.tableColumns = columns;
	}
	
	private void setButtons( List<Button> buttons ) {
		this.buttons = buttons;
	}
	
	protected abstract U convertFrom( T element );
	protected abstract V convertTo( U element );
	protected abstract List<SimpleColumnDefinition<U>> createColumnDefinitions();
}
