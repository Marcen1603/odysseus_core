package de.uniol.inf.is.odysseus.net.connect;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IOdysseusNodeConnector {

	public IOdysseusNodeConnection connect( IOdysseusNode node ) throws OdysseusNetConnectionException;
	public void disconnect( IOdysseusNode node );
	
	public boolean isConnected(IOdysseusNode node);
	
}
