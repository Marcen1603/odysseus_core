package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

public class QueryTextParseException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String exceptionMessage = "Error during processing query-file";
	
	public QueryTextParseException() {
		super(exceptionMessage);
	}
	
	public QueryTextParseException( String msg ) {
		super(exceptionMessage + ": " + msg);
	}
}
