package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class MissingAttributeIdentifierError extends ValidationError {

	public MissingAttributeIdentifierError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "Missing attribute identifier.";
	}
}
