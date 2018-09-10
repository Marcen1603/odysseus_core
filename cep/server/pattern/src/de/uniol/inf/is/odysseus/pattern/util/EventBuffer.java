package de.uniol.inf.is.odysseus.pattern.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Buffers events in a list. Gives the opportunity to sort the buffer.
 * @author Michael Falk
 *
 * @param <T>
 */

public class EventBuffer<T extends ITimeInterval> implements Iterable<EventObject<T>> {
	
	private LinkedList<EventObject<T>> objects;
	
	public EventBuffer() {
		this.objects = new LinkedList<EventObject<T>>();
	}
	
	@SuppressWarnings("unchecked")
	public EventBuffer(EventBuffer<T> buffer) {
		this.objects = (LinkedList<EventObject<T>>) buffer.objects.clone();
	}
	
	public EventObject<T> get(int i) {
		return objects.get(i);
	}
	
	public void add(EventObject<T> object) {
		objects.add(object);
	}
	
	public void removeFirst() {
		objects.removeFirst();
	}
	
	public void remove(Object obj) {
		objects.remove(obj);
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
	
	/**
	 * Sorts the event objects on the basis of their start time stamps.
	 */
	public void sort() {
		Collections.sort(objects);
	}
	
}
