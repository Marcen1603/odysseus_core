package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.QueryCostMap;



public interface IQuerySelectionStrategy {
	public QueryCostMap selectQueries(QueryCostMap costMap,double minCpuLoad, double minMemLoad, double minNetLoad);
}
