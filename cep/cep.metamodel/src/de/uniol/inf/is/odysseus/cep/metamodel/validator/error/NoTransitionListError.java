package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class NoTransitionListError extends ValidationError {

	public NoTransitionListError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "state has no transition list";
	}

}
