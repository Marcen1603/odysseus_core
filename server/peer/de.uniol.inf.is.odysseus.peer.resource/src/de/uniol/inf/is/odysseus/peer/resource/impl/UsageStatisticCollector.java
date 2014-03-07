package de.uniol.inf.is.odysseus.peer.resource.impl;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class UsageStatisticCollector {

	private static final int MAX_STAT_COUNT = 20;
	
	private final NumberAverager memFree = new NumberAverager(MAX_STAT_COUNT);
	private final NumberAverager cpuFree = new NumberAverager(MAX_STAT_COUNT);
	private final NumberAverager netOutputRate = new NumberAverager(MAX_STAT_COUNT);
	private final NumberAverager netInputRate = new NumberAverager(MAX_STAT_COUNT);

	private long memMaxBytes;
	private double cpuMax;
	private int runningQueriesCount;
	private int stoppedQueriesCount;
	private double netBandwidthMax;
	
	public synchronized void addStatistics(long memFreeBytes, long memMaxBytes, double cpuFree, double cpuMax, int runningQueriesCount, int stoppedQueriesCount, double netBandwidthMax, double netOutputRate, double netInputRate) {
		Preconditions.checkArgument(memFreeBytes >= 0, "Memory free bytes cannot be negative: %s", memFreeBytes);
		Preconditions.checkArgument(memMaxBytes >= 0, "Memory max bytes cannot be negative: %s", memMaxBytes);
		Preconditions.checkArgument(cpuFree >= 0, "Cpu free cannot be negative: %s", cpuFree);
		Preconditions.checkArgument(cpuMax >= 0, "Cpu max cannot be negative: %s", cpuMax);

		Preconditions.checkArgument(memFreeBytes <= memMaxBytes, "Memory free bytes cannot be higher than maximum bytes: %s > %s", memFreeBytes, memMaxBytes);
		Preconditions.checkArgument(cpuFree <= cpuMax, "Cpu free cannot be higher than cpu max: %s > %s", cpuFree, cpuMax);

		Preconditions.checkArgument(netBandwidthMax >= 0, "Network maximum bandwidth must be zero or positive");
		Preconditions.checkArgument(netOutputRate >= 0, "Network maximum bandwidth must be zero or positive");
		Preconditions.checkArgument(netInputRate >= 0, "Network maximum bandwidth must be zero or positive");

		this.memFree.addValue(memFreeBytes);
		this.cpuFree.addValue(cpuFree);
		this.netInputRate.addValue(netInputRate);
		this.netOutputRate.addValue(netOutputRate);
		
		this.memMaxBytes = memMaxBytes;
		this.cpuMax = cpuMax;
		this.stoppedQueriesCount = stoppedQueriesCount;
		this.runningQueriesCount = runningQueriesCount;
		this.netBandwidthMax = netBandwidthMax;
	}
	
	public synchronized IResourceUsage getCurrentResourceUsage() {
		return new ResourceUsage((long) memFree.getAverage(), memMaxBytes, cpuFree.getAverage(), cpuMax, runningQueriesCount, stoppedQueriesCount, netBandwidthMax, netOutputRate.getAverage(), netInputRate.getAverage());
	}
}
