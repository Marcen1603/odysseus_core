package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardexecutioncostmodel;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICost;

/**
 * Execution cost of a physical plan, partial plan or single operator.
 * 
 * @author Tobias Witt
 *
 */
public class StandardPlanExecutionCost implements ICost<IPhysicalOperator> {
	
	private float memoryConsumption;	// in bytes / 100 tuples
	private float cpuTime;			// in ms / 100 tuples
	private float latency;			// in ms from source to sink
	private float networkBandwidth;	// in bytes / 100 tuples
	private int score;
	
	public StandardPlanExecutionCost(float memoryConsumption, float cpuTime, float latency, float networkBandwidth) {
		this.memoryConsumption = memoryConsumption;
		this.cpuTime = cpuTime;
		this.latency = latency;
		this.networkBandwidth = networkBandwidth;
		this.score = 0;
	}
	
	StandardPlanExecutionCost(StandardPlanExecutionCost cost) {
		this.cpuTime = cost.getCpuTime();
		this.latency = cost.getLatency();
		this.memoryConsumption = cost.getMemoryConsumption();
		this.networkBandwidth = cost.getNetworkBandwidth();
		this.score = cost.getScore();
	}

	public float getMemoryConsumption() {
		return memoryConsumption;
	}

	public void setMemoryConsumption(float memoryConsumption) {
		this.memoryConsumption = memoryConsumption;
	}

	public float getCpuTime() {
		return cpuTime;
	}

	public void setCpuTime(float cpuTime) {
		this.cpuTime = cpuTime;
	}

	public float getLatency() {
		return latency;
	}

	public void setLatency(float latency) {
		this.latency = latency;
	}

	public float getNetworkBandwidth() {
		return networkBandwidth;
	}

	public void setNetworkBandwidth(float networkBandwidth) {
		this.networkBandwidth = networkBandwidth;
	}

	@Override
	public int compareTo(ICost<IPhysicalOperator> o) {
		return o.getScore() - this.score;
	}
	
	/**
	 * 
	 * @param rate input datarate
	 */
	public void scaleWithDatarate(float rate) {
		rate *= 1000;
		this.cpuTime *= rate;
		this.latency *= rate;
		this.memoryConsumption *= rate;
		this.networkBandwidth *= rate;
	}

	@Override
	public int getScore() {
		return this.score;
	}

	@Override
	public void setScore(int score) {
		this.score = score;
	}
	
	public void add(StandardPlanExecutionCost cost) {
		this.cpuTime += cost.getCpuTime();
		this.latency += cost.getLatency();
		this.memoryConsumption += cost.getMemoryConsumption();
		this.networkBandwidth += cost.getNetworkBandwidth();
	}
	
}
