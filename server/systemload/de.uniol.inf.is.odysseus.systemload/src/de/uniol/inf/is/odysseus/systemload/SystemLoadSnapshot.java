package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;

public final class SystemLoadSnapshot implements Serializable {

	private static final long serialVersionUID = 3828970167344397179L;
	
	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final SigarWrapper SIGAR_WRAPPER = new SigarWrapper();
	
	private final long timestamp;
	
	private final double cpuLoad;
	private final double cpuMax;
	
	private final double memLoad;
	private final double memMax;
	
	private final double netInLoad;
	private final double netOutLoad;
	private final double netMax;
	
	public SystemLoadSnapshot() {
		timestamp = System.nanoTime();
		
		cpuMax = SIGAR_WRAPPER.getCpuMax();
		cpuLoad = cpuMax - SIGAR_WRAPPER.getCpuFree();
		
		memMax = RUNTIME.totalMemory();
		memLoad = RUNTIME.totalMemory() - RUNTIME.freeMemory();
		
		netMax = SIGAR_WRAPPER.getNetMax();
		netInLoad = SIGAR_WRAPPER.getNetInputRate();
		netOutLoad = SIGAR_WRAPPER.getNetOutputRate();
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

	public double getCpuMax() {
		return cpuMax;
	}

	public double getMemLoad() {
		return memLoad;
	}

	public double getMemMax() {
		return memMax;
	}

	public double getNetInLoad() {
		return netInLoad;
	}

	public double getNetOutLoad() {
		return netOutLoad;
	}

	public double getNetMax() {
		return netMax;
	}
	
	
}
