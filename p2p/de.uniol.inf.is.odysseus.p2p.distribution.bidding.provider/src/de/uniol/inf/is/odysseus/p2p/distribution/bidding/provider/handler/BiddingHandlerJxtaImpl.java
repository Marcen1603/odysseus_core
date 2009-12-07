package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.handler;
//TODO Interface DistributionStrategy siehe Unterlagen
import java.util.ArrayList;
import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.SubplanJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;

public class BiddingHandlerJxtaImpl implements IBiddingHandler {

	int WAIT_TIME = 10000;
	private HashMap<Query, IExecutionListener> queries;
	private String events;

	public BiddingHandlerJxtaImpl(HashMap<Query, IExecutionListener> queries, String events) {
		this.queries = queries;
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
		
		for (Query q : getQueries().keySet()) {
			// Ist mindestens ein Gebot je Subplan vorhanden
			if(q.containsAllBidding() && q.getSubPlans().size()>0 && q.getStatus() != Lifecycle.RUNNING) {
				

				for (Subplan subplan : q.getSubPlans().values()) {
					//TODO Subplan Behandlung
//					if (subplan.getStatus() == Subplan.SubplanStatus.CLOSED) {
//						continue;
//					}
					//TODO: Zuweisung zu den Bewerbern durch Kriterien
					//Strategie, welche über die Zusage entscheidet. Gib Eine Liste von Gebote rein und erhalte einen passenden Peer für meine zu erledigende Aufgabe
					BidJxtaImpl optimalBid = (BidJxtaImpl) selectSuitableBid(subplan.getBiddings());
					
					
					// Adresse des bietenden Operator-Peers
					PipeAdvertisement opPeer = (optimalBid.getResponseSocket());
					
//					Log.logAction(q.getId(),
//							"Sende Zusage an Operator-Peer für Teilplan "
//									+ subplan.getId());
					
					//Baue Nachricht zusammen, dass dem Operator-Peer die Ausführung zugesagt wird
					HashMap<String, Object> messageElements = new HashMap<String, Object>();
					messageElements.put("queryId", q.getId());
					messageElements.put("subplanId", subplan.getId());
					messageElements.put("result", "granted");
					messageElements.put("events", getEvents());
					Message response = MessageTool.createSimpleMessage(
							"BiddingClient", messageElements);
					Log.addEvent(q.getId(), "Sende Zusage für Teilplan "+subplan.getId());
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
					
					((SubplanJxtaImpl)subplan).setResponseSocket(opPeer.toString());
//					((SubplanJxtaImpl) getQueries().get(s).getSubPlans().get(
//									subplannumber))
//							.setResponseSocket(((BidJxtaImpl) getQueries().get(s)
//									.getBiddings().get(subplannumber))
//									.getResponseSocket().toString());
//					subplan.setStatus(SubplanStatus.CLOSED);
//					q.getSubPlans().get(subplannumber).setStatus(
//									SubplanStatus.CLOSED);
//					subplannumber++;
//					subplan.setStatus(Subplan.SubplanStatus.CLOSED);
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

	public String getEvents() {
		return events;
	}

	public HashMap<Query, IExecutionListener> getQueries() {
		return queries;
	}


}
