package de.uniol.inf.is.odysseus.admission.status.mep;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ChooseStandartLoadSheddingFactorFunction extends AbstractFunction<Command> {

	private static final long serialVersionUID = 1359170669833482294L;

	public ChooseStandartLoadSheddingFactorFunction() {
		super("chooseStandartSheddingFactor", 1, null, SDFDatatype.COMMAND);
	}

	@Override 
	public Command getValue() {
		final int factor = ((Long) getInputValue(0)).intValue();
		
		return new Command() {
			@Override public boolean run() {
				LoadSheddingAdmissionStatusRegistry.setStandartMaxSheddingFactor(factor);
				return true;
			}
		};
	}

}
