package de.uniol.inf.is.odysseus.scheduler.exception;

/**
 * SchedulingException describes an {@link Exception} which occurs during
 * scheduling.
 * 
 * @author Wolf Bauer
 * 
 */
public class SchedulingException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6179180225457767336L;

	/**
	 * Constructor of SchedulingException.
	 * 
	 * @param message detailed exception message.
	 */
	public SchedulingException(String message) {
		super(message);
	}
	
	public SchedulingException( Throwable t ) {
		super(t);
	}
}
