package de.uniol.inf.is.odysseus.p2p_new;

public class InvalidP2PSource extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidP2PSource() {
		super();
	}

	public InvalidP2PSource(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidP2PSource(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidP2PSource(String message) {
		super(message);
	}

	public InvalidP2PSource(Throwable cause) {
		super(cause);
	}
}
