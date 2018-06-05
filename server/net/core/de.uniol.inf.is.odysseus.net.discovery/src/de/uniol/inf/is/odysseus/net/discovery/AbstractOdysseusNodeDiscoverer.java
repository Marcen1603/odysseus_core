package de.uniol.inf.is.odysseus.net.discovery;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;

public abstract class AbstractOdysseusNodeDiscoverer implements IOdysseusNodeDiscoverer {

	private boolean started = false;
	private IOdysseusNodeManager manager;
	private IOdysseusNode localNode;

	@Override
	public final void start(IOdysseusNodeManager manager, IOdysseusNode localNode) throws OdysseusNetDiscoveryException {
		Preconditions.checkNotNull(manager, "manager must not be null!");
		Preconditions.checkNotNull(localNode, "localNode must not be null!");

		this.manager = manager;
		this.localNode = localNode;
		
		startImpl(manager, localNode);
		
		started = true;
	}

	protected abstract void startImpl(IOdysseusNodeManager manager, IOdysseusNode localNode) throws OdysseusNetDiscoveryException;
	
	protected final IOdysseusNodeManager getNodeManager() {
		return manager;
	}
	
	protected final IOdysseusNode getLocalOdysseusNode() {
		return localNode;
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
