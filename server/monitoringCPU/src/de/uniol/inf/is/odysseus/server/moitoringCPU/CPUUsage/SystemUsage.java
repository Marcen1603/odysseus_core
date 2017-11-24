package de.uniol.inf.is.odysseus.server.moitoringCPU.CPUUsage;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class SystemUsage implements Runnable {
	
	private static MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	private static SystemUsage systemUsage;
	private volatile boolean running = true;

	private static final int sampleTime = 1000;
	private ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
	private Map<Long, Double> threadCPUUsage = new HashMap<Long, Double>();
	

	/**
	 * Calculates a CPU-Usage for every Thread based on the CPU-Time of every Thread.
	 */
	public void getThreadUsage() {
		// Collect initial readings
		ThreadInfo[] infos = threadMxBean.dumpAllThreads(false, false);
		Map<Long, Long> initialThreadCPUTimes = new HashMap<Long, Long>();
		long initialUptime = 0;
		double initialCPUUsage = getSystemCPULoad();

		for (ThreadInfo info : infos) {
			initialThreadCPUTimes.put(info.getThreadId(), threadMxBean.getThreadCpuTime(info.getThreadId()));
		}

		try {
			Thread.sleep(sampleTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Collect current readings
		Map<Long, Long> threadCurrentCPU = new HashMap<Long, Long>();
		long upTime = 0;
		double currentCPUUsage = getSystemCPULoad();
		double medianCPUUsage = (currentCPUUsage + initialCPUUsage) / 2;
		infos = threadMxBean.dumpAllThreads(false, false);

		for (ThreadInfo info : infos) {
			threadCurrentCPU.put(info.getThreadId(), threadMxBean.getThreadCpuTime(info.getThreadId()));
			upTime += threadCurrentCPU.get(info.getThreadId());
			initialUptime += initialThreadCPUTimes.get(info.getThreadId());
		}

		// TODO: remove sum. It is only for testing
		long sum = 0;
		long elapsedTime = (upTime - initialUptime);

		// Calculate CPU usage per Thread
		for (ThreadInfo info : infos) {
			Long initialCPU = initialThreadCPUTimes.get(info.getThreadId());
			if (initialCPU != null && elapsedTime != 0) {
				Long elapsedCpu = threadCurrentCPU.get(info.getThreadId()) - initialCPU;
				if (elapsedCpu != null) {
					double cpuUsage = ((elapsedCpu / elapsedTime) * medianCPUUsage);
					sum += cpuUsage;
					threadCPUUsage.put(info.getThreadId(), cpuUsage);
				}
			}
		}
		System.out.println("Summe: " + sum);
	}

	public SystemUsage() {

	}

	public static SystemUsage getInstance() {
		if (systemUsage == null) {
			systemUsage = new SystemUsage();
		}
		return systemUsage;
	}
	
	/**
	 * Asks MBeanServer for the the CPU-Usage of the Java process 
	 * @return CPU-Usage of the Java process
	 */
	private double getSystemCPULoad() {
		AttributeList list = new AttributeList();
		ObjectName name;

		try {
			name = ObjectName.getInstance("java.lang:type=OperatingSystem");
			list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });
		} catch (MalformedObjectNameException | NullPointerException | InstanceNotFoundException
				| ReflectionException e) {
			e.printStackTrace();
		}

		if (list.isEmpty()) {
			return -1.0;
		}

		Attribute att = (Attribute) list.get(0);
		Double cpuLoad = (Double) att.getValue();

		if (cpuLoad == -1.0) {
			return -1.0;
		}
		return ((int) (cpuLoad * 10000) / 100.00);
	}

	@Override
	public void run() {
		while (running) {
			getThreadUsage();
		}
	}

	
	public void shutdown() {
		this.running = false;
	}
	/**
	 * 
	 * @param threadID ID of a current active Thread
	 * @return CPU-Usage for a Thread. -1 if Thread has finished.
	 */
	public double getCPUUsageForThread(long threadID) {
		if (threadCPUUsage.containsKey(threadID)) {
			return threadCPUUsage.get(threadID);
		}
		return -1;
	}
}
