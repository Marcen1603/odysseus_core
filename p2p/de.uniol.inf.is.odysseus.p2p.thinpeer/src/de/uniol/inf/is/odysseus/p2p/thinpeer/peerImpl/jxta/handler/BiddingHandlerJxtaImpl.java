package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.handler;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.queryAdministration.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.queryAdministration.Query.Status;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.Bid;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.BidJxtaImpl;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (String s : ThinPeerJxtaImpl.getInstance().getQueries()
					.keySet()) {
				if (ThinPeerJxtaImpl.getInstance().getQueries().get(s)
						.getBiddings().size() == 0
						&& ThinPeerJxtaImpl.getInstance().getQueries().get(s)
								.getStatus() == Status.OPEN) {
					ThinPeerJxtaImpl.getInstance().getQueries().get(s)
							.incFailed();
					if (ThinPeerJxtaImpl.getInstance().getQueries().get(s)
							.getFailed() > 5) {
						ThinPeerJxtaImpl.getInstance().getQueries().get(s)
								.setStatus(Status.CANCELED);
					}
					continue;
				}

				if (ThinPeerJxtaImpl.getInstance().getQueries().get(s)
						.getStatus() == Status.OPEN) {
					ThinPeerJxtaImpl.getInstance().getQueries().get(s)
							.setStatus(Status.STOPBIDDING);
					ArrayList<Bid> biddings = new ArrayList<Bid>();
					for (Bid bid : ThinPeerJxtaImpl.getInstance().getQueries()
							.get(s).getBiddings().values()) {
						biddings.add(bid);
					}

					// Zufalls Prinzip auslagern in Strategy

					Bid bid = (Bid) ThinPeerJxtaImpl.getInstance()
							.getBiddingHandlerStrategy().handleBiddings(
									biddings);

					MessageTool.sendMessage(ThinPeerJxtaImpl.getInstance()
							.getNetPeerGroup(), ((BidJxtaImpl) bid)
							.getResponseSocket(), MessageTool
							.createSimpleMessage("BiddingResult", "queryId",
									"result", ThinPeerJxtaImpl.getInstance()
											.getQueries().get(s).getId(),
									"granted"));
					((QueryJxtaImpl) ThinPeerJxtaImpl.getInstance()
							.getQueries().get(s))
							.setAdminPeerPipe(((BidJxtaImpl) bid)
									.getResponseSocket());

					ThinPeerJxtaImpl.getInstance().getGui().addAdminPeer(
							ThinPeerJxtaImpl.getInstance().getQueries().get(s)
									.getId(),
							((BidJxtaImpl) bid).getPeer().getName());

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
