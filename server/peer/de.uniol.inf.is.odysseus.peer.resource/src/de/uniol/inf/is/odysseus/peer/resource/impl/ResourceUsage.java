package de.uniol.inf.is.odysseus.peer.resource.impl;

import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public final class ResourceUsage implements IResourceUsage {

	private static final double SIMILARITY_FACTOR_PERCENT = 9;

	private final PeerID peerID;

	private final long memFreeBytes;
	private final long memMaxBytes;

	private final double cpuFree;
	private final double cpuMax;
	
	private final int runningQueriesCount;
	private final int stoppedQueriesCount;
	
	private final double netBandwidthMax;
	private final double netOutputRate;
	private final double netInputRate;

	private final long timestamp;

	ResourceUsage(PeerID peerID, long memFreeBytes, long memMaxBytes, double cpuFree, double cpuMax, long timestamp, int runningQueriesCount, int stoppedQueriesCount, 
			double netBandwidthMax, double netOutputRate, double netInputRate) {
		Preconditions.checkNotNull(peerID, "PeerID must not be null!");
		
		Preconditions.checkArgument(memFreeBytes >= 0, "Memory free bytes cannot be negative: %s", memFreeBytes);
		Preconditions.checkArgument(memMaxBytes >= 0, "Memory max bytes cannot be negative: %s", memMaxBytes);
		Preconditions.checkArgument(cpuFree >= 0, "Cpu free cannot be negative: %s", cpuFree);
		Preconditions.checkArgument(cpuMax >= 0, "Cpu max cannot be negative: %s", cpuMax);
		Preconditions.checkArgument(timestamp >= 0, "Timestamp for resource usage must be positive: %s", timestamp);
		
		Preconditions.checkArgument(memFreeBytes <= memMaxBytes, "Memory free bytes cannot be higher than maximum bytes: %s > %s", memFreeBytes, memMaxBytes);
		Preconditions.checkArgument(cpuFree <= cpuMax, "Cpu free cannot be higher than cpu max: %s > %s", cpuFree, cpuMax);
		
		Preconditions.checkArgument(netBandwidthMax >= 0, "Network maximum bandwidth must be zero or positive");
		Preconditions.checkArgument(netOutputRate >= 0, "Network maximum bandwidth must be zero or positive");
		Preconditions.checkArgument(netInputRate >= 0, "Network maximum bandwidth must be zero or positive");
		

		this.peerID = peerID;
		this.memFreeBytes = memFreeBytes;
		this.memMaxBytes = memMaxBytes;
		this.cpuFree = cpuFree;
		this.cpuMax = cpuMax;
		
		this.stoppedQueriesCount = stoppedQueriesCount;
		this.runningQueriesCount = runningQueriesCount;
		
		this.netBandwidthMax = netBandwidthMax;
		this.netOutputRate = netOutputRate;
		this.netInputRate = netInputRate;

		this.timestamp = timestamp;
	}
	
	private ResourceUsage( ResourceUsage copy ) {
		Preconditions.checkNotNull(copy, "ResourceUsage to copy must not be null!");
		
		peerID = copy.peerID;
		memFreeBytes = copy.memFreeBytes;
		memMaxBytes = copy.memMaxBytes;
		cpuFree = copy.cpuFree;
		cpuMax = copy.cpuMax;
		stoppedQueriesCount = copy.stoppedQueriesCount;
		runningQueriesCount = copy.runningQueriesCount;
		
		netBandwidthMax = copy.netBandwidthMax;
		netInputRate = copy.netInputRate;
		netOutputRate = copy.netOutputRate;
		
		timestamp = copy.timestamp;
	}
		
	static boolean areSimilar(IResourceUsage one, IResourceUsage other) {
		Preconditions.checkNotNull(one, "First resource usage to check similarity must not be null!");
		Preconditions.checkNotNull(other, "Second resource usage to check similarity must not be null!");
		Preconditions.checkArgument(one.getPeerID().equals(other.getPeerID()), "resource usages to check similarity must be from the same peer");
		
		// shortcut to avoid calculations below if possible
		if( one.getRunningQueriesCount() != other.getRunningQueriesCount() || one.getStoppedQueriesCount() != other.getStoppedQueriesCount() ) {
			return false;
		}

		double memFreeDiffPercent = determineDiffPercent(one.getMemFreeBytes(), other.getMemFreeBytes());
		double memMaxDiffPercent = determineDiffPercent(one.getMemMaxBytes(), other.getMemMaxBytes());
		double cpuFreeDiffPercent = determineDiffPercent(one.getCpuFree(), other.getCpuFree());
		double cpuMaxDiffPercent = determineDiffPercent(one.getCpuMax(), other.getCpuMax());
		double netBandwidthMaxPercent = determineDiffPercent(one.getNetBandwidthMax(), other.getNetBandwidthMax());
		double netOutputRatePercent = determineDiffPercent(one.getNetInputRate(), other.getNetInputRate());
		double netInputRatePercent = determineDiffPercent(one.getNetOutputRate(), other.getNetOutputRate());
		
		return areAllValuesBelowThan(SIMILARITY_FACTOR_PERCENT, memFreeDiffPercent, memMaxDiffPercent, cpuFreeDiffPercent, cpuMaxDiffPercent, netBandwidthMaxPercent, netOutputRatePercent, netInputRatePercent);
	}

	private static double determineDiffPercent(double a, double b) {
		double dist = Math.abs(a - b);
		double factor = dist / a;

		return factor * 100.0;
	}

	private static boolean areAllValuesBelowThan(double max, double... values) {
		for( double value : values ) {
			if( value > max ) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public IResourceUsage clone() {
		return new ResourceUsage(this);
	}

	@Override
	public PeerID getPeerID() {
		return peerID;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
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
		sb.append("Network in = ").append(getNetInputRate()).append(", out = ").append(getNetOutputRate()).append(" of ").append(getNetBandwidthMax()).append("}");
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
}
