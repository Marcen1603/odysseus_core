package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.StateMachineInstance;

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
	 * This method adds a new tree item to the tree list. If there is an state
	 * machine instance from the same machine, they are grouped together. Else a
	 * new categorie for the machine is created.
	 * 
	 * @param stateMachineInstance
	 *            is an state machine instance
	 */
	@Override
	public void addStateMachineInstance(
			StateMachineInstance stateMachineInstance) {

		// if there is no item in the tree
		if (this.getTree().getItemCount() == 0) {
			StateTreeItem item = new StateTreeItem(this.getTree(), SWT.NONE,
					stateMachineInstance.getMachine());
			item.setText(stateMachineInstance.getMachine().getString());
		} else {
			boolean inserted = false;
			// if the machine of the instance was added before add the new item
			// into the matching tree item
			for (TreeItem statusItem : this.getTree().getItems()) {
				if (statusItem.getText().equals(
						stateMachineInstance.getMachine().getString())) {
					StateTreeItem item = new StateTreeItem(statusItem,
							SWT.NONE, stateMachineInstance);
					item.setText(stateMachineInstance.getMachine().getString()
							+ ": " + stateMachineInstance.getInstanceId());
					this.setStatusImage(item);
					inserted = true;
				}
			}
			// if the machine of the instance was not found in the tree, add a new 
			// tree item for this machine and add the tree item of this instance
			if (!inserted) {
				StateTreeItem item = new StateTreeItem(this.getTree(),
						SWT.NONE, stateMachineInstance.getMachine());
				item.setText(stateMachineInstance.getMachine().getString());
			}
		}
	}

}
