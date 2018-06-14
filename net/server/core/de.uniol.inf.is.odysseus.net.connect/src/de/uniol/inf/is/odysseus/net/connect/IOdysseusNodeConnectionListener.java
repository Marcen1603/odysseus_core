package de.uniol.inf.is.odysseus.net.connect;

public interface IOdysseusNodeConnectionListener {

	public void messageReceived( IOdysseusNodeConnection connection, byte[] data );
	public void disconnected( IOdysseusNodeConnection connection);
	
}
