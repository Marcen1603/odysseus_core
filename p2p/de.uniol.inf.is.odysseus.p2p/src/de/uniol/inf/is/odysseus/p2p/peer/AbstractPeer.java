package de.uniol.inf.is.odysseus.p2p.peer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandlerFactory;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerFactory;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

/**
 * 
 * 
 * @author Mart Köhler
 * 
 */
public abstract class AbstractPeer implements IPeer {

	private Logger logger;
	private HashMap<Query, IExecutionListener > queries;
	private HashMap<Lifecycle, List<IExecutionHandlerFactory>> executionHandlerFactoryList;
	private List<IMessageHandler> messageHandlerList;
	private ISocketServerListener socketServerListener;
	private IExecutionListenerFactory executionListenerFactory;

	public void addExecutionHandlerFactory(IExecutionHandlerFactory factory) {
		if(this.executionHandlerFactoryList.containsKey(factory.getProvidedLifecycle())) {
			this.executionHandlerFactoryList.get(factory.getProvidedLifecycle()).add(factory);
		}
		else {
			List<IExecutionHandlerFactory> tempFactory = new ArrayList<IExecutionHandlerFactory>();
			tempFactory.add(factory);
			this.executionHandlerFactoryList.put(factory.getProvidedLifecycle(), tempFactory);
		}
	}
	
	public void removeExecutionHandlerFactory(IExecutionHandlerFactory factory) {
		if(this.executionHandlerFactoryList.containsKey(factory.getProvidedLifecycle())) {
			this.executionHandlerFactoryList.get(factory.getProvidedLifecycle()).remove(factory);
		}
	}
	
	public void bindExecutionListenerFactory(IExecutionListenerFactory factory) {
		getLogger().info("Binding ExecutionListenerFactory");
		this.executionListenerFactory = factory;
	}
	
	public void unbindExecutionListenerFactory(IExecutionListenerFactory factory) {
		if(this.executionListenerFactory == factory) {
			this.executionListenerFactory = null;
		}
	}
	
	public AbstractPeer() {
		this.logger = LoggerFactory.getLogger(AbstractPeer.class);
		this.setQueries(new HashMap<Query, IExecutionListener>());
		this.messageHandlerList = new ArrayList<IMessageHandler>();
		this.executionHandlerFactoryList = new HashMap<Lifecycle, List<IExecutionHandlerFactory>>();
	
	}
	
	@Override
	public abstract void startPeer();

	@Override
	public abstract void stopPeer();
	
	public abstract Object getServerResponseAddress();
	
	public void registerMessageHandler(
			IMessageHandler messageHandler) {
		this.messageHandlerList.add(messageHandler);
	}

	public void registerMessageHandler(
			List<IMessageHandler> messageHandler) {
		for (IMessageHandler iMessageHandler : messageHandler) {
			registerMessageHandler(iMessageHandler);
		}
	}

	public Logger getLogger() {
		return logger;
	}

	protected ISocketServerListener getSocketServerListener() {
		return this.socketServerListener;
	}
	
	protected void setSocketServerListener(ISocketServerListener ssl) {
		this.socketServerListener = ssl;
	}

	public void setQueries(HashMap<Query, IExecutionListener > queries) {
		this.queries = queries;
	}

	public HashMap<Query, IExecutionListener > getQueries() {
		return queries;
	}
	
	
	public void addQuery(Query query) {
		List<IExecutionHandler<? extends IPeer>> execHandlerList = new ArrayList<IExecutionHandler<? extends IPeer>>();
		for(List<IExecutionHandlerFactory> factoryList: getExecutionHandlerFactoryList().values()) {
			for(IExecutionHandlerFactory factory : factoryList) {
				execHandlerList.add(factory.getNewInstance());
			}
		}
		IExecutionListener listener = getExecutionListenerFactory().getNewInstance(query, execHandlerList);
		getQueries().put(query, listener);
		listener.startListener();
		
	}

	public IExecutionListenerFactory getExecutionListenerFactory() {
		return this.executionListenerFactory;
	}

	public void removeQuery(Query query) {
		@SuppressWarnings("unused")
		IExecutionListener listener = getQueries().remove(query);
		listener = null;
	}
	
	public void deregisterMessageHandler(IMessageHandler messageHandler) {
		getMessageHandlerList().remove(messageHandler);
	}
	
	public List<IMessageHandler> getMessageHandlerList() {
		return messageHandlerList;
	}
	
	public List<IExecutionHandler<? extends IPeer>> getExecutionHandler() {
		List<IExecutionHandler<? extends IPeer>> handlerList = new ArrayList<IExecutionHandler<? extends IPeer>>();
		for(List<IExecutionHandlerFactory> factoryList : getExecutionHandlerFactoryList().values()) {
			for (IExecutionHandlerFactory factory : factoryList) {
				handlerList.add(factory.getNewInstance());
			}
		}
		return handlerList;
	}
	
	protected HashMap<Lifecycle, List<IExecutionHandlerFactory>> getExecutionHandlerFactoryList() {
		return executionHandlerFactoryList;
	}
	
}
