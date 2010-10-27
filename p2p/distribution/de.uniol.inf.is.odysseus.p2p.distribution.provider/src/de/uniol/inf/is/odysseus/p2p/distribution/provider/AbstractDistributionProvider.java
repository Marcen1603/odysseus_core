package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.IClientSelectorFactory;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.peer.IPeer;

public abstract class AbstractDistributionProvider<R> implements IDistributionProvider<R> {

	private Logger logger;
	private AbstractPeer peer;
	private IClientSelectorFactory<IExecutionListenerCallback> clientSelectorFactory;
	private List<IMessageHandler> registeredMessageHandler;
	private List<IExecutionHandler> registeredExecutionHandler;
	private IExecutionListenerCallback callback;
	
	public AbstractDistributionProvider () {
		this.logger = LoggerFactory.getLogger(AbstractDistributionProvider.class);
		this.registeredMessageHandler = new ArrayList<IMessageHandler>();
		this.registeredExecutionHandler = new ArrayList<IExecutionHandler>();
	}
	
	@Override
	public void setPeer(IPeer peer) {
		this.peer = (AbstractPeer) peer;
	}
	
	@Override
	public abstract void distributePlan(Query q, R serverResponse);
	
	@Override
	public abstract String getDistributionStrategy();

	@Override
	public abstract void initializeService();

	@Override
	public abstract void finalizeService();
	
	@Override
	public abstract void startService();
	
	protected AbstractPeer getPeer() {
		return this.peer;
	}
	
//	public void bindExecutionHandlerFactory(IExecutionHandlerFactory factory) {
//		
//		try {
//			getLogger().info(
//					"Binding ExecutionHandlerFactory: " + factory.getName());
//			this.executionHandlerFactory = factory;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void unbindExecutionHandlerFactory(IExecutionHandlerFactory factory) {
//		if(this.executionHandlerFactory == factory) {
//			this.executionHandlerFactory = null;
//		}
//	}
//
//	@Override
//	public IExecutionHandlerFactory getExecutionHandlerFactory() {
//		return this.executionHandlerFactory;
//	}

	@Override
	public IClientSelectorFactory<IExecutionListenerCallback> getClientSelectorFactory() {
		return this.clientSelectorFactory;
	}
	
	public void bindClientSelectorFactory(IClientSelectorFactory<IExecutionListenerCallback> selector) {
		getLogger().info("Binding Client Selector Factory: "+selector.getName());
		this.clientSelectorFactory = selector;
	}
	
	public void unbindClientSelectorFactory(IClientSelectorFactory<IExecutionListenerCallback> selector) {
		if(this.clientSelectorFactory == selector) {
			this.clientSelectorFactory = null;
		}
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	
	protected List<IMessageHandler> getRegisteredMessageHandler() {
		return registeredMessageHandler;
	}
	
	public List<IExecutionHandler> getRegisteredExecutionHandler() {
		return registeredExecutionHandler;
	}

	public void setCallback(IExecutionListenerCallback callback) {
		this.callback = callback;
	}

	public IExecutionListenerCallback getCallback() {
		return callback;
	}
	
}
