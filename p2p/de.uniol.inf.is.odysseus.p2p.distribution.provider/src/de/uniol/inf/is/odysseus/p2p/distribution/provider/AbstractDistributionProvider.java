package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.Query;

public abstract class AbstractDistributionProvider implements
		IDistributionProvider {

	protected HashMap<String, Query> managedQueries = new HashMap<String, Query>();
	protected HashMap<String, Query> activeQueries = new HashMap<String, Query>();
	
	@Override
	public void setManagedQueries(HashMap<String, Query> queries) {
		this.managedQueries = queries;
	}

	@Override
	public HashMap<String, Query> getManagedQueries() {
		return managedQueries;
	}
	
	@Override
	public HashMap<String, Query> getActiveQueries() {
		return activeQueries;
	}

	@Override
	public void setActiveQueries(HashMap<String, Query> queries) {
		this.activeQueries = queries;
		
	}

	@Override
	public abstract void distributePlan(Query query);

	@Override
	public abstract String getDistributionStrategy();

	@Override
	public abstract void initializeService();
	
	@Override
	public abstract void startService();
	
	public AbstractDistributionProvider() {
	}
	

}
