package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class MissingMatchinStrategyError extends ValidationError {

	public MissingMatchinStrategyError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "State Machine has no Matching Strategy";
	}
}
