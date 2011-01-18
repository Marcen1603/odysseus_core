package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler.BiddingMessageResultHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler.EventMessageHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.IClientSelector;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class BiddingProvider extends
		AbstractDistributionProvider<PipeAdvertisement> {

	static Logger logger = LoggerFactory.getLogger(BiddingProvider.class);
	
	static Logger getLogger(){
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

		getRegisteredMessageHandler().add(new EventMessageHandler());
		getRegisteredMessageHandler().add(
				new BiddingMessageResultHandler(getPeer().getQueries()));
		for (IMessageHandler handler : getRegisteredMessageHandler()) {
			getPeer().registerMessageHandler(handler);
		}
		getLogger().info("Initializing execution handler");
		BiddingProviderExecutionHandler handler = new BiddingProviderExecutionHandler(Lifecycle.DISTRIBUTION,
				this, getPeer());
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
	};

	private void startDiscoveryService() {
		this.discoveryService = PeerGroupTool.getPeerGroup()
				.getDiscoveryService();
	}

	@Override
	public void startService() {
		startDiscoveryService();
	}

	@Override
	public void distributePlan(Query query, PipeAdvertisement serverResponse) {
		if (getDiscoveryService() == null) {
			return;
		}
		// get("1"), weil die subplaene von den senken zu den quellen gehen und
		// wir die p2psink fuer den thin-peer wollen
		Subplan topSink = query.getSubPlans().get("1");
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
		Message message = MessageTool.createSimpleMessage("QueryNegotiation",
				messageElements);

		((JxtaMessageSender)(getPeer().getMessageSender())).sendMessage(PeerGroupTool.getPeerGroup(),
				message, ((QueryJxtaImpl) query).getResponseSocketThinPeer());
		Log.logAction(query.getId(),
				"Sende Thin-Peer Verbindungsinformationen zum Operatorplan"); // ??

		// Anfragen ausschreiben
		for (Subplan subplan : query.getSubPlans().values()) {
			// Falls der Subplan neu verteilt werden sollte, dann sind alte
			// Gebote nicht mehr gueltig
			subplan.getBiddings().clear();
			QueryExecutionSpezification adv = (QueryExecutionSpezification) AdvertisementFactory
					.newAdvertisement(QueryExecutionSpezification
							.getAdvertisementType());

			adv.setBiddingPipe(((PipeAdvertisement) serverResponse).toString());
			adv.setQueryId(query.getId());
			adv.setSubplanId(subplan.getId());

			try {
				adv.setSubplan(new String(toBase64(subplan).toByteArray(), "utf-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			adv.setLanguage(query.getLanguage());
			try {
				getDiscoveryService().publish(adv, 15000, 15000);
				getDiscoveryService().remotePublish(adv, 15000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		IClientSelector selector = getClientSelectorFactory().getNewInstance(
				15000, query, getCallback());
		Thread t = new Thread(selector);
		t.start();
	}

	private ByteArrayOutputStream toBase64(Object object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		Base64OutputStream b64os = null;
		try {

			b64os = new Base64OutputStream(bos);
			oos = new ObjectOutputStream(b64os);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			oos.writeObject(object);
			b64os.flush();
			oos.flush();
			b64os.close();
			oos.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos;
	}


}
