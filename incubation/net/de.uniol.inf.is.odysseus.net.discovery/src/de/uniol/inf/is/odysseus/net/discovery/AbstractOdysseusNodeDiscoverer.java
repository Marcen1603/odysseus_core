package de.uniol.inf.is.odysseus.net.discovery;

import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetStartupData;

public abstract class AbstractOdysseusNodeDiscoverer implements IOdysseusNodeDiscoverer {

	private boolean started = false;
	private IOdysseusNodeManager manager;
	private OdysseusNetStartupData data;

	@Override
	public final void start(IOdysseusNodeManager manager, OdysseusNetStartupData data) throws OdysseusNetDiscoveryException {
		this.manager = manager;
		this.data = data;
		
		startImpl(manager, data);
		
		started = true;
	}

	protected abstract void startImpl(IOdysseusNodeManager manager, OdysseusNetStartupData data) throws OdysseusNetDiscoveryException;
	
	protected final IOdysseusNodeManager getNodeManager() {
		return manager;
	}
	
	protected final OdysseusNetStartupData getStartupData() {
		return data;
	}
	
	@Override
	public final void stop() {
		stopImpl();
		started = false;
	}

	protected abstract void stopImpl();

	@Override
	public final boolean isStarted() {
		return started;
	}

	@Override
	public final boolean isStopped() {
		return !isStarted();
	}

}
