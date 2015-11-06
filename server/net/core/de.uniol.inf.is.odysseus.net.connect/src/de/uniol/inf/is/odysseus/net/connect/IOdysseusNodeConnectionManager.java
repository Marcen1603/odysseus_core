package de.uniol.inf.is.odysseus.net.connect;

import java.util.Collection;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IOdysseusNodeConnectionManager {

	public Collection<IOdysseusNodeConnection> getConnections();
	public Optional<IOdysseusNodeConnection> getConnection(IOdysseusNode node);
	
	public boolean isConnected(IOdysseusNode node);
	
	public void addListener(IOdysseusNodeConnectionManagerListener listener);
	public void removeListener(IOdysseusNodeConnectionManagerListener listener);
}
