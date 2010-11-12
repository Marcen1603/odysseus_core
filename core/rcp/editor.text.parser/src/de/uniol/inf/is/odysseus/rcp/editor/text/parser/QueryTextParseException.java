package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

public class QueryTextParseException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String exceptionMessage = "Error during processing query-file";
	
	public QueryTextParseException() {
		this(exceptionMessage);
	}
	
	public QueryTextParseException( String msg ) {
		super(msg);
	}
	
	public QueryTextParseException( String msg, Throwable ex ) {
		super(msg, ex);
	}
	
	public QueryTextParseException( Throwable ex) {
		this(exceptionMessage, ex);
	}
}
