package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryCostMap;




public interface IQuerySelector extends INamedInterface {
	public String getName();
	public QueryCostMap selectQueries(QueryCostMap costMap,double minCpuLoad, double minMemLoad, double minNetLoad);
}
