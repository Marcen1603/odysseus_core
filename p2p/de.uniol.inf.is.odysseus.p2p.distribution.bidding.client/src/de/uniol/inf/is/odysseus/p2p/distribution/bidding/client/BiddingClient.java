package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;



import de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.messagehandler.QueryResultHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.distribution.client.AbstractDistributionClient;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;

public class BiddingClient<T extends AbstractPeer> extends AbstractDistributionClient<T> {
	private IMessageHandler queryResultHandler;
	private IQuerySpecificationListener listener;

	public BiddingClient() {
	}
	
	@Override
	public String getDistributionStrategy() {
		return "BiddingClient";
	}

	@Override
	public void initializeService() {
		initQueryResultHandler();
		
	}

	private void startQuerySpezificationListener(){
		this.listener = new QuerySpecificationListenerJxtaImpl(getPeer(), getReceiverStrategy());
	}

	private void initQueryResultHandler() {
		queryResultHandler = new QueryResultHandlerJxtaImpl(getPeer().getQueries());
		
	}

	@Override
	public void startService() {
		startQuerySpezificationListener();
		Thread t = new Thread(getListener());
		t.start();
	}

	@Override
	public IMessageHandler getMessageHandler() {
		return this.queryResultHandler;
	}

	public IQuerySpecificationListener getListener() {
		return listener;
	}

}
