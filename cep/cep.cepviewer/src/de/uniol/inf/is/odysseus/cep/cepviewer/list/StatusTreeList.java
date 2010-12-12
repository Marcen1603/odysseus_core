package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import de.uniol.inf.is.odysseus.cep.epa.CepOperator;

import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;


/**
 * This class defines the status tree list.
 * 
 * @author Christian
 */
public class StatusTreeList extends AbstractTreeList {

	// categories of this tree list
	private TreeItem itemR;
	private TreeItem itemF;
	private TreeItem itemA;

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
		itemR = new TreeItem(this.getTree(), SWT.NONE);
		itemR.setText("Running");
		itemF = new TreeItem(this.getTree(), SWT.NONE);
		itemF.setText("Finished");
		itemA = new TreeItem(this.getTree(), SWT.NONE);
		itemA.setText("Aborted");
	}

//	public void addToTree(StateMachineInstance stateMachineInstance) {
//		Status status = null;
//		if(stateMachineInstance.getCurrentState().isAccepting()) {
//			status = Status.FINISHED;
//		} else if(!stateMachineInstance.getCurrentState().isAccepting()) {
//			status = Status.RUNNING;
//		}
//		for (TreeItem statusItem : this.getTree().getItems()) {
//			if (statusItem.getText().contains(status.toString())) {
//				StateTreeItem item = new StateTreeItem(statusItem, SWT.NONE, stateMachineInstance);
//				item.setText(stateMachineInstance.getMachine().getString() + ": " + stateMachineInstance.getInstanceId());
//				this.setStatusImage(item);
//			}
//		}
//	}
	
	@SuppressWarnings("unchecked")
	public boolean addToTree(CepOperator operator) {
		boolean inserted = false;
		for (Object instance : operator.getInstances()) {
			inserted = this.addToTree((StateMachineInstance) instance);
		}
		return inserted;
	}
	
	@SuppressWarnings("unchecked")
	public boolean addToTree(StateMachineInstance instance) {
		if(!instance.getCurrentState().isAccepting()) {
			TreeItem item = new TreeItem(itemR, SWT.NONE);
			item.setData("Instance", instance);
			// TODO same text for two instances of differnt machines
			item.setText("Instance " + instance.hashCode());
			this.setStatusImage(item);
			return true;
		} else if(instance.getCurrentState().isAccepting()) {
			TreeItem item = new TreeItem(itemF, SWT.NONE);
			item.setData("Instance", instance);
			// TODO same text for two instances of differnt machines
			item.setText("Instance " + instance.hashCode());
			this.setStatusImage(item);
			return true;
		} 
		// TODO in case the instance is aborted...
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void changeToStatus(TreeItem item) {
		Object instance = item.getData("Instance");
		item.dispose();
		this.addToTree((StateMachineInstance) instance);
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
