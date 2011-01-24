package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.IQueryProvider;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class BiddingMessageResultHandler extends AbstractMessageHandler {

	private IQueryProvider queryProvider;

	// Soll hier nicht den gesamten ExecutionListener kennen, sondern nur das
	// ihm zugehörige CallbackObjekt
	public BiddingMessageResultHandler(IQueryProvider queryProvider) {
		this.queryProvider = queryProvider;
		setInterestedNamespace("BiddingProvider");
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String queryId = MessageTool.getMessageElementAsString(namespace,
				"queryId", (Message) msg);
		String bid = MessageTool.getMessageElementAsString(namespace,
				"ExecutionBid", (Message) msg);
		PipeAdvertisement pipeAdv = MessageTool.getResponsePipe(namespace,
				(Message) msg, 0);

		String peerId = MessageTool.getMessageElementAsString(namespace,
				"peerId", (Message) msg);
		String subplanId = MessageTool.getMessageElementAsString(namespace,
				"subplanId", (Message) msg);
		Log.logAction(queryId, "Gebot (" + bid
				+ ") von einem Operator-Peer eingegangen.");
		P2PQuery actualQuery = queryProvider.getQuery(queryId);
		if (actualQuery != null && bid.equals("positive")) {
			// Fuege das Gebot dem entsprechenden Subplan der Query hinzu
			((P2PQueryJxtaImpl) actualQuery).addBidding(pipeAdv, peerId,
					subplanId, bid);
			Log.addBid(queryId, actualQuery.getSubPlans().get(subplanId)
					.getBiddings().size());
		}
	}


}
