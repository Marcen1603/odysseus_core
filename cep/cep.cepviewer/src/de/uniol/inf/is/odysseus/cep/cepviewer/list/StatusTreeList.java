package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.Status;

/**
 * This class defines the status tree list.
 * 
 * @author Christian
 */
public class StatusTreeList extends AbstractTreeList {

	// categories of this tree list
	private StateTreeItem itemR;
	private StateTreeItem itemF;
	private StateTreeItem itemA;

	/**
	 * This is the constructor.
	 * 
	 * @param parent
	 *            is the widget that inherits this tree list.
	 * @param style
	 *            contains the style of this tree list.
	 */
	public StatusTreeList(Composite parent, int style) {
		super(parent, style);
		itemR = new StateTreeItem(this.getTree(), SWT.NONE);
		itemR.setText(Status.RUNNING.getName());
		itemF = new StateTreeItem(this.getTree(), SWT.NONE);
		itemF.setText(Status.FINISHED.getName());
		itemA = new StateTreeItem(this.getTree(), SWT.NONE);
		itemA.setText(Status.ABORTED.getName());
	}

	/**
	 * This method adds a new tree item to the tree list.
	 * 
	 * @param stateMachineInstance
	 *            is an state machine instance
	 */
	@Override
	public void addStateMachineInstance(StateMachineInstance stateMachineInstance) {
		for (TreeItem statusItem : this.getTree().getItems()) {
			if (statusItem.getText().contains("" + Status.RUNNING.getName())) {
				StateTreeItem item = new StateTreeItem(statusItem, SWT.NONE, stateMachineInstance);
				item.setText(stateMachineInstance.getMachine().getString() + ": " + stateMachineInstance.getInstanceId());
				this.setStatusImage(item);
			}
		}
	}
	
	/**
	 * This method returns the number of running automates
	 * @return the number of running automates
	 */
	public int getNumberOfRunning() {
		return this.itemR.getItemCount();
	}

	/**
	 * This method returns the number of finished automates
	 * @return the number of finished automates
	 */
	public int getNumberOfFinished() {
		return this.itemF.getItemCount();
	}

	/**
	 * This method returns the number of aborted automates
	 * @return the number of aborted automates
	 */
	public int getNumberOfAborted() {
		return this.itemA.getItemCount();
	}

}
