package de.uniol.inf.is.odysseus.p2p.distribution.client;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.IExecutionListener;

public interface IDistributionClient {
	public void initializeService();
	public void startService();
	public String getDistributionStrategy();
	HashMap<Query, IExecutionListener> getManagedQueries();
	void setManagedQueries(HashMap<Query, IExecutionListener> hashMap);
	public IMessageHandler getMessageHandler();
	
}
