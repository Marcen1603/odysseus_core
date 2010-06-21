package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

/**
 * ExecutorInitializeException describes an {@link Exception} which occurs
 * during initializing an {@link IExecutor} .
 * 
 * @author Wolf Bauer
 * 
 */
public class ExecutorInitializeException extends PlanManagementException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4360303442668293801L;

	/**
	 * Constructor of ExecutorInitializeException.
	 * 
	 * @param m
	 *            detailed Exception message.
	 */
	public ExecutorInitializeException(String m) {
		super(m);
	}
}
