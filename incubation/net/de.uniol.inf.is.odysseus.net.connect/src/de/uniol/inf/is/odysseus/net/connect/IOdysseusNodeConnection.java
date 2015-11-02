package de.uniol.inf.is.odysseus.net.connect;

public interface IOdysseusNodeConnection {

	public void sendMessage( byte[] data );
	
	public void addListener( IOdysseusNodeConnectionListener listener );
	public void removeListener( IOdysseusNodeConnectionListener listener );
	
}
