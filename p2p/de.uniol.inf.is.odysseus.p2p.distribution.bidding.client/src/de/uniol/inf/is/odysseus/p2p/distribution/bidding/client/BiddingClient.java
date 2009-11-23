package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;



import de.uniol.inf.is.odysseus.p2p.distribution.client.AbstractDistributionClient;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;

public class BiddingClient extends AbstractDistributionClient {
	private IMessageHandler queryResultHandler;

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

	private void initQueryResultHandler() {
		queryResultHandler = new QueryResultHandlerJxtaImpl(getManagedQueries());
		
	}

	@Override
	public void startService() {
		
	}

	@Override
	public IMessageHandler getMessageHandler() {
		return this.queryResultHandler;
	}



	
	



}
