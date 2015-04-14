package de.uniol.inf.is.odysseus.peer.transmission;

public class DataTransmissionException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataTransmissionException() {
		super();
	}

	public DataTransmissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DataTransmissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataTransmissionException(String message) {
		super(message);
	}

	public DataTransmissionException(Throwable cause) {
		super(cause);
	}
}
