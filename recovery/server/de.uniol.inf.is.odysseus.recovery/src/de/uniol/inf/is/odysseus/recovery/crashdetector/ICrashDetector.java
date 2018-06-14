package de.uniol.inf.is.odysseus.recovery.crashdetector;

import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ISystemStateEventListener;
import de.uniol.inf.is.odysseus.systemlog.ISystemLog;

/**
 * Component to check, if there has been a crash of Odysseus. It uses the {@link ISystemLog}
 * to do so.
 * 
 * @author Michael Brand
 *
 */
public interface ICrashDetector {

	/**
	 * Adds a listener.
	 */
	public void addListener(ISystemStateEventListener listener);

	/**
	 * Removes a listener.
	 */
	public void removeListener(ISystemStateEventListener listener);

}