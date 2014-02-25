package de.uniol.inf.is.odysseus.peer.resource;


public interface IResourceUsage {

	public double getCpuFree();
	public double getCpuMax();
	public long getMemFreeBytes();
	public long getMemMaxBytes();
	
	public int getRunningQueriesCount();
	public int getStoppedQueriesCount();

	double getNetBandwidthMax();
	double getNetInputRate();
	double getNetOutputRate();
	
	public IResourceUsage clone();
}