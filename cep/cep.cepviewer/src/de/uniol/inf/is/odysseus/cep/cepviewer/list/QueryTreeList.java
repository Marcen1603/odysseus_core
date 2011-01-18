package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.MachineTreeItem;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;

/**
 * This class defines the query tree list.
 * 
 * @author Christian
 */
public class QueryTreeList extends AbstractTreeList {

	/**
	 * This is the constructor.
	 * 
	 * @param parent
	 *            is the widget that inherits this tree list.
	 * @param style
	 *            contains the style of this tree list.
	 */
	public QueryTreeList(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * This method adds an object of the class CEPInstance into the TreeViewer.
	 * 
	 * @param instance
	 *            is the instance which should be added
	 */
	public void addToTree(CEPInstance instance) {
		for (AbstractTreeItem child : this.root.getChildren()) {
			StateMachine<?> itemContent = ((MachineTreeItem) child)
					.getContent();
			if (itemContent.equals(instance.getStateMachine())) {
				// if the entry is the representative of the correct
				// StateMachine
				InstanceTreeItem newItem = new InstanceTreeItem(child, instance);
				child.add(newItem);
				this.getDisplay().asyncExec(new Runnable() {
					public void run() {
						tree.refresh();
					}
				});
				return;
			}
		}
		// if no entry for the StateMachine has been found.
		MachineTreeItem newMachineItem = new MachineTreeItem(this.root,
				instance.getStateMachine());
		this.root.add(newMachineItem);
		this.addToTree(instance);
	}

	/**
	 * This method removes one entry from the tree of the TreeViewer.
	 * 
	 * @param toRemove is the item which should be removed
	 * 
	 * @return true if the instance has been found, else false
	 */
	public boolean remove(InstanceTreeItem toRemove) {
		for (AbstractTreeItem machineItem : this.root.getChildren()) {
			if (toRemove.getContent().getStateMachine()
					.equals(((MachineTreeItem) machineItem).getContent())) {
				// search for the MAchineTreeItem for the StateMachine of the
				// instance
				for (AbstractTreeItem instanceItem : machineItem.getChildren()) {
					if (((CEPInstance) toRemove.getContent()).getInstance()
							.equals(((InstanceTreeItem) instanceItem)
									.getContent().getInstance())) {
						// if the instance has been found remove it
						machineItem.getChildren().remove(instanceItem);
						instanceItem.setParent(null);
						this.tree.refresh();
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * This method removes all entries of an CepOperator from the tree of the TreeViewer.
	 * 
	 * @param toRemove is the item which should be removed
	 * 
	 * @return true if the MachineTreeItem has been removed, else false
	 */
	public boolean remove(MachineTreeItem toRemove) {
		MachineTreeItem removeItem = (MachineTreeItem) toRemove;
		for (AbstractTreeItem machineItem : this.root.getChildren()) {
			// durchsuche die Rootelemente
			if (removeItem.getContent().equals(
					((MachineTreeItem) machineItem).getContent())) {
				// wenn die machinen passen
				this.root.getChildren().remove(machineItem);
				machineItem.setParent(null);
				this.tree.refresh();
				return true;
			}
		}
		return false;
	}

	/**
	 * This method removes all entries from the tree of the TreeViewer.
	 */
	public void removeAll() {
		this.root = new LabelTreeItem(null, "Root");
		this.tree.setInput(this.root.getChildren());
		this.tree.refresh();
	}

}
