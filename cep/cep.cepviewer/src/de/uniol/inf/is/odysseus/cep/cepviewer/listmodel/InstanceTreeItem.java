package de.uniol.inf.is.odysseus.cep.cepviewer.listmodel;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

/**
 * This class extends the class AbstractTreeItem and represents an CEPInstance
 * within the TreeViewer.
 * 
 * @author Christian
 */
public class InstanceTreeItem extends AbstractTreeItem {
	
	/**
	 * The constructor of this class.
	 * 
	 * @param parent
	 *            is the parent entry
	 * @param instance
	 *            the represented CEPInstance object
	 */
	public InstanceTreeItem(AbstractTreeItem parent, CEPInstance instance) {
		super(parent);
		this.name = Integer.toString(instance.getInstance().hashCode());
		this.content = instance;
	}
	
	/**
	 * This method overrides the getContent() method of the class
	 * AbstractTreeItem and return the CEPInstance object.
	 * 
	 * @return the StateMachine object
	 */
	public CEPInstance getContent() {
		return (CEPInstance) this.content;
	}
	
	/**
	 * This method returns a String which represents the text of this entry.
	 * 
	 * @return the text of this entry
	 */
	public String toString() {
		return StringConst.TREE_ITEM_INSTANCE_LABEL + this.name;
	}

}
