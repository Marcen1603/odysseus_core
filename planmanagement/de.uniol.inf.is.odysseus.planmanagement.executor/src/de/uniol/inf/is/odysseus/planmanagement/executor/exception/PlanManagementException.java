package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;

public class PlanManagementException extends Exception {

	private static final long serialVersionUID = 3420886636303978500L;

	public PlanManagementException(Exception e) {
		this(e.getMessage());
	}

	public PlanManagementException(String details) {
		super("PlanManagementException: An error occured during planmangement. (" + AppEnv.LINE_SEPERATOR + details + ")");
	}
}
