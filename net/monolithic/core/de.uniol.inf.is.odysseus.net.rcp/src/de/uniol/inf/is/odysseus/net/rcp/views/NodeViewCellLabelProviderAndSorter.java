package de.uniol.inf.is.odysseus.net.rcp.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.rcp.util.CellLabelProviderAndSorter;

public abstract class NodeViewCellLabelProviderAndSorter<T> extends CellLabelProviderAndSorter<IOdysseusNode, T> {

	public NodeViewCellLabelProviderAndSorter(TableViewer tableViewer, TableViewerColumn column) {
		super(tableViewer, column);
	}
}
