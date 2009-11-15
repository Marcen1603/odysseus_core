package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;

import de.uniol.inf.is.odysseus.p2p.distribution.client.IDistributionClient;

public class BiddingClient implements IDistributionClient {

	@Override
	public String getDistributionStrategy() {
		return "Bidding";
	}

	@Override
	public boolean initializeService() {
		// TODO Auto-generated method stub
		return false;
	}

}
