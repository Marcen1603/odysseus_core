package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.handler;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.Query.Status;
import de.uniol.inf.is.odysseus.p2p.Bid;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;

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

			for (String s : ThinPeerJxtaImpl.getInstance().getQueries()
					.keySet()) {
				if (((QueryJxtaImpl)ThinPeerJxtaImpl.getInstance().getQueries().get(s)).getAdminPeerBidding().size() == 0
						&& ThinPeerJxtaImpl.getInstance().getQueries().get(s)
								.getStatus() == Status.OPEN) {
					ThinPeerJxtaImpl.getInstance().getQueries().get(s)
							.addRetry();
					if (ThinPeerJxtaImpl.getInstance().getQueries().get(s)
							.getRetries() > 5) {
						ThinPeerJxtaImpl.getInstance().getQueries().get(s)
								.setStatus(Status.CANCELED);
					}
					continue;
				}

				if (ThinPeerJxtaImpl.getInstance().getQueries().get(s)
						.getStatus() == Status.OPEN) {
					ThinPeerJxtaImpl.getInstance().getQueries().get(s)
							.setStatus(Status.NOBIDDING);
					ArrayList<Bid> biddings = new ArrayList<Bid>();
					QueryJxtaImpl q = ((QueryJxtaImpl)ThinPeerJxtaImpl.getInstance().getQueries().get(s));
					
					for (Bid bid : q.getAdminPeerBidding().values()) {
						biddings.add(bid);
					}

					// Zufalls Prinzip auslagern in Strategy

					Bid bid = (Bid) ThinPeerJxtaImpl.getInstance()
							.getBiddingHandlerStrategy().handleBiddings(
									biddings);

					HashMap<String, Object> messageElements = new HashMap<String, Object>();
					messageElements.put("queryId", ThinPeerJxtaImpl.getInstance().getQueries().get(s).getId());
					messageElements.put("result", "granted");
					
					MessageTool.sendMessage(ThinPeerJxtaImpl.getInstance()
							.getNetPeerGroup(), ((BidJxtaImpl) bid)
							.getResponseSocket(), MessageTool
							.createSimpleMessage("BiddingResult", messageElements));
					((QueryJxtaImpl) ThinPeerJxtaImpl.getInstance()
							.getQueries().get(s))
							.setAdminPeerPipe(((BidJxtaImpl) bid)
									.getResponseSocket());

					ThinPeerJxtaImpl.getInstance().getGui().addAdminPeer(
							ThinPeerJxtaImpl.getInstance().getQueries().get(s)
									.getId(),
							bid.getPeerId());

					ThinPeerJxtaImpl.getInstance().getGui().addStatus(
							ThinPeerJxtaImpl.getInstance().getQueries().get(s)
									.getId(),
							ThinPeerJxtaImpl.getInstance().getQueries().get(s)
									.getStatus().toString());

				} else if (ThinPeerJxtaImpl.getInstance().getQueries().get(s)
						.getStatus() == Status.CANCELED) {
					// TODO
				}

			}

		}
	}

}
