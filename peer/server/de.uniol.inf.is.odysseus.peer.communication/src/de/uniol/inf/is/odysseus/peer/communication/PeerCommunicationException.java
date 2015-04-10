package de.uniol.inf.is.odysseus.peer.communication;

public class PeerCommunicationException extends Exception {

	private static final long serialVersionUID = 1L;

	public PeerCommunicationException() {
		super();
	}

	public PeerCommunicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PeerCommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PeerCommunicationException(String message) {
		super(message);
	}

	public PeerCommunicationException(Throwable cause) {
		super(cause);
	}
}
