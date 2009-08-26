package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class MissingSymbolTableOperationError extends ValidationError {

	public MissingSymbolTableOperationError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "Missing symbol table operation.";
	}
}
