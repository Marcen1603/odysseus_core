package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.Query;

public interface IDistributionProvider {
	public void distributePlan(Query query);
	public void initializeService();
	public void startService();
	public String getDistributionStrategy();
	public void setManagedQueries(HashMap<String, Query> queries);
	public HashMap<String, Query> getManagedQueries();
	void setActiveQueries(HashMap<String, Query> queries);
	public HashMap<String, Query> getActiveQueries();
	public IServerSocketConnectionHandler getServerSocketConnectionHandler();
	// Alle Objekte, welche auf Ebene des Frameworks nicht definiert werden sollten wie Jxta spezifische Objekte
	public void setParameter(Object... param);
}
