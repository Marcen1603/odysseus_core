package de.uniol.inf.is.odysseus.p2p.distribution.provider;


import java.util.HashMap;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IDistributionProvider {
	public void initializeService();
	public void startService();
	public String getDistributionStrategy();
	public HashMap<String, Query> getManagedQueries();
	public IMessageHandler getMessageHandler();
	void distributePlan(Query query, Object serverResponse);
	public void setManagedQueries(HashMap<String, Query> queries);
}
