package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;

public class MachineTreeItem extends AbstractTreeItem {

	private StateMachine<?> content;
	
	public MachineTreeItem(AbstractTreeItem parent, StateMachine<?> machine) {
		super(parent);
		this.name = Integer.toString(machine.hashCode());
		this.content = machine;
	}
	
	public StateMachine<?> getContent() {
		return this.content;
	}
	
	public String toString() {
		return StringConst.TREE_ITEM_MACHINE_LABEL + this.name + " (Total " + this.getChildren().size() + ")"; 
	}

}
