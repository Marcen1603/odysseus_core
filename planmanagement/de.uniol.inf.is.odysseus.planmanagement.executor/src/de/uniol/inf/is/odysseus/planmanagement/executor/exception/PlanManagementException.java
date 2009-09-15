package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;

/**
 * PlanManagementException describes an {@link Exception} which occurs during
 * plan management.
 * 
 * @author Wolf Bauer
 * 
 */
public class PlanManagementException extends Exception {

	private static final long serialVersionUID = 3420886636303978500L;

	/**
	 * Constructor of PlanManagementException.
	 * 
	 * @param e original {@link Exception} which occurs. 
	 */
	public PlanManagementException(Exception e) {
		this(e.getMessage());
		this.setStackTrace(e.getStackTrace());
	}

	/**
	 * Constructor of PlanManagementException.
	 * 
	 * @param details detailed exception message.
	 */
	protected PlanManagementException(String details) {
		super(
				"PlanManagementException: An error occured during planmangement. ("
						+ AppEnv.LINE_SEPERATOR + details + ")");
	}
}
