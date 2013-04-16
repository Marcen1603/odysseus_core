package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Speichert Events zwischen.
 * @author Michael
 *
 * @param <T>
 */

public class EventBuffer<T extends IMetaAttribute> {
	
	private LinkedList<EventObject<T>> objects;
	
	public EventBuffer() {
		this.objects = new LinkedList<EventObject<T>>();
	}
	
	public void add(EventObject<T> object) {
		objects.add(object);
	}
	
	public void removeFirst() {
		objects.removeFirst();
	}
	
	public Set<EventObject<T>> toSet() {
		return new HashSet<EventObject<T>>(objects);
	}
	
	public List<String> getEventTypes() {
		List<String> result = new ArrayList<String>();
		for (EventObject<T> object : objects) {
			result.add(object.getEventType());
		}
		return result;
	}
	
	public int getSize() {
		return objects.size();
	}
	
	public boolean contains(String eventType) {
		return getEventTypes().contains(eventType);
	}
	
	public boolean containsAll(List<String> eventTypes) {
		return getEventTypes().containsAll(eventTypes);
	}
	
	public boolean containsAllOnlyOnce(List<String> eventTypes) {
		for (String eventType : eventTypes) {
			boolean once = false;
			for (EventObject<T> object : objects) {
				if (eventType.equals(object.getEventType())) {
					if (once) {
						return false;
					} else once = true;
				}
			}
			if (!once) return false;
		}
		return true;
	}
	
	/**
	 * Liefert das erste EventObject mit dem entsprechenden Schema zurück.
	 * @param schema
	 * @return null, wenn kein Objekt des Schema vorhanden
	 */
	public EventObject<T> getEventObject(SDFSchema schema) {
		for (EventObject<T> obj : objects) {
			if (obj.getSchema().equals(schema))
				return obj;
		}
		return null;
	}
	
}
