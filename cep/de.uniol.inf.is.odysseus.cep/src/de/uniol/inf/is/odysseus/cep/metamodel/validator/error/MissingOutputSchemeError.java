package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class MissingOutputSchemeError extends ValidationError {

	public MissingOutputSchemeError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "State machine has no output scheme.";
	}
}
