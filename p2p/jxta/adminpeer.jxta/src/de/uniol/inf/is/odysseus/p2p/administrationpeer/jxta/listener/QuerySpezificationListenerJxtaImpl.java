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
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.strategy.MaxQueryBiddingStrategyJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IQuerySpezificationListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IThinPeerBiddingStrategy;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * Finden und bewerben von Anfragen, welche durch Thin-Peers ausgeschrieben werden. 
 * 
 * @author Mart Koehler
 *
 */
public class QuerySpezificationListenerJxtaImpl implements IQuerySpezificationListener,
		DiscoveryListener {
	
	
	private int ADVS_PER_PEER = 6;
	
	private int WAIT_TIME = 6000;
	
	private IThinPeerBiddingStrategy biddingStrategy;

	private JxtaMessageSender sender;

	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	private ILogListener log;

	public QuerySpezificationListenerJxtaImpl(JxtaMessageSender sender, AdministrationPeerJxtaImpl administrationPeerJxtaImpl, ILogListener log) {
		this.sender = sender;
		this.administrationPeerJxtaImpl = administrationPeerJxtaImpl;
		administrationPeerJxtaImpl.getDiscoveryService().addDiscoveryListener(this);
		//TODO: In Abhängigkeit der bereits laufenden Gebote und der laufenden Anfragen eine eigene Strategie?
		this.biddingStrategy = new MaxQueryBiddingStrategyJxtaImpl(administrationPeerJxtaImpl);
		this.log = log;
		
	}

	@Override
	public void run() {
		while (true) {
			//Suchanfrage nach Ausschreibungen von Thin-Peers
			administrationPeerJxtaImpl.getDiscoveryService().getRemoteAdvertisements(
					null, DiscoveryService.ADV, "queryId", "*", ADVS_PER_PEER, null);
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}

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

					if (administrationPeerJxtaImpl.hasQuery(adv.getQueryId()) ) {
						//Diese Ausschreibung wurde schon gefunden und kann ignoriert werden
						continue;
					}
					
					PipeAdvertisement pipeAdv = MessageTool.createPipeAdvertisementFromXml(adv.getBiddingPipe());
					P2PQueryJxtaImpl q = new P2PQueryJxtaImpl();
					q.setId(adv.getQueryId());
					q.setLanguage(adv.getLanguage());
					q.setDeclarativeQuery(adv.getQuery());
					q.setUser(GlobalState.getActiveUser());
					q.setDataDictionary(GlobalState.getActiveDatadictionary());
					q.setResponseSocketThinPeer(pipeAdv);
					PeerAdvertisement peerAdv = administrationPeerJxtaImpl.getNetPeerGroup().getPeerAdvertisement();
					
						if(!administrationPeerJxtaImpl.hasQuery(q.getId())) {
							administrationPeerJxtaImpl.addQuery(q);
							log.addQuery(adv.getQueryId());
							log.logAction(adv.getQueryId(), "Anfrage gefunden.");
							log.logAction(adv.getQueryId(), adv.getQuery());
							if (biddingStrategy.bidding(q)){
								HashMap<String, Object> messageElements = new HashMap<String, Object>();
								messageElements.put("queryAction", "Bidding");
								
								messageElements.put("queryId", q.getId());
								messageElements.put("responsePipeAdvertisement", administrationPeerJxtaImpl.getServerPipeAdvertisement());
								messageElements.put("peerAdvertisement", peerAdv);
								Message response = MessageTool.createSimpleMessage(
										"QueryNegotiation", messageElements);

								this.sender.sendMessage(administrationPeerJxtaImpl.getNetPeerGroup(), response, pipeAdv);
								log.logAction(adv.getQueryId(), "Fuer Anfrage beworben. Warte auf Antwort...");
							}
						}
//					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}


}
