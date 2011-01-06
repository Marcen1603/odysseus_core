package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

public class InstanceTreeItem extends AbstractTreeItem {

	private StateMachineInstance<?> content;
	
	public InstanceTreeItem(AbstractTreeItem parent, StateMachineInstance<?> instance) {
		super(parent);
		this.name = Integer.toString(instance.hashCode());
		this.content = instance;
		if (this.content.getCurrentState().isAccepting()) {
			this.setImage(StringConst.PATH_TO_FINISHED_IMAGE);
		} else if (!this.content.getCurrentState().isAccepting()) {
			this.setImage(StringConst.PATH_TO_RUNNING_IMAGE);
		} // TODO: else if (Instanze wurde abgebrochen) {}
	}
	
	public StateMachineInstance<?> getContent() {
		return this.content;
	}
	
	public String toString() {
		return StringConst.TREE_ITEM_INSTANCE_LABEL + this.name;
	}

}
