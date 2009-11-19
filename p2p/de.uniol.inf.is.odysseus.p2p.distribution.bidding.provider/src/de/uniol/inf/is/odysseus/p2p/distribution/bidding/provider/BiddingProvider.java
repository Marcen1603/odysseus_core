package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

import org.apache.commons.codec.binary.Base64OutputStream;


import de.uniol.inf.is.odysseus.p2p.IQueryResultHandler;
import de.uniol.inf.is.odysseus.p2p.Log;
import de.uniol.inf.is.odysseus.p2p.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.Query;
import de.uniol.inf.is.odysseus.p2p.Subplan;
import de.uniol.inf.is.odysseus.p2p.Query.Status;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.handler.BiddingHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.IServerSocketConnectionHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryExecutionSpezification;

public class BiddingProvider extends AbstractDistributionProvider {

	private Thread biddingHandlerThread;
	private Thread eventListenerThread;
	private IBiddingHandler biddingHandler;
//	private IThinPeerBiddingStrategy biddingStrategy;
	protected IEventListener eventListener;
	private String events =  "OpenInit,OpenDone,ProcessInit,ProcessDone,ProcessInitNeg,ProcessDoneNeg,PushInit,PushDone,PushInitNeg,PushDoneNeg,Done" ;
	private IServerSocketConnectionHandler connectionHandler;
	private PipeAdvertisement serverPipeAdvertisement;
	private DiscoveryService discoveryService;
	private IQueryResultHandler queryResultHandler;
	
	public PipeAdvertisement getServerPipeAdvertisement() {
		return serverPipeAdvertisement;
	}

	public DiscoveryService getDiscoveryService() {
		return discoveryService;
	}

	public IQueryResultHandler getQueryResultHandler() {
		return queryResultHandler;
	}

	@Override
	public String getDistributionStrategy() {
		return "Bidding";
	}

	public BiddingProvider() {
		initializeService();
	}
	
	@Override
	public void initializeService() {
		System.out.println("Initialisiere Bidding Provider Dienste");
			initEventListener();
			initBiddingHandler();
			initServerSocketConnectionHandler();
	}
	

	private void initServerSocketConnectionHandler() {
		this.connectionHandler = new ServerSocketConnectionHandler(this);
	}

	@Override
	public void startService() {
		startEventListener();
		startBiddingHandler();
		
	}

	@Override
	public void distributePlan(Query query) {
		// get("1"), weil die subpläne von den senken zu den quellen gehen und wir die p2psink für den thin-peer wollen
		Subplan topSink = query.getSubPlans().get("1");
		String pipeAdv = ((P2PSinkAO) topSink.getAo()).getAdv();
		PeerAdvertisement peerAdv =PeerGroupTool.getPeerGroup().getPeerAdvertisement();
		//Thin-Peer erhält Verbindungsinformationen zur letzten P2PSink
		Message thinPeerResponse = MessageTool
				.createMessage(
						"ResultStreaming",
						"queryId",
						query.getId(),
						MessageTool
								.createPipeAdvertisementFromXml(pipeAdv),
						peerAdv);
		MessageTool.sendMessage(PeerGroupTool.getPeerGroup(),
				((QueryJxtaImpl)query)
						.getResponseSocketThinPeer(),
				thinPeerResponse);
		
		
			Log.logAction(query.getId(), "Anfrage ausschreiben");
			// Anfragen ausschreiben
			query.setStatus(Status.BIDDING);
			// Hier wird dann die Ausführung der Subpläne ausgeschrieben
			for(Subplan subplan : query.getSubPlans().values()) {
				

				QueryExecutionSpezification adv = (QueryExecutionSpezification) AdvertisementFactory
						.newAdvertisement(QueryExecutionSpezification
								.getAdvertisementType());

				adv
						.setBiddingPipe(getServerPipeAdvertisement().toString());
				adv.setQueryId(query.getId());
				adv.setSubplanId(subplan.getId());
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
					oos.writeObject(subplan);
					b64os.flush();
				    oos.flush();
				    b64os.close();
				    oos.close();
				    bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
							 				

				try {
					adv.setSubplan(new String(bos.toByteArray(),"utf-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				adv.setLanguage(query.getLanguage());
				try {
					getDiscoveryService().publish(adv, 15000, 15000);
					getDiscoveryService().remotePublish(adv,15000);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
	}
	
	private void startBiddingHandler() {
	if (biddingHandlerThread != null && biddingHandlerThread.isAlive()) {
		biddingHandlerThread.interrupt();
	}
	this.biddingHandlerThread = new Thread(biddingHandler);
	biddingHandlerThread.start();
	}

	private void initBiddingHandler() {
		this.biddingHandler = new BiddingHandlerJxtaImpl(getManagedQueries(), getEvents(), getEventListener());
	}
	

	private void initEventListener() {

		eventListener = new EventListenerJxtaImpl();
	}	
	
	private void startEventListener() {
		if (eventListenerThread != null
				&& eventListenerThread.isAlive()) {
			eventListenerThread.interrupt();
		}
		//PipeAdvertisement muss noch gesetzt werden. Erst dann, wenn alle Dienste initialisiert wurden. PeerGroupTool wird erst nach der Service Einbindung initialisiert
		((EventListenerJxtaImpl)eventListener).setPipeAdv(AdvertisementTools.getServerPipeAdvertisement(PeerGroupTool.getPeerGroup()));
		eventListenerThread = new Thread(eventListener);
		eventListenerThread.start();
		
	}
	public IEventListener getEventListener() {
		return eventListener;
	}

	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
	}

	@Override
	public IServerSocketConnectionHandler getServerSocketConnectionHandler() {
		return connectionHandler;
	}

	@Override
	public void setParameter(Object... param) {
		for(Object elem : param) {
			//aus ((SocketServerListenerJxtaImpl) AdministrationPeerJxtaImpl.getInstance().getSocketServerListener()).getServerPipeAdvertisement().toString()
			if(elem instanceof PipeAdvertisement) {
				this.serverPipeAdvertisement = (PipeAdvertisement)elem;
			}
			else if(elem instanceof DiscoveryService) {
				this.discoveryService = (DiscoveryService)elem;
			}
			else if(elem instanceof IQueryResultHandler) {
				this.queryResultHandler = (IQueryResultHandler)elem;
			}
		}
	}
}
