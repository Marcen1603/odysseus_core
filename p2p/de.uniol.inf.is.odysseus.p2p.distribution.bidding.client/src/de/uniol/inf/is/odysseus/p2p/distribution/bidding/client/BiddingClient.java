package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;



import de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.messagehandler.QueryResultHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.distribution.client.AbstractDistributionClient;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IQuerySpecificationListener;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;

public class BiddingClient extends AbstractDistributionClient {
	private IMessageHandler queryResultHandler;
	private IQuerySpecificationListener<QueryExecutionSpezification> listener;

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
		this.listener = new QuerySpecificationListenerJxtaImpl<QueryExecutionSpezification>(getPeer(), getQuerySelectionStrategy());
	}

	private void initQueryResultHandler() {
		this.queryResultHandler = new QueryResultHandlerJxtaImpl(getPeer().getQueries());
		getPeer().registerMessageHandler(this.queryResultHandler);
		
	}

	@Override
	public void startService() {
		startQuerySpezificationListener();
		Thread t = new Thread(getQuerySpecificationListener());
		t.start();
	}

	@Override
	public IQuerySpecificationListener<QueryExecutionSpezification> getQuerySpecificationListener() {
		return listener;
	}


}
