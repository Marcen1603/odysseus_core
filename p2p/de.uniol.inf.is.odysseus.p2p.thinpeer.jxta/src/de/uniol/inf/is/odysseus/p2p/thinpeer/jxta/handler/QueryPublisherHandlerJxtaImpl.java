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
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IQueryPublisher;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

public class QueryPublisherHandlerJxtaImpl implements IQueryPublisher {

	// Wie lange ist eine Ausschreibung gültig.
	private int VALID = 15000;

	public void publishQuerySpezification(String queryId, String query,
			String language) {

		QueryJxtaImpl q = new QueryJxtaImpl();
		q.setDeclarativeQuery(query);
		q.setId(queryId);
		QueryTranslationSpezification adv = (QueryTranslationSpezification) AdvertisementFactory
				.newAdvertisement(QueryTranslationSpezification
						.getAdvertisementType());
		adv.setQuery(query);
		adv
				.setBiddingPipe(ThinPeerJxtaImpl.getInstance().getServerResponseAddress().toString());
		adv.setPeer(ThinPeerJxtaImpl.getInstance().getNetPeerGroup()
				.getPeerAdvertisement().toString());
		adv.setQueryId(q.getId());
		adv.setLanguage(language);
		ThinPeerJxtaImpl.getInstance().addQuery(q);
		try {
			ThinPeerJxtaImpl.getInstance().getDiscoveryService().publish(adv,
					VALID, VALID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ThinPeerJxtaImpl.getInstance().getDiscoveryService().remotePublish(adv,
				VALID);

		Log.addTab(q.getId(), query);
	}

	@Override
	public void sendQuerySpezificationToAdminPeer(String queryId, String query,
			String language, String adminPeer) {

		PipeAdvertisement adminPipe = MessageTool
				.createPipeAdvertisementFromXml(((ExtendedPeerAdvertisement)ThinPeerJxtaImpl.getInstance()
						.getAdminPeers().get(adminPeer)).getPipe());
		PipeAdvertisement thinPeerPipe = (PipeAdvertisement) ThinPeerJxtaImpl
				.getInstance().getServerResponseAddress();

		QueryJxtaImpl q = new QueryJxtaImpl();
		q.setDeclarativeQuery(query);
		q.setId(queryId);
		BidJxtaImpl bid = new BidJxtaImpl();
		bid.setResponseSocket(adminPipe);
		ThinPeerJxtaImpl.getInstance().addQuery(q);

//		Message message = MessageTool.createSimpleMessage("DoQuery", "queryId",
//				"query", "language", "result", queryId, query, language,
//				"granted", thinPeerPipe);
		
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", queryId);
		messageElements.put("language", language);
		messageElements.put("result", "granted");
		messageElements.put("thinPeerPipe", thinPeerPipe);
		
		Message message = MessageTool.createSimpleMessage("DoQuery", messageElements);
		MessageTool.sendMessage(ThinPeerJxtaImpl.getInstance()
				.getNetPeerGroup(), adminPipe, message);

		Log.addTab(q.getId(), query);
	}

}
