package de.uniol.inf.is.odysseus.base;

public class CloseFailedException extends Exception {

	private static final long serialVersionUID = -4302422721151313207L;

	public CloseFailedException(String s, Throwable e) {
		super(s, e);
	}

	public CloseFailedException(String s) {
		super(s);
	}

	public CloseFailedException(Throwable e) {
		super(e);
	}
	
	

}
