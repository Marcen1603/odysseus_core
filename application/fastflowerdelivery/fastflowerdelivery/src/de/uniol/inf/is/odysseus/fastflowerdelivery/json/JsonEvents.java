package de.uniol.inf.is.odysseus.fastflowerdelivery.json;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;

/**
 * 
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class JsonEvents extends JsonData {

	private List<AbstractEvent> events = new ArrayList<AbstractEvent>();
	
	public List<AbstractEvent> getEvents() {
		return events;
	}

	public void setEvents(List<AbstractEvent> events) {
		this.events = events;
	}
	
	/**
	 * Default constructor necessary for gson serialization
	 */
	public JsonEvents() {}
	
	public JsonEvents(List<AbstractEvent> events) {
		this.setEvents(events);
	}
	
}
