package de.uniol.inf.is.odysseus.p2p.peer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public abstract class AbstractPeer implements IPeer {
	
	private Logger logger;
	private Map<String, IMessageHandler> messageHandler = new HashMap<String, IMessageHandler>();
	private HashMap<String, Query> queries = new HashMap<String, Query>();

	public AbstractPeer() {
		this.logger = LoggerFactory.getLogger(AbstractPeer.class);
	}
	
	

	@Override
	public abstract void startPeer();

	@Override
	public abstract void stopPeer();

	
	public Logger getLogger() {
		return logger;
	}
	
	public void setMessageHandler(Map<String, IMessageHandler> messageHandler) {
		this.messageHandler = messageHandler;
	}

	public synchronized Map<String, IMessageHandler> getMessageHandler() {
		return messageHandler;
	}
	public void setQueries(HashMap<String, Query> queries) {
		this.queries = queries;
	}

	public HashMap<String, Query> getQueries() {
		return queries;
	}
}
