package de.uniol.inf.is.odysseus.monitoring.standardsystemmonitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;

/**
 * 
 * @author Tobias Witt
 *
 */
public class StandardSystemMonitor implements ISystemMonitor {
	
	private Map<Long, Long> last;
	private long maxLoad;
	private long measurePeriod;
	private double avgLoad;
	private LoadUpdater loadUpdater;
	private Thread loadUpdaterThread;
	
	private class LoadRunnable implements Runnable {
		private boolean run = true;
		public void stop() {
			this.run = false;
		}
		@Override
		public void run() {
			while(run) {
				Thread.yield();
				Math.random();
			}
		}
	}
	
	private class LoadUpdater implements Runnable {
		private boolean run = true;
		public void stop() {
			this.run = false;
		}
		@Override
		public void run() {
			while(run) {
				try {
					Thread.sleep(measurePeriod);
					update();
				} catch (InterruptedException e) {}
			}
		}
	}
	
	public void initialize() {
		initialize(1000L);
	}
	
	public void initialize(long measurePeriod) {
		this.measurePeriod = measurePeriod;
		this.last = new HashMap<Long, Long>();
		
		// figure out max load
		int numCpus = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
		LoadRunnable loadRunnable[] = new LoadRunnable[numCpus];
		
		for (int i=0; i<numCpus; i++) {
			loadRunnable[i] = new LoadRunnable();
			new Thread(loadRunnable[i]).start();
		}
		this.maxLoad = 0L;
		for (int i=0; i<5; i++) {
			long load = getLoadForLastPeriod();
			if (load > this.maxLoad) {
				this.maxLoad = load;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		for (int i=0; i<numCpus; i++) {
			loadRunnable[i].stop();
		}
		this.maxLoad *= this.measurePeriod / 100;
		
		// start measuring every measurePeriod
		this.avgLoad = 0.0;
		this.loadUpdater = new LoadUpdater();
		this.loadUpdaterThread = new Thread(this.loadUpdater);
		this.loadUpdaterThread.start();
	}
	
	public void stop() {
		if (this.loadUpdater != null) {
			this.loadUpdater.stop();
			this.loadUpdaterThread.interrupt();
		}
	}
	
	private void update() {
		long sum = getLoadForLastPeriod();
		this.avgLoad = (double)sum * 100.0 / (double)this.maxLoad;
	}
	
	private long getLoadForLastPeriod() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		long[] ids = bean.getAllThreadIds();
		long sum = 0L;
		List<Long> remove = Arrays.asList(this.last.keySet().toArray(new Long[this.last.keySet().size()]));
		for (long id:ids) {
			long time = bean.getThreadCpuTime(id);
			Long lastTime = this.last.get(id);
			sum += time - (lastTime!=null ? lastTime : 0L);
			last.put(id, time);
			remove.remove(id);
		}
		for (Long id:remove) {
			this.last.remove(id);
		}
		return sum;
	}
	
	public double getAverageCPULoad() {
		return this.avgLoad;
	}
	
	public double getHeapMemoryUsage() {
		MemoryUsage usage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
		return (double)usage.getUsed() * 100.0 / (double)usage.getMax();
	}
}
