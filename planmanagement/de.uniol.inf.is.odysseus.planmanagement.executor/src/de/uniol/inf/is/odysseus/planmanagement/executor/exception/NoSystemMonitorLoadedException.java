package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorFactory;

/**
 * NoSystemMonitorLoadedException describes an {@link Exception} which occurs if no
 * {@link ISystemMonitorFactory} service is registered.
 * 
 * @author Tobias Witt
 *
 */
public class NoSystemMonitorLoadedException extends PlanManagementException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1891916223678489431L;

	public NoSystemMonitorLoadedException() {
		super("No System Monitor plugin is loaded.");
	}

}
