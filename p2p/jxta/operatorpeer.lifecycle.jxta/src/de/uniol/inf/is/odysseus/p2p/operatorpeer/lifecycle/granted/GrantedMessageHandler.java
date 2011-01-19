package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.granted;

import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class GrantedMessageHandler extends AbstractMessageHandler {

	static Logger logger = LoggerFactory.getLogger(GrantedExecutionHandler.class);
	
	private Query query = null;
	private IExecutionListenerCallback callback;
	@SuppressWarnings("rawtypes")
	private IMessageSender messageSender;
	private boolean granted = false;
	
	public GrantedMessageHandler(IExecutionListenerCallback callback, String namespace, IMessageSender<?,?,?> sender) {
		this.setQuery(callback.getQuery());
		setInterestedNamespace(namespace);
		this.callback = callback;
		this.messageSender = sender;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Object msg, String namespace) {
		this.granted = true;
		String query = MessageTool.getMessageElementAsString(
				namespace, "queryId", (Message)msg);
		String subplanId = MessageTool.getMessageElementAsString(
				namespace, "subplanId", (Message)msg);
		PipeAdvertisement pipeAdv = MessageTool
		.getResponsePipe(namespace, (Message)msg, 0);
		logger.debug("Erhalte im MessageHandler querid "+query+" subplanid "+subplanId);
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		//Sende Anfrage an die bestaetigten Peers
		messageElements.put("queryId", query);
		messageElements.put("subplanId", subplanId);
		logger.debug("Sende Granted Antwort zu Teilplan "+subplanId);
		this.messageSender.sendMessage(PeerGroupTool.getPeerGroup(), MessageTool
				.createSimpleMessage("Granted"+query, messageElements), pipeAdv);
	}

	public IExecutionListenerCallback getCallback() {
		return callback;
	}


	public void setQuery(Query query) {
		this.query = query;
	}

	public Query getQuery() {
		return query;
	}

	public boolean isGranted() {
		return granted;
	}


}
