package de.uniol.inf.is.odysseus.monitoring.standardsystemmonitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorListener;

/**
 * Default implementation of {@link ISystemMonitor}. Uses measuring methods of
 * the Java API.
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
	private List<ISystemMonitorListener> listeners;
	
	private double avgMem;
	private double maxMem;
	private long periodCounter;
	
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
	
	public StandardSystemMonitor() {
		this.listeners = Collections.synchronizedList(new ArrayList<ISystemMonitorListener>());
		this.periodCounter = 1;
	}
	
	@Override
	public void initialize() {
		initialize(1000L);
	}
	
	@Override
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
	
	@Override
	public void stop() {
		if (this.loadUpdater != null) {
			this.loadUpdater.stop();
			this.loadUpdaterThread.interrupt();
		}
	}
	
	private void update() {
		this.periodCounter++;
		long sum = getLoadForLastPeriod();
		this.avgLoad = (double)sum * 100.0 / (double)this.maxLoad;
		for (ISystemMonitorListener listener:this.listeners) {
			listener.updateOccured();
		}
		
		long mem = this.getMemForLastPeriod();
		if(mem > this.maxMem){
			this.maxMem = mem;
		}
		this.avgMem = ((avgMem * (this.periodCounter - 1)) + mem) / this.periodCounter;
	}
	
	private long getLoadForLastPeriod() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		long[] ids = bean.getAllThreadIds();
		long sum = 0L;
		List<Long> remove = new ArrayList<Long>(ids.length);
		for (long id:ids) {
			remove.add(id);
		}
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
	
	@Override
	public double getAverageCPULoad() {
		return this.avgLoad;
	}
	
	@Override
	public double getHeapMemoryUsage() {
		MemoryUsage usage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
		return (double)usage.getUsed() * 100.0 / (double)usage.getMax();
	}
	
	@Override
	public double getAverageMemoryUsage(){
		return this.avgMem;
	}
	
	@Override
	public double getMaxMemoryUsage(){
		return this.maxMem;
	}
	
	public long getMemForLastPeriod(){
		MemoryUsage heapMem = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
		MemoryUsage nonHeapMem = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
		return heapMem.getUsed() + nonHeapMem.getUsed();
	}
	
	@Override
	public void addListener(ISystemMonitorListener listener) {
		this.listeners.add(listener);
	}
	
	@Override
	public void removeListener(ISystemMonitorListener listener) {
		this.listeners.remove(listener);
		if (this.listeners.isEmpty()) {
			stop();
		}
	}
}
