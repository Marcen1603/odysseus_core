package de.uniol.inf.is.odysseus.admission.status.mep;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LoadSheddingAdmissionStatusRegistry;
import de.uniol.inf.is.odysseus.admission.status.loadshedding.QuerySelectionStrategy;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ChooseLoadSheddingSelectionStrategy extends AbstractFunction<Command> {

	private static final long serialVersionUID = 1009270106426313893L;

	public ChooseLoadSheddingSelectionStrategy() {
		super("chooseLoadSheddingSelectionStrategy", 1, null, SDFDatatype.COMMAND);
	}

	@Override 
	public Command getValue() {
		final String strategy = (String) getInputValue(0);
		
		return new Command() {
			@Override public boolean run() {
				switch(strategy) {
				case "equally":
					LoadSheddingAdmissionStatusRegistry.setSelectionStrategy(QuerySelectionStrategy.EQUALLY);
					break;
				case "separately":
					LoadSheddingAdmissionStatusRegistry.setSelectionStrategy(QuerySelectionStrategy.SEPARATELY);
					break;
				case "default":
					LoadSheddingAdmissionStatusRegistry.setSelectionStrategy(QuerySelectionStrategy.DEFAULT);
					break;
				}
				
				return true;
			}
		};
	}


}
