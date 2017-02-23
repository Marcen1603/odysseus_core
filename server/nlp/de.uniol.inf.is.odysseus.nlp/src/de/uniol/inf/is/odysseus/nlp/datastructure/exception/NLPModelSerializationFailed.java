package de.uniol.inf.is.odysseus.nlp.datastructure.exception;

public class NLPModelSerializationFailed extends RuntimeException {
	private static final long serialVersionUID = 1055587850843151874L;

	public NLPModelSerializationFailed(String message) {
		super(message);
	}
}
