package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

public class TreeLabelProvider extends LabelProvider {
	
	public Image getImage(Object object) {
		if(object instanceof InstanceTreeItem) {
			InstanceTreeItem instance = (InstanceTreeItem) object;
			return instance.getContent().getImage();
		} else if(object instanceof LabelTreeItem) {
			LabelTreeItem instance = (LabelTreeItem) object;
			return instance.getImage();
		}
		return null;
	}

	public String getText(Object object) {
		if(object instanceof InstanceTreeItem) {
			InstanceTreeItem item = (InstanceTreeItem) object;
			return item.toString() + "(" + StringConst.TREE_ITEM_MACHINE_LABEL + Integer.toString(item.getContent().getStateMachine().hashCode()) + ")";
		} else {
			AbstractTreeItem item = (AbstractTreeItem) object;
			return item.toString();
		}
	}
}