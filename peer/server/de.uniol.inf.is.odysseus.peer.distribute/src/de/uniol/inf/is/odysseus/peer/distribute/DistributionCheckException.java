package de.uniol.inf.is.odysseus.peer.distribute;

public class DistributionCheckException extends Exception {

	private static final long serialVersionUID = 1L;

	public DistributionCheckException() {
		super();
	}

	public DistributionCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DistributionCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public DistributionCheckException(String message) {
		super(message);
	}

	public DistributionCheckException(Throwable cause) {
		super(cause);
	}
}
