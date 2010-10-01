package de.uniol.inf.is.odysseus.event.error;

/**
 * Describes an object which is informed by an other object if an error occurs.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IErrorEventListener {
	/**
	 * Informs this object if an error occurs.
	 * 
	 * @param eventArgs The error event which occurred.
	 */
	public void sendErrorEvent(ErrorEvent eventArgs);
}
