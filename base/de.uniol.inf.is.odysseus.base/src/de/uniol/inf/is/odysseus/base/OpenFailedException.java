package de.uniol.inf.is.odysseus.base;


/**
 * Superclass for all exceptions which can occur during a call to open
 * on a {@link ISource} or {@link ISink}.
 * @author Jonas Jacobi
 */
public class OpenFailedException extends Exception {

	private static final long serialVersionUID = 762295616036628102L;
	
	public OpenFailedException(String message) {
		super(message);
	}
	
	public OpenFailedException(Exception e){
		super(e);
	}

}
