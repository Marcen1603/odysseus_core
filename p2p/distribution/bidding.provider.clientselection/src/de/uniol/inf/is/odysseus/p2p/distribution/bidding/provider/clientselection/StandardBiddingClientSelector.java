/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.clientselection;

import java.util.ArrayList;
import java.util.HashMap;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.ac.bidselection.IP2PBidSelector;
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
		log.logAction(q.getId(), "Evaluate query processing bids");
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
				IP2PBidSelector selector = ClientSelectionPlugIn.getBidSelector();
				BidJxtaImpl optimalBid = (BidJxtaImpl) selector.selectBid(subplan.getBiddings());

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
						"Sending accept for subplan" + subplan.getId() + " with optimal bid " + optimalBid.getBidValue());
				// Sende die Zusage
				MessageTool.sendMessage(PeerGroupTool.getPeerGroup(), opPeer,
					response,10);
				subplan.setPeerId(optimalBid.getPeerId());
				subplan.setResponseSocket(opPeer.toString());
				
				// Sende an die anderen eine Absage:
				ArrayList<Bid> allBidddings = subplan.getBiddings();
				allBidddings.remove(optimalBid);
				for (Bid deny:allBidddings){
					opPeer = ((BidJxtaImpl)deny).getResponseSocket();
					messageElements.clear();
					messageElements.put("queryId", q.getId());
					messageElements.put("subplanId", subplan.getId());
					messageElements.put("result", "denied");
					response = MessageTool.createOdysseusMessage(
							OdysseusMessageType.BiddingClient, messageElements);
					MessageTool.sendMessage(PeerGroupTool.getPeerGroup(), opPeer,
							response, 10);
				}
			}

			getCallback().changeState(Lifecycle.SUCCESS);
		} else {
			getCallback().changeState(Lifecycle.FAILED);
		}

	}

//	private BidJxtaImpl selectPromisingClient(ArrayList<Bid> biddings) {
//
//		// 1. Are there positiv bids?
//		List<Bid> validBids = new ArrayList<Bid>();
//		for (Bid b : biddings) {
//			Double bidValue = Double.valueOf(b.getBid());
//			
//			if (bidValue >= 0.0) {
//				validBids.add(b);
//			}
//		}
//
//		if (!validBids.isEmpty()) {
//			double highestBidValue = Double.MIN_VALUE;
//			Bid highestBid = null;
//			for( Bid b : validBids ) {
//				Double val = Double.valueOf(b.getBid());
//				if( val > highestBidValue ) {
//					highestBidValue = val;
//					highestBid = b;
//				}
//			}
//			
//			return (BidJxtaImpl)highestBid;
//			
//		} else {
//			return null;
//		}
//
//	}
}
