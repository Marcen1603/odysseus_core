package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;

/**
 * QueryAddException describes an {@link Exception} which occurs during
 * adding a query to odysseus.
 * 
 * @author Wolf Bauer
 * 
 */
public class QueryAddException extends PlanManagementException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4360303442668293801L;

	
	/**
	 * Constructor of QueryAddException.
	 * 
	 * @param exception Exception {@link Exception} which occurs. 
	 */
	public QueryAddException(Exception exception) {
		this(exception.getMessage());
		this.setStackTrace(exception.getStackTrace());
	}

	/**
	 * Constructor of QueryAddException.
	 * 
	 * @param error detailed exception message.
	 */
	public QueryAddException(String error) {
		super(
				"Error while adding query." + (error != null
						&& error.length() > 0 ? "Additional Info:"
						+ AppEnv.LINE_SEPARATOR + error : ""));
		
	}
}
