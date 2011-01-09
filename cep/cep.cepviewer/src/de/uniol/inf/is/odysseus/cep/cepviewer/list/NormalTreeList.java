package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPStatus;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

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

	public void addToTree(CEPInstance instance) {
		InstanceTreeItem newItem = new InstanceTreeItem(this.root, instance);
		this.root.add(newItem);
		this.getDisplay().asyncExec(new Runnable() {
			public void run() {
				tree.refresh();
			}
		});
	}

	public int getItemCount() {
		return this.root.getChildren().size();
	}

	public void removeAll() {
		this.root = new LabelTreeItem(null, "Root");
		this.tree.setInput(this.root);
		this.tree.refresh();
	}

	public boolean remove(AbstractTreeItem toRemove) {
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

	public void stateChanged(StateMachineInstance<?> instance) {
		for (AbstractTreeItem instanceItem : this.root.getChildren()) {
			if (instance.equals(((CEPInstance) instanceItem.getContent())
					.getInstance())) {
				CEPInstance cepInstance = (CEPInstance) instanceItem
						.getContent();
				cepInstance.currentStateChanged();
				return;
			}
		}
	}

	public void statusChanged(StateMachineInstance<?> instance,
			CEPStatus newStatus) {
	}
}
