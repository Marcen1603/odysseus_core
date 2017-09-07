package de.uniol.inf.is.odysseus.server.monitoring.system;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

import javax.management.*;

public class SystemUsage implements Runnable{
	private static MBeanServer mbs;
	private static int interval = 100;
	private static double currentCPUUsage =5;
	private static SystemUsage systemUsage;
	
		private int sampleTime = 10000;
		private ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
		private RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		private OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
		private Map<Long, Long> threadInitialCPU = new HashMap<Long, Long>();
		private Map<Long, Float> threadCPUUsage = new HashMap<Long, Float>();
		private long initialUptime = runtimeMxBean.getUptime();
		
	public void getThreadUsage(){
		ThreadInfo[] threadInfos = threadMxBean.dumpAllThreads(false, false);
		for (ThreadInfo info : threadInfos) {
			threadInitialCPU.put(info.getThreadId(), threadMxBean.getThreadCpuTime(info.getThreadId()));
		}
		try {Thread.sleep(sampleTime);} catch (InterruptedException e) {}

		long upTime = runtimeMxBean.getUptime();

		Map<Long, Long> threadCurrentCPU = new HashMap<Long, Long>();
		threadInfos = threadMxBean.dumpAllThreads(false, false);
		for (ThreadInfo info : threadInfos) {
		    threadCurrentCPU.put(info.getThreadId(), threadMxBean.getThreadCpuTime(info.getThreadId()));
		}

		// CPU over all processes
		int nrCPUs = osMxBean.getAvailableProcessors();
		// total CPU: CPU % can be more than 100% (devided over multiple cpus)
		//long nrCPUs = 1;
		// elapsedTime is in ms.
		long elapsedTime = (upTime - initialUptime);
		for (ThreadInfo info : threadInfos) {
		    Long initialCPU = threadInitialCPU.get(info.getThreadId());
		    if (initialCPU != null) {
		        long elapsedCpu = threadCurrentCPU.get(info.getThreadId()) - initialCPU;
		        float cpuUsage = elapsedCpu / (elapsedTime * 1000000F * nrCPUs);
		        threadCPUUsage.put(info.getThreadId(), cpuUsage);
		    }
		}

		// threadCPUUsage contains cpu % per thread
		System.out.println(threadCPUUsage);
		// You can use osMxBean.getThreadInfo(theadId) to get information on every thread reported in threadCPUUsage and analyze the most CPU intensive threads
	}
	public SystemUsage(){
		//openMBeanConnection();
	}
	
	public static SystemUsage getInstance(){
		if (systemUsage ==null){
			systemUsage = new SystemUsage();
		}
		return systemUsage;
	}

	
	private void openMBeanConnection() {
		if (mbs == null) {
			mbs = ManagementFactory.getPlatformMBeanServer();
		}
		new Thread(() -> {
			try {
				while (true) {
					setCurrentCPUUsage(getSystemCPULoad());
					Thread.sleep(interval);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}).start();
	}

	private double getSystemCPULoad() throws MalformedObjectNameException, NullPointerException, InstanceNotFoundException, ReflectionException {
		AttributeList list = new AttributeList();
			ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
			list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });
		
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

	public static double getCurrentCPUUsage() {
		return currentCPUUsage;
	}

	public void setCurrentCPUUsage(double usage) {
		currentCPUUsage = usage;
	}
	@Override
	public void run() {
		while(true){
			getThreadUsage();
		}
		
	}
}
