package de.uniol.inf.is.odysseus.peer.distribute;

public class QueryPartModificationException extends Exception {

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
