package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

/**
 * Der QueryBiddingHandler schickt in regelmaessigen Abstaenden, Antworten auf
 * Bewerbungen von Verwaltungs-Peers heraus. Die Verwaltungs-Peers werden
 * daruber informiert, ob sie eine Anfrage zugeteilt bekommen oder nicht.
 * 
 * @author christian
 * 
 */
public class BiddingHandlerJxtaImpl implements IBiddingHandler {

	// Wie oft werden Antworten auf Bewerbungen herausgeschickt
	private int WAIT_TIME = 10000;

	public BiddingHandlerJxtaImpl() {

	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Query q : ThinPeerJxtaImpl.getInstance().getQueries().keySet()) {
				
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

				if (q
						.getStatus() == Lifecycle.OPEN) {
					q
							.setStatus(Lifecycle.GRANTED);
					ArrayList<Bid> biddings = new ArrayList<Bid>();
					QueryJxtaImpl query = ((QueryJxtaImpl)q);
					
					for (Bid bid : query.getAdminPeerBidding().values()) {
						biddings.add(bid);
					}

					// Zufalls Prinzip auslagern in Strategy

					Bid bid = (Bid) ThinPeerJxtaImpl.getInstance()
							.getBiddingHandlerStrategy().handleBiddings(
									biddings);

					HashMap<String, Object> messageElements = new HashMap<String, Object>();
					messageElements.put("queryId", q.getId());
					messageElements.put("result", "granted");
					
					MessageTool.sendMessage(ThinPeerJxtaImpl.getInstance()
							.getNetPeerGroup(), ((BidJxtaImpl) bid)
							.getResponseSocket(), MessageTool
							.createSimpleMessage("BiddingResult", messageElements));
					((QueryJxtaImpl) q)
							.setAdminPeerPipe(((BidJxtaImpl) bid)
									.getResponseSocket());

					ThinPeerJxtaImpl.getInstance().getGui().addAdminPeer(
							q
									.getId(),
							bid.getPeerId());

					ThinPeerJxtaImpl.getInstance().getGui().addStatus(
							q
									.getId(),
							q
									.getStatus().toString());

//				} else if (q
//						.getStatus() == Status.CANCELED) {
//					// TODO
//				}

				}

			}
		}
	}
}
