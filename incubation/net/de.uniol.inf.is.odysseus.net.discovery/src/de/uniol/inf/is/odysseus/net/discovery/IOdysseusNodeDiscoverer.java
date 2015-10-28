package de.uniol.inf.is.odysseus.net.discovery;

import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetStartupData;

public interface IOdysseusNodeDiscoverer {

	public void start(IOdysseusNodeManager manager, OdysseusNetStartupData data) throws OdysseusNetDiscoveryException;
	public void stop();
	public boolean isStarted();
	public boolean isStopped();
	
}
