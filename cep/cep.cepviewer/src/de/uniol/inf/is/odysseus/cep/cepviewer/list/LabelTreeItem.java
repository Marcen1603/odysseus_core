package de.uniol.inf.is.odysseus.cep.cepviewer.list;

public class LabelTreeItem extends AbstractTreeItem {

	public LabelTreeItem(AbstractTreeItem parent, String name) {
		super(parent);
		this.name = name;
	}
	
	public String toString() {
		return this.name + " (Total " + this.getChildren().size() + ")";
	}

}
