package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPStatus;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

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

	public boolean remove(AbstractTreeItem item) {
		CEPInstance cepInstance = (CEPInstance) item.getContent();
		if (cepInstance.getStatus().equals(CEPStatus.FINISHED)) {
			System.out.println("Status remove 1f");
			for (AbstractTreeItem instanceItem : this.itemF.getChildren()) {
				System.out.println("Status remove 2f");
				if (cepInstance.getInstance().equals(
						((InstanceTreeItem) instanceItem).getContent()
								.getInstance())) {
					System.out.println("Status remove finished");
					this.itemF.getChildren().remove(instanceItem);
					instanceItem.setParent(null);
					this.tree.refresh();
					return true;
				}
			}
		} else if (cepInstance.getStatus().equals(CEPStatus.RUNNING)) {
			System.out.println("Status remove 1r");
			for (AbstractTreeItem instanceItem : this.itemR.getChildren()) {
				System.out.println("Status remove 2r");
				if (cepInstance.getInstance().equals(
						((InstanceTreeItem) instanceItem).getContent()
								.getInstance())) {
					System.out.println("Status remove running");
					this.itemR.getChildren().remove(instanceItem);
					instanceItem.setParent(null);
					this.tree.refresh();
					return true;
				}
			}
		} else if (cepInstance.getStatus().equals(CEPStatus.ABORTED)) {
			System.out.println("Status remove 1a");
			for (AbstractTreeItem instanceItem : this.itemA.getChildren()) {
				System.out.println("Status remove 2a");
				if (cepInstance.getInstance().equals(
						((InstanceTreeItem) instanceItem).getContent()
								.getInstance())) {
					System.out.println("Status remove aborted");
					this.itemA.getChildren().remove(instanceItem);
					instanceItem.setParent(null);
					this.tree.refresh();
					return true;
				}
			}
		}
		return false;
	}

	public void stateChanged(StateMachineInstance<?> instance) {
		for (AbstractTreeItem instanceItem : this.itemR.getChildren()) {
			if (instance.equals(((CEPInstance) instanceItem.getContent())
					.getInstance())) {
				CEPInstance cepInstance = (CEPInstance) instanceItem
						.getContent();
				cepInstance.currentStateChanged();
				if (cepInstance.getCurrentState().getState().isAccepting()) {
					cepInstance.setStatus(CEPStatus.FINISHED);
					return;
				}
			}
		}
	}

	public void statusChanged(StateMachineInstance<?> instance,
			CEPStatus newStatus) {
		for (AbstractTreeItem instanceItem : this.itemR.getChildren()) {
			if (instance.equals(((CEPInstance) instanceItem.getContent())
					.getInstance())) {
				System.out.println("---Remove old reference");
				this.remove(instanceItem);
				System.out.println("---set new status to reference");
				((CEPInstance) instanceItem.getContent()).setStatus(newStatus);
				System.out.println("---add new reference");
				this.addToTree((CEPInstance) instanceItem.getContent());
				return;
			}
		}
	}

}
