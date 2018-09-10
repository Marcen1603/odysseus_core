package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class SystemLoadEntry implements Serializable, Cloneable {

	private static final long serialVersionUID = 7962567634409661546L;
	private static final Runtime RUNTIME = Runtime.getRuntime();
		
	private final String name;

	private final double cpuLoad;
	private final double memLoad;
	private final double netLoad;
	
	public SystemLoadEntry(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name for systemload entry must not be empty or null!");
		
		this.name = name;
		
		double cpuMax = SystemLoadPlugIn.SIGAR_WRAPPER.getCpuMax();
		cpuLoad = ( cpuMax - SystemLoadPlugIn.SIGAR_WRAPPER.getCpuFree() ) / cpuMax;
		
		double memMax = RUNTIME.totalMemory();
		memLoad = ( memMax - RUNTIME.freeMemory() ) / memMax;
		
		double netMax = SystemLoadPlugIn.SIGAR_WRAPPER.getNetMax();
		double netInLoad = SystemLoadPlugIn.SIGAR_WRAPPER.getNetInputRate();
		double netOutLoad = SystemLoadPlugIn.SIGAR_WRAPPER.getNetOutputRate();
		
		netLoad = (netInLoad + netOutLoad ) / netMax;
	}

	/**
	 * Constructor only needed for meta data transport
	 * @param name
	 * @param cpuLoad
	 * @param memLoad
	 * @param netLoad
	 */
	public SystemLoadEntry(String name, double cpuLoad, double memLoad, double netLoad) {
		this.name = name;
		this.cpuLoad = cpuLoad;
		this.memLoad = memLoad;
		this.netLoad = netLoad;
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
