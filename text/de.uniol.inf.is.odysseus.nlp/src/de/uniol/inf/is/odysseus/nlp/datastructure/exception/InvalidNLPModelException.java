package de.uniol.inf.is.odysseus.nlp.datastructure.exception;

public class InvalidNLPModelException extends NLPException {
	private static final long serialVersionUID = 2535669934436783086L;

	public InvalidNLPModelException(String model) {
		super("Model:" + model +" not found");
	}


}
