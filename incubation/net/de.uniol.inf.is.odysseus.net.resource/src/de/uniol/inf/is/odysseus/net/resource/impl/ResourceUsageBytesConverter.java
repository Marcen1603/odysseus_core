package de.uniol.inf.is.odysseus.net.resource.impl;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.net.resource.IResourceUsage;

public final class ResourceUsageBytesConverter {

	private ResourceUsageBytesConverter() {
	}
	
	public static ByteBuffer toByteBuffer(IResourceUsage resourceUsage) {
		ByteBuffer bb = ByteBuffer.allocate(92);
		bb.putLong(resourceUsage.getMemFreeBytes());
		bb.putLong(resourceUsage.getMemMaxBytes());
		bb.putDouble(resourceUsage.getCpuFree());
		bb.putDouble(resourceUsage.getCpuMax());
		bb.putInt(resourceUsage.getRunningQueriesCount());
		bb.putInt(resourceUsage.getStoppedQueriesCount());
		bb.putInt(resourceUsage.getRemoteNodeCount());
		bb.putDouble(resourceUsage.getNetBandwidthMax());
		bb.putDouble(resourceUsage.getNetOutputRate());
		bb.putDouble(resourceUsage.getNetInputRate());
		
		int[] version = resourceUsage.getVersion();
		bb.putInt(version[0]);
		bb.putInt(version[1]);
		bb.putInt(version[2]);
		bb.putInt(version[3]);
		bb.putLong(resourceUsage.getStartupTimestamp());
		
		bb.flip();
		return bb;
	}
	
	public static IResourceUsage toResourceUsage(ByteBuffer bb) {
		long memFree = bb.getLong();
		long memMax = bb.getLong();
		double cpuFree = bb.getDouble();
		double cpuMax = bb.getDouble();
		int runQ = bb.getInt();
		int stopQ = bb.getInt();
		int remoteNodeCount = bb.getInt();
		double netMax = bb.getDouble();
		double netOut = bb.getDouble();
		double netIn = bb.getDouble();
		
		int[] version = new int[4];
		version[0] = bb.getInt();
		version[1] = bb.getInt();
		version[2] = bb.getInt();
		version[3] = bb.getInt();
		
		long startupTimestamp = bb.getLong();

		// TODO: transport timestamp over network
		return new ResourceUsage(memFree, memMax, cpuFree, cpuMax, runQ, stopQ, remoteNodeCount, netMax, netOut, netIn, version, startupTimestamp, System.currentTimeMillis());
	}
}
