package de.uniol.inf.is.odysseus.cep.cepviewer.list.entry;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TreeLabelProvider extends LabelProvider {

	public Image getImage(Object object) {
		if (object instanceof InstanceTreeItem) {
			InstanceTreeItem instance = (InstanceTreeItem) object;
			return instance.getContent().getImage();
		} else if (object instanceof LabelTreeItem) {
			LabelTreeItem instance = (LabelTreeItem) object;
			return instance.getImage();
		}
		return null;
	}

	public String getText(Object object) {
		if (object instanceof AbstractTreeItem) {
			AbstractTreeItem item = (AbstractTreeItem) object;
			return item.toString();
		}
		return null;
	}
}