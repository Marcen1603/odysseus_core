package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.systemload.impl.BufferedSigarWrapper;

public final class SystemLoadSnapshot implements Serializable, Cloneable {

	private static final long serialVersionUID = 3828970167344397179L;
	
	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final BufferedSigarWrapper SIGAR_WRAPPER = new BufferedSigarWrapper();
	
	private final long timestamp;
	
	private final double cpuLoad;
	private final double memLoad;
	private final double netLoad;
	
	public SystemLoadSnapshot() {
		timestamp = System.nanoTime();
		
		double cpuMax = SIGAR_WRAPPER.getCpuMax();
		cpuLoad = ( cpuMax - SIGAR_WRAPPER.getCpuFree() ) / cpuMax;
		
		double memMax = RUNTIME.totalMemory();
		memLoad = ( memMax - RUNTIME.freeMemory() ) / memMax;
		
		double netMax = SIGAR_WRAPPER.getNetMax();
		double netInLoad = SIGAR_WRAPPER.getNetInputRate();
		double netOutLoad = SIGAR_WRAPPER.getNetOutputRate();
		
		netLoad = (netInLoad + netOutLoad ) / netMax;
	}

	public SystemLoadSnapshot(SystemLoadSnapshot copy) {
		timestamp = copy.timestamp;
		cpuLoad = copy.cpuLoad;
		memLoad = copy.memLoad;
		netLoad = copy.netLoad;
	}

	public static Runtime getRuntime() {
		return RUNTIME;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public double getCpuLoad() {
		return cpuLoad;
	}

	public double getMemLoad() {
		return memLoad;
	}

	public double getNetLoad() {
		return netLoad;
	}
	
	@Override
	public SystemLoadSnapshot clone() {
		return new SystemLoadSnapshot(this);
	}
}
	
