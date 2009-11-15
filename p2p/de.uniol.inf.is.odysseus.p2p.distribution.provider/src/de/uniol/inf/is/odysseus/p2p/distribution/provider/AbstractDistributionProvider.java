package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.Query;

public abstract class AbstractDistributionProvider implements
		IDistributionProvider {

	protected HashMap<String, Query> queries = new HashMap<String, Query>();
	
	@Override
	public void setQueries(HashMap<String, Query> queries) {
		this.queries = queries;
	}

	@Override
	public HashMap<String, Query> getQueries() {
		return queries;
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
