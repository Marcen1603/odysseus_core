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
package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import java.io.IOException;
import java.util.HashMap;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.id.IDFactory;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler.BiddingMessageResultHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler.EventMessageHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.IClientSelector;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class BiddingProvider extends
		AbstractDistributionProvider<PipeAdvertisement> {

	static Logger logger = LoggerFactory.getLogger(BiddingProvider.class);

	static Logger getLogger() {
		return logger;
	}

	private DiscoveryService discoveryService;

	public DiscoveryService getDiscoveryService() {
		return discoveryService;
	}

	@Override
	public String getDistributionStrategy() {
		return "Bidding";
	}

	public BiddingProvider() {
		super();

	}

	@Override
	public void initializeService() {
		getLogger().info("Initializing message handler");

		getRegisteredMessageHandler().add(
				new EventMessageHandler(getPeer().getLog()));
		getRegisteredMessageHandler().add(
				new BiddingMessageResultHandler(getPeer(), getPeer().getLog()));
		for (IMessageHandler handler : getRegisteredMessageHandler()) {
			getPeer().registerMessageHandler(handler);
		}
		getLogger().info("Initializing execution handler");
		BiddingProviderExecutionHandler handler = new BiddingProviderExecutionHandler(
				Lifecycle.DISTRIBUTION, this, getPeer());
		getRegisteredExecutionHandler().add(handler);
		getPeer().bindExecutionHandler(handler);
	}

	@Override
	public void finalizeService() {
		for (IExecutionHandler<?> handler : getRegisteredExecutionHandler()) {
			getPeer().unbindExecutionHandler(handler);
		}
		for (IMessageHandler handler : getRegisteredMessageHandler()) {
			getPeer().deregisterMessageHandler(handler);
		}
	}

	private void startDiscoveryService() {
		this.discoveryService = PeerGroupTool.getPeerGroup()
				.getDiscoveryService();
	}

	@Override
	public void startService() {
		startDiscoveryService();
	}

	@Override
	public void distributePlan(P2PQuery query, PipeAdvertisement serverResponse) {
		if (getDiscoveryService() == null) {
			return;
		}
		Subplan topSink = query.getTopSink();
		String pipeAdv = ((P2PSinkAO) topSink.getAo()).getAdv();
		PeerAdvertisement peerAdv = PeerGroupTool.getPeerGroup()
				.getPeerAdvertisement();

		// Thin-Peer erhaelt Verbindungsinformationen zur letzten P2PSink
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", query.getId());
		messageElements.put("queryAction", "ResultStreaming");
		messageElements.put("pipeAdvertisement",
				MessageTool.createPipeAdvertisementFromXml(pipeAdv));
		messageElements.put("peerAdvertisement", peerAdv);
		Message message = MessageTool.createOdysseusMessage(
				OdysseusMessageType.QueryNegotiation, messageElements);

		((JxtaMessageSender) (getPeer().getMessageSender())).sendMessage(
				PeerGroupTool.getPeerGroup(), message,
				((P2PQueryJxtaImpl) query).getResponseSocketThinPeer(), 10);
		log.logAction(query.getId(),
				"Send query plan connection information to thin peer");

		// Anfragen ausschreiben
		for (Subplan subplan : query.getSubPlans().values()) {
			// Falls der Subplan neu verteilt werden sollte, dann sind alte
			// Gebote nicht mehr gueltig
			subplan.getBiddings().clear();
			QueryExecutionSpezification adv = (QueryExecutionSpezification) AdvertisementFactory
					.newAdvertisement(QueryExecutionSpezification
							.getAdvertisementType());
			adv.setID(IDFactory.newPipeID(PeerGroupTool.getPeerGroup().getPeerGroupID()));
			adv.setBiddingPipe(serverResponse.toString());
			adv.setQueryId(query.getId());
			adv.setSubplanId(subplan.getId());
			adv.setSubplan(AdvertisementTools.toBase64String(subplan));
			adv.setLanguage(query.getLanguage());
			try {
				getDiscoveryService().publish(adv, 15000, 15000);
				getDiscoveryService().remotePublish(adv, 15000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		IClientSelector selector = getClientSelectorFactory().getNewInstance(
				15000, query, getCallback(), log);
		Thread t = new Thread(selector);
		t.start();
	}

}
