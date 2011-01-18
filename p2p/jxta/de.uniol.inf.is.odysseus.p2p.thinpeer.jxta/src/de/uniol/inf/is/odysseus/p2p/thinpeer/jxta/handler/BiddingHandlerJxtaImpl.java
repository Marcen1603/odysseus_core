package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.strategy.BiddingHandlerStrategyStandard;
import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IBiddingHandlerStrategy;

/**
 * TODO am besten in ein eigenes Plugin auslagern
 *
 * Der QueryBiddingHandler schickt in regelmaessigen Abstaenden, Antworten auf
 * Bewerbungen von Verwaltungs-Peers heraus. Die Verwaltungs-Peers werden
 * daruber informiert, ob sie eine Anfrage zugeteilt bekommen oder nicht.
 * 
 * @author christian
 * 
 */
public class BiddingHandlerJxtaImpl implements IBiddingHandler {

	static Logger logger = LoggerFactory.getLogger(BiddingHandlerJxtaImpl.class);
	
	// Wie oft werden Antworten auf Bewerbungen herausgeschickt
	private int WAIT_TIME = 10000;
	private Query query;
	private JxtaMessageSender sender;

	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	public BiddingHandlerJxtaImpl(Query query, JxtaMessageSender sender, ThinPeerJxtaImpl thinPeerJxtaImpl) {
		this.query = query;
		this.sender = sender;
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
	}

	@Override
	public void run() {
//		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			for (Query q : ThinPeerJxtaImpl.getInstance().getQueries().keySet()) {
				
				//TODO Behandlung fehlgeschlagener Verteilung zwischen Thin-Peer und Verwaltungs-Peer
//				if (((QueryJxtaImpl)q).getAdminPeerBidding().size() == 0
//						&& q.getStatus() == Lifecycle.OPEN) {
//					ThinPeerJxtaImpl.getInstance().getQueries().get(s)
//							.addRetry();
//					if (ThinPeerJxtaImpl.getInstance().getQueries().get(s)
//							.getRetries() > 5) {
//						ThinPeerJxtaImpl.getInstance().getQueries().get(s)
//								.setStatus(Status.CANCELED);
//					}
//					continue;
//				}

				if (getQuery()
						.getStatus() == Lifecycle.NEW) {
					ArrayList<Bid> biddings = new ArrayList<Bid>();
					QueryJxtaImpl query = ((QueryJxtaImpl)getQuery());
					
					for (Bid bid : query.getAdminPeerBidding().values()) {
						biddings.add(bid);
					}

					// Zufalls Prinzip auslagern in Strategy
					if(!biddings.isEmpty()) {
						IBiddingHandlerStrategy strategy = new BiddingHandlerStrategyStandard();
						Bid bid = (Bid) strategy.handleBiddings(
										biddings);
	
						HashMap<String, Object> messageElements = new HashMap<String, Object>();
						messageElements.put("queryId", getQuery().getId());
						messageElements.put("result", "granted");
						this.sender.sendMessage(thinPeerJxtaImpl
								.getNetPeerGroup(), MessageTool
								.createSimpleMessage("BiddingResult", messageElements), ((BidJxtaImpl) bid)
								.getResponseSocket());
//						MessageTool.sendMessage(ThinPeerJxtaImpl.getInstance()
//								.getNetPeerGroup(), ((BidJxtaImpl) bid)
//								.getResponseSocket(), MessageTool
//								.createSimpleMessage("BiddingResult", messageElements));
						((QueryJxtaImpl) getQuery())
								.setAdminPeerPipe(((BidJxtaImpl) bid)
										.getResponseSocket());
	
						thinPeerJxtaImpl.getGui().addAdminPeer(
								getQuery()
										.getId(),
								bid.getPeerId());
	
						thinPeerJxtaImpl.getGui().addStatus(
								getQuery()
										.getId(),
								getQuery()
										.getStatus().toString());
					}

				}
logger.debug("timer abgelaufen");
			
//		}
	}
	
	public Query getQuery() {
		return query;
	}
}
