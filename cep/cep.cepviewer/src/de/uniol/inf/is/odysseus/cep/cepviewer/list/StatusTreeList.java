package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPStatus;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.MachineTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;

/**
 * This class defines the status tree list.
 * 
 * @author Christian
 */
public class StatusTreeList extends AbstractTreeList {

	// categories of this tree list
	private LabelTreeItem itemA;
	private LabelTreeItem itemF;
	private LabelTreeItem itemR;

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
		this.itemR = new LabelTreeItem(this.root, StringConst.STATUS_RUNNING);
		this.itemR.setImage(StringConst.PATH_TO_RUNNING_IMAGE);
		this.root.add(this.itemR);
		this.itemF = new LabelTreeItem(this.root, StringConst.STATUS_FINISHED);
		this.itemF.setImage(StringConst.PATH_TO_FINISHED_IMAGE);
		this.root.add(this.itemF);
		this.itemA = new LabelTreeItem(this.root, StringConst.STATUS_ABORTED);
		this.itemA.setImage(StringConst.PATH_TO_ABORTED_IMAGE);
		this.root.add(this.itemA);
		this.tree.refresh();
	}

	/**
	 * This method adds an object of the class CEPInstance into the TreeViewer.
	 * 
	 * @param instance
	 *            is the instance which should be added
	 */
	public void addToTree(CEPInstance instance) {
		// check the current status of the instance and add it to
		// status entry
		if (instance.getStatus().equals(CEPStatus.FINISHED)) {
			InstanceTreeItem newItem = new InstanceTreeItem(this.itemF,
					instance);
			this.itemF.add(newItem);
		} else if (instance.getStatus().equals(CEPStatus.RUNNING)) {
			InstanceTreeItem newItem = new InstanceTreeItem(this.itemR,
					instance);
			this.itemR.add(newItem);
		} else if (instance.getStatus().equals(CEPStatus.ABORTED)) {
			InstanceTreeItem newItem = new InstanceTreeItem(this.itemR,
					instance);
			this.itemA.add(newItem);
		}
		// refresh the list
		this.getDisplay().asyncExec(new Runnable() {
			public void run() {
				tree.refresh();
			}
		});
	}

	/**
	 * This method removes one entry from the tree of the TreeViewer.
	 * 
	 * @param toRemove
	 *            is the item which should be removed
	 * 
	 * @return true if the instance has been found, else false
	 */
	public boolean remove(InstanceTreeItem toRemove) {
		// check the current status of the instance and remove it from the
		// status entry
		if (toRemove.getContent().getStatus().equals(CEPStatus.FINISHED)) {
			this.removeInstanceFromList(this.itemF, toRemove.getContent()
					.getInstance());
			return true;
		} else if (toRemove.getContent().getStatus().equals(CEPStatus.RUNNING)) {
			this.removeInstanceFromList(this.itemR, toRemove.getContent()
					.getInstance());
			return true;
		} else if (toRemove.getContent().getStatus().equals(CEPStatus.ABORTED)) {
			this.removeInstanceFromList(this.itemA, toRemove.getContent()
					.getInstance());
			return true;
		}
		return false;
	}

	/**
	 * This method removes all entries of an CepOperator from the tree of the
	 * TreeViewer.
	 * 
	 * @param toRemove
	 *            is the item which should be removed
	 * 
	 * @return true
	 */
	public boolean remove(MachineTreeItem toRemove) {
		this.removeMachineFromList(this.itemR, toRemove.getContent());
		this.removeMachineFromList(this.itemF, toRemove.getContent());
		this.removeMachineFromList(this.itemA, toRemove.getContent());
		this.tree.refresh();
		return true;
	}

	/**
	 * This method removes all entries from the tree of the TreeViewer.
	 */
	public void removeAll() {
		this.itemA.removeAllChildren();
		this.itemR.removeAllChildren();
		this.itemF.removeAllChildren();
		this.tree.refresh();
	}

	/**
	 * This private method removes an instance from a LabelTreeItem.
	 * 
	 * @param labelItem
	 *            is the status entry from which the instance should be removed
	 * @param instance
	 *            the instance which should be removed
	 */
	private void removeInstanceFromList(LabelTreeItem labelItem,
			StateMachineInstance<?> instance) {
		for (Object item : labelItem.getChildren().toArray()) {
			if (((InstanceTreeItem) item).getContent().getInstance()
					.equals(instance)) {
				labelItem.getChildren().remove(item);
				((InstanceTreeItem) item).setParent(null);
				return;
			}
		}
	}

	/**
	 * This private method removes all instances with the given StateMachine
	 * from a LabelTreeItem.
	 * 
	 * @param labelItem
	 *            is the status entry from which the instance should be removed
	 * @param machine
	 *            is the StateMachine which instances should be removed.
	 */
	private void removeMachineFromList(LabelTreeItem labelItem,
			StateMachine<?> machine) {
		for (Object item : labelItem.getChildren().toArray()) {
			if (((InstanceTreeItem) item).getContent().getStateMachine()
					.equals(machine)) {
				labelItem.getChildren().remove(item);
				((InstanceTreeItem) item).setParent(null);
			}
		}
	}

	/**
	 * This method is called if the current state of an instance changed. It
	 * updates the CEPInstance object which holds the changed instance.
	 * 
	 * @param instance
	 *            is the instance which has been changed
	 */
	public void stateChanged(StateMachineInstance<?> instance) {
		// search all entries of the TreeViewer for the given instance
		for (Object object : this.itemR.getChildren().toArray()) {
			AbstractTreeItem item = (AbstractTreeItem) object;
			if (instance
					.equals(((CEPInstance) item.getContent()).getInstance())) {
				// if found change the current state within the CEPInstance
				// object
				CEPInstance cepInstance = (CEPInstance) item.getContent();
				cepInstance.currentStateChanged();
				if (cepInstance.getInstance().getCurrentState().isAccepting()) {
					// if the status changed becaus of the changed state change
					// the status.
					this.remove((InstanceTreeItem) item);
					((CEPInstance) item.getContent())
							.setStatus(CEPStatus.FINISHED);
					this.addToTree((CEPInstance) item.getContent());
					return;
				}
			}
		}
	}

	/**
	 * This method is called if the status of an instance has been changed.
	 * 
	 * @param instance
	 *            is the instance which status has been changed
	 * @param newStatus
	 *            the new status of the instance
	 */
	public void statusChanged(StateMachineInstance<?> instance,
			CEPStatus newStatus) {
		for (Object item : this.itemR.getChildren().toArray()) {
			InstanceTreeItem instanceItem = (InstanceTreeItem) item;
			if (instance.equals(((CEPInstance) instanceItem.getContent())
					.getInstance())) {
				// if the instance has been found, change the status within the
				// CEPInstance object
				this.remove((InstanceTreeItem) instanceItem);
				((CEPInstance) instanceItem.getContent()).setStatus(newStatus);
				this.addToTree((CEPInstance) instanceItem.getContent());
				return;
			}
		}
	}

	/**
	 * This method returns the number of aborted automates
	 * 
	 * @return the number of aborted automates
	 */
	public int getNumberOfAborted() {
		return this.itemA.getChildren().size();
	}

	/**
	 * This method returns the number of finished automates
	 * 
	 * @return the number of finished automates
	 */
	public int getNumberOfFinished() {
		return this.itemF.getChildren().size();
	}

	/**
	 * This method returns the number of running automates
	 * 
	 * @return the number of running automates
	 */
	public int getNumberOfRunning() {
		return this.itemR.getChildren().size();
	}

}
