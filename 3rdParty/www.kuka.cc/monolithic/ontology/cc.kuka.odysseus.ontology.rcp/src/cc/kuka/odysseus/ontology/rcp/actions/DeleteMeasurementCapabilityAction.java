package cc.kuka.odysseus.ontology.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;

@SuppressWarnings("unused")
public class DeleteMeasurementCapabilityAction extends Action {
	private Shell shell;
	private MeasurementCapability measurementCapability;

	/**
	 * Class constructor.
	 *
	 */
	public DeleteMeasurementCapabilityAction(Shell shell, MeasurementCapability measurementCapability) {
		super();
		this.shell = shell;
		this.measurementCapability = measurementCapability;
	}
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

    }
}
