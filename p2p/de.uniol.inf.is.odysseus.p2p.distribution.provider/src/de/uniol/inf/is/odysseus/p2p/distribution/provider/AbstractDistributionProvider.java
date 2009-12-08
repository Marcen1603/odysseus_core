package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.distribution.provider.selector.IClientSelectorFactory;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandlerFactory;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;

public abstract class AbstractDistributionProvider<P extends AbstractPeer> implements IDistributionProvider<P> {

	private Logger logger;
	private P peer;
	private IExecutionHandlerFactory executionHandlerFactory;
	private IClientSelectorFactory clientSelectorFactory;
	
	public AbstractDistributionProvider () {
		this.logger = LoggerFactory.getLogger(AbstractDistributionProvider.class);
	}
	
	@Override
	public void setPeer(P peer) {
		this.peer = peer;
	}
	
	@Override
	public abstract void distributePlan(IExecutionListenerCallback callback,
			Object serverResponse);

	@Override
	public abstract String getDistributionStrategy();

	@Override
	public abstract void initializeService();

	@Override
	public abstract void finalizeService();
	
	@Override
	public abstract void startService();
	
	protected P getPeer() {
		return this.peer;
	}
	
	public void bindExecutionHandlerFactory(IExecutionHandlerFactory factory) {
		
		try {
			getLogger().info(
					"Binding ExecutionHandlerFactory: " + factory.getName());
			this.executionHandlerFactory = factory;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unbindExecutionHandlerFactory(IExecutionHandlerFactory factory) {
		if(this.executionHandlerFactory == factory) {
			this.executionHandlerFactory = null;
		}
	}

	@Override
	public IExecutionHandlerFactory getExecutionHandlerFactory() {
		return this.executionHandlerFactory;
	}

	@Override
	public IClientSelectorFactory getClientSelectorFactory() {
		return this.clientSelectorFactory;
	}
	
	public void bindClientSelectorFactory(IClientSelectorFactory selector) {
		getLogger().info("Binding Client Selector Factory: "+selector.getName());
		this.clientSelectorFactory = selector;
	}
	
	public void unbindClientSelectorFactory(IClientSelectorFactory selector) {
		if(this.clientSelectorFactory == selector) {
			this.clientSelectorFactory = null;
		}
	}
	
	public Logger getLogger() {
		return logger;
	}
	
}
