package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class TransitionWithoutDestinationStateError extends ValidationError {

	public TransitionWithoutDestinationStateError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "The transition doesn't have any destination state.";
	}

}
