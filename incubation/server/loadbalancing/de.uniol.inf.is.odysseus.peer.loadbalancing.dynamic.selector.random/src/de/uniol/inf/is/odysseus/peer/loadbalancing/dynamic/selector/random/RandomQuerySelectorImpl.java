package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.selector.random;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryCostMap;

public class RandomQuerySelectorImpl implements IQuerySelector {


	private static final Logger LOG = LoggerFactory.getLogger(RandomQuerySelectorImpl.class);
	
	@Override
	public String getName() {
		return "Random";
	}

	@Override
	public QueryCostMap selectQueries(QueryCostMap costMap, double minCpuLoad,
			double minMemLoad, double minNetLoad) {
		QueryCostMap result = new QueryCostMap();
		
		
		if(costMap.getQueryIds().size()<=1) {
			return new QueryCostMap(costMap);
		}
		
		List<Integer> queryIDs = costMap.getQueryIds();
		result.add(costMap.getQueryInformation(chooseRandom(queryIDs)));
		
		LOG.debug("Selected Random QueryID for LoadBalancing: {}",result.toString());
		
		return result;
	}
	
	
	private int chooseRandom(List<Integer> integers) {
		Random rnd = new Random(System.currentTimeMillis());
		int indexOfResult = rnd.nextInt(integers.size());
		return integers.get(indexOfResult);
		
	}

}
