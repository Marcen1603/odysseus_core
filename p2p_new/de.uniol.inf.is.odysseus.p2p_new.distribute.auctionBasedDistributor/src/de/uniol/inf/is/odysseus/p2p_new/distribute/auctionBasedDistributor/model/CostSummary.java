package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model;

import java.util.Collection;

import com.google.common.base.Objects;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class CostSummary {
	private final String operatorId;
	private final double cpuCost;
	private final double memCost;
	private final transient ILogicalOperator plan;
	
	public CostSummary(String operatorId, double cpuCost,
			double memCost, ILogicalOperator plan) {
		this.operatorId = operatorId;
		this.cpuCost = cpuCost;
		this.memCost = memCost;
		this.plan = plan;
	}
	
	public String getOperatorId() {
		return operatorId;
	}
	public double getCpuCost() {
		return cpuCost;
	}
	public double getMemCost() {
		return memCost;
	}
	
	public ILogicalOperator getPlan() {
		return plan;
	}

	public String toString() {
	      return Objects.toStringHelper(this)
	                .addValue(this.operatorId)
	                .addValue(this.cpuCost)
	                .addValue(this.memCost)
	                .toString();
	}
	
	public static CostSummary calcAvg(Collection<CostSummary> costs) {
		CostSummary sum = calcSum(costs);
		if(costs.size()>0)
			return new CostSummary(sum.getOperatorId(), sum.getCpuCost()/costs.size(), sum.getMemCost()/costs.size(), sum.getPlan());
		else
			return new CostSummary(sum.getOperatorId(), 0, 0, sum.getPlan());
	}
	
	public static CostSummary calcSum(Collection<CostSummary> costs) {
		double cpuCosts =  0;
		double memCosts =  0;
		ILogicalOperator operator = null;
		for(CostSummary cost : costs) {
			cpuCosts += cost.getCpuCost()*100; // um fehler bei floatingpoints zu vermeiden (ziemlich dumm diese loesung)
			memCosts += cost.getMemCost();
			operator = cost.getPlan();
		}
		return new CostSummary(null, cpuCosts/100, memCosts, operator);
	}	
}
