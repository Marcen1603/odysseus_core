package de.uniol.inf.is.odysseus.net.resource.impl;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SigarWrapper {

	private static final Logger LOG = LoggerFactory.getLogger(SigarWrapper.class);

	private static final long DEFAULT_NET_BANDWIDTH_KB = 1024 * 10;
	
	private final Sigar sigar;
	
	private long previousInputTotal = 0;
	private long previousOutputTotal = 0;
	
	private double cpuMax;
	private long netMax;
	
	public SigarWrapper() {
		sigar = new Sigar();
		
		cpuMax = getCpuMaxImpl();
		netMax = getNetMaxImpl();
	}
	
	private double getCpuMaxImpl() {
		try {
			return sigar.getCpuPercList().length;
		} catch (Throwable e) {
			LOG.warn("Could not get cpu max from sigar", e);
			return Runtime.getRuntime().availableProcessors();
		}
	}
	
	private long getNetMaxImpl() {
		try {
			String interfaceName = sigar.getNetInterfaceConfig(null).getName();
			NetInterfaceStat net = sigar.getNetInterfaceStat(interfaceName);
			long speed = net.getSpeed();
			return speed >= 0 ? speed : DEFAULT_NET_BANDWIDTH_KB;
		} catch( Throwable t ) {
			LOG.warn("Could not get net output rate from sigar", t);
			return 1024;
		}
	}
	
	public double getCpuMax() {
		return cpuMax;
	}
	
	public double getCpuFree() {
		try {
			CpuPerc perc = sigar.getCpuPerc();
			double cpuFree = cpuMax - (perc != null ? perc.getUser() : 0.0) * cpuMax;
			double result = Math.min(cpuMax, Math.max(0, cpuFree));
			if( Double.isNaN(result)) {
				result = 0.0;
			}
			return result;
		} catch( Throwable t ) {
			LOG.warn("Could not get cpu free from sigar", t);
			return Runtime.getRuntime().availableProcessors();
		}
	}
	
	public double getNetInputRate() {
		try {
			String interfaceName = sigar.getNetInterfaceConfig(null).getName();
			NetInterfaceStat net = sigar.getNetInterfaceStat(interfaceName);
			long inputTotal = net != null ? net.getRxBytes() : previousInputTotal;
			previousInputTotal = inputTotal;
	
			return Math.max(0, (inputTotal - previousInputTotal) / 1024);
		} catch( Throwable t ) {
			LOG.warn("Could not get net input rate from sigar", t);
			return 0.0;
		}
	}
	
	public double getNetOutputRate() {
		try {
			String interfaceName = sigar.getNetInterfaceConfig(null).getName();
			NetInterfaceStat net = sigar.getNetInterfaceStat(interfaceName);
			long outputTotal = net != null ? net.getTxBytes() : previousOutputTotal;
			previousOutputTotal = outputTotal;
	
			return Math.max(0, (outputTotal - previousOutputTotal) / 1024);
		} catch( Throwable t ) {
			LOG.warn("Could not get net output rate from sigar", t);
			return 0.0;
		}
	}
	
	public long getNetMax() {
		return netMax;
	}
}
