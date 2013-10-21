package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.resourceusage;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import de.uniol.inf.is.odysseus.costmodel.operator.CPUUsage;

/**
 * This monitor collects information about cpu-, network and memory-usage on a peer
 * and periodically triggers an update for the master
 *
 */
public class ResourceUsageMonitor extends Thread {
	private IResourceUsageUpdateListener caller;
	private CPUUsage cpuUsage;
	private NetworkMonitor networkMonitor;
	private Sigar sigar = new Sigar();
	private long timeIntervalInMillis = 30000;
	public ResourceUsageMonitor(IResourceUsageUpdateListener caller) {
		this.caller = caller;
	}
	
	@Override
	public void run() {
		this.cpuUsage = new CPUUsage();
		cpuUsage.start();
		this.networkMonitor = new NetworkMonitor();
		networkMonitor.start();
		try {
			while(true) {
				double cpuMean = cpuUsage.getCpuMeanUsage();
				Mem mem = sigar.getMem();
				double networkUsage = networkMonitor.getNetUsage();
				caller.updateResourceUsage(cpuMean, mem, networkUsage);

				waitASecond();
			}
		} catch (SigarException e) {
			e.printStackTrace();
		}

	}
	
	private void waitASecond() {
		Thread.currentThread();
		try {
			Thread.sleep(timeIntervalInMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
