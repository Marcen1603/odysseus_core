package de.uniol.inf.is.odysseus.net.communication;

public class OdysseusNodeCommunicationException extends Exception {

	private static final long serialVersionUID = 1L;

	public OdysseusNodeCommunicationException() {
		super();
	}

	public OdysseusNodeCommunicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OdysseusNodeCommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OdysseusNodeCommunicationException(String message) {
		super(message);
	}

	public OdysseusNodeCommunicationException(Throwable cause) {
		super(cause);
	}
}
