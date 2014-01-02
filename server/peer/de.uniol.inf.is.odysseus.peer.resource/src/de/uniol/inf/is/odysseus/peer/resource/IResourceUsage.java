package de.uniol.inf.is.odysseus.peer.resource;

import net.jxta.peer.PeerID;

import com.google.common.collect.ImmutableMap;

public interface IResourceUsage {

	public PeerID getPeerID();
	public long getTimestamp();
	
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
	
	public ImmutableMap<PeerID, Long> getPingMap();
}