package de.uniol.inf.is.odysseus.core.server.distribution;

public class QueryDistributionException extends Exception {

	private static final long serialVersionUID = 1L;

	public QueryDistributionException() {
		super();
	}

	public QueryDistributionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public QueryDistributionException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryDistributionException(String message) {
		super(message);
	}

	public QueryDistributionException(Throwable cause) {
		super(cause);
	}
}
