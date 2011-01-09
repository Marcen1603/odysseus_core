package de.uniol.inf.is.odysseus.cep.cepviewer.list.entry;

import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

public class InstanceTreeItem extends AbstractTreeItem {
	
	public InstanceTreeItem(AbstractTreeItem parent, CEPInstance instance) {
		super(parent);
		this.name = Integer.toString(instance.getInstance().hashCode());
		this.content = instance;
	}
	
	public CEPInstance getContent() {
		return (CEPInstance) this.content;
	}
	
	public String toString() {
		return StringConst.TREE_ITEM_INSTANCE_LABEL + this.name;
	}

}
