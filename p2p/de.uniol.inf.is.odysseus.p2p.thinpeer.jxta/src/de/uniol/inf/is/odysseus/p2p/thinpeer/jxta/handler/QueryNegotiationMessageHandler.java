package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.thinpeer.gui.ErrorPopup;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

public class QueryNegotiationMessageHandler implements IMessageHandler {

	@Override
	public String getInterestedNamespace() {
		return "QueryNegotiation";
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String action = MessageTool.getMessageElementAsString(
 				namespace, "queryAction", (Message)msg);
//		String bid = MessageTool.getMessageElementAsString(
//				namespace, "ExecutionBid", (Message)msg);
		
//		String peerId = MessageTool.getMessageElementAsString(
//				namespace, "peerId", (Message)msg);
//		String subplanId = MessageTool.getMessageElementAsString(
//				namespace, "subplanId", (Message)msg);
		
		
		if (action.equals("Bidding")) {
			String queryId = MessageTool.getMessageElementAsString(
					namespace, "queryId", (Message)msg);
			PipeAdvertisement pipeAdv = MessageTool
			.getResponsePipe(namespace, (Message)msg, 0);
			PeerAdvertisement peerAdv = MessageTool
					.getPeerAdvertisement(namespace, (Message)msg);
			for(Query q : ThinPeerJxtaImpl.getInstance().getQueries().keySet()) {
				if(q.getId().equals(queryId) && (q instanceof QueryJxtaImpl)) {
					((QueryJxtaImpl)q).addAdminBidding(pipeAdv, peerAdv);
				}
			}
		}
		if (action.equals("ResultStreaming")) {
			StreamHandlerJxtaImpl shandler = new StreamHandlerJxtaImpl(
					MessageTool.getResponsePipe(namespace,
							(Message)msg, 0), MessageTool
							.getMessageElementAsString(
									namespace, "queryId",
									(Message)msg));
			Thread t = new Thread(shandler);
			t.start();
		}
		if (action.equals("UnknownSource")) {
			String queryId = MessageTool.getMessageElementAsString(
					"UnknownSource", "query", (Message)msg);
			ThinPeerJxtaImpl.getInstance().getQueries().remove(
					queryId);
			ThinPeerJxtaImpl.getInstance().getGui().removeTab(
					queryId);
			new ErrorPopup(
					"Fehler bei der Übersetzung der Anfrage.");
		}
		
		if (action.equals("QueryFailed")) {
			String queryId = MessageTool.getMessageElementAsString(
					"QueryFailed", "queryId", (Message)msg);
			ThinPeerJxtaImpl.getInstance().getQueries().remove(
					queryId);
			ThinPeerJxtaImpl.getInstance().getGui().removeTab(
					queryId);
			new ErrorPopup(
					"<html>Anfrage konnte nicht verteilt werden.<br />Zu wenig Angebote.</html>");
		}
	}

	@Override
	public void setInterestedNamespace(String namespace) {
		// TODO Auto-generated method stub
		
	}

}
