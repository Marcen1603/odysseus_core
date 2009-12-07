package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import org.apache.commons.codec.binary.Base64OutputStream;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.handler.BiddingHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler.BiddingMessageResultHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler.EventMessageHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;

public class BiddingProvider extends AbstractDistributionProvider<AbstractPeer> {

	private Thread biddingHandlerThread;
	private IBiddingHandler biddingHandler;
	private String events =  "OpenInit,OpenDone,ProcessInit,ProcessDone,ProcessInitNeg,ProcessDoneNeg,PushInit,PushDone,PushInitNeg,PushDoneNeg,Done" ;
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
		getPeer().getLogger().info("Initializing message handler");
		getPeer().registerMessageHandler(new BiddingMessageResultHandler(getPeer().getQueries()));
		getPeer().registerMessageHandler(new EventMessageHandler());
		getPeer().getLogger().info("Initializing execution handler factories");
		IExecutionHandler<AbstractPeer> executionHandler = new BiddingProviderExecutionHandler<AbstractPeer>(Lifecycle.DISTRIBUTION, this, getPeer());
		getExecutionHandlerFactory().setExecutionHandler(executionHandler);
		getPeer().addExecutionHandlerFactory(getExecutionHandlerFactory());
		initBiddingHandler();
			
	}
	
	@Override
	public void finalizeService() {
		getPeer().removeExecutionHandlerFactory(getExecutionHandlerFactory());
	};
	
	private void startDiscoveryService() {
		this.discoveryService = PeerGroupTool.getPeerGroup().getDiscoveryService();
	}

	@Override
	public void startService() {
		startBiddingHandler();
		startDiscoveryService();
	}

	@Override
	public void distributePlan(Query query, Object serverResponse) {
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
			//query.setStatus(Status.BIDDING);
			// Hier wird dann die Ausführung der Subpläne ausgeschrieben
			for(Subplan subplan : query.getSubPlans().values()) {
				

				QueryExecutionSpezification adv = (QueryExecutionSpezification) AdvertisementFactory
						.newAdvertisement(QueryExecutionSpezification
								.getAdvertisementType());

				adv
						.setBiddingPipe(((PipeAdvertisement)serverResponse).toString());
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

	/**
	 * In Intervallen werden die Gebote zu den Queries überprüft und Teilpläne den entsprechenden Operator-Peers zugewiesen.
	 */
	private void initBiddingHandler() {
		this.biddingHandler = new BiddingHandlerJxtaImpl(getPeer().getQueries(), getEvents());
	}

	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
	}
}
