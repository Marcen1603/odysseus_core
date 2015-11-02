package de.uniol.inf.is.odysseus.net.connect;

import java.util.Collection;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IOdysseusNodeConnector {

	public IOdysseusNodeConnection connect( IOdysseusNode node ) throws OdysseusNetConnectionException;
	public void disconnect( IOdysseusNode node );
	
	public Optional<IOdysseusNodeConnection> getConnection(IOdysseusNode node);
	public Collection<IOdysseusNodeConnection> getConnections();
	
	public boolean isConnected(IOdysseusNode node);
	
}
