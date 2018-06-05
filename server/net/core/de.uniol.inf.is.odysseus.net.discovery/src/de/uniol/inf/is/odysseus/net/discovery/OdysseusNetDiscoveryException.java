package de.uniol.inf.is.odysseus.net.discovery;

import de.uniol.inf.is.odysseus.net.OdysseusNetException;

public class OdysseusNetDiscoveryException extends OdysseusNetException {

	private static final long serialVersionUID = 1L;

	public OdysseusNetDiscoveryException() {
		super();
	}

	public OdysseusNetDiscoveryException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public OdysseusNetDiscoveryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OdysseusNetDiscoveryException(String arg0) {
		super(arg0);
	}

	public OdysseusNetDiscoveryException(Throwable arg0) {
		super(arg0);
	}
}
