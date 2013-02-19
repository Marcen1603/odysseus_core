package de.uniol.inf.is.odysseus.fastflowerdelivery.json;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;

/**
 * Transports a single event to the client
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class JsonEvent extends JsonData {

	private AbstractEvent event;

	public AbstractEvent getEvent() {
		return event;
	}

	public void setEvent(AbstractEvent event) {
		this.event = event;
	}
	
	/**
	 * Default constructor necessary for gson serialization
	 */
	public JsonEvent() { }
	
	/**
	 * @param event
	 * 			the event to send to the client
	 */
	public JsonEvent(AbstractEvent event) {
		this.event = event;
	}
	
}
