package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

/**
 * 
 * @author Tobias Witt
 *
 */
public class PlanExecutionCost implements Comparable<PlanExecutionCost> {
	
	private int memoryConsumption;	// in bytes / 100 tuples
	private int cpuTime;			// in ms / 100 tuples
	private int latency;			// in ms from source to sink
	private int networkBandwidth;	// in bytes / 100 tuples
	private int score;
	
	public PlanExecutionCost(int memoryConsumption, int cpuTime, int latency, int networkBandwidth) {
		this.memoryConsumption = memoryConsumption;
		this.cpuTime = cpuTime;
		this.latency = latency;
		this.networkBandwidth = networkBandwidth;
		this.score = 0;
	}
	
	public int getMemoryConsumption() {
		return memoryConsumption;
	}

	public void setMemoryConsumption(int memoryConsumption) {
		this.memoryConsumption = memoryConsumption;
	}

	public int getCpuTime() {
		return cpuTime;
	}

	public void setCpuTime(int cpuTime) {
		this.cpuTime = cpuTime;
	}

	public int getLatency() {
		return latency;
	}

	public void setLatency(int latency) {
		this.latency = latency;
	}

	public int getNetworkBandwidth() {
		return networkBandwidth;
	}

	public void setNetworkBandwidth(int networkBandwidth) {
		this.networkBandwidth = networkBandwidth;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int compareTo(PlanExecutionCost o) {
		return o.getScore() - this.score;
	}

}
