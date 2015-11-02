package de.uniol.inf.is.odysseus.net.connect;

public interface IOdysseusNodeConnection {

	public void send( byte[] data ) throws OdysseusNetConnectionException;
	public void disconnect();
	
	public void addListener( IOdysseusNodeConnectionListener listener );
	public void removeListener( IOdysseusNodeConnectionListener listener );
	
}
