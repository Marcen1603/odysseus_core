package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.calculator.internal;

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
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.Helper;

public class BidCalculator {
	private static final Logger log = LoggerFactory.getLogger(BidCalculator.class);
	private IServerExecutor executor;
	private OperatorCostModel costModel;

	
	public BidCalculator(IServerExecutor executor, OperatorCostModel costModel) {
		this.executor = executor;
		this.costModel = costModel;
	}
	
	public double calcBid(String pqlStatement, String transCfgName) {
		ILogicalQuery query = Helper.getLogicalQuery(executor, pqlStatement).get(0);
		
		IPhysicalQuery q = Helper.getPhysicalQuery(executor, query, transCfgName);
		ICost<IPhysicalOperator> cost = costModel.estimateCost(q.getPhysicalChilds(), false);
		if(cost instanceof OperatorCost) {
			OperatorCost<IPhysicalOperator>  c = ((OperatorCost<IPhysicalOperator> )cost);
			return calcBid(query.getLogicalPlan(), c.getCpuCost(), c.getMemCost());
		}
		else {
			throw new RuntimeException("Did not expect this implementation of ICost: "+ cost.getClass().getName());
		}
	}
	


	public double calcBid(ILogicalOperator query, double cpuCosts, double memCosts) {		
		if(!Helper.allSourcesAvailable(query))
			return 0;
//		log.debug("CPU cost for query {}: {}", cpuCosts);
//		log.debug("Mem cost for query {}: {}", memCosts);
//		
//		log.debug("Free Memory: {}", Helper.getFreeMemory());
//		log.debug("Max Memory: {}", Helper.getMaxMemory());
		double remainingMem = Math.max(0,( Helper.getFreeMemory()-memCosts ) /
				Helper.getMaxMemory() );
//		log.debug("Remaining memory: {}", remainingMem);
		
		double remainingCpu = 1 - Math.min((Helper.getCpuCostTotal(executor, costModel)+cpuCosts),1);
//		log.debug("Remaining CPU performance: {}", remainingCpu);
		
		if(remainingMem > 0 && remainingCpu > 0) {
			return 0.5 * remainingMem + 0.5 * remainingCpu;
		}
		else {
			return 0;
		}
	}
}
