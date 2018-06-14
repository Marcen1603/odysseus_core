package de.uniol.inf.is.odysseus.net.discovery;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;

public interface IOdysseusNodeDiscoverer {

	public void start(IOdysseusNodeManager manager, IOdysseusNode localNode) throws OdysseusNetDiscoveryException;
	public void stop();
	public boolean isStarted();
	public boolean isStopped();
	
}
