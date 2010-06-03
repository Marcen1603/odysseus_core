package de.uniol.inf.is.odysseus.monitoring;

/**
 * Creates {@link ISystemMonitor}s.
 * 
 * @author Tobias Witt
 *
 */
public interface ISystemMonitorFactory {
	public ISystemMonitor newSystemMonitor();
}
