package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class InvalidVariableNameError extends ValidationError {

	public InvalidVariableNameError() {
		super();
	}

	@Override
	public String getErrorMessage() {
		return "The variable name is invalid due to a violation of name restrictions.";
	}
}
