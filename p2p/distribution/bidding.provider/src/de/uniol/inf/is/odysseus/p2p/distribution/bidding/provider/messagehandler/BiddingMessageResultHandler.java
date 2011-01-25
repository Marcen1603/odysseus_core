package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.IQueryProvider;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class BiddingMessageResultHandler extends AbstractJxtaMessageHandler {

	private IQueryProvider queryProvider;

	public BiddingMessageResultHandler(IQueryProvider queryProvider, ILogListener log) {
		super(log, "BiddingProvider");
		this.queryProvider = queryProvider;
	}

	@Override
	public void handleMessage(Object oMsg, String namespace) {
		Message msg = (Message) oMsg;
		String queryId = meas(namespace, "queryId", msg);
		String bid = meas(namespace, "ExecutionBid", msg);
		PipeAdvertisement pipeAdv = MessageTool.getResponsePipe(namespace, msg,
				0);

		String peerId = meas(namespace, "peerId", msg);
		String subplanId = meas(namespace, "subplanId", msg);
		logAction(queryId, "Bid (" + bid
				+ ") from "+peerId);
		P2PQuery actualQuery = queryProvider.getQuery(queryId);
		if (actualQuery != null && bid.equals("positive")) {
			((P2PQueryJxtaImpl) actualQuery).addBidding(pipeAdv, peerId,
					subplanId, bid);
			addBid(queryId, actualQuery.getSubPlans().get(subplanId)
					.getBiddings().size());
		}
	}

}
