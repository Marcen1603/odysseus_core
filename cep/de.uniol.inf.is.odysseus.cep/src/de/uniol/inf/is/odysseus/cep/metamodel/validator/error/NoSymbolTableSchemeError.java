package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class NoSymbolTableSchemeError extends ValidationError {

	public NoSymbolTableSchemeError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "State machine has no SymbolTableScheme.";
	}
}
