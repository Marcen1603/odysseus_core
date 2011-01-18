package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.MachineTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.IntConst;

/**
 * This class defines the normal tree list.
 * 
 * @author Christian
 */
public class NormalTreeList extends AbstractTreeList {

	/**
	 * This is the constructor.
	 * 
	 * @param parent
	 *            is the widget that inherits this tree list.
	 * @param style
	 *            contains the style of this tree list.
	 */
	public NormalTreeList(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * This method adds an object of the class CEPInstance into the TreeViewer.
	 * 
	 * @param instance
	 *            is the instance which should be added
	 */
	public void addToTree(CEPInstance instance) {
		InstanceTreeItem newItem = new InstanceTreeItem(this.root, instance);
		this.root.add(newItem);
		this.getDisplay().asyncExec(new Runnable() {
			public void run() {
				tree.refresh();
			}
		});
	}

	/**
	 * This method return the total number of entries within the tree of the
	 * TreeViewer.
	 * 
	 * @return the number of entries within the TreeViewer
	 */
	public int getItemCount() {
		return this.root.getChildren().size();
	}

	/**
	 * This method checks if the maximal number of entries within the tree of
	 * the TreeViewer has been reached.
	 * 
	 * @return true if the maximal number has been reached, else false
	 */
	public boolean isFull() {
		if (this.getItemCount() >= IntConst.MAX_LIST_ENTRIES) {
			return true;
		}
		return false;
	}

	/**
	 * This method removes one entry from the tree of the TreeViewer.
	 * 
	 * @param toRemove is the item which should be removed
	 * 
	 * @return true if the instance has been found, else false
	 */
	public boolean remove(InstanceTreeItem toRemove) {
		for (AbstractTreeItem instanceItem : this.root.getChildren()) {
			if (toRemove.getContent().equals(instanceItem.getContent())) {
				this.root.getChildren().remove(instanceItem);
				instanceItem.setParent(null);
				this.tree.refresh();
				return true;
			}
		}
		return false;
	}

	/**
	 * This method removes all entries of an CepOperator from the tree of the TreeViewer.
	 * 
	 * @param toRemove is the item which should be removed
	 * 
	 * @return true
	 */
	public boolean remove(MachineTreeItem toRemove) {
		for (Object listItem : this.root.getChildren().toArray()) {
			// search for every instance with the equal StateMachine object
			if (toRemove.getContent().equals(
					((InstanceTreeItem) listItem).getContent()
							.getStateMachine())) {
				// remove the item
				this.root.getChildren().remove(listItem);
				((InstanceTreeItem) listItem).setParent(null);
			}
		}
		this.tree.refresh();
		return true;
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
