package de.uniol.inf.is.odysseus.rest2.server.exception;

public class QueryNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -7212439143138678315L;

	public QueryNotFoundException(String message) {
		super(message);
	}
}
