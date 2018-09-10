package de.uniol.inf.is.odysseus.admission.status.mep;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ChooseLoadSheddingGrowthFunction extends AbstractFunction<Command> {

	private static final long serialVersionUID = 2699448496486141245L;

	public ChooseLoadSheddingGrowthFunction() {
		super("chooseSheddingGrowth", 1, null, SDFDatatype.COMMAND);
	}

	@Override 
	public Command getValue() {
		final int growth = ((Long) getInputValue(0)).intValue();
		
		return new Command() {
			@Override public boolean run() {
				LoadSheddingAdmissionStatusRegistry.setSheddingGrowth(growth);
				return true;
			}
		};
	}

}
