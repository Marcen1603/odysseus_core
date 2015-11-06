package de.uniol.inf.is.odysseus.net;

public class OdysseusNetException extends Exception {

	private static final long serialVersionUID = 1L;

	public OdysseusNetException() {
		super();
	}

	public OdysseusNetException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public OdysseusNetException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OdysseusNetException(String arg0) {
		super(arg0);
	}

	public OdysseusNetException(Throwable arg0) {
		super(arg0);
	}
}
