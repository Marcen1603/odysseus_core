package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.handler;

import java.util.ArrayList;
import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.Bid;
import de.uniol.inf.is.odysseus.p2p.Query;
import de.uniol.inf.is.odysseus.p2p.Subplan;
import de.uniol.inf.is.odysseus.p2p.Subplan.SubplanStatus;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.EventListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.IEventListener;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.SubplanJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.PeerGroupTool;

public class BiddingHandlerJxtaImpl implements IBiddingHandler {

	int WAIT_TIME = 10000;
	private HashMap<String, Query> queries;
	private IEventListener eventListener;
	private String events;

	public BiddingHandlerJxtaImpl(HashMap<String, Query> queries, String events, IEventListener eventListener) {
		this.queries = queries;
		this.eventListener = eventListener;
		this.events = events;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (getQueries()) {
				if(!getQueries().isEmpty()) {
					handleBidding();
				}
			}

		}

	}
	
	public void handleBidding() {
		
		for (Query q : getQueries()
				.values()) {
			// Ist mindestens ein Gebot je Subplan vorhanden
			if(q.containsAllBidding() && q.getSubPlans().size()>0 && q.getStatus() != Query.Status.RUN) {
				
				int subplannumber = 0;

				for (Subplan subplan : q.getSubPlans().values()) {
					if (subplan.getStatus() == Subplan.SubplanStatus.CLOSED) {
						continue;
					}
					//TODO: Zuweisung zu den Bewerbern durch Kriterien
					//Strategie, welche über die Zusage entscheidet. Gib Eine Liste von Gebote rein und erhalte einen passenden Peer für meine zu erledigende Aufgabe
					BidJxtaImpl optimalBid = (BidJxtaImpl) selectSuitableBid(subplan.getBiddings());
					
					
					// Adresse des bietenden Operator-Peers
					PipeAdvertisement opPeer = (optimalBid.getResponseSocket());
					
//					Log.logAction(q.getId(),
//							"Sende Zusage an Operator-Peer für Teilplan "
//									+ subplan.getId());
					
					//Baue Nachricht zusammen, dass dem Operator-Peer die Ausführung zugesagt wird
					//TODO: An dieser Stelle sollte der Subplan verschickt werden. Das muss vorher bei der Ausschreibung bereits geschehen
					HashMap<String, Object> messageElements = new HashMap<String, Object>();
					messageElements.put("queryId", q.getId());
					messageElements.put("subplanId", subplan.getId());
					messageElements.put("result", "granted");
					messageElements.put("events", getEvents());
					messageElements.put("pipeAdvertisement", ((EventListenerJxtaImpl) getEventListener())
									.getPipeAdv());
					
					Message response = MessageTool.createSimpleMessage(
							"QueryResult", messageElements);
					// Sende die Zusage
					MessageTool.sendMessage(PeerGroupTool.getPeerGroup(), opPeer, response);
					
					// Den Peer setzen der gerade den ausgewählten Teilplan ausführt
					
					((SubplanJxtaImpl)subplan).setPeerId(optimalBid.getPeerId());
//					((SubplanJxtaImpl) AbstractDistributionProvider.getInstance()
//							.getQueries().get(s).getSubPlans().get(
//									subplannumber))
//							.setPeerId(((BidJxtaImpl) AbstractDistributionProvider
//									.getInstance().getQueries().get(s)
//									.getBiddings().get(subplannumber))
//									.getPeerId());
					// Socket von dem Peer setzen der gerade den
					// Teilplan ausführt. Für den Verwaltungs-Peer interessant, damit er weiß wen er kontaktieren muss.
					
					((SubplanJxtaImpl)subplan).setResponseSocket(optimalBid.getResponseSocket().toString());
//					((SubplanJxtaImpl) getQueries().get(s).getSubPlans().get(
//									subplannumber))
//							.setResponseSocket(((BidJxtaImpl) getQueries().get(s)
//									.getBiddings().get(subplannumber))
//									.getResponseSocket().toString());
					subplan.setStatus(SubplanStatus.CLOSED);
//					q.getSubPlans().get(subplannumber).setStatus(
//									SubplanStatus.CLOSED);
//					subplannumber++;
					subplan.setStatus(Subplan.SubplanStatus.CLOSED);
//					Log.setStatus(getQueries().get(s).getId(), "Running");
				}

			} else {
				
				//TODO: An dieser Stelle Maßnahmen, so dass doch auf Anfragen geboten wird?
//				if (AbstractDistributionProvider.getInstance().getQueries()
//						.get(s).getRetries() >= 3
//						&& AbstractDistributionProvider.getInstance()
//								.getQueries().get(s).getSubPlans().size() != 0) {
//					// Es kommen nicht genug Angebote also wird die
//					// Anfrage abgebrochen
//					String queryId = AbstractDistributionProvider.getInstance()
//							.getQueries().get(s).getId();
//					Log.logAction(AbstractDistributionProvider.getInstance()
//							.getQueries().get(s).getId(),
//							"Nicht genug Gebote von Operator-Peers");
//					Message msg = MessageTool.createSimpleMessage(
//							"QueryFailed", "queryId", queryId);
//					Log.setStatus(queryId, "Failed");
//					MessageTool.sendMessage(AbstractDistributionProvider
//							.getInstance().getNetPeerGroup(),
//							AbstractDistributionProvider.getInstance()
//									.getQueries().get(s)
//									.getResponseSocketThinPeer(), msg);
//					AbstractDistributionProvider.getInstance().getQueries()
//							.remove(queryId);
//					Log.removeQuery(queryId);
//				} else {
//					AbstractDistributionProvider.getInstance().getQueries()
//							.get(s).addRetry();
//				}

			}

		}

	}

	private BidJxtaImpl selectSuitableBid(ArrayList<Bid> biddings) {
		// TODO: Erstmal irgendein Bid zurückgeben, in Strategie auslagern.
		
		return (BidJxtaImpl) biddings.get(0);
	}

	public IEventListener getEventListener() {
		return eventListener;
	}

	public String getEvents() {
		return events;
	}

	public HashMap<String, Query> getQueries() {
		return queries;
	}


}
