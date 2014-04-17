package de.uniol.inf.is.odysseus.costmodel.impl;

import com.google.common.base.Preconditions;

public final class DetailCost {

	private final double memCost;
	private final double cpuCost;
	private final double netCost;

	public DetailCost(double memCost, double cpuCost, double netCost) {
		Preconditions.checkArgument(memCost >= 0, "Memory cost must be non-negative instead of %s", memCost);
		Preconditions.checkArgument(cpuCost >= 0, "Cpu cost must be non-negative instead of %s", cpuCost);
		Preconditions.checkArgument(netCost >= 0, "Network cost must be non-negative instead of %s", netCost);

		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.netCost = netCost;
	}

	public double getMemCost() {
		return memCost;
	}

	public double getCpuCost() {
		return cpuCost;
	}

	public double getNetCost() {
		return netCost;
	}
}
