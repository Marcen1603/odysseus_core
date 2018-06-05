package de.uniol.inf.is.odysseus.recovery.systemstatelogger;

import de.uniol.inf.is.odysseus.systemlog.ISystemLog;

/**
 * Component to add the following actions to the {@link ISystemLog}: startup and shutdown of
 * Odysseus. Both is defined as the binding/unbinding of the executor.
 * 
 * @author Michael Brand
 *
 */
public interface ISystemStateLogger {
	
	/**
	 * Adds a listener.
	 */
	public void addListener(ISystemStateEventListener listener);
	
	/**
	 * Removes a listener.
	 */
	public void removeListener(ISystemStateEventListener listener);

}