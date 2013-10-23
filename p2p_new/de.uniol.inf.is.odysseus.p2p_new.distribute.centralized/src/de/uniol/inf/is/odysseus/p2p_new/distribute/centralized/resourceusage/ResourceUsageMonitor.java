package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.resourceusage;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.CentralizedDistributor;

/**
 * This monitor collects information about cpu-, network and memory-usage on a peer
 * and periodically triggers an update for the master
 *
 */
public class ResourceUsageMonitor extends Thread {
	// can be used to let a peer report a multiple of its resource consumption for testing purposes
	private int loadMultiplicationFactor = 20;
	private IResourceUsageUpdateListener caller;
	private CPUUsage cpuUsage;
	private NetworkMonitor networkMonitor;
	private Sigar sigar = new Sigar();
	private long timeIntervalInMillis = 5000;
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
				doUpdate(cpuMean, mem, networkUsage);

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
	
	private void doUpdate(double cpuMean, Mem mem, double networkUsage) {
		double cpu = cpuMean;
		double mem_free = mem.getFree();
		double mem_total = mem.getTotal();
		double mem_used = mem.getUsed();
		double net = networkUsage;
		// modify the values for evaluation-purposes
		if(caller.getPeerName().toLowerCase().contains("overloaded")) {
			cpu = 0.95;
			net = 0.95;
			mem_free = 5;
			mem_total = 100;
			mem_used = 95;
		} else if (caller.getPeerName().toLowerCase().contains("underused")){
			cpu = 0.15;
			net = 0.15;
			mem_free = 85;
			mem_total = 100;
			mem_used = 15;
		} else if (caller.getPeerName().toLowerCase().contains("incrementingUsageWithQueryCount".toLowerCase())){
			int factor = CentralizedDistributor.getInstance().getNumberOfRunningQueries();
			cpu = 0.05 * factor * loadMultiplicationFactor;
			net = 0.05 * factor * loadMultiplicationFactor;
			mem_free = 100 - 5 * factor * loadMultiplicationFactor;
			mem_total = 100;
			mem_used = 5*factor * loadMultiplicationFactor;
		} else if (caller.getPeerName().toLowerCase().contains("incrementingUsageWithOpCount".toLowerCase())){
			int factor = CentralizedDistributor.getInstance().getNumberOfRunningOperators();
			cpu = 0.01 * factor * loadMultiplicationFactor;
			net = 0.01 * factor * loadMultiplicationFactor;
			mem_free = 100 - 1 * factor * loadMultiplicationFactor;
			mem_total = 100;
			mem_used = 1*factor * loadMultiplicationFactor;
		}
		caller.updateResourceUsage(cpu, mem_free, mem_total, mem_used, net);
	}
}
