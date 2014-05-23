package de.uniol.inf.is.odysseus.systemload.impl;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BufferedSigarWrapper {

	private static final Logger LOG = LoggerFactory.getLogger(BufferedSigarWrapper.class);

	private static final long BUFFER_TIME_MILLIS = 2000;
	private static final long DEFAULT_NET_BANDWIDTH_KB = 1024 * 10;

	private final Sigar sigar;

	private final NumberAverager cpuFree = new NumberAverager(7);
	private final NumberAverager netInput = new NumberAverager(7);
	private final NumberAverager netOutput = new NumberAverager(7);

	private long previousInputTotal = 0;
	private long previousOutputTotal = 0;

	private long cpuFreeTimestamp = 0;
	private long netInputTimestamp = 0;
	private long netOutputTimestamp = 0;

	private double cpuMax;
	private long netMax;

	public BufferedSigarWrapper() {
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
		} catch (Throwable t) {
			LOG.warn("Could not get net output rate from sigar", t);
			return 1024;
		}
	}

	public double getCpuMax() {
		return cpuMax;
	}

	public long getNetMax() {
		return netMax;
	}

	public double getCpuFree() {
		try {
			if (System.currentTimeMillis() - cpuFreeTimestamp > BUFFER_TIME_MILLIS) {
				cpuFreeTimestamp = System.currentTimeMillis();

				CpuPerc perc = sigar.getCpuPerc();
				double cpuFree = cpuMax - (perc != null ? perc.getUser() : 0.0) * cpuMax;
				double result = Math.min(cpuMax, Math.max(0, cpuFree));
				if (Double.isNaN(result)) {
					result = 0.0;
				}

				this.cpuFree.addValue(cpuFree);
			}

			return this.cpuFree.getAverage();

		} catch (Throwable t) {
			LOG.warn("Could not get cpu free from sigar", t);
			return Runtime.getRuntime().availableProcessors();
		}
	}

	public double getNetInputRate() {
		try {
			if (System.currentTimeMillis() - netInputTimestamp > BUFFER_TIME_MILLIS) {
				netInputTimestamp = System.currentTimeMillis();

				String interfaceName = sigar.getNetInterfaceConfig(null).getName();
				NetInterfaceStat net = sigar.getNetInterfaceStat(interfaceName);
				long inputTotal = net != null ? net.getRxBytes() : previousInputTotal;
				previousInputTotal = inputTotal;
				double value = Math.max(0, (inputTotal - previousInputTotal) / 1024);

				netInput.addValue(value);
			}

			return netInput.getAverage();

		} catch (Throwable t) {
			LOG.warn("Could not get net input rate from sigar", t);
			return 0.0;
		}
	}

	public double getNetOutputRate() {
		try {
			if (System.currentTimeMillis() - netOutputTimestamp > BUFFER_TIME_MILLIS) {
				netOutputTimestamp = System.currentTimeMillis();
				String interfaceName = sigar.getNetInterfaceConfig(null).getName();
				NetInterfaceStat net = sigar.getNetInterfaceStat(interfaceName);
				long outputTotal = net != null ? net.getTxBytes() : previousOutputTotal;
				previousOutputTotal = outputTotal;

				double value = Math.max(0, (outputTotal - previousOutputTotal) / 1024);
				
				netOutput.addValue(value);
			}
			
			return netOutput.getAverage();
			
		} catch (Throwable t) {
			LOG.warn("Could not get net output rate from sigar", t);
			return 0.0;
		}
	}
}
