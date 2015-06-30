package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;


public interface IQuerySelectionStrategy {
	public QueryCostMap selectQueries(QueryCostMap costMap,double minCpuLoad, double minMemLoad, double minNetLoad);
}
