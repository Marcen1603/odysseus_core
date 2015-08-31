package de.uniol.inf.is.odysseus.net.discovery;

import de.uniol.inf.is.odysseus.net.INodeManager;

public interface INodeDiscoverer {

	public void start(INodeManager manager) throws OdysseusNetDiscoveryException;
	public void stop();
	public boolean isStarted();
	public boolean isStopped();
	
}
