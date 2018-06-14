package de.uniol.inf.is.odysseus.net.querydistribute;

import de.uniol.inf.is.odysseus.net.OdysseusNetException;

public class DistributionCheckException extends OdysseusNetException {

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
