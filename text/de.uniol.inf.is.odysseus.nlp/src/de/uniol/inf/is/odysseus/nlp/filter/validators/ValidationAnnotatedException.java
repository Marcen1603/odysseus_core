package de.uniol.inf.is.odysseus.nlp.filter.validators;

public class ValidationAnnotatedException extends ValidationException {
	private static final long serialVersionUID = -3836945317783210470L;

	public ValidationAnnotatedException() {
		super("The input stream must be annotated as far as the expressions demand it.");
	}

}
