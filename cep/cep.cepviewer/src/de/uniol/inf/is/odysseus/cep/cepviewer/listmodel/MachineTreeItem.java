package de.uniol.inf.is.odysseus.cep.cepviewer.listmodel;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;

/**
 * This class extends the class AbstractTreeItem and represents an StateMachine
 * within the TreeViewer.
 * 
 * @author Christian
 */
public class MachineTreeItem extends AbstractTreeItem {

	/**
	 * The constructor of this class.
	 * 
	 * @param parent
	 *            is the parent entry
	 * @param machine
	 *            the represented StateMachine object
	 */
	public MachineTreeItem(AbstractTreeItem parent, StateMachine<?> machine) {
		super(parent);
		this.name = Integer.toString(machine.hashCode());
		this.content = machine;
	}

	/**
	 * This method overrides the getContent() method of the class
	 * AbstractTreeItem and return the StateMachine object.
	 * 
	 * @return the StateMachine object
	 */
	public StateMachine<?> getContent() {
		return (StateMachine<?>) this.content;
	}

	/**
	 * This method returns a String which represents the text of this entry.
	 * 
	 * @return the text of this entry
	 */
	public String toString() {
		return StringConst.TREE_ITEM_MACHINE_LABEL + this.name + " (Total "
				+ this.getChildren().size() + ")";
	}

}
