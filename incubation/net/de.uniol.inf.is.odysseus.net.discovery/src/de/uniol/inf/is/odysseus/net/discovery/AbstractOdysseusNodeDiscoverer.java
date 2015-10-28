package de.uniol.inf.is.odysseus.net.discovery;

import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;

public abstract class AbstractOdysseusNodeDiscoverer implements IOdysseusNodeDiscoverer {

	private boolean started = false;
	private IOdysseusNodeManager manager;

	@Override
	public final void start(IOdysseusNodeManager manager) throws OdysseusNetDiscoveryException {
		this.manager = manager;
		startImpl(manager);
		started = true;
	}

	protected abstract void startImpl(IOdysseusNodeManager manager) throws OdysseusNetDiscoveryException;
	
	protected final IOdysseusNodeManager getNodeManager() {
		return manager;
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
