package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.MachineTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPStatus;
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
	private LabelTreeItem itemR;
	private LabelTreeItem itemF;
	private LabelTreeItem itemA;

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

	public void addToTree(CEPInstance instance) {
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
		this.getDisplay().asyncExec(new Runnable() {
			public void run() {
				tree.refresh();
			}
		});
	}

	public void removeAll() {
		this.itemA.removeAllChildren();
		this.itemR.removeAllChildren();
		this.itemF.removeAllChildren();
		this.tree.refresh();
	}

	/**
	 * This method returns the number of running automates
	 * 
	 * @return the number of running automates
	 */
	public int getNumberOfRunning() {
		return this.itemR.getChildren().size();
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
	 * This method returns the number of aborted automates
	 * 
	 * @return the number of aborted automates
	 */
	public int getNumberOfAborted() {
		return this.itemA.getChildren().size();
	}

	public boolean remove(InstanceTreeItem toRemove) {
		if (toRemove.getContent().getStatus().equals(CEPStatus.FINISHED)) {
			this.removeInstanceFromList(this.itemF, toRemove
					.getContent().getInstance());
			return true;
		} else if (toRemove.getContent().getStatus().equals(CEPStatus.RUNNING)) {
			this.removeInstanceFromList(this.itemR, toRemove
					.getContent().getInstance());
			return true;
		} else if (toRemove.getContent().getStatus().equals(CEPStatus.ABORTED)) {
			this.removeInstanceFromList(this.itemA, toRemove
					.getContent().getInstance());
			return true;
		}
		return false;
	}

	public boolean remove(MachineTreeItem toRemove) {
		this.removeMachineFromList(this.itemR, toRemove.getContent());
		this.removeMachineFromList(this.itemF, toRemove.getContent());
		this.removeMachineFromList(this.itemA, toRemove.getContent());
		this.tree.refresh();
		return true;
	}

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

	private void removeMachineFromList(LabelTreeItem labelItem,
			StateMachine<?> machine) {
		for (Object item : labelItem.getChildren().toArray()) {
			if (((InstanceTreeItem) item).getContent().getStateMachine()
					.equals(machine)) {
				labelItem.getChildren().remove(item);
				((InstanceTreeItem)item).setParent(null);
			}
		}
	}

	public void stateChanged(StateMachineInstance<?> instance) {
		for (Object object : this.itemR.getChildren().toArray()) {
			AbstractTreeItem item = (AbstractTreeItem) object; 
			if (instance.equals(((CEPInstance) item.getContent())
					.getInstance())) {
				CEPInstance cepInstance = (CEPInstance) item
						.getContent();
				cepInstance.currentStateChanged();
				if (cepInstance.getInstance().getCurrentState().isAccepting()) {
					this.remove((InstanceTreeItem) item);
					((CEPInstance) item.getContent()).setStatus(CEPStatus.FINISHED);
					this.addToTree((CEPInstance) item.getContent());
					return;
				}
			}
		}
	}

	public void statusChanged(StateMachineInstance<?> instance,
			CEPStatus newStatus) {
		for (Object item : this.itemR.getChildren().toArray()) {
			InstanceTreeItem instanceItem = (InstanceTreeItem) item;
			if (instance.equals(((CEPInstance) instanceItem.getContent())
					.getInstance())) {
				this.remove((InstanceTreeItem) instanceItem);
				((CEPInstance) instanceItem.getContent()).setStatus(newStatus);
				this.addToTree((CEPInstance) instanceItem.getContent());
				return;
			}
		}
	}

}
