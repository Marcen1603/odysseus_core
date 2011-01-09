package de.uniol.inf.is.odysseus.cep.cepviewer.list.entry;

import java.util.ArrayList;

public abstract class AbstractTreeItem {
	
	protected String name;
	protected AbstractTreeItem parent;
	protected ArrayList<AbstractTreeItem> children;
	protected Object content;

	public AbstractTreeItem(AbstractTreeItem parent) {
		this.parent = parent;
		this.children = new ArrayList<AbstractTreeItem>();
	}
	
	public void add(AbstractTreeItem item) {
		this.children.add(item);
	}
	
	public void removeAllChildren(){
		this.children = new ArrayList<AbstractTreeItem>();
	}
	
	public abstract String toString();
	
	public String getName() {
		return this.name;
	}
	
	public AbstractTreeItem getParent() {
		return this.parent;
	}
	
	public void setParent(AbstractTreeItem newParent) {
		this.parent = newParent;
	}
	
	public ArrayList<AbstractTreeItem> getChildren() {
		return this.children;
	}
	
	public Object getContent() {
		return this.content;
	}
	
}
