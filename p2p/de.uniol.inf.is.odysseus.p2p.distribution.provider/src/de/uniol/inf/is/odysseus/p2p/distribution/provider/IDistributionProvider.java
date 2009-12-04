package de.uniol.inf.is.odysseus.p2p.distribution.provider;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.peer.execution.IExecutionListener;

public interface IDistributionProvider {
	public void initializeService();
	public void startService();
	public String getDistributionStrategy();
	public HashMap<Query, IExecutionListener > getManagedQueries();
	public IMessageHandler getMessageHandler();
	void distributePlan(Query query, Object serverResponse);
	public void setManagedQueries(HashMap<Query, IExecutionListener > queries);
}
