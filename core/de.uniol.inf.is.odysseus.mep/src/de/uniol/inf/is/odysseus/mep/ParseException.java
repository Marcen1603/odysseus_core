package de.uniol.inf.is.odysseus.mep;

public class ParseException extends Exception {

	private static final long serialVersionUID = -7832093047514492062L;

	public ParseException() {
		super();
	}
	
	public ParseException(String message){
		super(message);
	}
	
	public ParseException(Exception e){
		super(e);
	}

}
