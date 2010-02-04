package de.uniol.inf.is.odysseus.p2p.peer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerFactory;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

/**
 * 
 * 
 * @author Mart Köhler
 * 
 */
public abstract class AbstractPeer implements IPeer {

	private Logger logger;
	private HashMap<Query, IExecutionListener > queries;
//	private HashMap<Lifecycle, List<IExecutionHandlerFactory>> executionHandlerFactoryList;
	private List<IExecutionHandler> executionHandler;
	private List<IMessageHandler> messageHandlerList;
	private IMessageSender messageSender;
	private ISocketServerListener socketServerListener;
	private IExecutionListenerFactory executionListenerFactory;

//	public void addExecutionHandlerFactory(IExecutionHandlerFactory factory) {
//		if(this.executionHandlerFactoryList.containsKey(factory.getProvidedLifecycle())) {
//			getLogger().info("Adding ("+factory.getProvidedLifecycle()+") ExecutionHandlerFactory: "+factory.getName());
//			this.executionHandlerFactoryList.get(factory.getProvidedLifecycle()).add(factory);
//		}
//		else {
//			List<IExecutionHandlerFactory> tempFactory = new ArrayList<IExecutionHandlerFactory>();
//			tempFactory.add(factory);
//			getLogger().info("Adding ("+factory.getProvidedLifecycle()+") ExecutionHandlerFactory: "+factory.getName());
//			this.executionHandlerFactoryList.put(factory.getProvidedLifecycle(), tempFactory);
//		}
//	}
//	
//	public void removeExecutionHandlerFactory(IExecutionHandlerFactory factory) {
//		if(this.executionHandlerFactoryList.containsKey(factory.getProvidedLifecycle())) {
//			getLogger().info("Removing ("+factory.getProvidedLifecycle()+") ExecutionHandlerFactory: "+factory.getName());
//			this.executionHandlerFactoryList.get(factory.getProvidedLifecycle()).remove(factory);
//		}
//	}
	
	public void bindExecutionListenerFactory(IExecutionListenerFactory factory) {
		getLogger().info("Binding ExecutionListenerFactory: "+factory.getName());
		this.executionListenerFactory = factory;
	}
	
	public void unbindExecutionListenerFactory(IExecutionListenerFactory factory) {
		if(this.executionListenerFactory == factory) {
			this.executionListenerFactory = null;
		}
	}
	
	public void bindExecutionHandler(IExecutionHandler handler) {
		getLogger().info("Binding Execution Handler: "+handler.getName());
		if(handler.getPeer()==null) {
			handler.setPeer(this);
		}
		this.executionHandler.add(handler); 
	}
	public void unbindExecutionHandler(IExecutionHandler handler) {
		if(this.executionHandler.contains(handler)) {
			this.executionHandler.remove(handler);
		}
	}
	
	
	
	public AbstractPeer() {
		this.logger = LoggerFactory.getLogger(AbstractPeer.class);
		this.queries = new HashMap<Query, IExecutionListener>();
		this.messageHandlerList = new ArrayList<IMessageHandler>();
//		this.executionHandlerFactoryList = new HashMap<Lifecycle, List<IExecutionHandlerFactory>>();
		this.executionHandler = new ArrayList<IExecutionHandler>();
	
	}
	
	@Override
	public abstract void startPeer();

	@Override
	public abstract void stopPeer();
	
	public abstract void initLocalMessageHandler();
	
	public abstract void initMessageSender();
	
	public abstract void initLocalExecutionHandler();
	
	public abstract Object getServerResponseAddress();
	
	public synchronized void registerMessageHandler(
			IMessageHandler messageHandler) {
		if(getSocketServerListener()==null) {
			this.messageHandlerList.add(messageHandler);
		}
		else {
			this.messageHandlerList =  getSocketServerListener().registerMessageHandler(messageHandler);
		}
	}

	public synchronized void registerMessageHandler(
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

	public HashMap<Query, IExecutionListener > getQueries() {
		return queries;
	}
	
	
	public void addQuery(Query query) {
		//Für alle Peers, welche die Ausführungsumgebung nutzen
		if(getExecutionListenerFactory()!=null) {
			boolean contain = false;
			Query actualQuery = null;
			for(Query q : getQueries().keySet()) {
				if(q.getId().equals(query.getId())) {
					contain = true;
					actualQuery = q;
					break;
				}
			}
			if(contain) {				
				Collection<Subplan> sub =  query.getSubPlans().values();
				actualQuery.addSubPlan(""+(actualQuery.getSubPlans().size()+1), sub.iterator().next());
			}
			else {
				List<IExecutionHandler> executionHandlerCopy = new ArrayList<IExecutionHandler>();
				for(IExecutionHandler handler : getExecutionHandler()) {
					try { 
						executionHandlerCopy.add(handler.clone());
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
				
//			
//			List<IExecutionHandler> execHandlerList = new ArrayList<IExecutionHandler>();
//			for(List<IExecutionHandlerFactory> factoryList: getExecutionHandlerFactoryList().values()) {
//				for(IExecutionHandlerFactory factory : factoryList) {
//					execHandlerList.add(factory.getNewInstance());
//				}
//			}
				IExecutionListener listener = getExecutionListenerFactory().getNewInstance(query, executionHandlerCopy);
				getQueries().put(query, listener);
//			listener.startListener();
			}
		}
		//Für alle Peers bspw. Thin-Peer, welche nicht aktiv an der Anfrageausführung mitwirken und daher nur die Anfrage intern in einer Liste verwalten möchten
		//Kann zu Fehlern führen, wenn man nicht beachtet, dass value null ist.
		else {
			if(!getQueries().containsKey(query)) {
				getQueries().put(query, null);
			}
		}
		
	}

	protected IExecutionListenerFactory getExecutionListenerFactory() {
		return this.executionListenerFactory;
	}

	public void removeQuery(Query query) {
		@SuppressWarnings("unused")
		IExecutionListener listener = getQueries().remove(query);
		listener = null;
	}
	
	public void deregisterMessageHandler(IMessageHandler messageHandler) {
		if(getSocketServerListener()==null) {
			getMessageHandlerList().remove(messageHandler);
		}
		else{
			getSocketServerListener().deregisterMessageHandler(messageHandler);
		}
		 
	}
	
	protected synchronized List<IMessageHandler> getMessageHandlerList() {
		return messageHandlerList;
	}
	
	protected synchronized List<IExecutionHandler> getExecutionHandler() {
//		List<IExecutionHandler> handlerList = new ArrayList<IExecutionHandler>();
//		for(List<IExecutionHandlerFactory> factoryList : getExecutionHandlerFactoryList().values()) {
//			for (IExecutionHandlerFactory factory : factoryList) {
//				handlerList.add(factory.getNewInstance());
//			}
//		}
//		return handlerList;
		return this.executionHandler;
	}
	
//	protected HashMap<Lifecycle, List<IExecutionHandlerFactory>> getExecutionHandlerFactoryList() {
//		return executionHandlerFactoryList;
//	}
	public IMessageSender getMessageSender() {
		return messageSender;
	}
	
	protected void setMessageSender(IMessageSender messageSender) {
		this.messageSender = messageSender;
	}
}
