package de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.IExcludedQueryRegistryListener;

public interface IExcludedQueriesRegistry {

	public void excludeQueryIdFromLoadBalancing(int queryID);
	public void removeExcludedQueryID(int queryID);
	public boolean isQueryIDExcludedFromLoadBalancing(int queryID);
	

	public void addListener(IExcludedQueryRegistryListener listener);
	public void removeListener(IExcludedQueryRegistryListener listener);

}
