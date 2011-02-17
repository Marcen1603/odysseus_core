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
package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener;

import java.util.Enumeration;
import java.util.HashMap;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.endpoint.Message;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.OdysseusQueryAction;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IQuerySpezificationListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IThinPeerBiddingStrategy;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * Discover queries and possibly bid for administration
 * 
 * @author Mart Koehler, Marco Grawunder
 * 
 */
public class APQuerySpezificationListenerJxtaImpl implements
		IQuerySpezificationListener, DiscoveryListener {

	private int ADVS_PER_PEER = 6;
	private int WAIT_TIME = 6000;
	private IThinPeerBiddingStrategy biddingStrategy;
	private JxtaMessageSender sender;
	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;
	private ILogListener log;

	public APQuerySpezificationListenerJxtaImpl(JxtaMessageSender sender,
			AdministrationPeerJxtaImpl administrationPeerJxtaImpl,
			IThinPeerBiddingStrategy biddingStrategy,
			ILogListener log) {
		this.sender = sender;
		this.administrationPeerJxtaImpl = administrationPeerJxtaImpl;
		this.biddingStrategy = biddingStrategy;
		this.log = log;

	}

	@Override
	public void run() {
		while (true) {
			// State interest for query advertisments (from thin peers)
			administrationPeerJxtaImpl.getDiscoveryService()
					.getRemoteAdvertisements(null, DiscoveryService.ADV,
							"queryId", "*", ADVS_PER_PEER, this);
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}

		}
	}

	@Override
	// This Method is called if new queries are found
	public synchronized void discoveryEvent(DiscoveryEvent ev) {
		DiscoveryResponseMsg res = ev.getResponse();
		QueryTranslationSpezification adv = null;
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {
				Object temp2 = en.nextElement();
				try {
					// Handle only QueryTranslationSpezification that are not
					// already found
					if ((temp2 instanceof QueryTranslationSpezification)
							&& !administrationPeerJxtaImpl
									.hasQuery(((QueryTranslationSpezification) temp2)
											.getQueryId())) {

						adv = (QueryTranslationSpezification) temp2;

						log.addQuery(adv.getQueryId());
						log.logAction(adv.getQueryId(), "Found new query");
						log.logAction(adv.getQueryId(), adv.getQuery());

						PipeAdvertisement pipeAdv = MessageTool
								.createPipeAdvertisementFromXml(adv
										.getBiddingPipe());
						P2PQueryJxtaImpl q = createQuery(adv, pipeAdv);

						if (biddingStrategy.bidding(q)) {
							PeerAdvertisement peerAdv = administrationPeerJxtaImpl
									.getNetPeerGroup().getPeerAdvertisement();

							administrationPeerJxtaImpl.addQuery(q);

							HashMap<String, Object> messageElements = new HashMap<String, Object>();
							messageElements.put("queryAction", OdysseusQueryAction.bidding);

							messageElements.put("queryId", q.getId());
							messageElements.put("responsePipeAdvertisement",
									administrationPeerJxtaImpl
											.getServerPipeAdvertisement());
							messageElements.put("peerAdvertisement", peerAdv);
							Message response = MessageTool.createOdysseusMessage(
									OdysseusMessageType.QueryNegotiation, messageElements);

							sender.sendMessage(administrationPeerJxtaImpl
									.getNetPeerGroup(), response, pipeAdv,10);
							log.logAction(adv.getQueryId(),
									"Bidding for query. waiting for response...");
						} else {
							log.logAction(adv.getQueryId(),
									"Not bidding for query");

						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private P2PQueryJxtaImpl createQuery(QueryTranslationSpezification adv,
			PipeAdvertisement pipeAdv) {
		P2PQueryJxtaImpl q = new P2PQueryJxtaImpl();
		q.setId(adv.getQueryId());
		q.setLanguage(adv.getLanguage());
		q.setDeclarativeQuery(adv.getQuery());
		q.setUser(GlobalState.getActiveUser());
		q.setDataDictionary(GlobalState.getActiveDatadictionary());
		q.setResponseSocketThinPeer(pipeAdv);
		return q;
	}

}
