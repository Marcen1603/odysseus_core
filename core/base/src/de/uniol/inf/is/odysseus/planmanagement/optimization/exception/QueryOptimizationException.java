package de.uniol.inf.is.odysseus.planmanagement.optimization.exception;

import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;

/**
 * QueryOptimizationException describes an {@link Exception} which occurs during
 * optimization.
 * 
 * @author Wolf Bauer
 * 
 */
public class QueryOptimizationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4360303442668293801L;

	/**
	 * Constructor of QueryOptimizationException.
	 * 
	 * @param m
	 *            detailed Exception message.
	 */
	public QueryOptimizationException(String m) {
		this(m, null);
	}

	/**
	 * Constructor of QueryOptimizationException.
	 * 
	 * @param m
	 *            detailed Exception message.
	 * @param e
	 *            {@link Throwable} which raised this exception.
	 */
	public QueryOptimizationException(String m, Throwable e) {
		super(m
				+ (e != null ? AppEnv.LINE_SEPARATOR + "Additional info:"
						+ AppEnv.LINE_SEPARATOR + e.getMessage() : ""));
		this.setStackTrace(e.getStackTrace());
	}
}
