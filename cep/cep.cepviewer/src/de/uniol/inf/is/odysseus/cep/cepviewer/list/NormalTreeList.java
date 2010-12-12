package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

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
		TreeItem item = new TreeItem(this.getTree(), SWT.NONE);
		item.setData("Instance", instance);
		// TODO same text for two instances of differnt machines
		item.setText("Instance " + instance.hashCode());
		this.setStatusImage(item);
		return true;
	}
}
