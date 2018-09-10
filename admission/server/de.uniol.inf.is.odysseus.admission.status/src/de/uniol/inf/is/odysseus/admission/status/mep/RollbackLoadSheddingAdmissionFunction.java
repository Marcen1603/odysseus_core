package de.uniol.inf.is.odysseus.admission.status.mep;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class RollbackLoadSheddingAdmissionFunction  extends AbstractFunction<Command> {

	private static final long serialVersionUID = -5074148701507211739L;

	public RollbackLoadSheddingAdmissionFunction() {
		super("rollbackLoadShedding", 0, null, SDFDatatype.COMMAND);
	}

	@Override 
	public Command getValue() {
		return new Command() {
			@Override public boolean run() {
				LoadSheddingAdmissionStatusRegistry.getActiveComponent().rollbackLoadShedding();
		
				return true;
			}
		};
	}
}
