package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.StateMachineInstance;

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
	
	/**
	 * This method adds a new tree item to the tree list.
	 * 
	 * @param stateMachineInstance
	 *            is an state machine instance
	 */
	public void addStateMachineInstance(StateMachineInstance stateMachineInstance) {
		StateTreeItem item = new StateTreeItem(this.getTree(), SWT.NONE, stateMachineInstance);
		item.setText(stateMachineInstance.getMachine().getString() + ": " + stateMachineInstance.getInstanceId());
		this.setStatusImage(item);
	}

}
