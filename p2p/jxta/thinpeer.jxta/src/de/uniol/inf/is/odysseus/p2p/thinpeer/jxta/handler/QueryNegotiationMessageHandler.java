package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.thinpeer.gui.ErrorPopup;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

public class QueryNegotiationMessageHandler extends AbstractMessageHandler {

	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	public QueryNegotiationMessageHandler(ThinPeerJxtaImpl thinPeerJxtaImpl) {
		setInterestedNamespace("QueryNegotiation");
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String action = MessageTool.getMessageElementAsString(namespace,
				"queryAction", (Message) msg);

		if (action.equals("Bidding")) {
			String queryId = MessageTool.getMessageElementAsString(namespace,
					"queryId", (Message) msg);
			PipeAdvertisement pipeAdv = MessageTool.getResponsePipe(namespace,
					(Message) msg, 0);
			PeerAdvertisement peerAdv = MessageTool.getPeerAdvertisement(
					namespace, (Message) msg);
			P2PQuery q = thinPeerJxtaImpl.getQuery(queryId);
			if (q != null && (q instanceof P2PQueryJxtaImpl)) {
				((P2PQueryJxtaImpl) q).addAdminBidding(pipeAdv, peerAdv);
			}

		}
		if (action.equals("ResultStreaming")) {
			StreamHandlerJxtaImpl shandler = new StreamHandlerJxtaImpl(
					MessageTool.getResponsePipe(namespace, (Message) msg, 0),
					MessageTool.getMessageElementAsString(namespace, "queryId",
							(Message) msg), thinPeerJxtaImpl);
			Thread t = new Thread(shandler);
			t.start();
		}
		if (action.equals("UnknownSource")) {
			String queryId = MessageTool.getMessageElementAsString(
					"UnknownSource", "query", (Message) msg);
			thinPeerJxtaImpl.removeQuery(queryId);
			thinPeerJxtaImpl.getGui().removeTab(queryId);
			new ErrorPopup("Fehler bei der Uebersetzung der Anfrage.");
		}

		if (action.equals("QueryFailed")) {
			String queryId = MessageTool.getMessageElementAsString(
					"QueryFailed", "queryId", (Message) msg);
			thinPeerJxtaImpl.removeQuery(queryId);
			thinPeerJxtaImpl.getGui().removeTab(queryId);
			new ErrorPopup(
					"<html>Anfrage konnte nicht verteilt werden.<br />Zu wenig Angebote.</html>");
		}
	}
}
