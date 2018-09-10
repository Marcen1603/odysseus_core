package de.uniol.inf.is.odysseus.rcp.logging.view.util;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;

public abstract class CellLabelProviderAndSorter<V, T> extends CellLabelProvider {

	private ColumnViewerSorter sorter;
	
	public CellLabelProviderAndSorter(TableViewer tableViewer, TableViewerColumn column) {
		sorter = new ColumnViewerSorter(tableViewer, column) {
			@SuppressWarnings("unchecked")
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				T val1 = getValue((V) e1);
				T val2 = getValue((V) e2);
				
				return compareValuesImpl(val1, val2); 
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update(ViewerCell cell) {
		V entry = (V) cell.getElement();
		T value = getValue(entry);
		if( value == null ) {
			cell.setText("");
		} else {
			cell.setText(toString(value));
			Image img = getImage( value );
			if( img != null ) {
				cell.setImage(img);
			}
		}
	}
	
	protected abstract T getValue( V entry );
	
	private int compareValuesImpl( T a, T b ) {
		if (a == null && b == null ) {
			return 0;
		} else if (a == null) {
			return 1;
		} else if (b == null) {
			return -1;
		}

		return compareValues(a, b);
	}
	
	protected int compareValues( T a, T b ) {
		return a.toString().compareTo(b.toString());
	}
	
	protected String toString( T value ) {
		return value.toString();
	}
	
	protected Image getImage( T value ) {
		return null;
	}
}
