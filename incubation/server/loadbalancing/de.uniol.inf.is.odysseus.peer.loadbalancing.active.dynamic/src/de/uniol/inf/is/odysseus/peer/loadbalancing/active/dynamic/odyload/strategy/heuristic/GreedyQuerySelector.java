package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.QueryLoadInformation;

public class GreedyQuerySelector implements IQuerySelectionStrategy {
	
			
	
	private static final Logger LOG = LoggerFactory.getLogger(GreedyQuerySelector.class);
	
	public GreedyQuerySelector() {
		
	}
	
	@Override
	public QueryCostMap selectQueries(QueryCostMap allQueries, double minCpuLoad, double minMemLoad, double minNetLoad) {
		
		LOG.debug("Greedy Selection: Need to find combination to remove:");
		LOG.debug("{} of CPU-Load",minCpuLoad);
		LOG.debug("{} of MEM-Load",minMemLoad);
		LOG.debug("{} of NET-Load",minNetLoad);
		
		
		QueryCostMap chosenSubset = new QueryCostMap();
		
		
		List<QueryLoadInformation> sortedQueries = allQueries.getQueriesSortedByCost();
		Iterator<QueryLoadInformation> iter = sortedQueries.iterator();
		
		while((chosenSubset.getTotalCpuLoad()<=minCpuLoad || chosenSubset.getTotalMemLoad()<=minMemLoad || chosenSubset.getTotalNetLoad()<=minNetLoad) && iter.hasNext()) {
			chosenSubset.add(iter.next().clone());
		}
		
		LOG.debug("Chosen Subset is:");
		LOG.debug(chosenSubset.toString());
		return chosenSubset;
	
	}
	
}
