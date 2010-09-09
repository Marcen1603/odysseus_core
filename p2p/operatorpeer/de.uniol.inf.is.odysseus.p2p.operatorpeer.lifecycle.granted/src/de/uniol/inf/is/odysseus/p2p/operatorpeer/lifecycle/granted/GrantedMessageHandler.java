package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.granted;


import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class GrantedMessageHandler implements IMessageHandler {

	private Query query = null;
	private String namespace = null;
	private IExecutionListenerCallback callback;
	private IMessageSender messageSender;
	private boolean granted = false;
	
	public GrantedMessageHandler(IExecutionListenerCallback callback, String namespace, IMessageSender sender) {
		this.setQuery(callback.getQuery());
		setInterestedNamespace(namespace);
		this.callback = callback;
		this.messageSender = sender;
	}

	@Override
	public String getInterestedNamespace() {
		return this.namespace;
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
		System.out.println("Erhalte im MessageHandler querid "+query+" subplanid "+subplanId);
		//id muss eigentlich immer stimmen


		
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		//Sende Anfrage an die bestätigten Peers
		messageElements.put("queryId", query);
		messageElements.put("subplanId", subplanId);
//		messageElements.put("peerId", PeerGroupTool.getPeerGroup().getPeerID().toString());

//		messageElements.put("pipeAdvertisement", (PipeAdvertisement)getPeer().getServerResponseAddress());
//		for(Subplan s : getExecutionListenerCallback().getQuery().getSubPlans().values()) {
//			if(s.getBiddings().get(0) instanceof BidJxtaImpl) {
//				messageElements.put("subplanId", s.getId());
			
//				this.messageSender.sendMessage(PeerGroupTool.getPeerGroup(), MessageTool
//						.createSimpleMessage("Granted"+query, messageElements), ((BidJxtaImpl)s.getBiddings().get(0)).getResponseSocket());
		System.out.println("Sende Granted Antwort zu Teilplan "+subplanId);
		this.messageSender.sendMessage(PeerGroupTool.getPeerGroup(), MessageTool
				.createSimpleMessage("Granted"+query, messageElements), pipeAdv);
//			}
//			
//		}
	}

	public IExecutionListenerCallback getCallback() {
		return callback;
	}

	@Override
	public void setInterestedNamespace(String namespace) {
		this.namespace = namespace;
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
