package de.uniol.inf.is.odysseus.planmanagement.optimization.advancedoptimizer;

import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorListener;

/**
 * 
 * @author Tobias Witt
 *
 */
public class LowSystemLoadWaiter implements ISystemMonitorListener {
	
	private double lowCpuLoad;
	private double lowMemLoad;
	private ISystemMonitor systemMonitor;
	private AdvancedOptimizer optimizer;
	
	public LowSystemLoadWaiter(ISystemMonitor monitor, AdvancedOptimizer optimizer, double lowCpuLoad, double lowMemLoad) {
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
