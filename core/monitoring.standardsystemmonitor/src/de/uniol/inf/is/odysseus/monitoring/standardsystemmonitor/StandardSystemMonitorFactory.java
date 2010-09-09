package de.uniol.inf.is.odysseus.monitoring.standardsystemmonitor;

import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorFactory;

/**
 * Creates new {@link StandardSystemMonitor}s.
 * 
 * @author Tobias Witt
 *
 */
public class StandardSystemMonitorFactory implements ISystemMonitorFactory {

	@Override
	public ISystemMonitor newSystemMonitor() {
		return new StandardSystemMonitor();
	}

}
