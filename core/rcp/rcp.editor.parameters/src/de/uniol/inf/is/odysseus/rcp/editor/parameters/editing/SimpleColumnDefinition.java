package de.uniol.inf.is.odysseus.rcp.editor.parameters.editing;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;


public abstract class SimpleColumnDefinition<T> {

	private String title;
	
	public SimpleColumnDefinition( String title ) {
		setTitle(title);
	}
	
	public final String getTitle() {
		return title;
	}
	
	public final TableViewerColumn toTableViewerColumn( TableViewer viewer, TableColumnLayout layout ) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText(getTitle());
		layout.setColumnData(column.getColumn(), new ColumnWeightData(5, getWeight(), true));
		
		column.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				@SuppressWarnings("unchecked")
				T item = (T) cell.getElement();
				cell.setText(getStringValue(item));
			}
			
		});
		
		AbstractEditingSupport edSupport = createEditingSupport(viewer);
		if( edSupport != null )
			column.setEditingSupport(edSupport);
		
		return column;
	}
	
	protected int getWeight() {
		return 25;
	}
	
	protected AbstractEditingSupport createEditingSupport(TableViewer viewer) {
		return null; // no editing
	}
	
	private void setTitle( String title ) {
		if( title == null || title.length() == 0 )
			throw new IllegalArgumentException("title is invalid");
		this.title = title;
	}
	
	protected abstract String getStringValue( T element );

}
