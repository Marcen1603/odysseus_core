package de.uniol.inf.is.odysseus.event.error;

/**
 * Describes an object which knows other objects which are informed if an error
 * occurs.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IErrorEventHandler {
	/**
	 * Adds an object which is informed if an error occurs.
	 * 
	 * @param errorEventListener
	 *            Object which is informed if an error occurs.
	 */
	public void addErrorEventListener(IErrorEventListener errorEventListener);

	/**
	 * Removes an object which should no more be informed if an error occurs.
	 * 
	 * @param errorEventListener
	 *            Object which should no more be informed if an error occurs.
	 */
	public void removeErrorEventListener(IErrorEventListener errorEventListener);
}
