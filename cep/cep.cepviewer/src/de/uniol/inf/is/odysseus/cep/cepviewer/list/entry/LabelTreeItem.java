package de.uniol.inf.is.odysseus.cep.cepviewer.list.entry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.cep.cepviewer.Activator;

public class LabelTreeItem extends AbstractTreeItem {

	private Image image;
	
	public LabelTreeItem(AbstractTreeItem parent, String name) {
		super(parent);
		this.name = name;
	}
	
	public String toString() {
		return this.name + " (Total " + this.getChildren().size() + ")";
	}
	
	public void setImage(String path){
		Bundle bundle = Activator.getDefault().getBundle();
		this.image = ImageDescriptor.createFromURL(bundle.getEntry(path)).createImage();
	}

	public Image getImage() {
		return image;
	}

}
