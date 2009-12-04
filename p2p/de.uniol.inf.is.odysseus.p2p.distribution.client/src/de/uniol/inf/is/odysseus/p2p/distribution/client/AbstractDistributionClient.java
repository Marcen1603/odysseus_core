package de.uniol.inf.is.odysseus.p2p.distribution.client;

import java.util.HashMap;
import de.uniol.inf.is.odysseus.p2p.peer.execution.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public abstract class AbstractDistributionClient implements IDistributionClient {

	protected HashMap<Query, IExecutionListener> managedQueries = new HashMap<Query, IExecutionListener>();
	
	@Override
	public abstract String getDistributionStrategy();

	@Override
	public HashMap<Query, IExecutionListener> getManagedQueries() {
		return this.managedQueries;
	}

	@Override
	public void setManagedQueries(HashMap<Query, IExecutionListener> managedQueries) {
		this.managedQueries = managedQueries;
	}
	
	@Override
	public abstract IMessageHandler getMessageHandler();

	@Override
	public abstract void initializeService();


	@Override
	public abstract void startService();

}
