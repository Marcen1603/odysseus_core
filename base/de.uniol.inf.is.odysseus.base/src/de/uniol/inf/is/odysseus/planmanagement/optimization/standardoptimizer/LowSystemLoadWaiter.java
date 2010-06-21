package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorListener;

/**
 * Helper class, that informs the {@link StandardOptimizer}, when the system
 * load is below a certain limit.
 * 
 * @author Tobias Witt
 * 
 */
public class LowSystemLoadWaiter implements ISystemMonitorListener {
	
	private double lowCpuLoad;
	private double lowMemLoad;
	private ISystemMonitor systemMonitor;
	private StandardOptimizer optimizer;
	
	public LowSystemLoadWaiter(ISystemMonitor monitor, StandardOptimizer optimizer, double lowCpuLoad, double lowMemLoad) {
		this.systemMonitor = monitor;
		this.lowCpuLoad = lowCpuLoad;
		this.lowMemLoad = lowMemLoad;
		this.optimizer = optimizer;
		
		initialize();
	}
	
	private void initialize() {
		this.systemMonitor.addListener(this);
	}

	@Override
	public void updateOccured() {
		double mem = this.systemMonitor.getHeapMemoryUsage();
		double cpu = this.systemMonitor.getAverageCPULoad();
		//System.out.println("mem: "+mem);
		if (mem <= this.lowMemLoad && cpu <= this.lowCpuLoad) {
			this.systemMonitor.stop();
			this.optimizer.processPendingRequests();
		}
	}
	
}
