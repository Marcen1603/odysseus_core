package de.uniol.inf.is.odysseus.rcp.ac.views;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;

public class OperatorStatistic {

	private final String name;
	private final String type;
	
	private final double cpuCost;
	private final double selectivity;
	private final double dataRate;
	
	public OperatorStatistic(IPhysicalOperator operator, OperatorCost<IPhysicalOperator> cost, OperatorEstimation<IPhysicalOperator> est) {
		Preconditions.checkNotNull(operator, "Operator for statistics must not be null!");
		
		this.name = operator.getName();
		this.type = operator.getClass().getSimpleName();
		
		this.cpuCost = cost.getCpuCost();
		
		this.selectivity = est.getSelectivity();
		this.dataRate = est.getDataStream().getDataRate();
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public double getCpuCost() {
		return cpuCost;
	}

	public double getSelectivity() {
		return selectivity;
	}
	
	public double getDataRate() {
		return dataRate;
	}
}
