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

import de.uniol.inf.is.odysseus.logicaloperator.base.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.IClientSelector;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler.BiddingMessageResultHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler.EventMessageHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

public class BiddingProvider<R extends PipeAdvertisement> extends AbstractDistributionProvider<R> {

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
	
	@SuppressWarnings("unchecked")
	@Override
	public void initializeService() {
		getPeer().getLogger().info("Initializing message handler");
		
		getRegisteredMessageHandler().add(new EventMessageHandler());
		getRegisteredMessageHandler().add(new BiddingMessageResultHandler(getPeer().getQueries()));
		for(IMessageHandler handler : getRegisteredMessageHandler()) {
			getPeer().registerMessageHandler(handler);
		}
		getPeer().getLogger().info("Initializing execution handler");
//		IExecutionHandler h = new BiddingProviderExecutionHandler<AbstractPeer, IDistributionProvider>(Lifecycle.DISTRIBUTION, this, getPeer());
		IExecutionHandler handler = new BiddingProviderExecutionHandler(Lifecycle.DISTRIBUTION, this, getPeer());
			getRegisteredExecutionHandler().add(new BiddingProviderExecutionHandler(Lifecycle.DISTRIBUTION, this, getPeer()));
			getPeer().bindExecutionHandler(handler);
//		getExecutionHandlerFactory().setExecutionHandler(executionHandler);
//		getPeer().addExecutionHandlerFactory(getExecutionHandlerFactory());
//		getPeer().bindExecutionHandler(executionHandler);
//		getPeer().getExecutionHandler().add(executionHandler);
			
	}
	
	@Override
	public void finalizeService() {
//		getPeer().removeExecutionHandlerFactory(getExecutionHandlerFactory());
		for(IExecutionHandler handler : getRegisteredExecutionHandler()) {
//			getPeer().getExecutionHandler().remove(handler);
			getPeer().unbindExecutionHandler(handler);
		}
		for(IMessageHandler handler : getRegisteredMessageHandler()) {
			getPeer().deregisterMessageHandler(handler);
		}		
	};
	
	private void startDiscoveryService() {
		this.discoveryService = PeerGroupTool.getPeerGroup().getDiscoveryService();
	}

	@Override
	public void startService() {
//		startBiddingHandler();
		startDiscoveryService();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void distributePlan(Query query, PipeAdvertisement serverResponse) {
		if(getDiscoveryService()==null) {
			return;
		}
		// get("1"), weil die subpläne von den senken zu den quellen gehen und wir die p2psink für den thin-peer wollen
		Subplan topSink = query.getSubPlans().get("1");
		String pipeAdv = ((P2PSinkAO) topSink.getAo()).getAdv();
		PeerAdvertisement peerAdv =PeerGroupTool.getPeerGroup().getPeerAdvertisement();
		//Thin-Peer erhält Verbindungsinformationen zur letzten P2PSink
		
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", query.getId());
		messageElements.put("queryAction", "ResultStreaming");
		messageElements.put("pipeAdvertisement", MessageTool
				.createPipeAdvertisementFromXml(pipeAdv));
		messageElements.put("peerAdvertisement", peerAdv);
		Message message = MessageTool.createSimpleMessage("QueryNegotiation", messageElements);
		
//		Message thinPeerResponse = MessageTool
//				.createMessage(
//						"ResultStreaming",
//						"queryId",
//						query.getId(),
//						MessageTool
//								.createPipeAdvertisementFromXml(pipeAdv),
//						peerAdv);
//		MessageTool.sendMessage(PeerGroupTool.getPeerGroup(),
//				((QueryJxtaImpl)query)
//						.getResponseSocketThinPeer(),
//				message);
			getPeer().getMessageSender().sendMessage(PeerGroupTool.getPeerGroup(), message, ((QueryJxtaImpl)query)
									.getResponseSocketThinPeer());
			Log.logAction(query.getId(), "Sende Thin-Peer Verbindungsinformationen zum Operatorplan");
			
			// Anfragen ausschreiben
			//query.setStatus(Status.BIDDING);
			// Hier wird dann die Ausführung der Subpläne ausgeschrieben
			for(Subplan subplan : query.getSubPlans().values()) {
				Log.logAction(query.getId(), "Teilplan "+ subplan.getId()+" ausschreiben: "+AbstractTreeWalker.prefixWalk(subplan.getAo(),
						new AlgebraPlanToStringVisitor()));
				//Falls der Subplan neu verteilt werden sollte, dann sind alte Gebote nicht mehr gültig
				subplan.getBiddings().clear();
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
			IClientSelector selector = getClientSelectorFactory().getNewInstance(15000, query, getCallback());
			Thread t = new Thread(selector);
			t.start();
	}


	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
	}


}
