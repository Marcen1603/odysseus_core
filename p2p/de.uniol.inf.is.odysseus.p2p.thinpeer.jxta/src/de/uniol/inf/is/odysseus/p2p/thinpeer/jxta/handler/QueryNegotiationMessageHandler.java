package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
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
				namespace, "QueryAction", (Message)msg);
		String bid = MessageTool.getMessageElementAsString(
				namespace, "ExecutionBid", (Message)msg);
		PipeAdvertisement pipeAdv = MessageTool
				.getResponsePipe(namespace, (Message)msg, 0);
		
		String peerId = MessageTool.getMessageElementAsString(
				namespace, "peerId", (Message)msg);
		String subplanId = MessageTool.getMessageElementAsString(
				namespace, "subplanId", (Message)msg);
		
		
		if (action.equals("Bidding")) {
			String query = MessageTool.getMessageElementAsString(
					"Bidding", "query", (Message)msg);
			PeerAdvertisement peerAdv = MessageTool
					.getPeerAdvertisement("Bidding", (Message)msg);
			((QueryJxtaImpl) ThinPeerJxtaImpl.getInstance()
					.getQueries().get(query)).addAdminBidding(pipeAdv,
					peerAdv);
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
		if (action.equals("ResultStreaming")) {
			StreamHandlerJxtaImpl shandler = new StreamHandlerJxtaImpl(
					MessageTool.getResponsePipe("ResultStreaming",
							(Message)msg, 0), MessageTool
							.getMessageElementAsString(
									"ResultStreaming", "queryId",
									(Message)msg));
			Thread t = new Thread(shandler);
			System.out.println("will StreamHandler Thread ausführen");
			t.start();
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

}
