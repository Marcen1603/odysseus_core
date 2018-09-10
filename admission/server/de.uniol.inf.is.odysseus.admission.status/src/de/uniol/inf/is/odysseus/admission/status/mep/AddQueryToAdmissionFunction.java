package de.uniol.inf.is.odysseus.admission.status.mep;

import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;

public class AddQueryToAdmissionFunction extends AbstractFunction<Command> {

	private static final long serialVersionUID = 8903365960756247370L;
	
	public AddQueryToAdmissionFunction() {
		super("addQueryToAdmission", 1, null, SDFDatatype.COMMAND);
	}

	@Override 
	public Command getValue() {
		final int queryID = (int) getInputValue(0);
		
		return new Command() {
			@Override public boolean run() {
				LoadSheddingAdmissionStatusRegistry.getActiveComponent().addQuery(queryID);
		
				return true;
			}
		};
	}

}
