package de.uniol.inf.is.odysseus.cep.metamodel.validator.error;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationError;

public class InvalidCharactersInNameError extends ValidationError {

	public InvalidCharactersInNameError() {
		super();
	}

	@Override
	public String getErrorMessage() {
		return "Name contains invalid characters. Names are only allowed to consist of letters and digits and have to start with a letter.";
	}
}
