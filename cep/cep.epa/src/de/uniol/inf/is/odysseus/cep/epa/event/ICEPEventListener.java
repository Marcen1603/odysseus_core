package de.uniol.inf.is.odysseus.cep.epa.event;

import java.util.EventListener;

/**
 * This is the interface for an CEPEventListener.
 * 
 * @author Christian
 */
public interface ICEPEventListener extends EventListener {

	/**
	 * This method handles the occurrence of a CEPEvent.
	 * 
	 * @param event
	 *            the fired event
	 */
	public void cepEventOccurred(CEPEvent event);

}
