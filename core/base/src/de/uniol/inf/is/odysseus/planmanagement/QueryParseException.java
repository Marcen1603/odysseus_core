package de.uniol.inf.is.odysseus.planmanagement;

public class QueryParseException extends Exception {
	private static final long serialVersionUID = -3000645620548786308L;
	
	public QueryParseException(String message) {
		super(message);
	}
	
	public QueryParseException(Exception e) {
		super(e);
	}
	
	public QueryParseException(String message, Throwable e) {
		super(message, e);
	}

}
