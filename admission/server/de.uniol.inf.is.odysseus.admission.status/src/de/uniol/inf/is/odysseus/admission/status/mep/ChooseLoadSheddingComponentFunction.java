package de.uniol.inf.is.odysseus.admission.status.mep;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ChooseLoadSheddingComponentFunction extends AbstractFunction<Command> {

	private static final long serialVersionUID = 5688557517825048673L;

	public ChooseLoadSheddingComponentFunction() {
		super("chooseLoadShedding", 1, null, SDFDatatype.COMMAND);
	}

	@Override 
	public Command getValue() {
		final String component = (String) getInputValue(0);
		
		return new Command() {
			@Override public boolean run() {
				LoadSheddingAdmissionStatusRegistry.setActiveComponent(component);
				return true;
			}
		};
	}
}
