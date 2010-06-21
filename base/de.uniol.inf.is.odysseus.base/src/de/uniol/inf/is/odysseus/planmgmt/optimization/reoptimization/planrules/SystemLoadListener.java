package de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules;

import de.uniol.inf.is.odysseus.base.planmanagement.plan.AbstractPlanReoptimizeRule;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorListener;

/**
 * Default implementation of {@link ISystemMonitorListener}.
 * 
 * @author Tobias Witt
 *
 */
public class SystemLoadListener extends AbstractPlanReoptimizeRule implements ISystemMonitorListener {
	
	private double criticalCPULoad;
	private double criticalMemoryLoad;
	private ISystemMonitor systemMonitor;
	
	public SystemLoadListener(ISystemMonitor systemMonitor, double criticalCPULoad, double criticalMemoryLoad) {
		this.criticalCPULoad = criticalCPULoad;
		this.criticalMemoryLoad = criticalMemoryLoad;
		this.systemMonitor = systemMonitor;
		
		initialize();
	}
	
	public void initialize() {
		this.systemMonitor.addListener(this);
	}

	@Override
	public void deinitialize() {
		this.systemMonitor.removeListener(this);
	}

	@Override
	public void updateOccured() {
		double cpuLoad = this.systemMonitor.getAverageCPULoad();
		double memLoad = this.systemMonitor.getHeapMemoryUsage();
		//System.out.println("mem: "+memLoad);
		if (cpuLoad >= this.criticalCPULoad
				|| memLoad >= this.criticalMemoryLoad) {
			fireReoptimizeEvent();
		}
	}
	
}
