package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

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

	public boolean addToTree(Object object) {
		if(object instanceof StateMachineInstance) {
			// if the object is an instance of StateMachineInstance
			StateMachineInstance<?> instance = (StateMachineInstance<?>) object;
			for(AbstractTreeItem child :  this.root.getChildren()) {
				StateMachine<?> itemContent = ((MachineTreeItem) child).getContent();
				if(itemContent.equals(instance.getStateMachine())) {
					// if the treeItem is the representative of the correct StateMachine
					InstanceTreeItem newItem = new InstanceTreeItem(child, instance);
					child.add(newItem);
					this.getDisplay().asyncExec(new Runnable() {
						public void run() {
							tree.refresh();
						}
					});
					return true;
				}
			}
			MachineTreeItem newMachineItem = new MachineTreeItem(this.root, instance.getStateMachine());
			this.root.add(newMachineItem);
			return this.addToTree(instance);
		} else {
			return false;
		}
	}
	
	public void removeAll() {
		this.root.removeAllChildren();
	}

	public boolean remove(InstanceTreeItem toRemove) {
		for(AbstractTreeItem machineItem : this.root.children) {
			// durchsuche die Rootelemente
			if(toRemove.getContent().getStateMachine().equals(((MachineTreeItem)machineItem).getContent())) {
				// wenn die machinen passen
				for(AbstractTreeItem instanceItem : machineItem.children) {
					// durchsuche die instanzen
					if(toRemove.getContent().equals(((InstanceTreeItem) instanceItem).getContent())) {
						instanceItem = null;
						return true;
					}
				}
			}
		}
		return false;
	}

}
