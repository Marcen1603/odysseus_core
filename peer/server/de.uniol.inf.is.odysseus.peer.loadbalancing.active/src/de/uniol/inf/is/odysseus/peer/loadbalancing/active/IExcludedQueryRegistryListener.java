package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.List;

public interface IExcludedQueryRegistryListener {

	public void notifyExcludedQueriesChanged(List<Integer> queryIDs);
}
