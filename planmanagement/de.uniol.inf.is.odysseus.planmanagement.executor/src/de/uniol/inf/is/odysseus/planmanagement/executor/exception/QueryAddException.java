package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;

public class QueryAddException extends PlanManagementException {
	private static final long serialVersionUID = -4360303442668293801L;

	public QueryAddException(Exception exception) {
		this(exception.getMessage());
	}

	public QueryAddException(String error) {
		super(
				"Error while adding query." + (error != null
						&& error.length() > 0 ? "Additional Info:"
						+ AppEnv.LINE_SEPERATOR + error : ""));
	}
}
