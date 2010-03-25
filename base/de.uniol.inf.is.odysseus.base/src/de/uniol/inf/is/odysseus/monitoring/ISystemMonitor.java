package de.uniol.inf.is.odysseus.monitoring;

/**
 * 
 * @author Tobias Witt
 *
 */
public interface ISystemMonitor {
	public void initialize();
	public void initialize(long measurePeriod);
	public void stop();
	public double getAverageCPULoad();
	public double getHeapMemoryUsage();
	public void addListener(ISystemMonitorListener listener);
	public void removeListener(ISystemMonitorListener listener);
}
