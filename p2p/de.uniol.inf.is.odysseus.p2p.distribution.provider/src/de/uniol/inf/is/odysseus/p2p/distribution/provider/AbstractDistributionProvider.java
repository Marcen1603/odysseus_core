package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandlerFactory;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public abstract class AbstractDistributionProvider<T extends AbstractPeer> implements IDistributionProvider<T> {

	private T peer;
	private IExecutionHandlerFactory executionHandlerFactory;
	
	@Override
	public void setPeer(T peer) {
		this.peer = peer;
	}
	
	@Override
	public abstract void distributePlan(Query query, Object serverResponse);

	@Override
	public abstract String getDistributionStrategy();

	@Override
	public abstract void initializeService();

	@Override
	public abstract void finalizeService();
	
	@Override
	public abstract void startService();
	
	protected T getPeer() {
		return this.peer;
	}
	
	public void bindExecutionHandlerFactory(IExecutionHandlerFactory factory) {
		this.executionHandlerFactory = factory;
	}
	
	public void unbindExecutionHandlerFactory(IExecutionHandlerFactory factory) {
		if(this.executionHandlerFactory == factory) {
			this.executionHandlerFactory = null;
		}
	}

	public IExecutionHandlerFactory getExecutionHandlerFactory() {
		return this.executionHandlerFactory;
	}
	
}
