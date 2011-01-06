package de.uniol.inf.is.odysseus.cep.epa.event;

import javax.swing.event.EventListenerList;

/**
 * This class defines a CEPEventAgent which notify its listeners of a new
 * CEPEvent.
 * 
 * @author Christian
 */
public class CEPEventAgent {

	// the list for the event listeners
	private EventListenerList listenerList;

	/**
	 * This is the constructor for this class.
	 */
	public CEPEventAgent() {
		this.listenerList = new EventListenerList();
	}

	/**
	 * This method adds a listener to the EventListenerList.
	 * 
	 * @param listener
	 *            is an implementation of the ICEPEventListener interface
	 */
	public void addCEPEventListener(ICEPEventListener listener) {
		this.listenerList.add(ICEPEventListener.class, listener);
	}

	/**
	 * This method removes a listener from the EventListenerList.
	 * 
	 * @param listener
	 *            is an implementation of the ICEPEventListener interface
	 */
	public void removeCEPEventListener(ICEPEventListener listener) {
		this.listenerList.remove(ICEPEventListener.class, listener);
	}

	/**
	 * This method creates a new CEPEvent and notifies all registered listeners.
	 * 
	 * @param type
	 *            is the type of the CEPEvent
	 * @param content
	 *            is the content of the CEPEvent
	 */
	public void fireCEPEvent(int type, Object content) {
		Object[] listeners = listenerList.getListenerList();
		ICEPEventListener listener = null;
		for (int i = 1; i < listeners.length; i += 2) {
			if (listeners[i] instanceof ICEPEventListener) {
				listener = (ICEPEventListener) listeners[i];
				CEPEvent event = new CEPEvent(listener, type, content);
				listener.cepEventOccurred(event);
			}
		}
	}

}
