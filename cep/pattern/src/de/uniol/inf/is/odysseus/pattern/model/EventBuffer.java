package de.uniol.inf.is.odysseus.pattern.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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

public class EventBuffer<T extends IMetaAttribute> implements Iterable<EventObject<T>> {
	
	private LinkedList<EventObject<T>> objects;
	
	public EventBuffer() {
		this.objects = new LinkedList<EventObject<T>>();
	}
	
	@SuppressWarnings("unchecked")
	public EventBuffer(EventBuffer<T> buffer) {
		this.objects = (LinkedList<EventObject<T>>) buffer.objects.clone();
	}
	
	public void add(EventObject<T> object) {
		objects.add(object);
	}
	
	public void removeFirst() {
		objects.removeFirst();
	}
	
	public List<EventObject<T>> getEventObjects() {
		return objects;
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
	
	/**
	 * Returns the first n EventObjects.
	 * @param count Indicates n.
	 * @return
	 */
	public List<EventObject<T>> getFirstEventObjects(int count) {
		List<EventObject<T>> result = new ArrayList<>();
		if (count > objects.size())
			count = objects.size();
		for (int i=0; i < count; i++) {
			result.add(objects.get(i));
		}
		return result;
	}
	
	/**
	 * Returns the last n EventObjects.
	 * @param count Indicates n.
	 * @return
	 */
	public List<EventObject<T>> getLastEventObjects(int count) {
		List<EventObject<T>> result = new ArrayList<>();
		if (count > objects.size())
			count = objects.size();
		for (int i=0; i < count; i++) {
			result.add(objects.get(objects.size() - 1 - i));
		}
		return result;
	}
	
	public void clear() {
		objects.clear();
	}

	@Override
	public Iterator<EventObject<T>> iterator() {
		return this.objects.iterator();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objects == null) ? 0 : objects.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventBuffer<T> other = (EventBuffer<T>) obj;
		if (objects == null) {
			if (other.objects != null)
				return false;
		} else if (!objects.equals(other.objects))
			return false;
		return true;
	}

	@Override
	public EventBuffer<T> clone() {
		return new EventBuffer<T>(this);
	}

	@Override
	public String toString() {
		return "EventBuffer [objects=" + objects + "]";
	}	
	
}
