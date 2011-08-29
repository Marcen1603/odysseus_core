package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public class OperatorCost implements ICost {

	private Map<IPhysicalOperator, OperatorEstimation> estimations = null;
	private double memCost;
	private double cpuCost;
	
	public OperatorCost(Map<IPhysicalOperator, OperatorEstimation> estimations, double memCost, double cpuCost) {
		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.estimations = estimations;
	}
	
	OperatorCost( double memCost, double cpuCost ) {
		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.estimations = new HashMap<IPhysicalOperator, OperatorEstimation>();
	}
	
	public Map<IPhysicalOperator, OperatorEstimation> getOperatorEstimations() {
		return estimations;
	}
	
	@Override
	public int compareTo(ICost o) {
		OperatorCost cost = (OperatorCost)o;
		
		if( memCost > cost.memCost || cpuCost > cost.cpuCost ) return 1;	
		if( memCost < cost.memCost && cpuCost < cost.cpuCost ) return -1;
		
		return 0;
	}

	@Override
	public ICost merge(ICost otherCost) {
		if( otherCost == null ) 
			return new OperatorCost(memCost, cpuCost);
		
		OperatorCost cost = (OperatorCost)otherCost;
		
		return new OperatorCost(memCost + cost.memCost, cpuCost + cost.cpuCost);
	}

	@Override
	public ICost substract(ICost otherCost) {
		if( otherCost == null ) 
			return new OperatorCost(memCost, cpuCost);

		OperatorCost cost = (OperatorCost)otherCost;
		
		return new OperatorCost(memCost - cost.memCost, cpuCost - cost.cpuCost);
	}

	@Override
	public String toString() {
		return String.format("%-10.6f, %-10.6f", memCost, cpuCost);
	}

	@Override
	public Collection<IPhysicalOperator> getOperators() {
		return estimations.keySet();
	}

	@Override
	public ICost getCostOfOperator(IPhysicalOperator operator) {
		OperatorEstimation est = estimations.get(operator);
		if( est == null ) 
			return new OperatorCost(0,0);
		
		return new OperatorCost(est.getDetailCost().getMemoryCost(), est.getDetailCost().getProcessorCost());
	}
	
	public double getMemCost() {
		return memCost;
	}
	
	public double getCpuCost() {
		return cpuCost;
	}
}
