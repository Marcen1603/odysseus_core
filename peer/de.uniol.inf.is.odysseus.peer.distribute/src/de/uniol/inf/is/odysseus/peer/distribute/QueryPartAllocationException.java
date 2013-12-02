package de.uniol.inf.is.odysseus.peer.distribute;

public class QueryPartAllocationException extends Exception {

	private static final long serialVersionUID = 1L;

	public QueryPartAllocationException() {
		super();
	}

	public QueryPartAllocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public QueryPartAllocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryPartAllocationException(String message) {
		super(message);
	}

	public QueryPartAllocationException(Throwable cause) {
		super(cause);
	}
}
