package de.uniol.inf.is.odysseus.nlp.datastructure.exception;

import java.io.IOException;

public class NLPModelSerializationFailed extends RuntimeException {

	public NLPModelSerializationFailed(String message) {
		super(message);
	}
	
	private static final long serialVersionUID = 1055587850843151874L;

}
