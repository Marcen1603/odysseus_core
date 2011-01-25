package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.clientselection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.AbstractClientSelector;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class StandardBiddingClientSelector<C extends IExecutionListenerCallback>
		extends AbstractClientSelector<C> {

	public StandardBiddingClientSelector(int time, P2PQuery query, C callback,
			ILogListener log) {
		super(time, query, callback, log);
	}

	public StandardBiddingClientSelector(int time, P2PQuery query,
			ILogListener log) {
		super(time, query, log);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(getTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		handleBidding();
	}

	@Override
	public String getName() {
		return "StandardBiddingClientSelector";
	}

	private void handleBidding() {
		P2PQuery q = getQuery();
		log.logAction(q.getId(), "Werte Gebote fuer Anfrage aus");
		// for (Query q : getQueries().keySet()) {
		// Ist mindestens ein Gebot je Subplan vorhanden
		if (q.containsAllBidding() && q.getSubPlans().size() > 0
				&& q.getStatus() != Lifecycle.RUNNING) {

			for (Subplan subplan : q.getSubPlans().values()) {
				// TODO Subplan Behandlung
				// if (subplan.getStatus() == Subplan.SubplanStatus.CLOSED) {
				// continue;
				// }
				// TODO: Zuweisung zu den Bewerbern durch Kriterien
				// Strategie, welche über die Zusage entscheidet. Gib Eine
				// Liste von Gebote rein und erhalte einen passenden Peer für
				// meine zu erledigende Aufgabe
				BidJxtaImpl optimalBid = (BidJxtaImpl) selectPromisingClient(subplan
						.getBiddings());

				// Adresse des bietenden Operator-Peers
				PipeAdvertisement opPeer = (optimalBid.getResponseSocket());

				// Baue Nachricht zusammen, dass dem Operator-Peer die
				// Ausführung zugesagt wird
				HashMap<String, Object> messageElements = new HashMap<String, Object>();
				messageElements.put("queryId", q.getId());
				messageElements.put("subplanId", subplan.getId());
				messageElements.put("result", "granted");
				Message response = MessageTool.createOdysseusMessage(
						OdysseusMessageType.BiddingClient, messageElements);
				log.addEvent(q.getId(),
						"Sende Zusage fuer Teilplan " + subplan.getId());
				// Sende die Zusage
				MessageTool.sendMessage(PeerGroupTool.getPeerGroup(), opPeer,
						response);

				subplan.setPeerId(optimalBid.getPeerId());

				subplan.setResponseSocket(opPeer.toString());
			}

			getCallback().changeState(Lifecycle.SUCCESS);
		} else {
			getCallback().changeState(Lifecycle.FAILED);
		}

	}

	private BidJxtaImpl selectPromisingClient(ArrayList<Bid> biddings) {

		// 1. Are there positiv bids
		boolean found = false;
		for (Bid b : biddings) {
			if (b.getBid().equals("positive")) {
				found = true;
				break;
			}
		}

		if (found) {
			// Zufaellig Selektion
			Random random = new Random(1);
			while (true) {
				int b = random.nextInt(biddings.size());
				Bid bid = biddings.get(b);
				if (bid.getBid().equals("positive")){
					return (BidJxtaImpl)bid;
				}
			}
		} else {
			return null;
		}

	}
}
