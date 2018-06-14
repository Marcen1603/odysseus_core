package de.uniol.inf.is.odysseus.net.connect;

public interface IOdysseusNodeConnectorCallback {

	public void successfulConnection(IOdysseusNodeConnection establishedConnection);
	public void failedConnection(Throwable cause);
	
}
