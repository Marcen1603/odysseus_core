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
			return this.removeInstanceFromList(this.itemF, toRemove
					.getContent().getInstance());
		} else if (toRemove.getContent().getStatus().equals(CEPStatus.RUNNING)) {
			return this.removeInstanceFromList(this.itemR, toRemove
					.getContent().getInstance());
		} else if (toRemove.getContent().getStatus().equals(CEPStatus.ABORTED)) {
			return this.removeInstanceFromList(this.itemA, toRemove
					.getContent().getInstance());
		}
		return false;
	}

	public boolean remove(MachineTreeItem toRemove) {
		System.out.println("StatusList: remove Machine R");
		this.removeMachineFromList(this.itemR, toRemove.getContent());
		System.out.println("StatusList: remove Machine F");
		this.removeMachineFromList(this.itemF, toRemove.getContent());
		System.out.println("StatusList: remove Machine A");
		this.removeMachineFromList(this.itemA, toRemove.getContent());
		System.out.println("StatusList: remove Machine refresh");
		this.tree.refresh();
		return true;
	}

	private boolean removeInstanceFromList(LabelTreeItem labelItem,
			StateMachineInstance<?> instance) {
		for (Object item : labelItem.getChildren().toArray()) {
			if (((InstanceTreeItem) item).getContent().getInstance()
					.equals(instance)) {
				labelItem.getChildren().remove(item);
				((InstanceTreeItem) item).setParent(null);
				return true;
			}
		}
		return false;
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
		System.out.println("!");
		try{
		for (AbstractTreeItem instanceItem : this.itemR.getChildren()) {
			System.out.println("!");
			if (instance.equals(((CEPInstance) instanceItem.getContent())
					.getInstance())) {
				System.out.println("!");
				CEPInstance cepInstance = (CEPInstance) instanceItem
						.getContent();
				cepInstance.currentStateChanged();
				if (cepInstance.getCurrentState().getState().isAccepting()) {
					cepInstance.setStatus(CEPStatus.FINISHED);
					return;
				}
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
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
