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
package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.IBid;
import de.uniol.inf.is.odysseus.p2p.OdysseusBidAnswer;
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

	final private int waitTime;
	final private P2PQuery query;
	final private JxtaMessageSender sender;
	final private IBiddingHandlerStrategy strategy;

	final private ThinPeerJxtaImpl thinPeerJxtaImpl;

	public BiddingHandlerJxtaImpl(P2PQuery query, JxtaMessageSender sender,
			ThinPeerJxtaImpl thinPeerJxtaImpl, int waitTime, IBiddingHandlerStrategy strategy) {
		this.query = query;
		this.sender = sender;
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
		this.waitTime = waitTime;
		this.strategy = strategy;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (getQuery().getStatus() == Lifecycle.NEW) {
			List<IBid> biddings = new ArrayList<IBid>();
			P2PQueryJxtaImpl query = ((P2PQueryJxtaImpl) getQuery());

			for (Bid bid : query.getAdminPeerBidding().values()) {
				biddings.add(bid);
			}

			if (!biddings.isEmpty()) {
				Map<String, Object> messageElements = new HashMap<String, Object>();
				// Determine AdminPeer to handle this query
				IBid bid = strategy.handleBiddings(biddings);
				
				// Send granted message to peer with this bid
				messageElements.put("queryId", query.getId());
				messageElements.put("result", OdysseusBidAnswer.granted);
				query.setStatus(Lifecycle.GRANTED);
				this.sender.sendMessage(thinPeerJxtaImpl.getNetPeerGroup(),
						MessageTool.createOdysseusMessage(
								OdysseusMessageType.BiddingResult,
								messageElements), ((BidJxtaImpl) bid)
								.getResponseSocket(), 10);
				query.setAdminPeerPipe(((BidJxtaImpl) bid)
								.getResponseSocket());
				thinPeerJxtaImpl.adminPeerFound(query.getId(),
						bid.getPeerId());
				thinPeerJxtaImpl.log(query.getId(),
						query.getStatus().toString());
				
				// Send deny message to other peers
				List<IBid> denyList = new ArrayList<IBid>(biddings);
				denyList.remove(bid);
				for (IBid b:denyList){
					messageElements.clear();
					messageElements.put("queryId", query.getId());
					messageElements.put("result", OdysseusBidAnswer.denied);
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
