package de.uniol.inf.is.odysseus.planmanagement.optimization.exception;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;

public class QueryOptimizationException extends Exception {
	private static final long serialVersionUID = -4360303442668293801L;

	public QueryOptimizationException(String m) {
		this(m, null);
	}

	public QueryOptimizationException(String m, Exception e) {
		super(m + (e != null ? AppEnv.LINE_SEPERATOR + "Additional info:" + AppEnv.LINE_SEPERATOR
				+ e.getMessage() : ""));
	}
}
