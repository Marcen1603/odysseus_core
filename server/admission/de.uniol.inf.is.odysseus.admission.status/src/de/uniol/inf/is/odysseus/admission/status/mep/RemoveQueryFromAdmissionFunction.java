package de.uniol.inf.is.odysseus.admission.status.mep;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class RemoveQueryFromAdmissionFunction extends AbstractFunction<Command> {

	private static final long serialVersionUID = 8274514444365214961L;
	
	public RemoveQueryFromAdmissionFunction() {
		super("removeQueryFromAdmission", 1, null, SDFDatatype.COMMAND);
	}

	@Override
	public Command getValue() {
		final int queryID = (int) getInputValue(0);
		
		return new Command() {
			@Override public boolean run() {
				LoadSheddingAdmissionStatusRegistry.getActiveComponent().removeQuery(queryID);
				return true;
			}
		};
	}

}
