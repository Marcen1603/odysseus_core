package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class MissingStateIdentifierError extends ValidationError {

	public MissingStateIdentifierError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "Missing state Identifier.";
	}
	
}
