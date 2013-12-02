package de.uniol.inf.is.odysseus.peer.distribute;

public class QueryPartitionException extends Exception {

	private static final long serialVersionUID = 1L;

	public QueryPartitionException() {
		super();
	}

	public QueryPartitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public QueryPartitionException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryPartitionException(String message) {
		super(message);
	}

	public QueryPartitionException(Throwable cause) {
		super(cause);
	}
}
