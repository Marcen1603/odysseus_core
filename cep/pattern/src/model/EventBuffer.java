package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

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
	
	public List<String> getEventTypes() {
		List<String> result = new ArrayList<String>();
		for (EventObject<T> object : objects) {
			result.add(object.getEventType());
		}
		return result;
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
	 * Überprüft, ob für diese Menge das All-Pattern erfüllt ist.
	 * @param eventTypes
	 * @param expression
	 * @param attrMapping 
	 * @return true, wenn alle eventTypes vorhanden sind und die expression erfüllt ist.
	 */
	public boolean matchAllPattern(List<String> eventTypes, SDFExpression expression, AttributeMap[] attrMapping, Tuple<T> event) {
		boolean matchEventTypes = containsAllOnlyOnce(eventTypes);
		if (matchEventTypes) {
			// Values an Expression binden
			Object[] values = new Object[attrMapping.length];
			for (int i = 0; i < attrMapping.length; i++) {
				EventObject<T> obj = getEventObject(attrMapping[i].getSchema());
				if (obj != null) {
					int attrPos = attrMapping[i].getAttrPos();
					values[i] = obj.getEvent().getAttribute(attrPos);
				}
			}
			
			expression.bindMetaAttribute(event.getMetadata());
			expression.bindAdditionalContent(event.getAdditionalContent());
			expression.bindVariables(values);
			boolean predicate = expression.getValue();
			if (predicate) return true;
		}
		return false;
	}
	
}
