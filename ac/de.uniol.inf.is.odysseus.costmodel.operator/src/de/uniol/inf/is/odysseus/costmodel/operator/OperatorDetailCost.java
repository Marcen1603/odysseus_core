package de.uniol.inf.is.odysseus.costmodel.operator;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public class OperatorDetailCost implements IOperatorDetailCost {

	private final IPhysicalOperator operator;
	private final double memCost;
	private final double cpuCost;
	
	public OperatorDetailCost( IPhysicalOperator operator, double memCost, double cpuCost ) {
		this.operator = operator;
		this.memCost = memCost;
		this.cpuCost = cpuCost;
	}
	
	@Override
	public IPhysicalOperator getOperator() {
		return operator;
	}

	@Override
	public double getMemoryCost() {
		return memCost;
	}

	@Override
	public double getProcessorCost() {
		return cpuCost;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ mem = ").append(memCost).append(", cpu = ").append(cpuCost).append(" }");
		return sb.toString();
	}
}
