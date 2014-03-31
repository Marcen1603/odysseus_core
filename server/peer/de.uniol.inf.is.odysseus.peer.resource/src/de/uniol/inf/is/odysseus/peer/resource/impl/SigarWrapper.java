package de.uniol.inf.is.odysseus.peer.resource.impl;

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
	
	public SigarWrapper() {
		sigar = new Sigar();
	}
	
	public double getCpuMax() {
		try {
			return sigar.getCpuPercList().length;
		} catch (Throwable e) {
			LOG.warn("Could not get cpu max from sigar", e);
			return Runtime.getRuntime().availableProcessors();
		}
	}
	
	public double getCpuFree() {
		try {
			CpuPerc perc = sigar.getCpuPerc();
			double cpuMax = sigar.getCpuPercList().length;
			double cpuFree = cpuMax - (perc != null ? perc.getUser() : 0.0) * cpuMax;
			return Math.min(cpuMax, Math.max(0, cpuFree));
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
}
