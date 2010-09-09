package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.clientselection;

import java.util.ArrayList;
import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.AbstractClientSelector;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.SubplanJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class StandardBiddingClientSelector<C extends IExecutionListenerCallback> extends AbstractClientSelector<C>{


	public StandardBiddingClientSelector(int time, Query query, C callback) {
		super(time, query, callback);
	}

	public StandardBiddingClientSelector(int time, Query query) {
		super(time, query);
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
		Query q = getQuery();
		Log.logAction(q.getId(), "Werte Gebote für Anfrage aus");
//		for (Query q : getQueries().keySet()) {
			// Ist mindestens ein Gebot je Subplan vorhanden
			if(q.containsAllBidding() && q.getSubPlans().size()>0 && q.getStatus() != Lifecycle.RUNNING) {
				

				for (Subplan subplan : q.getSubPlans().values()) {
					//TODO Subplan Behandlung
//					if (subplan.getStatus() == Subplan.SubplanStatus.CLOSED) {
//						continue;
//					}
					//TODO: Zuweisung zu den Bewerbern durch Kriterien
					//Strategie, welche über die Zusage entscheidet. Gib Eine Liste von Gebote rein und erhalte einen passenden Peer für meine zu erledigende Aufgabe
					BidJxtaImpl optimalBid = (BidJxtaImpl) selectPromisingClient(subplan.getBiddings());
					
					
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
//					messageElements.put("events", getEvents());
					Message response = MessageTool.createSimpleMessage(
							"BiddingClient", messageElements);
					Log.addEvent(q.getId(), "Sende Zusage für Teilplan "+subplan.getId());
					// Sende die Zusage
					MessageTool.sendMessage(PeerGroupTool.getPeerGroup(), opPeer, response);
//					this.sender.sendMessage(PeerGroupTool.getPeerGroup(), response, opPeer);
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
				
				getCallback().changeState(Lifecycle.SUCCESS);
			} else {
				getCallback().changeState(Lifecycle.FAILED);
			}


	}

	private BidJxtaImpl selectPromisingClient(ArrayList<Bid> biddings) {
		
		//TODO: Anständige Selektion
		for(Bid b : biddings) {
			if(b.getBid().equals("positive")) {
				return (BidJxtaImpl) b;
			}
		}
		return null;
	}
}
