package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

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

	public boolean addToTree(Object object) {
		if(object instanceof StateMachineInstance) {
			InstanceTreeItem newItem = new InstanceTreeItem(this.root, (StateMachineInstance<?>) object);
			this.root.add(newItem);
			this.getDisplay().syncExec(new Runnable() {
				public void run() {
					tree.refresh();
				}
			});
			return true;
		}
		return false;
	}

	public int getItemCount() {
		return this.root.getChildren().size();
	}

	public void removeAll() {
		this.root.removeAllChildren();
	}

	public boolean remove(InstanceTreeItem toRemove) {
		for(AbstractTreeItem instanceItem : this.root.children) {
			if(toRemove.getContent().equals(((InstanceTreeItem)instanceItem).getContent())) {
				instanceItem = null;
				return true;
			}
		}
		return false;
	}
}
