package de.uniol.inf.is.odysseus.p2p_new;

public class P2PNetworkException extends Exception {

	private static final long serialVersionUID = 1L;

	public P2PNetworkException() {
		super();
	}

	public P2PNetworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public P2PNetworkException(String message, Throwable cause) {
		super(message, cause);
	}

	public P2PNetworkException(String message) {
		super(message);
	}

	public P2PNetworkException(Throwable cause) {
		super(cause);
	}
	
}
