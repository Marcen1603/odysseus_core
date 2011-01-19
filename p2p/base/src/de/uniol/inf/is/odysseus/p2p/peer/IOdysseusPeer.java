package de.uniol.inf.is.odysseus.p2p.peer;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerFactory;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IOdysseusPeer extends IPeer {
	void registerMessageHandler(IMessageHandler messageHandler);
	Object getServerResponseAddress();
	void bindExecutionListenerFactory(IExecutionListenerFactory factory);
	void unbindExecutionListenerFactory(IExecutionListenerFactory factory);
	void bindExecutionHandler(IExecutionHandler<?> handler);
	void unbindExecutionHandler(IExecutionHandler<?> handler);
	void initLocalMessageHandler();
	void initMessageSender();
	void initLocalExecutionHandler();
	void registerMessageHandler(List<IMessageHandler> messageHandler);
	HashMap<Query, IExecutionListener> getQueries();
	void addQuery(Query query);
	void removeQuery(Query query);
	void deregisterMessageHandler(IMessageHandler messageHandler);
	IMessageSender<?,?,?> getMessageSender();

}
