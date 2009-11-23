package de.uniol.inf.is.odysseus.p2p.distribution.client;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public abstract class AbstractDistributionClient implements IDistributionClient {

	protected HashMap<String, Query> managedQueries = new HashMap<String, Query>();
	
	@Override
	public abstract String getDistributionStrategy();

	@Override
	public HashMap<String, Query> getManagedQueries() {
		return this.managedQueries;
	}

	@Override
	public void setManagedQueries(HashMap<String, Query> managedQueries) {
		this.managedQueries = managedQueries;
	}
	
	@Override
	public abstract IMessageHandler getMessageHandler();

	@Override
	public abstract void initializeService();


	@Override
	public abstract void startService();

}
