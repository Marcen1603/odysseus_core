package mep;

import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;

public class AddQueryToAdmissionFunktion extends AbstractFunction<Command> {

	private static final long serialVersionUID = 8903365960756247370L;
	
	public AddQueryToAdmissionFunktion() {
		super("addQueryToAdmission", 2, new SDFDatatype[][] {{SDFDatatype.INTEGER}, {SDFDatatype.STRING}}, SDFDatatype.COMMAND);
	}

	@Override 
	public Command getValue() {
		final int queryID = (int) getInputValue(0);
		final String loadSheddingComponent = (String) getInputValue(1);
		
		return new Command() {
			@Override public boolean run() {
				if (LoadSheddingAdmissionStatusRegistry.hasLoadSheddingAdmissionComponent(loadSheddingComponent)) {
					LoadSheddingAdmissionStatusRegistry.getLoadSheddingAdmissionComponent(loadSheddingComponent).addQuery(queryID);
				}
				return true;
			}
		};
	}

}
