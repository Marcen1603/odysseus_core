package de.uniol.inf.is.odysseus.net.connect;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IOdysseusNodeConnection {

	public void send( byte[] data ) throws OdysseusNetConnectionException;
	public void disconnect();
	
	public IOdysseusNode getOdysseusNode();
	
	public void addListener( IOdysseusNodeConnectionListener listener );
	public void removeListener( IOdysseusNodeConnectionListener listener );
	
}
