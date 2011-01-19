package de.uniol.inf.is.odysseus.cep.cepviewer.listmodel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * This is the LabelProvider of the TreeViewer within the lists of the
 * CEPListView.
 * 
 * @author Christian
 */
public class TreeLabelProvider extends LabelProvider {

	/**
	 * This method returns the image of the entry or null if the entry is not an
	 * instance of the classes InstanceTreeItem or LabelTreeItem.
	 * 
	 * @param object is the entry to get the image from.
	 * 
	 * @return the image of the given entry
	 */
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

	/**
	 * This method returns the name of the entry or null if the entry is not an
	 * instance of the class AbstractTreeItem.
	 * 
	 * @param object is the entry to get the name from.
	 * 
	 * @return the name of the given entry
	 */
	public String getText(Object object) {
		if (object instanceof AbstractTreeItem) {
			AbstractTreeItem item = (AbstractTreeItem) object;
			return item.toString();
		}
		return null;
	}
}