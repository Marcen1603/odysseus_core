package de.uniol.inf.is.odysseus.peer.resource;


public interface IResourceUsage {

	public double getCpuFree();
	public double getCpuMax();
	public long getMemFreeBytes();
	public long getMemMaxBytes();
	
	public int getRunningQueriesCount();
	public int getStoppedQueriesCount();
	public int getRemotePeerCount();

	public double getNetBandwidthMax();
	public double getNetInputRate();
	public double getNetOutputRate();
	
	public int[] getVersion();
	public long getStartupTimestamp();
	public long getTimestamp();
	
	public IResourceUsage clone();
}