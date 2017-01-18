package de.uniol.inf.is.odysseus.admission.status.mep;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;
import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;

public class RollbackLoadSheddingAdmissionFunction  extends AbstractTargetedCommandFunction {

	private static final long serialVersionUID = -5074148701507211739L;

	public RollbackLoadSheddingAdmissionFunction() {
		super("rollbackLoadShedding", null);
	}

	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<String>(getTargets(), false) {
			@Override protected boolean run(String loadSheddingComponent) {
				if (LoadSheddingAdmissionStatusRegistry.hasLoadSheddingAdmissionComponent(loadSheddingComponent)) {
					LoadSheddingAdmissionStatusRegistry.getLoadSheddingAdmissionComponent(loadSheddingComponent).rollBackLoadShedding();
				}
				return true;
			}
		};
	}
}
