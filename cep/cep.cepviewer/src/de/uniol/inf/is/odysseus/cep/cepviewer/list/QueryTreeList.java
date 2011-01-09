package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.MachineTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPStatus;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
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

	public void addToTree(CEPInstance instance) {
		for (AbstractTreeItem child : this.root.getChildren()) {
			StateMachine<?> itemContent = ((MachineTreeItem) child)
					.getContent();
			if (itemContent.equals(instance.getStateMachine())) {
				// if the treeItem is the representative of the correct
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
		MachineTreeItem newMachineItem = new MachineTreeItem(this.root,
				instance.getStateMachine());
		this.root.add(newMachineItem);
		this.addToTree(instance);
	}

	public void removeAll() {
		this.root = new LabelTreeItem(null, "Root");
		this.tree.setInput(this.root);
		this.tree.refresh();
	}

	public boolean remove(AbstractTreeItem toRemove) {
		if (toRemove instanceof InstanceTreeItem) {
			InstanceTreeItem removeItem = (InstanceTreeItem) toRemove;
			for (AbstractTreeItem machineItem : this.root.getChildren()) {
				System.out.println("Query remove: 1");
				// durchsuche die Rootelemente
				if (removeItem.getContent().getStateMachine()
						.equals(((MachineTreeItem) machineItem).getContent())) {
					System.out.println("Query remove: 2");
					// wenn die machinen passen
					for (AbstractTreeItem instanceItem : machineItem
							.getChildren()) {
						System.out.println("Query remove: 3");
						// durchsuche die instanzen
						if (((CEPInstance) toRemove.getContent()).getInstance()
								.equals(((InstanceTreeItem) instanceItem)
										.getContent().getInstance())) {
							System.out.println("Query remove: 4");
							machineItem.getChildren().remove(instanceItem);
							instanceItem.setParent(null);
							this.tree.refresh();
							return true;
						}
					}
				}
			}
		} else if (toRemove instanceof MachineTreeItem) {
			System.out.println("QueryList: remove Machine 1");
			MachineTreeItem removeItem = (MachineTreeItem) toRemove;
			for (AbstractTreeItem machineItem : this.root.getChildren()) {
				System.out.println("QueryList: remove Machine 2");
				// durchsuche die Rootelemente
				if (removeItem.getContent().equals(
						((MachineTreeItem) machineItem).getContent())) {
					System.out.println("QueryList: remove Machine 2");
					// wenn die machinen passen
					this.root.getChildren().remove(machineItem);
					machineItem.setParent(null);
					this.tree.refresh();
					return true;
				}
			}
		}
		return false;
	}

	public void stateChanged(StateMachineInstance<?> instance) {
		for (AbstractTreeItem machineItem : this.root.getChildren()) {
			if (instance.getStateMachine().equals(machineItem.getContent())) {
				for (AbstractTreeItem instanceItem : machineItem.getChildren()) {
					if (instance.equals(((CEPInstance) instanceItem
							.getContent()).getInstance())) {
						CEPInstance cepInstance = (CEPInstance) instanceItem
								.getContent();
						cepInstance.currentStateChanged();
						if (cepInstance.getCurrentState().getState()
								.isAccepting()) {
							cepInstance.setStatus(CEPStatus.FINISHED);
							return;
						}
					}
				}
			}
		}
	}

	public void statusChanged(StateMachineInstance<?> instance,
			CEPStatus newStatus) {
	}

}
