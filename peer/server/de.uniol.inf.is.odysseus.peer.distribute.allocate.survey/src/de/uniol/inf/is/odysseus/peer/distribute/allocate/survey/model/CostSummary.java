package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public final class CostSummary {

	private final double cpuCost;
	private final double memCost;
	private final transient ILogicalOperator plan;

	public CostSummary(double cpuCost, double memCost, ILogicalOperator plan) {
		Preconditions.checkArgument(cpuCost >= 0.0, "Cpu cost of cost summary must not be zero or positive!");
		Preconditions.checkArgument(memCost >= 0.0, "Mem cost of cost summary must be zero or positive!");
		Preconditions.checkNotNull(plan, "Plan must not be null!");
		
		this.cpuCost = cpuCost;
		this.memCost = memCost;
		this.plan = plan;
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

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("CpuCost", this.cpuCost)
				.add("MemCost", this.memCost)
				.toString();
	}
}
