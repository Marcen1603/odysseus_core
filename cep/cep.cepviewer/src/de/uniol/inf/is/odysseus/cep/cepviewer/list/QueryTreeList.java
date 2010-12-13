package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;

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
		if(this.getRoot().getChildren().isEmpty()) {
			CEPTreeItem item = new CEPTreeItem(operator);
			item.setParent(this.getRoot());
			this.getRoot().add(item);
			QueryTreeList.machine++;
		} else {
			for(Object instance : operator.getInstances()) {
				if(!this.addToTree((StateMachineInstance) instance)) {
					CEPTreeItem parent = new CEPTreeItem(operator);
					parent.setParent(this.getRoot());
					this.getRoot().add(parent);
					QueryTreeList.machine++;
					CEPTreeItem item = new CEPTreeItem((StateMachineInstance)instance);
					item.setParent(parent);
					parent.add(item);
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean addToTree(StateMachineInstance instance) {
		for (CEPTreeItem item : this.getRoot().getChildren()) {
			CepOperator nextOp = (CepOperator) item.getContent();
			StateMachine machine = nextOp.getStateMachine();
			if(machine.equals(instance.getStateMachine())) {
				CEPTreeItem newItem = new CEPTreeItem(instance);
				newItem.setParent(item);
				item.add(newItem);
				return true;
			}
		}
		return false;
	}
}
