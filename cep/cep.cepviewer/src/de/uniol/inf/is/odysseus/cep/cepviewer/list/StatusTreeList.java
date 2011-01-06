package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

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
		
		// DELETE: Test
		LabelTreeItem testA = new LabelTreeItem(this.itemR, StringConst.STATUS_ABORTED);
		testA.setImage(StringConst.PATH_TO_RUNNING_IMAGE);
		this.itemR.add(testA);
		LabelTreeItem testB = new LabelTreeItem(this.itemF, StringConst.STATUS_ABORTED);
		testB.setImage(StringConst.PATH_TO_FINISHED_IMAGE);
		this.itemF.add(testB);
		
		this.tree.refresh();
	}

	public boolean addToTree(Object object) {
		if (object instanceof StateMachineInstance) {
			// if the object is an instance of StateMachineInstance
			StateMachineInstance<?> instance = (StateMachineInstance<?>) object;
			if (instance.getCurrentState().isAccepting()) {
				InstanceTreeItem newItem = new InstanceTreeItem(this.itemF, instance);
				this.itemF.add(newItem);
			} else if (!instance.getCurrentState().isAccepting()) {
				InstanceTreeItem newItem = new InstanceTreeItem(this.itemR, instance);
				this.itemR.add(newItem);
			}  // TODO: else if (Instanze wurde abgebrochen) {}
			this.getDisplay().asyncExec(new Runnable() {
				public void run() {
					tree.refresh();
				}
			});
			return true;
		}
		return false;
	}

	public void changeToStatus(InstanceTreeItem item) {
		Object instance = item.getContent();
		item.getParent().getChildren().remove(item);
		
		this.addToTree(instance);
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

	public boolean remove(InstanceTreeItem item) {
		StateMachineInstance<?> instance = item.getContent();
		if (instance.getCurrentState().isAccepting()) {
			for(AbstractTreeItem instanceItem : this.itemF.children) {
				if(instance.equals(((InstanceTreeItem) instanceItem).getContent())) {
					instanceItem = null;
					return true;
				}
			}
		} else if (!instance.getCurrentState().isAccepting()) {
			for(AbstractTreeItem instanceItem : this.itemR.children) {
				if(instance.equals(((InstanceTreeItem) instanceItem).getContent())) {
					instanceItem = null;
					return true;
				}
			}
		}  // TODO: else if (Instanze wurde abgebrochen) {}
		return false;
	}

}
