package cc.kuka.odysseus.ontology.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import cc.kuka.odysseus.ontology.common.model.Property;

@SuppressWarnings("unused")
public class DeletePropertyAction extends Action {

	private Shell shell;

	private Property property;

	/**
	 * Class constructor.
	 *
	 */
	public DeletePropertyAction(Shell shell, Property property) {
		super();
		this.shell = shell;
		this.property = property;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {

	}
}
