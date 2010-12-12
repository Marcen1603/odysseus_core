package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

/**
 * This class defines the query tree list.
 * 
 * @author Christian
 */
public class QueryTreeList extends AbstractTreeList {

	private static int machine = 0;
	
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

	@SuppressWarnings("unchecked")
	public boolean addToTree(CepOperator operator) {
		// if there is no item in the tree
		if (this.getTree().getItemCount() == 0) {
			TreeItem item = new TreeItem(this.getTree(), SWT.NONE);
			item.setData("Machine", operator);
			item.setText("Machine " + QueryTreeList.machine);
			QueryTreeList.machine++;
		} else {
			for(Object instance : operator.getInstances()) {
				if(!this.addToTree((StateMachineInstance) instance)) {
					TreeItem item = new TreeItem(this.getTree(), SWT.NONE);
					item.setData("Machine", operator);
					item.setText("Machine " + QueryTreeList.machine);
					QueryTreeList.machine++;
					TreeItem newItem = new TreeItem(item, SWT.NONE);
					newItem.setData("Instance", instance);
					newItem.setText("Instance " + instance.hashCode());
					this.setStatusImage(item);
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean addToTree(StateMachineInstance instance) {
		for (TreeItem item : this.getTree().getItems()) {
			CepOperator nextOp = (CepOperator) item.getData("Machine");
			for(Object next : nextOp.getInstances()) {
				if(((StateMachineInstance) next).equals(instance)) {
					TreeItem newItem = new TreeItem(item,SWT.NONE);
					newItem.setData("Instance", next);
					newItem.setText("Instance " + instance.hashCode());
					this.setStatusImage(item);
					return true;
				}
			}
		}
		return false;
	}
}
