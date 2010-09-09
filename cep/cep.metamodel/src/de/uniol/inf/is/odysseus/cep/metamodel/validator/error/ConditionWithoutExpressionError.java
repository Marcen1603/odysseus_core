package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class ConditionWithoutExpressionError extends ValidationError {

	public ConditionWithoutExpressionError() {
		super();
	}

	@Override
	public String getErrorMessage() {
		return "Condition has no expression";
	}
	
}
