package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.calculator.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModel;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;

public class BidCalculator {
	
	private static final Logger log = LoggerFactory.getLogger(BidCalculator.class);
	
	private IServerExecutor executor;
	@SuppressWarnings("rawtypes")
	private OperatorCostModel costModel;

	
	@SuppressWarnings("rawtypes")
	public BidCalculator(IServerExecutor executor, OperatorCostModel costModel) {
		this.executor = executor;
		this.costModel = costModel;
	}
	
	public double calcBid(String pqlStatement, String transCfgName) {
		return calcBid(pqlStatement, transCfgName, true);
	}
	
	@SuppressWarnings("unchecked")
	public double calcBid(String pqlStatement, String transCfgName, boolean respectSourceAvailability) {
		ILogicalQuery query = Helper.getLogicalQuery(executor, pqlStatement).get(0);
		
		IPhysicalQuery q = Helper.getPhysicalQuery(executor, query, transCfgName);
		ICost<IPhysicalOperator> cost = costModel.estimateCost(q.getPhysicalChilds(), respectSourceAvailability);
		if(cost instanceof OperatorCost) {
			OperatorCost<IPhysicalOperator>  c = ((OperatorCost<IPhysicalOperator> )cost);
			return calcBid(query.getLogicalPlan(), c.getCpuCost(), c.getMemCost(), respectSourceAvailability);
		}
		
		throw new RuntimeException("Did not expect this implementation of ICost: "+ cost.getClass().getName());
	}	
	


	public double calcBid(ILogicalOperator query, double cpuCosts, double memCosts, boolean respectSourceAvailability) {		
		if(respectSourceAvailability && !Helper.allSourcesAvailable(query)) {
			log.debug("Not all sources are available. Bid = 0 then.");
			return 0;
		}
		
		OperatorCost<?> maximumCost = (OperatorCost<?>) costModel.getMaximumCost();		
		log.debug("Maximum costs     : {}", maximumCost);
		
		OperatorCost<?> overallCost = (OperatorCost<?>) costModel.getOverallCost();
		log.debug("Current costs     : {}", overallCost);
		
		double remainingCpu = maximumCost.getCpuCost() - overallCost.getCpuCost();
		double remainingMem = maximumCost.getMemCost() - overallCost.getMemCost();
		log.debug("Cost for query    : {}", formatCosts(memCosts, cpuCosts));
		log.debug("Remaining costs   : {}", formatCosts(remainingMem, remainingCpu));
		if( memCosts > remainingMem || cpuCosts > remainingCpu ) {
			log.debug("Costs are too high. Bid = 0 then.");
			return 0;
		}
		
		double remainingMemPerc = ( remainingMem - memCosts ) / maximumCost.getMemCost();
		double remainingCpuPerc = ( remainingCpu - cpuCosts ) / maximumCost.getCpuCost();
		log.debug("Remaining costs(%): MEM at {} %, CPU at {} %", remainingMemPerc * 100, remainingCpuPerc * 100);
		
		double bid = 0.5 * remainingMemPerc + 0.5 * remainingCpuPerc;
		log.debug("Resulting bid = {}", bid);
		
		return bid;
	}
	
	private static String formatCosts( double memCost, double cpuCost ) {
		return String.format("m = %-10.6f MB ; c = %-10.6f", memCost / 1024 / 1024, cpuCost);
	}
}
