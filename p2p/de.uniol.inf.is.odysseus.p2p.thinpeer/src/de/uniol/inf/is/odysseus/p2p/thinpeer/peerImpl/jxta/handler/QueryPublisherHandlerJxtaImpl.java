package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.handler;

import java.io.IOException;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IQueryPublisher;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.listener.SocketServerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.queryAdministration.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.queryAdministration.Query;
import de.uniol.inf.is.odysseus.p2p.thinpeer.queryAdministration.Query.Status;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryTranslationSpezification;

public class QueryPublisherHandlerJxtaImpl implements IQueryPublisher {

	// Wie lange ist eine Ausschreibung gültig.
	private int VALID = 15000;

	public void publishQuerySpezification(String queryId, String query,
			String language) {

		QueryJxtaImpl q = new QueryJxtaImpl(query, queryId);

		QueryTranslationSpezification adv = (QueryTranslationSpezification) AdvertisementFactory
				.newAdvertisement(QueryTranslationSpezification
						.getAdvertisementType());
		adv.setQuery(query);
		adv
				.setBiddingPipe(((SocketServerListenerJxtaImpl) ThinPeerJxtaImpl.getInstance().getSocketServerListener())
						.getServerPipeAdvertisement().toString());
		adv.setPeer(ThinPeerJxtaImpl.getInstance().getNetPeerGroup()
				.getPeerAdvertisement().toString());
		adv.setQueryId(q.getId());
		adv.setLanguage(language);

		ThinPeerJxtaImpl.getInstance().getQueries().put(q.getId(), q);
		try {
			ThinPeerJxtaImpl.getInstance().getDiscoveryService().publish(adv,
					VALID, VALID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ThinPeerJxtaImpl.getInstance().getDiscoveryService().remotePublish(adv,
				VALID);

		ThinPeerJxtaImpl.getInstance().getGui().addTab(q.getId(), query);
	}

	@Override
	public void sendQuerySpezificationToAdminPeer(String queryId, String query,
			String language, String adminPeer) {

		PipeAdvertisement adminPipe = MessageTool
				.createPipeAdvertisementFromXml(ThinPeerJxtaImpl.getInstance()
						.getAdminPeers().get(adminPeer).getPipe());
		PipeAdvertisement thinPeerPipe = ((SocketServerListenerJxtaImpl) ThinPeerJxtaImpl
				.getInstance().getSocketServerListener())
				.getServerPipeAdvertisement();

		Query q = new Query(query, queryId);
		q.setStatus(Status.DIRECT);
		BidJxtaImpl bid = new BidJxtaImpl();
		bid.setResponseSocket(adminPipe);
		ThinPeerJxtaImpl.getInstance().getQueries().put(q.getId(), q);

		Message message = MessageTool.createSimpleMessage("DoQuery", "queryId",
				"query", "language", "result", queryId, query, language,
				"granted", thinPeerPipe);
		MessageTool.sendMessage(ThinPeerJxtaImpl.getInstance()
				.getNetPeerGroup(), adminPipe, message);

		ThinPeerJxtaImpl.getInstance().getGui().addTab(q.getId(), query);
	}

}
