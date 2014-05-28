package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.systemload.impl.BufferedSigarWrapper;

public final class SystemLoadEntry implements Serializable, Cloneable {

	private static final long serialVersionUID = 7962567634409661546L;
	private static final BufferedSigarWrapper SIGAR_WRAPPER = new BufferedSigarWrapper();
	private static final Runtime RUNTIME = Runtime.getRuntime();
	
	private final String name;

	private double cpuLoadSum;
	private double memLoadSum;
	private double netLoadSum;
	
	private int count;

	public SystemLoadEntry(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name for systemload entry must not be empty or null!");
		
		this.name = name;
	}
	
	public SystemLoadEntry(SystemLoadEntry copy) {
		this.name = copy.name;
		
		this.cpuLoadSum = copy.cpuLoadSum;
		this.memLoadSum = copy.memLoadSum;
		this.netLoadSum = copy.netLoadSum;
		this.count = copy.count;
	}

	public String getName() {
		return name;
	}
	
	public void addSystemLoad() {
		double cpuMax = SIGAR_WRAPPER.getCpuMax();
		double cpuLoad = ( cpuMax - SIGAR_WRAPPER.getCpuFree() ) / cpuMax;
		cpuLoadSum += cpuLoad;
		
		double memMax = RUNTIME.totalMemory();
		double memLoad = ( memMax - RUNTIME.freeMemory() ) / memMax;
		memLoadSum += memLoad;
		
		double netMax = SIGAR_WRAPPER.getNetMax();
		double netInLoad = SIGAR_WRAPPER.getNetInputRate();
		double netOutLoad = SIGAR_WRAPPER.getNetOutputRate();
		
		double netLoad = (netInLoad + netOutLoad ) / netMax;
		netLoadSum += netLoad;
		
		count++;
	}

	public void addExistingSystemLoad(SystemLoadEntry other) {
		cpuLoadSum += other.cpuLoadSum;
		memLoadSum += other.memLoadSum;
		netLoadSum += other.netLoadSum;
		
		count += other.count;
	}
	
	public double getCpuLoad() {
		return cpuLoadSum / count;
	}
	
	public double getMemLoad() {
		return memLoadSum / count;
	}
	
	public double getNetLoad() {
		return netLoadSum / count;
	}
	
	public int getCount() {
		return count;
	}
	
	@Override
	public SystemLoadEntry clone() {
		return new SystemLoadEntry(this);
	}

}
