package de.uniol.inf.is.odysseus.cep.cepviewer.listmodel;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.cep.cepviewer.Activator;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

/**
 * This class extends the class AbstractTreeItem and represents an Label within
 * the TreeViewer.
 * 
 * @author Christian
 */
public class LabelTreeItem extends AbstractTreeItem {

	// the status image this label represents
	private Image image;

	/**
	 * The constructor of this class.
	 * 
	 * @param parent
	 *            is the parent entry
	 * @param name
	 *            the text of this label
	 */
	public LabelTreeItem(AbstractTreeItem parent, String name) {
		super(parent);
		this.name = name;
	}

	/**
	 * This method returns a String which represents the text of this entry.
	 * 
	 * @return the text of this entry
	 */
	public String toString() {
		return StringConst.LABEL_TREE_ITEM_TEXT.replaceFirst(
				StringConst.WILDCARD, this.name)
				.replaceFirst(StringConst.WILDCARD,
						String.valueOf(this.getChildren().size()));
	}

	/**
	 * The getter for the image of this entry.
	 * 
	 * @return the image of this entry
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * This method sets the image of this entry
	 * 
	 * @param path
	 *            the relative path to the image
	 */
	public void setImage(String path) {
		Bundle bundle = Activator.getDefault().getBundle();
		this.image = ImageDescriptor.createFromURL(bundle.getEntry(path))
				.createImage();
	}

}
