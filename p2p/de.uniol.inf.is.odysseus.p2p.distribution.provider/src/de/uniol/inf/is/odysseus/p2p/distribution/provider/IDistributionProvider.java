package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.Query;

public interface IDistributionProvider {
	public void distributePlan(Query query);
	public void initializeService();
	public void startService();
	public String getDistributionStrategy();
	public void setQueries(HashMap<String, Query> queries);
	public HashMap<String, Query> getQueries();
}
