package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public abstract class AbstractDistributionProvider implements IDistributionProvider {

	protected HashMap<Query, IExecutionListener> managedQueries = null;
	
	public AbstractDistributionProvider() {
		this.managedQueries = new HashMap<Query, IExecutionListener>();
	}
	
	@Override
	public abstract void distributePlan(Query query, Object serverResponse);

	@Override
	public abstract String getDistributionStrategy();

	@Override
	public HashMap<Query, IExecutionListener> getManagedQueries() {
		return this.managedQueries;
	}

	@Override
	public void setManagedQueries(HashMap<Query, IExecutionListener> queries) {
		this.managedQueries = queries;
	}

	@Override
	public abstract IMessageHandler getMessageHandler();

	@Override
	public abstract void initializeService();


	@Override
	public abstract void startService();

}
