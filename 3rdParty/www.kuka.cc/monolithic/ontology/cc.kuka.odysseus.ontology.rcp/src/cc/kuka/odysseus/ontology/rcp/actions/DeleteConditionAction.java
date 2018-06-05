package cc.kuka.odysseus.ontology.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import cc.kuka.odysseus.ontology.common.SensorOntologyService;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;

@SuppressWarnings("unused")
public class DeleteConditionAction extends Action {
	private Shell shell;
	private Condition condition;

	/**
	 * Class constructor.
	 *
	 */
	public DeleteConditionAction(Shell shell, Condition condition) {
		super();
		this.shell = shell;
		this.condition = condition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		final SensorOntologyService ontology = SensorRegistryPlugIn.getSensorOntologyService();
		// FIXME get condition object
		// condition = new Conditon();
		// ontology.delete(condition);
	}
}
