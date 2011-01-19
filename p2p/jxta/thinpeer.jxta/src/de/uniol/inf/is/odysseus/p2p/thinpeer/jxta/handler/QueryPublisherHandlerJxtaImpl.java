package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import java.io.IOException;
import java.util.HashMap;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IQueryPublisher;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class QueryPublisherHandlerJxtaImpl implements IQueryPublisher {


	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	public QueryPublisherHandlerJxtaImpl(ThinPeerJxtaImpl thinPeerJxtaImpl) {
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
	}
	// Wie lange ist eine Ausschreibung gültig.
	private int VALID = 15000;

	@Override
	public void publishQuerySpezification(String queryId, String query,
			String language) {

		QueryJxtaImpl q = new QueryJxtaImpl();
		q.setDeclarativeQuery(query);
		q.setId(queryId);
		q.setStatus(Lifecycle.NEW);
		q.setUser(GlobalState.getActiveUser());
		q.setDataDictionary(GlobalState.getActiveDatadictionary());
		q.setLanguage(language);
		QueryTranslationSpezification adv = (QueryTranslationSpezification) AdvertisementFactory
				.newAdvertisement(QueryTranslationSpezification
						.getAdvertisementType());
		adv.setQuery(query);
		adv
				.setBiddingPipe(thinPeerJxtaImpl.getServerResponseAddress().toString());
		adv.setPeer(thinPeerJxtaImpl.getNetPeerGroup()
				.getPeerAdvertisement().toString());
		adv.setQueryId(q.getId());
		adv.setLanguage(language);
		thinPeerJxtaImpl.addQuery(q);
		try {
			thinPeerJxtaImpl.getDiscoveryService().publish(adv,
					VALID, VALID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		thinPeerJxtaImpl.getDiscoveryService().remotePublish(adv,
				VALID);

		Log.addTab(q.getId(), query);
		
		//Hier den "Timer" starten für die Behandlung der Gebote
		IBiddingHandler handler = new BiddingHandlerJxtaImpl(q, (JxtaMessageSender) this.thinPeerJxtaImpl.getMessageSender(), thinPeerJxtaImpl);
		Thread t = new Thread(handler);
		t.start();
	}

	@Override
	public void sendQuerySpezificationToAdminPeer(String queryId, String query,
			String language, String adminPeer) {

		PipeAdvertisement adminPipe = MessageTool
				.createPipeAdvertisementFromXml(((ExtendedPeerAdvertisement)thinPeerJxtaImpl
						.getAdminPeers().get(adminPeer)).getPipe());
		PipeAdvertisement thinPeerPipe = (PipeAdvertisement) thinPeerJxtaImpl.getServerResponseAddress();

		QueryJxtaImpl q = new QueryJxtaImpl();
		q.setDeclarativeQuery(query);
		q.setUser(GlobalState.getActiveUser());
		q.setDataDictionary(GlobalState.getActiveDatadictionary());
		q.setId(queryId);
		BidJxtaImpl bid = new BidJxtaImpl();
		bid.setResponseSocket(adminPipe);
		thinPeerJxtaImpl.addQuery(q);

//		Message message = MessageTool.createSimpleMessage("DoQuery", "queryId",
//				"query", "language", "result", queryId, query, language,
//				"granted", thinPeerPipe);
		
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", queryId);
		messageElements.put("language", language);
		messageElements.put("result", "granted");
		messageElements.put("thinPeerPipe", thinPeerPipe);
		
		Message message = MessageTool.createSimpleMessage("DoQuery", messageElements);
//		MessageTool.sendMessage(ThinPeerJxtaImpl.getInstance()
//				.getNetPeerGroup(), adminPipe, message);
		((JxtaMessageSender)(thinPeerJxtaImpl.getMessageSender())).sendMessage(thinPeerJxtaImpl
				.getNetPeerGroup(), message, adminPipe);
		Log.addTab(q.getId(), query);
	}

}
