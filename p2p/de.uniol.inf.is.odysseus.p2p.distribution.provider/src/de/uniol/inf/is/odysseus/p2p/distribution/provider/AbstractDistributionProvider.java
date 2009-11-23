package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public abstract class AbstractDistributionProvider implements IDistributionProvider {

	protected HashMap<String, Query> managedQueries = null;
	
	public AbstractDistributionProvider() {
		this.managedQueries = new HashMap<String, Query>();
	}
	
	@Override
	public abstract void distributePlan(Query query, Object serverResponse);

	@Override
	public abstract String getDistributionStrategy();

	@Override
	public HashMap<String, Query> getManagedQueries() {
		return this.managedQueries;
	}

	@Override
	public abstract IMessageHandler getMessageHandler();

	@Override
	public abstract void initializeService();


	@Override
	public abstract void startService();

	@Override
	public abstract void setManagedQueries(HashMap<String, Query> queries);

}
