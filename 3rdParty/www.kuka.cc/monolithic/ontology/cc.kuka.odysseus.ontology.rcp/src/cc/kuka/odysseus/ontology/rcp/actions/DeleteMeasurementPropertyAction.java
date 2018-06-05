package cc.kuka.odysseus.ontology.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;

@SuppressWarnings("unused")
public class DeleteMeasurementPropertyAction extends Action {
	private Shell shell;
	private MeasurementProperty measurementProperty;

	/**
	 * Class constructor.
	 *
	 */
	public DeleteMeasurementPropertyAction(Shell shell, MeasurementProperty measurementProperty) {
		super();
		this.shell = shell;
		this.measurementProperty = measurementProperty;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

    }
}
