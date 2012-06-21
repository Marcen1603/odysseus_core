package de.uniol.inf.is.odysseus.core.datahandler;

public class DataHandlerException extends RuntimeException {

	private static final long serialVersionUID = 8085083438052854001L;
	
	public DataHandlerException(Exception e) {
		super(e);
	}
	
	public DataHandlerException(String message) {
		super(message);
	}
	
	public DataHandlerException(String message, Exception e) {
		super(message,e);
	}
}
