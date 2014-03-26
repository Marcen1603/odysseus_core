package de.uniol.inf.is.odysseus.peer.resource.impl;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public final class ResourceUsageBytesConverter {

	private ResourceUsageBytesConverter() {
	}
	
	public static ByteBuffer toByteBuffer(IResourceUsage localUsage) {
		ByteBuffer bb = ByteBuffer.allocate(80);
		bb.putLong(localUsage.getMemFreeBytes());
		bb.putLong(localUsage.getMemMaxBytes());
		bb.putDouble(localUsage.getCpuFree());
		bb.putDouble(localUsage.getCpuMax());
		bb.putInt(localUsage.getRunningQueriesCount());
		bb.putInt(localUsage.getStoppedQueriesCount());
		bb.putDouble(localUsage.getNetBandwidthMax());
		bb.putDouble(localUsage.getNetOutputRate());
		bb.putDouble(localUsage.getNetInputRate());
		
		int[] version = localUsage.getVersion();
		bb.putInt(version[0]);
		bb.putInt(version[1]);
		bb.putInt(version[2]);
		bb.putInt(version[3]);
		
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
		double netMax = bb.getDouble();
		double netOut = bb.getDouble();
		double netIn = bb.getDouble();
		
		int[] version = new int[4];
		version[0] = bb.getInt();
		version[1] = bb.getInt();
		version[2] = bb.getInt();
		version[3] = bb.getInt();

		return new ResourceUsage(memFree, memMax, cpuFree, cpuMax, runQ, stopQ, netMax, netOut, netIn, version);
	}
}
