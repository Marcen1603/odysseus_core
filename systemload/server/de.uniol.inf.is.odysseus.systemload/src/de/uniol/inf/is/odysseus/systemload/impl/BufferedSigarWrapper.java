package de.uniol.inf.is.odysseus.systemload.impl;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class BufferedSigarWrapper {

	private static final Logger LOG = LoggerFactory.getLogger(BufferedSigarWrapper.class);

	private static final String BUFFER_TIME_SETTING_NAME = "systemLoadInterval";
	private static final long DEFAULT_NET_BANDWIDTH_KB = 1024 * 10;
	private static final long BUFFER_TIME_MILLIS_DEFAULT = 3 * 1000;

	private static long BUFFER_TIME_MILLIS = -1;

	private final Sigar sigar;

	private long previousInputTotal = 0;
	private long previousOutputTotal = 0;

	private double cpuMax;
	private long netMax;

	private double cpuFreeBuffer;
	private double netInBuffer;
	private double netOutBuffer;

	private long cpuFreeTimestamp;
	private long netInTimestamp;
	private long netOutTimestamp;

	private OdysseusConfiguration config;

	public BufferedSigarWrapper() {
		sigar = new Sigar();

		cpuMax = getCpuMaxImpl();
		netMax = getNetMaxImpl();

		if (BUFFER_TIME_MILLIS == -1) {
			checkBufferTimeMillis();
		}
	}

	private void checkBufferTimeMillis() {
		if (config != null) {
			BUFFER_TIME_MILLIS = config.getLong(BUFFER_TIME_SETTING_NAME, BUFFER_TIME_MILLIS_DEFAULT);
		}
		if (BUFFER_TIME_MILLIS <= 0) {
			BUFFER_TIME_MILLIS = BUFFER_TIME_MILLIS_DEFAULT;
		}
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
			LOG.warn("Could not get net maximum from sigar", t);
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
				CpuPerc perc = sigar.getCpuPerc();
				double cpuFree = cpuMax - (perc != null ? perc.getUser() : 0.0) * cpuMax;
				double result = Math.min(cpuMax, Math.max(0, cpuFree));
				if (Double.isNaN(result)) {
					result = 0.0;
				}

				cpuFreeBuffer = result;
				cpuFreeTimestamp = System.currentTimeMillis();
				checkBufferTimeMillis();
			}

			return cpuFreeBuffer;

		} catch (Throwable t) {
			LOG.warn("Could not get cpu free from sigar", t);
			return Runtime.getRuntime().availableProcessors();
		}
	}

	public double getNetInputRate() {
		try {
			if (System.currentTimeMillis() - netInTimestamp > BUFFER_TIME_MILLIS) {
				String interfaceName = sigar.getNetInterfaceConfig(null).getName();
				NetInterfaceStat net = sigar.getNetInterfaceStat(interfaceName);
				long inputTotal = net != null ? net.getRxBytes() : previousInputTotal;
				previousInputTotal = inputTotal;

				netInBuffer = Math.max(0, (inputTotal - previousInputTotal) / 1024);
				netInTimestamp = System.currentTimeMillis();
				checkBufferTimeMillis();
			}

			return netInBuffer;

		} catch (Throwable t) {
			LOG.warn("Could not get net input rate from sigar", t);
			return 0.0;
		}
	}

	public double getNetOutputRate() {
		try {
			if (System.currentTimeMillis() - netOutTimestamp > BUFFER_TIME_MILLIS) {
				String interfaceName = sigar.getNetInterfaceConfig(null).getName();
				NetInterfaceStat net = sigar.getNetInterfaceStat(interfaceName);
				long outputTotal = net != null ? net.getTxBytes() : previousOutputTotal;
				previousOutputTotal = outputTotal;

				netOutBuffer = Math.max(0, (outputTotal - previousOutputTotal) / 1024);
				netOutTimestamp = System.currentTimeMillis();
				checkBufferTimeMillis();
			}

			return netOutBuffer;

		} catch (Throwable t) {
			LOG.warn("Could not get net output rate from sigar", t);
			return 0.0;
		}
	}

	public void setConfig(OdysseusConfiguration c) {
		config = c;
	}
}
