package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class NoInitialStateError extends ValidationError  {

	@Override
	public String getErrorMessage() {
		return "The state machine has no initial state.";
	}

}
