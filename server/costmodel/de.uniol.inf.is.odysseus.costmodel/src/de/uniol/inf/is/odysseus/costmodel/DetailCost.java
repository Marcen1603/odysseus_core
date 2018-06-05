package de.uniol.inf.is.odysseus.costmodel;

import com.google.common.base.Preconditions;

public final class DetailCost {

	private final double memCost;
	private final double cpuCost;
	private final double netCost;
	private final double selectivity;
	private final double datarate;
	private final double windowSize;

	public DetailCost(double memCost, double cpuCost, double netCost, double selectivity, double datarate, double windowSize) {
		Preconditions.checkArgument(memCost >= 0, "Memory cost must be non-negative instead of %s", memCost);
		Preconditions.checkArgument(cpuCost >= 0, "Cpu cost must be non-negative instead of %s", cpuCost);
		Preconditions.checkArgument(netCost >= 0, "Network cost must be non-negative instead of %s", netCost);
		Preconditions.checkArgument(selectivity >= 0, "Selectivity must be non-negative instead of %s", selectivity);
		Preconditions.checkArgument(datarate >= 0, "Datarate must be non-negative instead of %s", datarate);
		Preconditions.checkArgument(windowSize >= 0, "Window size must be non-negative instead of %s", windowSize);

		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.netCost = netCost;
		this.selectivity = selectivity;
		this.datarate = datarate;
		this.windowSize = windowSize;
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
	
	public double getSelectivity() {
		return selectivity;
	}
	
	public double getDatarate() {
		return datarate;
	}
	
	public double getWindowSize() {
		return windowSize;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{cpu = ").append(getCpuCost()).append(", ");
		sb.append("mem = ").append(getMemCost()).append(", ");
		sb.append("net = ").append(getNetCost()).append(", ");
		sb.append("sel = ").append(getSelectivity()).append(", ");
		sb.append("rate= ").append(getDatarate()).append(", ");
		sb.append("wnd = ").append(getWindowSize()).append("}");
		return sb.toString();
	}
}
