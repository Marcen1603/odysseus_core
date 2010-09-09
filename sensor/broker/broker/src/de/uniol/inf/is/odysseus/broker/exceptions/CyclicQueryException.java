package de.uniol.inf.is.odysseus.broker.exceptions;

/**
 * This exception is thrown, if something goes
 * wrong by translation or transformation of
 * a cyclic query.
 * 
 * This is a runtime exception, since most
 * errors occur due to wrong user inputs
 * 
 * @author Andre Bolles
 *
 */
public class CyclicQueryException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public CyclicQueryException(){
		super();
	}
	
	public CyclicQueryException(String msg){
		super(msg);
	}
	
	public CyclicQueryException(Exception nestedException){
		super(nestedException);
	}
}
