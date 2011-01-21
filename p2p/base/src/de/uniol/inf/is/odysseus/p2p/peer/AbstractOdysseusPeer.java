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
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

/**
 * 
 * 
 * @author Mart Koehler
 * 
 */
public abstract class AbstractOdysseusPeer implements IOdysseusPeer {

	static private Logger logger = LoggerFactory
			.getLogger(AbstractOdysseusPeer.class);;
	private HashMap<P2PQuery, IExecutionListener> queries;
	private List<IExecutionHandler<?>> executionHandler;
	private IMessageSender<?, ?, ?> messageSender;
	final private ISocketServerListener socketServerListener;
	private IExecutionListenerFactory executionListenerFactory;

	public AbstractOdysseusPeer(ISocketServerListener socketServerListener) {
		this.queries = new HashMap<P2PQuery, IExecutionListener>();
		this.executionHandler = new ArrayList<IExecutionHandler<?>>();
		this.socketServerListener = socketServerListener;
	}

	@Override
	public abstract void startPeer();

	@Override
	public abstract void stopPeer();

	@Override
	public abstract void initLocalMessageHandler();

	@Override
	public abstract void initMessageSender();

	@Override
	public abstract void initLocalExecutionHandler();

	@Override
	public abstract Object getServerResponseAddress();

	@Override
	public void bindExecutionListenerFactory(IExecutionListenerFactory factory) {
		logger.info(
				"Binding ExecutionListenerFactory: " + factory.getName());
		this.executionListenerFactory = factory;
	}

	@Override
	public void unbindExecutionListenerFactory(IExecutionListenerFactory factory) {
		if (this.executionListenerFactory == factory) {
			this.executionListenerFactory = null;
		}
	}

	@Override
	public void bindExecutionHandler(IExecutionHandler<?> handler) {
		logger.info("Binding Execution Handler: " + handler.getName());
		if (handler.getPeer() == null) {
			handler.setPeer(this);
		}
		this.executionHandler.add(handler);
	}

	@Override
	public void unbindExecutionHandler(IExecutionHandler<?> handler) {
		if (this.executionHandler.contains(handler)) {
			this.executionHandler.remove(handler);
		}
	}

	@Override
	public synchronized void registerMessageHandler(
			IMessageHandler messageHandler) {
		getSocketServerListener().registerMessageHandler(messageHandler);
	}

	@Override
	public synchronized void registerMessageHandler(
			List<IMessageHandler> messageHandler) {
		getSocketServerListener().registerMessageHandler(messageHandler);
	}

	protected ISocketServerListener getSocketServerListener() {
		return this.socketServerListener;
	}

	@Override
	public HashMap<P2PQuery, IExecutionListener> getQueries() {
		return queries;
	}

	@Override
	public void addQuery(P2PQuery query) {
		// Fuer alle Peers, welche die Ausfuehrungsumgebung nutzen
		if (getExecutionListenerFactory() != null) {
			boolean contain = false;
			P2PQuery actualQuery = null;
			// Find query with id ...??
			for (P2PQuery q : getQueries().keySet()) {
				if (q.getId().equals(query.getId())) {
					contain = true;
					actualQuery = q;
					break;
				}
			}
			if (contain) {
				Collection<Subplan> sub = query.getSubPlans().values();
				actualQuery.addSubPlan(""
						+ (actualQuery.getSubPlans().size() + 1), sub
						.iterator().next());
			} else {
				List<IExecutionHandler<?>> executionHandlerCopy = new ArrayList<IExecutionHandler<?>>();
				for (IExecutionHandler<?> handler : getExecutionHandler()) {
					executionHandlerCopy.add(handler.clone());
				}

				IExecutionListener listener = getExecutionListenerFactory()
						.getNewInstance(query, executionHandlerCopy);
				getQueries().put(query, listener);
				// listener.startListener();
			}
		}
		// Für alle Peers bspw. Thin-Peer, welche nicht aktiv an der
		// Anfrageausführung mitwirken und daher nur die Anfrage intern in
		// einer Liste verwalten möchten
		// Kann zu Fehlern führen, wenn man nicht beachtet, dass value null
		// ist.
		else {
			if (!getQueries().containsKey(query)) {
				getQueries().put(query, null);
			}
		}

	}

	protected IExecutionListenerFactory getExecutionListenerFactory() {
		return this.executionListenerFactory;
	}

	@Override
	public void removeQuery(P2PQuery query) {
		@SuppressWarnings("unused")
		IExecutionListener listener = getQueries().remove(query);
		listener = null;
	}

	@Override
	public void deregisterMessageHandler(IMessageHandler messageHandler) {
		getSocketServerListener().deregisterMessageHandler(messageHandler);
	}

	protected synchronized Collection<IMessageHandler> getMessageHandlerList() {
		return getSocketServerListener().getMessageHandler();
	}

	protected synchronized List<IExecutionHandler<?>> getExecutionHandler() {
		return this.executionHandler;
	}

	@Override
	public IMessageSender<?, ?, ?> getMessageSender() {
		return messageSender;
	}

	protected void setMessageSender(IMessageSender<?, ?, ?> messageSender) {
		this.messageSender = messageSender;
	}
}
