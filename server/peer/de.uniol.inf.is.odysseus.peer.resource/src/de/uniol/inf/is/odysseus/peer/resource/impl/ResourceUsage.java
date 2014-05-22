package de.uniol.inf.is.odysseus.peer.resource.impl;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public final class ResourceUsage implements IResourceUsage {

	private final long memFreeBytes;
	private final long memMaxBytes;

	private final double cpuFree;
	private final double cpuMax;
	
	private final int runningQueriesCount;
	private final int stoppedQueriesCount;
	private final int remotePeerCount;
	
	private final double netBandwidthMax;
	private final double netOutputRate;
	private final double netInputRate;
	
	private final int[] version;

	ResourceUsage(long memFreeBytes, long memMaxBytes, double cpuFree, double cpuMax, int runningQueriesCount, int stoppedQueriesCount, int remotePeerCount,
			double netBandwidthMax, double netOutputRate, double netInputRate, int[] version ) {
		
		Preconditions.checkArgument(memFreeBytes >= 0, "Memory free bytes cannot be negative: %s", memFreeBytes);
		Preconditions.checkArgument(memMaxBytes >= 0, "Memory max bytes cannot be negative: %s", memMaxBytes);
		Preconditions.checkArgument(cpuFree >= 0, "Cpu free cannot be negative: %s", cpuFree);
		Preconditions.checkArgument(cpuMax >= 0, "Cpu max cannot be negative: %s", cpuMax);
		
		Preconditions.checkArgument(memFreeBytes <= memMaxBytes, "Memory free bytes cannot be higher than maximum bytes: %s > %s", memFreeBytes, memMaxBytes);
		Preconditions.checkArgument(cpuFree <= cpuMax, "Cpu free cannot be higher than cpu max: %s > %s", cpuFree, cpuMax);
		
		Preconditions.checkArgument(netBandwidthMax >= 0, "Network maximum bandwidth must be zero or positive");
		Preconditions.checkArgument(netOutputRate >= 0, "Network maximum bandwidth must be zero or positive");
		Preconditions.checkArgument(netInputRate >= 0, "Network maximum bandwidth must be zero or positive");
		
		Preconditions.checkArgument(remotePeerCount >= 0, "Count of remote peers must be non-negative!");
		
		Preconditions.checkNotNull(version, "Version must not be null!");

		this.memFreeBytes = memFreeBytes;
		this.memMaxBytes = memMaxBytes;
		this.cpuFree = cpuFree;
		this.cpuMax = cpuMax;
		
		this.stoppedQueriesCount = stoppedQueriesCount;
		this.runningQueriesCount = runningQueriesCount;
		this.remotePeerCount = remotePeerCount;
		
		this.netBandwidthMax = netBandwidthMax;
		this.netOutputRate = netOutputRate;
		this.netInputRate = netInputRate;
		
		this.version = version;
	}
	
	private ResourceUsage( ResourceUsage copy ) {
		Preconditions.checkNotNull(copy, "ResourceUsage to copy must not be null!");
		
		memFreeBytes = copy.memFreeBytes;
		memMaxBytes = copy.memMaxBytes;
		cpuFree = copy.cpuFree;
		cpuMax = copy.cpuMax;
		stoppedQueriesCount = copy.stoppedQueriesCount;
		runningQueriesCount = copy.runningQueriesCount;
		
		netBandwidthMax = copy.netBandwidthMax;
		netInputRate = copy.netInputRate;
		netOutputRate = copy.netOutputRate;
		version = copy.version;
		remotePeerCount = copy.remotePeerCount;
	}
	
	@Override
	public IResourceUsage clone() {
		return new ResourceUsage(this);
	}

	@Override
	public double getCpuFree() {
		return cpuFree;
	}

	@Override
	public double getCpuMax() {
		return cpuMax;
	}

	@Override
	public long getMemFreeBytes() {
		return memFreeBytes;
	}

	@Override
	public long getMemMaxBytes() {
		return memMaxBytes;
	}
	
	@Override
	public int getRunningQueriesCount() {
		return runningQueriesCount;
	}
	
	@Override
	public int getStoppedQueriesCount() {
		return stoppedQueriesCount;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{Free MEM = ").append(getMemFreeBytes()).append(" / ").append(getMemMaxBytes()).append(", Free Cpu = ").append(getCpuFree()).append(" / ").append(getCpuMax());
		sb.append("Network in = ").append(getNetInputRate()).append(", out = ").append(getNetOutputRate()).append(" of ").append(getNetBandwidthMax()).append(", ");
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public double getNetBandwidthMax() {
		return netBandwidthMax;
	}
	
	@Override
	public double getNetInputRate() {
		return netInputRate;
	}
	
	@Override
	public double getNetOutputRate() {
		return netOutputRate;
	}
	
	@Override
	public int[] getVersion() {
		return version;
	}
	
	@Override
	public int getRemotePeerCount() {
		return remotePeerCount;
	}
}
