package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.cep.cepviewer.Activator;

public abstract class AbstractTreeItem {
	
	protected String name;
	protected Image image;
	protected AbstractTreeItem parent;
	protected ArrayList<AbstractTreeItem> children;

	public AbstractTreeItem(AbstractTreeItem parent) {
		this.parent = parent;
		this.children = new ArrayList<AbstractTreeItem>();
	}
	
	public void add(AbstractTreeItem item) {
		this.children.add(item);
	}
	
	public void setImage(String path){
		Bundle bundle = Activator.getDefault().getBundle();
		this.image = ImageDescriptor.createFromURL(bundle.getEntry(path)).createImage();
	}
	
	public void removeAllChildren(){
		this.children = new ArrayList<AbstractTreeItem>();
	}
	
	public abstract String toString();
	
	public String getName() {
		return this.name;
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public AbstractTreeItem getParent() {
		return this.parent;
	}
	
	public ArrayList<AbstractTreeItem> getChildren() {
		return this.children;
	}
	
}
