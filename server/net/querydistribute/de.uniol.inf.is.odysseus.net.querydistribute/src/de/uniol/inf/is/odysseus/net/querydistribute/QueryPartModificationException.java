package de.uniol.inf.is.odysseus.net.querydistribute;

import de.uniol.inf.is.odysseus.net.OdysseusNetException;

public class QueryPartModificationException extends OdysseusNetException {

	private static final long serialVersionUID = 1L;

	public QueryPartModificationException() {
		super();
	}

	public QueryPartModificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public QueryPartModificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryPartModificationException(String message) {
		super(message);
	}

	public QueryPartModificationException(Throwable cause) {
		super(cause);
	}
}
