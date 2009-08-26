package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class NoExpressionLabelError extends ValidationError {

	public NoExpressionLabelError() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "The transition has no label.";
	}
}
