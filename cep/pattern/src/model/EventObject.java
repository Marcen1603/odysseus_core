package model;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Diese Klasse repr�sentiert ein Event.
 * @author Michael
 *
 * @param <T>
 */
public class EventObject<T extends IMetaAttribute> {
	private Tuple<T> event;
	private String eventType;
	private SDFSchema schema;
	private int port;
	
	public EventObject(Tuple<T> event, String eventType, SDFSchema schema, int port) {
		this.event = event;
		this.eventType = eventType;
		this.schema = schema;
		this.port = port;
	}

	public Tuple<T> getEvent() {
		return event;
	}

	public String getEventType() {
		return eventType;
	}
	
	public SDFSchema getSchema() {
		return schema;
	}
	
	public int getPort() {
		return port;
	}
}
