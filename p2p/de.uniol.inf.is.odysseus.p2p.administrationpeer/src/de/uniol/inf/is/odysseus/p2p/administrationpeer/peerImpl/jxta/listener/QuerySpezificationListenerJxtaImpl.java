package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener;

import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IQuerySpezificationListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.logging.Log;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryTranslationSpezification;

/**
 * Finden und bewerben von Anfragen, welche durch Thin-Peers ausgeschrieben werden. 
 * 
 * @author Mart Köhler
 *
 */
public class QuerySpezificationListenerJxtaImpl implements IQuerySpezificationListener,
		DiscoveryListener {
	
	
	private int ADVS_PER_PEER = 6;
	
	private int WAIT_TIME = 6000;

	public QuerySpezificationListenerJxtaImpl() {
		AdministrationPeerJxtaImpl.getInstance().getDiscoveryService().addDiscoveryListener(this);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
			//Suchanfrage nach Ausschreibungen von Thin-Peers
			AdministrationPeerJxtaImpl.getInstance().getDiscoveryService().getRemoteAdvertisements(
					null, DiscoveryService.ADV, "queryId", "*", ADVS_PER_PEER, null);
		}
	}

	@Override
	public synchronized void discoveryEvent(DiscoveryEvent ev) {
		DiscoveryResponseMsg res = ev.getResponse();
		QueryTranslationSpezification adv = null;
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {
				try {
					Object temp2 = en.nextElement();
					if (temp2 instanceof QueryTranslationSpezification ){
						adv = (QueryTranslationSpezification) temp2;
					}
					else{
						//Ist keine Ausschreibung
						return;
					}

					if (AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().keySet().contains(adv.getQueryId())) {
						//Diese Ausschreibung wurde schon gefunden und kann ignoriert werden
						continue;
					}
					
					PipeAdvertisement pipeAdv = MessageTool.createPipeAdvertisementFromXml(adv.getBiddingPipe());
					QueryJxtaImpl q = new QueryJxtaImpl();
					q.setId(adv.getQueryId());
					q.setLanguage(adv.getLanguage());
					q.setQuery(adv.getQuery());
					q.setResponseSocketThinPeer(pipeAdv);
					PeerAdvertisement peerAdv = AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup().getPeerAdvertisement();
					
					synchronized(AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries()){
						AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().put(adv
							.getQueryId().toString(),q);
						Log.addQuery(adv.getQueryId());
						Log.logAction(adv.getQueryId(), "Anfrage gefunden !");
					}
					
					//TODO: einkommentieren, war zu testzwecken auskommentiert
//					if (biddingStrategy.bidding(q)){
						MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup(), pipeAdv, MessageTool.createMessage("Bidding", "query", adv.getQueryId(),  ((SocketServerListenerJxtaImpl) AdministrationPeerJxtaImpl.getInstance().getSocketServerListener()).getServerPipeAdvertisement(), peerAdv));
						Log.logAction(adv.getQueryId(), "Für Anfrage beworben !");
//					}
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}
		}

	}

}
