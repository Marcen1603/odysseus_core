package de.uniol.inf.is.odysseus.admission.status;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.status.impl.BufferedSigarWrapper;

public class SystemLoadAdmissionStatusComponent implements IAdmissionStatusComponent {
	
	private static final BufferedSigarWrapper SIGAR_WRAPPER = new BufferedSigarWrapper();
	private static final Runtime RUNTIME = Runtime.getRuntime();
	
	public double getCpuMax() {
		return SIGAR_WRAPPER.getCpuMax();
	}
	
	public double getCpuFree() {
		return SIGAR_WRAPPER.getCpuFree();
	}
	
	public double getCpuLoad() {
		return getCpuMax() - getCpuFree();
	}
	
	public double getCpuLoadPercentage() {
		return getCpuLoad() <= 0.0 ? 0.0 : ( getCpuLoad() / getCpuMax() ) * 100.0;
	}
	
	public double getNetworkMax() {
		return SIGAR_WRAPPER.getNetMax();
	}
	
	public double getNetworkInputRate() {
		return SIGAR_WRAPPER.getNetInputRate();
	}
	
	public double getNetworkOutputRate() {
		return SIGAR_WRAPPER.getNetOutputRate();
	}
	
	public double getNetworkLoad() {
		return getNetworkInputRate() + getNetworkOutputRate();
	}
	
	public double getNetworkFree() {
		return getNetworkMax() - getNetworkInputRate() - getNetworkOutputRate();
	}
	
	public double getNetworkLoadPercentage() {
		return getNetworkLoad() <= 0.0 ? 0.0 : (getNetworkMax() / getNetworkLoad()) * 100.0;
	}
	
	public long getMemoryFree() {
		return RUNTIME.freeMemory();
	}
	
	public long getMemoryMax() {
		return RUNTIME.maxMemory();
	}
	
	public long getMemoryLoad() {
		return getMemoryMax() - getMemoryFree();
	}
	
	public double getMemoryLoadPercentage() {
		return getMemoryLoad() <= 0 ? 0 : (getMemoryLoad() / getMemoryMax() ) * 100.0;
	}
}
