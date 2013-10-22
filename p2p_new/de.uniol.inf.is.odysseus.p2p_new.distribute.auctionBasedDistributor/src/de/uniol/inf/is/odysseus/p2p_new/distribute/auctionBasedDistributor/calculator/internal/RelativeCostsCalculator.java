package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.calculator.internal;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModel;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util.Helper;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class RelativeCostsCalculator {
	private final OperatorCostModel costModel;
	private final IServerExecutor executor;
	private final IPQLGenerator generator;
	
	

	public RelativeCostsCalculator(OperatorCostModel costModel,
			IServerExecutor executor, IPQLGenerator generator) {
		super();
		this.costModel = costModel;
		this.executor = executor;
		this.generator = generator;
	}
	
	public CostSummary calcBearableCostsInPercentage(String id, double cpuCosts, double memCosts, ILogicalOperator plan) {		
		return new CostSummary(id,
				calcBearableCpuCostsInPercentage(cpuCosts),
				calcBearableMemCostsInPercentage(memCosts),plan);
	}		
	
	public double calcBearableCpuCostsInPercentage(double cpuCost) {				
		double totalCpuCost = Helper.getCpuCostTotal(executor, costModel)+cpuCost;
		
		if(totalCpuCost > 1)
			return 1/totalCpuCost;
		
		return 1d;
	}	
	
	public double calcBearableMemCostsInPercentage(double memCost) {
		double freeMem = Helper.getFreeMemory();
		double remainingMem = freeMem - memCost;
		
		if(remainingMem <= 0) {
			return freeMem/memCost;
		}
		
		return 1d;
	}
}
