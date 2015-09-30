package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.selector.random;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryCostMap;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryLoadInformation;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing.TransformationHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.IExcludedQueriesRegistry;

public class RandomQuerySelectorImpl implements IQuerySelector {

	private IExcludedQueriesRegistry excludedQueriesRegistry;
	private IServerExecutor executor;
	
	public void bindExecutor(IExecutor serv) {
		this.executor = (IServerExecutor)serv;
	}
	
	public void unbindExecutor(IExecutor serv) {
		if(this.executor==serv) {
			this.executor = null;
		}
	}
	
	public void bindExcludedQueriesRegistry(IExcludedQueriesRegistry serv) {
		this.excludedQueriesRegistry = serv;
	}
	
	public void unbindExcludedQueriesRegistry(IExcludedQueriesRegistry serv) {
		if(excludedQueriesRegistry==serv) {
			excludedQueriesRegistry = null;
		}
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(RandomQuerySelectorImpl.class);
	
	@Override
	public String getName() {
		return "Random";
	}

	@Override
	public QueryCostMap selectQueries(QueryCostMap costMap, double minCpuLoad,
			double minMemLoad, double minNetLoad) {
		
		//Do not select Queries with Calc Latency and DataratePOs
		costMap = removeCalclatencyAndDataratePOQueries(costMap);
		
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
	
	
	private QueryCostMap removeCalclatencyAndDataratePOQueries(QueryCostMap costmap) {
		QueryCostMap result = new QueryCostMap();
		for(QueryLoadInformation info : costmap.getQueriesSortedByCost()) {
			if(TransformationHelper.hasCalclatencyPOs(info.getQueryId(), executor)) {
				excludedQueriesRegistry.excludeQueryIdFromLoadBalancing(info.getQueryId());
			}
			
			if(TransformationHelper.hasDataratePOs(info.getQueryId(), executor)) {
				excludedQueriesRegistry.excludeQueryIdFromLoadBalancing(info.getQueryId());
			}
			result.add(info);
			
		}
		
		return result;
	}

}
