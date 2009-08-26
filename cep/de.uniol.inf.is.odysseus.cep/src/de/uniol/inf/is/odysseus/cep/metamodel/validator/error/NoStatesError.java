package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class NoStatesError extends ValidationError {

	public NoStatesError() {
		
	}

	@Override
	public String getErrorMessage() {
		return "The state machine has no states.";
	}
	
}
