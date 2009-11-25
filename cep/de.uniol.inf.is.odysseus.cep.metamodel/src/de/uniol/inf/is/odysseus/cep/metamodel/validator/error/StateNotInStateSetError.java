package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class StateNotInStateSetError extends ValidationError {

	public StateNotInStateSetError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "The state is not an element in the state machines state set.";
	}

}
