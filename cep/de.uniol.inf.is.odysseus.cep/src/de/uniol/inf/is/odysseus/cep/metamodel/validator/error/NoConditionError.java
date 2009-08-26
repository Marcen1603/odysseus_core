package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class NoConditionError extends ValidationError {

	public NoConditionError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "Transition has no Condition.";
	}

}
