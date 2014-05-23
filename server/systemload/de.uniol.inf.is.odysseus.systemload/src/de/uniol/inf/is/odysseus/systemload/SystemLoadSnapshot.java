package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;

public final class SystemLoadSnapshot implements Serializable {

	private static final long serialVersionUID = 3828970167344397179L;
	
	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final SigarWrapper SIGAR_WRAPPER = new SigarWrapper();
	
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
		
		netLoad = (netMax - netInLoad - netOutLoad ) / netMax;
	}

	public static Runtime getRuntime() {
		return RUNTIME;
	}

	public static SigarWrapper getSigarWrapper() {
		return SIGAR_WRAPPER;
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
}
	
