package de.uniol.inf.is.odysseus.nlp.datastructure.exception;

public class NLPModelStoringFailed extends RuntimeException {
	private static final long serialVersionUID = 1055587850843151874L;

	public NLPModelStoringFailed(String message) {
		super(message);
	}
}
