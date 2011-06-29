package de.uniol.inf.is.odysseus.slamodel;

/**
 * Interface for event listeners of {@link SLAChangedEvent}
 * 
 * @author Thomas Vogelgesang
 * 
 */
public interface ISLAChangedEventListener {
	/**
	 * handles the given event
	 * 
	 * @param event
	 *            the event to handle
	 */
	public void slaChanged(SLAChangedEvent event);

}
