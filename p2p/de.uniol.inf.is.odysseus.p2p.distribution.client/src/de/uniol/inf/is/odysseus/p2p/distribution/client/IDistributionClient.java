package de.uniol.inf.is.odysseus.p2p.distribution.client;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;

public interface IDistributionClient {
	public void initializeService();
	public void startService();
	public String getDistributionStrategy();
	HashMap<String, Query> getManagedQueries();
	void setManagedQueries(HashMap<String, Query> managedQueries);
	public IMessageHandler getMessageHandler();
	
}
