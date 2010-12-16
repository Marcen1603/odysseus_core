package de.uniol.inf.is.odysseus.cep.cepviewer.list;

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
	private CEPTreeItem itemR;
	private CEPTreeItem itemF;
	private CEPTreeItem itemA;

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
		itemR = new CEPTreeItem();
		itemR.setParent(this.getRoot());
		itemR.setName("Running");
		this.getRoot().add(itemR);
		itemF = new CEPTreeItem();
		itemF.setParent(this.getRoot());
		itemF.setName("Finished");
		this.getRoot().add(itemF);
		itemA = new CEPTreeItem();
		itemA.setParent(this.getRoot());
		itemA.setName("Aborted");
		this.getRoot().add(itemA);
		this.getTree().setInput(this.getRoot().getChildren());
	}

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
			CEPTreeItem item = new CEPTreeItem(instance);
			item.setParent(this.itemR);
			this.itemR.add(item);
			this.getDisplay().asyncExec(new Runnable() {
				public void run() {
					getTree().setInput(getRoot().getChildren());	
				}
			});
			return true;
		} else if(instance.getCurrentState().isAccepting()) {
			CEPTreeItem item = new CEPTreeItem(instance);
			item.setParent(this.itemF);
			this.itemF.add(item);
			this.getDisplay().asyncExec(new Runnable() {
				public void run() {
					getTree().setInput(getRoot().getChildren());	
				}
			});
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
		return this.itemR.getChildren().size();
	}

	/**
	 * This method returns the number of finished automates
	 * @return the number of finished automates
	 */
	public int getNumberOfFinished() {
		return this.itemF.getChildren().size();
	}

	/**
	 * This method returns the number of aborted automates
	 * @return the number of aborted automates
	 */
	public int getNumberOfAborted() {
		return this.itemA.getChildren().size();
	}

}
