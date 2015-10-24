package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryCostMap;




/**
 * Provides Methods to implement custom Query Selector that selects Query Set for dynamic Load Balancing.
 * @author Carsten Cordess
 */
public interface IQuerySelector extends INamedInterface {
	
	/** Gets name of Trigger.
	 * @see de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface#getName()
	 */
	public String getName();
	
	/**
	 * Selects Querie Set to redistribute for dynamic Load balancing.
	 * @param allQueries @link{de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryCostMap} of all Queries to select from 
	 * @param minCpuLoad Minimum Cpu Load that needs to be removed
	 * @param minMemLoad Minimum Memory Load that needs to be removed
	 * @param minNetLoad Minimum Net Load that needs to be removed.
	 * @return @link{de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryCostMap} with selected Queries.
	 */
	public QueryCostMap selectQueries(QueryCostMap allQueries,double minCpuLoad, double minMemLoad, double minNetLoad);
}
