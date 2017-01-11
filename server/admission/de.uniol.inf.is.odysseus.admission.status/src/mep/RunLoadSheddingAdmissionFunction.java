package mep;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;
import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.mep.functions.command.AbstractTargetedCommandFunction;

public class RunLoadSheddingAdmissionFunction extends AbstractTargetedCommandFunction {

	private static final long serialVersionUID = -3416491486540386576L;

	public RunLoadSheddingAdmissionFunction() {
		super("runLoadShedding", null);
	}

	@Override public TargetedCommand<?> getValue() {
		return new TargetedCommand<String>(getTargets(), false) {
			@Override protected boolean run(String loadSheddingComponent) {
				if (LoadSheddingAdmissionStatusRegistry.hasLoadSheddingAdmissionComponent(loadSheddingComponent)) {
					LoadSheddingAdmissionStatusRegistry.getLoadSheddingAdmissionComponent(loadSheddingComponent).runLoadShedding();
				}
				return true;
			}
		};
	}
}
