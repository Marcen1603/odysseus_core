package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;

/**
 * SchedulerException stellt einen Fehler bezogen auf das Scheduling dar.
 * @author wolf
 *
 */
public class SchedulerException extends PlanManagementException {

	private static final long serialVersionUID = -7047933396969916561L;
	
	/**
	 * Standard-Konstruktor
	 */
	public SchedulerException() {
		super("SchedulerException: Scheduler plugin is not loaded.");
	}

	/**
	 * Konstruktor. Die Information
	 * @param exception
	 */
	public SchedulerException(Exception exception) {
		this(exception.getMessage());
		this.setStackTrace(exception.getStackTrace());
	}

	public SchedulerException(String details) {
		super("SchedulerException:Scheduler plugin is not loaded. (" + AppEnv.LINE_SEPERATOR + details + ")");
	}
}
