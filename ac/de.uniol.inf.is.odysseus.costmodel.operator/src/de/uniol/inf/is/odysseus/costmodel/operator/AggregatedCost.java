package de.uniol.inf.is.odysseus.costmodel.operator;

public class AggregatedCost {

	private double cpuCost;
	private double memCost;
	
	public AggregatedCost( double cpuCost, double memCost ) {
		this.cpuCost = cpuCost;
		this.memCost = memCost;
	}

	public double getCpuCost() {
		return cpuCost;
	}

	public double getMemCost() {
		return memCost;
	}
	
	@Override
	public String toString() {
		return String.format("{ %-10.6f, %-10.6f}", memCost, cpuCost);
	}
}
