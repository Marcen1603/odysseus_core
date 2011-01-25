package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import java.io.IOException;
import java.util.HashMap;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IQueryPublisher;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class QueryPublisherHandlerJxtaImpl implements IQueryPublisher {

	private ThinPeerJxtaImpl thinPeerJxtaImpl;
	private ILogListener log;

	public QueryPublisherHandlerJxtaImpl(ThinPeerJxtaImpl thinPeerJxtaImpl) {
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
		this.log = thinPeerJxtaImpl.getLog();
	}

	// Wie lange ist eine Ausschreibung gültig.
	private int VALID = 15000;

	@Override
	public void publishQuerySpezification(String queryId, String query,
			String language, User user) {

		P2PQueryJxtaImpl q = new P2PQueryJxtaImpl();
		q.setDeclarativeQuery(query);
		q.setId(queryId);
		q.setStatus(Lifecycle.NEW);
		q.setUser(user);
		q.setDataDictionary(GlobalState.getActiveDatadictionary());
		q.setLanguage(language);
		thinPeerJxtaImpl.addQuery(q);
		log.addTab(q.getId(), query);
		QueryTranslationSpezification adv = (QueryTranslationSpezification) AdvertisementFactory
				.newAdvertisement(QueryTranslationSpezification
						.getAdvertisementType());
		adv.setQuery(query);
		adv.setBiddingPipe(thinPeerJxtaImpl.getServerResponseAddress()
				.toString());
		adv.setPeer(thinPeerJxtaImpl.getNetPeerGroup().getPeerAdvertisement()
				.toString());
		adv.setQueryId(q.getId());
		adv.setLanguage(language);

		try {
			thinPeerJxtaImpl.getDiscoveryService().publish(adv, VALID, VALID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		thinPeerJxtaImpl.getDiscoveryService().remotePublish(adv, VALID);

		// Start bid response handler
		IBiddingHandler handler = new BiddingHandlerJxtaImpl(q,
				(JxtaMessageSender) this.thinPeerJxtaImpl.getMessageSender(),
				thinPeerJxtaImpl);
		Thread t = new Thread(handler);
		t.start();
	}

	@Override
	public void sendQuerySpezificationToAdminPeer(String queryId, String query,
			String language, String adminPeer) {

		PipeAdvertisement adminPipe = MessageTool
				.createPipeAdvertisementFromXml(((ExtendedPeerAdvertisement) thinPeerJxtaImpl
						.getAdminPeers().get(adminPeer)).getPipe());
		PipeAdvertisement thinPeerPipe = (PipeAdvertisement) thinPeerJxtaImpl
				.getServerResponseAddress();

		P2PQueryJxtaImpl q = new P2PQueryJxtaImpl();
		q.setDeclarativeQuery(query);
		q.setUser(GlobalState.getActiveUser());
		q.setDataDictionary(GlobalState.getActiveDatadictionary());
		q.setId(queryId);
		BidJxtaImpl bid = new BidJxtaImpl();
		bid.setResponseSocket(adminPipe);
		thinPeerJxtaImpl.addQuery(q);

		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", queryId);
		messageElements.put("language", language);
		messageElements.put("result", "granted");
		messageElements.put("thinPeerPipe", thinPeerPipe);

		Message message = MessageTool.createOdysseusMessage(OdysseusMessageType.DoQuery,
				messageElements);

		((JxtaMessageSender) (thinPeerJxtaImpl.getMessageSender()))
				.sendMessage(thinPeerJxtaImpl.getNetPeerGroup(), message,
						adminPipe);
		log.addTab(q.getId(), query);
	}

}
