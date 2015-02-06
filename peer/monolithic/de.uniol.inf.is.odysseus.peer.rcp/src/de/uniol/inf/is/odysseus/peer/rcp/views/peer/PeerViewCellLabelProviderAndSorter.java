package de.uniol.inf.is.odysseus.peer.rcp.views.peer;

import net.jxta.peer.PeerID;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.peer.rcp.views.ColumnViewerSorter;

public abstract class PeerViewCellLabelProviderAndSorter<T> extends CellLabelProvider {

	private ColumnViewerSorter sorter;
	
	public PeerViewCellLabelProviderAndSorter(TableViewer tableViewer, TableViewerColumn column) {
		sorter = new ColumnViewerSorter(tableViewer, column) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				T val1 = getValue((PeerID) e1);
				T val2 = getValue((PeerID) e2);
				
				return compareValuesImpl(val1, val2); 
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);
	}
	
	@Override
	public void update(ViewerCell cell) {
		PeerID pid = (PeerID) cell.getElement();
		T value = getValue(pid);
		if( value == null ) {
			cell.setText("");
		} else {
			cell.setText(toString(value));
			Image img = getImage( value );
			if( img != null ) {
				cell.setImage(img);
			}
		}
		
		if (isLocalID(pid)) {
			cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		

	}
	
	private static boolean isLocalID(PeerID pid) {
		return RCPP2PNewPlugIn.getP2PNetworkManager().getLocalPeerID().equals(pid);
	}

	protected abstract T getValue( PeerID pid );
	
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
