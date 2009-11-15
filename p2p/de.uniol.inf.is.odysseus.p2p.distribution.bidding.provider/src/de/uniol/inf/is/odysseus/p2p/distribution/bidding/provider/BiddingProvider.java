package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import de.uniol.inf.is.odysseus.p2p.Query;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.bidding.IThinPeerBiddingStrategy;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.bidding.MaxQueryBiddingStrategyJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.handler.BiddingHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.PeerGroupTool;

public class BiddingProvider extends AbstractDistributionProvider {

	private Thread biddingHandlerThread;
	private Thread eventListenerThread;
	private IBiddingHandler biddingHandler;
	private IThinPeerBiddingStrategy biddingStrategy;
	protected IEventListener eventListener;
	private String events =  "OpenInit,OpenDone,ProcessInit,ProcessDone,ProcessInitNeg,ProcessDoneNeg,PushInit,PushDone,PushInitNeg,PushDoneNeg,Done" ;
	
	@Override
	public String getDistributionStrategy() {
		return "Bidding";
	}

	@Override
	public void initializeService() {
		System.out.println("Initialisiere Bidding Provider Dienste");
			initBiddingStrategy();
			initEventListener();
			initBiddingHandler();
	}
	
	@Override
	public void startService() {
		startEventListener();
		startBiddingHandler();
	}

	@Override
	public void distributePlan(Query query) {
		// TODO Auto-generated method stub
		
	}
	
	private void startBiddingHandler() {
	if (biddingHandlerThread != null && biddingHandlerThread.isAlive()) {
		biddingHandlerThread.interrupt();
	}
	this.biddingHandlerThread = new Thread(biddingHandler);
	biddingHandlerThread.start();
	}

	private void initBiddingHandler() {
		this.biddingHandler = new BiddingHandlerJxtaImpl(queries, getEvents(), getEventListener());
	}
	
	private void initBiddingStrategy() {
		this.biddingStrategy = new MaxQueryBiddingStrategyJxtaImpl(queries);
	}

	private void initEventListener() {

		eventListener = new EventListenerJxtaImpl();
	}	
	
	private void startEventListener() {
		if (eventListenerThread != null
				&& eventListenerThread.isAlive()) {
			eventListenerThread.interrupt();
		}
		//PipeAdvertisement muss noch gesetzt werden. Erst dann, wenn alle Dienste initialisiert wurden.
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
}
