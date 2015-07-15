package de.uniol.inf.is.odysseus.net.discovery;

import de.uniol.inf.is.odysseus.net.INodeManager;

public abstract class AbstractNodeDiscoverer implements INodeDiscoverer {

	private boolean started = false;
	private INodeManager manager;

	@Override
	public final void start(INodeManager manager) {
		this.manager = manager;
		startImpl(manager);
		
		started = true;
	}

	protected abstract void startImpl(INodeManager manager);
	
	protected final INodeManager getNodeManager() {
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
