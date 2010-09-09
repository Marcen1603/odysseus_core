package de.uniol.inf.is.odysseus.monitoring;

/**
 * An {@link ISystemMonitorListener} gets informed about load updates measured
 * by an {@link ISystemMonitor}.
 * 
 * @author Tobias Witt
 * 
 */
public interface ISystemMonitorListener {
	public void updateOccured();
}
