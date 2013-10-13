package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import net.jxta.peer.PeerID;

public class ResourceUsage {
	// These factors determine the influence of cpu- and memory-usage on the overall usage-score
	private final double MEM_USAGE_WEIGHT = 1;
	private final double CPU_USAGE_WEIGHT = 2;
	private final double MEM_USAGE_THRESHOLD = 0.9;
	private final double CPU_USAGE_THRESHOLD = 0.9;
	private final double COMBINED_THRESHOLD = 0.9;

	private double cpu_usage;
	private double mem_free;
	private double mem_used;
	private double mem_total;
	private long timestamp;
	private PeerID peerID;
	private double networkUsage;

	/**
	 * calculates the overall usage-percentage of the peer based on the weighted memory- and cpu-usages
	 */
	public double getOverallUsage() {
		double mem_percUsage = mem_used/mem_total;
		return (CPU_USAGE_WEIGHT * cpu_usage + MEM_USAGE_WEIGHT * mem_percUsage) / (MEM_USAGE_WEIGHT + CPU_USAGE_WEIGHT);
	}
	
	public double getCpu_usage() {
		return cpu_usage;
	}
	public void setCpu_usage(double cpu_usage) {
		this.cpu_usage = cpu_usage;
	}
	public double getMem_free() {
		return mem_free;
	}
	public void setMem_free(double mem_free) {
		this.mem_free = mem_free;
	}
	public double getMem_used() {
		return mem_used;
	}
	public void setMem_used(double mem_used) {
		this.mem_used = mem_used;
	}
	public double getMem_total() {
		return mem_total;
	}
	public void setMem_total(double mem_total) {
		this.mem_total = mem_total;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public PeerID getPeerID() {
		return peerID;
	}
	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}
	
	public double getCOMBINED_THRESHOLD() {
		return COMBINED_THRESHOLD;
	}

	/**
	 * Returns true, if either the cpu- or the memory-usage on the peer surpasses the specified thresholds
	 */
	public boolean isOverLoaded() {
		return mem_free/mem_total > MEM_USAGE_THRESHOLD || cpu_usage > CPU_USAGE_THRESHOLD;
	}

	public void setNetworkUsage(double networkUsage) {
		this.networkUsage = networkUsage;		
	}

	public double getNetworkUsage() {
		return networkUsage;
	}
}
