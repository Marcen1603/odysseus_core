package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class TreeContentProvider extends ArrayContentProvider implements
		ITreeContentProvider {

	public Object[] getChildren(Object parent) {
		AbstractTreeItem item = (AbstractTreeItem) parent;
		return item.getChildren().toArray();
	}

	public Object getParent(Object element) {
		AbstractTreeItem item = (AbstractTreeItem) element;
		return item.parent;
	}

	public boolean hasChildren(Object element) {
		AbstractTreeItem item = (AbstractTreeItem) element;
		return !item.getChildren().isEmpty();
	}
}