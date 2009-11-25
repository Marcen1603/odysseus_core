package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class MissingAcceptingStateError extends ValidationError {

	 public MissingAcceptingStateError() {
		super();
	 }
	 
	 @Override
	public String getErrorMessage() {
		return "The state machine has no reachable accepting state.";
	}
}
