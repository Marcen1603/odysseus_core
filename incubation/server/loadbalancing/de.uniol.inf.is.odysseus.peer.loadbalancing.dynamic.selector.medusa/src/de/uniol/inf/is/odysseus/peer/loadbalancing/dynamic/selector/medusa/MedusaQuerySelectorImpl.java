package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.selector.medusa;


import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryCostMap;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryLoadInformation;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.PriceCalculator;

public class MedusaQuerySelectorImpl implements IQuerySelector {
	

	@Override
	public String getName() {
		return "Medusa";
	}

	@Override
	public QueryCostMap selectQueries(QueryCostMap costMap, double minCpuLoad,
			double minMemLoad, double minNetLoad) {
		
		double maxDifference=0;
		QueryLoadInformation queryWithMaxDifference = null;
		
		
		double currentLoad = PriceCalculator.getCurrentLoad();
		double currentMarginCost = PriceCalculator.calcCurrentMarginCost();
		for(QueryLoadInformation queryCost : costMap.getQueriesSortedByCost()) {
			
			double loadForQuery = queryCost.getCpuLoad()*100;
			double marginCostForQuery = PriceCalculator.calcMarginCost(currentLoad-loadForQuery);
			double marginCostDifference = currentMarginCost-marginCostForQuery;
			if(marginCostDifference>maxDifference) {
				maxDifference = marginCostDifference;
				queryWithMaxDifference = queryCost;
			}
			
		}
		QueryCostMap resultMap = new QueryCostMap();
		if(queryWithMaxDifference!=null) {
			resultMap.add(queryWithMaxDifference);
		}
		return resultMap;
	}

}
