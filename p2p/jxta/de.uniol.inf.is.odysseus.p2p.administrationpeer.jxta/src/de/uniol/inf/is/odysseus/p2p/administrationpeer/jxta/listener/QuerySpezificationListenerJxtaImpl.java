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
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

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
	
	private IThinPeerBiddingStrategy biddingStrategy;

	private JxtaMessageSender sender;

	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	public QuerySpezificationListenerJxtaImpl(JxtaMessageSender sender, AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
		this.sender = sender;
		this.administrationPeerJxtaImpl = administrationPeerJxtaImpl;
		administrationPeerJxtaImpl.getDiscoveryService().addDiscoveryListener(this);
		//TODO: In Abhängigkeit der bereits laufenden Gebote und der laufenden Anfragen eine eigene Strategie?
		this.biddingStrategy = new MaxQueryBiddingStrategyJxtaImpl(administrationPeerJxtaImpl.getQueries());
		
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
			//Suchanfrage nach Ausschreibungen von Thin-Peers
			administrationPeerJxtaImpl.getDiscoveryService().getRemoteAdvertisements(
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

					if (administrationPeerJxtaImpl.getQueries().keySet().contains(adv.getQueryId()) ) {
						//Diese Ausschreibung wurde schon gefunden und kann ignoriert werden
						continue;
					}
					
					PipeAdvertisement pipeAdv = MessageTool.createPipeAdvertisementFromXml(adv.getBiddingPipe());
					QueryJxtaImpl q = new QueryJxtaImpl();
					q.setId(adv.getQueryId());
					q.setLanguage(adv.getLanguage());
					q.setDeclarativeQuery(adv.getQuery());
					q.setUser(GlobalState.getActiveUser());
					q.setDataDictionary(GlobalState.getActiveDatadictionary());
					q.setResponseSocketThinPeer(pipeAdv);
					PeerAdvertisement peerAdv = administrationPeerJxtaImpl.getNetPeerGroup().getPeerAdvertisement();
					
//					synchronized(AdministrationPeerJxtaImpl.getInstance().getQueries()){
						if(!administrationPeerJxtaImpl.getQueries().containsKey(q)) {
							administrationPeerJxtaImpl.addQuery(q);
//							AdministrationPeerJxtaImpl.getInstance().getQueries().put(q, AdministrationPeerJxtaImpl.getInstance().getExecutionListenerFactory().getNewInstance(q, AdministrationPeerJxtaImpl.getInstance().getExecutionHandler()));
							Log.addQuery(adv.getQueryId());
							Log.logAction(adv.getQueryId(), "Anfrage gefunden !");
							Log.logAction(adv.getQueryId(), adv.getQuery());
							if (biddingStrategy.bidding(q)){
								HashMap<String, Object> messageElements = new HashMap<String, Object>();
								messageElements.put("queryAction", "Bidding");
								
								messageElements.put("queryId", q.getId());
								messageElements.put("responsePipeAdvertisement", administrationPeerJxtaImpl.getServerPipeAdvertisement());
								messageElements.put("peerAdvertisement", peerAdv);
//								messageElements.put("thinPeerPipeAdvertisement", pipeAdv);
								Message response = MessageTool.createSimpleMessage(
										"QueryNegotiation", messageElements);

//								MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup(), pipeAdv, response);
								this.sender.sendMessage(administrationPeerJxtaImpl.getNetPeerGroup(), response, pipeAdv);
								//TODO Überprüfen, ob die Kommunikation funktioniert
//								MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup(), pipeAdv, MessageTool.createMessage("QueryNegotiation", "Bidding", adv.getQueryId(),  AdministrationPeerJxtaImpl.getInstance().getServerPipeAdvertisement(), peerAdv));
								Log.logAction(adv.getQueryId(), "Für Anfrage beworben !");
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
