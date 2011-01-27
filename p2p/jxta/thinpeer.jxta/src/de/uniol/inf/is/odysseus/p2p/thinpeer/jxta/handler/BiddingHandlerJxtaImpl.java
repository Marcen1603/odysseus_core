package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.strategy.BiddingHandlerStrategyStandard;
import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IBiddingHandlerStrategy;

/** 
 * Der QueryBiddingHandler schickt in regelmaessigen Abstaenden, Antworten auf
 * Bewerbungen von Verwaltungs-Peers heraus. Die Verwaltungs-Peers werden
 * daruber informiert, ob sie eine Anfrage zugeteilt bekommen oder nicht.
 * 
 * @author Christian Zillmann, Marco Grawunder
 * 
 */
public class BiddingHandlerJxtaImpl implements IBiddingHandler {

	static Logger logger = LoggerFactory
			.getLogger(BiddingHandlerJxtaImpl.class);

	// Wie oft werden Antworten auf Bewerbungen herausgeschickt
	private int WAIT_TIME = 10000;
	private P2PQuery query;
	private JxtaMessageSender sender;

	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	public BiddingHandlerJxtaImpl(P2PQuery query, JxtaMessageSender sender,
			ThinPeerJxtaImpl thinPeerJxtaImpl) {
		this.query = query;
		this.sender = sender;
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(WAIT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (getQuery().getStatus() == Lifecycle.NEW) {
			List<Bid> biddings = new ArrayList<Bid>();
			P2PQueryJxtaImpl query = ((P2PQueryJxtaImpl) getQuery());

			for (Bid bid : query.getAdminPeerBidding().values()) {
				biddings.add(bid);
			}

			if (!biddings.isEmpty()) {
				IBiddingHandlerStrategy strategy = new BiddingHandlerStrategyStandard();
				Bid bid = (Bid) strategy.handleBiddings(biddings);

				HashMap<String, Object> messageElements = new HashMap<String, Object>();
				// Send granted message to peer
				messageElements.put("queryId", getQuery().getId());
				messageElements.put("result", "granted");
				getQuery().setStatus(Lifecycle.GRANTED);
				this.sender.sendMessage(thinPeerJxtaImpl.getNetPeerGroup(),
						MessageTool.createOdysseusMessage(
								OdysseusMessageType.BiddingResult,
								messageElements), ((BidJxtaImpl) bid)
								.getResponseSocket(), 10);
				((P2PQueryJxtaImpl) getQuery())
						.setAdminPeerPipe(((BidJxtaImpl) bid)
								.getResponseSocket());
				thinPeerJxtaImpl.getGui().addAdminPeer(getQuery().getId(),
						bid.getPeerId());
				thinPeerJxtaImpl.getGui().addStatus(getQuery().getId(),
						getQuery().getStatus().toString());
				// Send deny message to other peers? 
				List<Bid> denyList = new ArrayList<Bid>(biddings);
				denyList.remove(bid);
				for (Bid b:denyList){
					messageElements.clear();
					messageElements.put("queryId", getQuery().getId());
					messageElements.put("result", "denied");
					this.sender.sendMessage(thinPeerJxtaImpl.getNetPeerGroup(),
							MessageTool.createOdysseusMessage(
									OdysseusMessageType.BiddingResult,
									messageElements), ((BidJxtaImpl) b)
									.getResponseSocket(), 10);					
				}
				
			}

		}
		logger.debug("Timer done");

	}

	public P2PQuery getQuery() {
		return query;
	}
}
