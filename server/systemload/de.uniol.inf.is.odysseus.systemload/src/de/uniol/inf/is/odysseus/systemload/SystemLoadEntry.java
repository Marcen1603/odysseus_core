package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.systemload.impl.BufferedSigarWrapper;

public final class SystemLoadEntry implements Serializable, Cloneable {

	private static final long serialVersionUID = 7962567634409661546L;
	private static final Runtime RUNTIME = Runtime.getRuntime();
	
	public static final BufferedSigarWrapper SIGAR_WRAPPER = new BufferedSigarWrapper();
	
	private final String name;

	private final double cpuLoad;
	private final double memLoad;
	private final double netLoad;
	
	public SystemLoadEntry(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name for systemload entry must not be empty or null!");
		
		this.name = name;
		
		double cpuMax = SIGAR_WRAPPER.getCpuMax();
		cpuLoad = ( cpuMax - SIGAR_WRAPPER.getCpuFree() ) / cpuMax;
		
		double memMax = RUNTIME.totalMemory();
		memLoad = ( memMax - RUNTIME.freeMemory() ) / memMax;
		
		double netMax = SIGAR_WRAPPER.getNetMax();
		double netInLoad = SIGAR_WRAPPER.getNetInputRate();
		double netOutLoad = SIGAR_WRAPPER.getNetOutputRate();
		
		netLoad = (netInLoad + netOutLoad ) / netMax;
	}
	
	public SystemLoadEntry(SystemLoadEntry copy) {
		this.name = copy.name;
		
		this.cpuLoad = copy.cpuLoad;
		this.memLoad = copy.memLoad;
		this.netLoad = copy.netLoad;
	}

	public String getName() {
		return name;
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
	public SystemLoadEntry clone() {
		return new SystemLoadEntry(this);
	}

}
