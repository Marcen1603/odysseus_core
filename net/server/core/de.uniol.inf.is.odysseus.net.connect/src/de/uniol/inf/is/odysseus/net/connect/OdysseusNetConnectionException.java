package de.uniol.inf.is.odysseus.net.connect;

import de.uniol.inf.is.odysseus.net.OdysseusNetException;

public class OdysseusNetConnectionException extends OdysseusNetException {

	private static final long serialVersionUID = 1L;

	public OdysseusNetConnectionException() {
		super();
	}

	public OdysseusNetConnectionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public OdysseusNetConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OdysseusNetConnectionException(String arg0) {
		super(arg0);
	}

	public OdysseusNetConnectionException(Throwable arg0) {
		super(arg0);
	}
}
