package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.pattern.util.AttributeMap;
import de.uniol.inf.is.odysseus.pattern.util.EventBuffer;
import de.uniol.inf.is.odysseus.pattern.util.EventObject;
import de.uniol.inf.is.odysseus.pattern.util.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.util.PatternType;

/**
 * Operator, um Pattern-Matching durchzuführen.
 * @author Michael Falk
 * @param <T>
 */
public class AllPatternMatchingPO<T extends ITimeInterval> extends AbstractPatternMatchingPO<T> {
	
	/**
	 * Port-Map, die für jeden Port eine Liste mit Event-Objekten enthält.
	 */
	private Map<Integer, EventBuffer<T>> objectLists;
	
	public AllPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea, Integer inputPort) {
        super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea, inputPort);
        this.objectLists = new HashMap<Integer, EventBuffer<T>>();
        // create lists for every port that is in the eventTypes list
        for (Integer port : this.inputTypeNames.keySet()) {
        	if (this.eventTypes.contains(this.inputTypeNames.get(port))) {
        		EventBuffer<T> buffer = new EventBuffer<T>();
            	this.objectLists.put(port, buffer);
        	}
        }
    }
	
	// Copy-Konstruktor
    public AllPatternMatchingPO(AllPatternMatchingPO<T> patternPO) {
    	super(patternPO);
        this.objectLists = new HashMap<Integer, EventBuffer<T>>();
        this.objectLists.putAll(patternPO.objectLists);
    }
	
	@Override
	public String toString(){
		return super.toString() + " type: " + type + " eventTypes: " + eventTypes.toString(); 
	}
	
	@Override
	public void process_internal(Tuple<T> event, int port) {
		super.process_internal(event, port);
		String eventType = inputTypeNames.get(port);
		SDFSchema schema = inputSchemas.get(port);
		EventObject<T> eventObj = new EventObject<T>(event, eventType, schema, port);
		if (eventType == null) {
			throw new InvalidEventException("Der Datentyp des Events ist null!");
		}
		if (type == PatternType.ALL) {
			if (eventTypes.contains(eventType)) {
				// Event einsortieren und alte Events entfernen
				objectLists.get(port).add(eventObj);
				dropOldEvents(eventObj);
				// Kombinationen suchen und Bedingungen überprüfen
				List<List<EventObject<T>>> output = calcSatisfiedCombinations(eventObj, computeCrossProduct(eventObj));
				for (List<EventObject<T>> outputObject : output) {
					if (outputObject != null && output.size() > 0) {
						Tuple<T> complexEvent = this.createComplexEvent(outputObject, outputObject.get(outputObject.size() - 1), null);
						outputTransferArea.transfer(complexEvent);
					}
				}
			}
		}
	}
	
	private void dropOldEvents(EventObject<T> event) {
		EventBuffer<T> currentEvents = objectLists.get(event.getPort());
		// Anzahl Elemente berücksichtigen
		if (size != null) {
			while (currentEvents.getSize() > size) {
				currentEvents.removeFirst();
			}
		}
		// Zeit berücksichtigen
		if (time != null) {
			PointInTime newStartTime = event.getEvent().getMetadata().getStart();
			for (EventBuffer<T> eventBuffer : objectLists.values()) {
				// alte Elemente löschen
				Iterator<EventObject<T>> iterator = eventBuffer.getEventObjects().iterator();
				while (iterator.hasNext()) {
					EventObject<T> currEvent = iterator.next();
					PointInTime currStartTime = currEvent.getEvent().getMetadata().getStart();
					// Annahme: Zeiteinheit von PointInTime ist Millisekunden 
					if (newStartTime.minus(currStartTime).getMainPoint() > time) {
						iterator.remove();
					}
				}
			}
		}
	}
	
	/**
	 * Berechnet das Kreuzprodukt von dem Event, das reingekommen ist und den anderen gespeicherten
	 * Events, die einen anderen Eventtyp haben.
	 * @param object
	 * @return
	 */
	private Set<List<EventObject<T>>> computeCrossProduct(EventObject<T> object) {
		// Alle EventBuffer außer den des empfangenen Events auswählen
		List<EventBuffer<T>> relevantEventBuffer = new ArrayList<EventBuffer<T>>();
		for (Entry<Integer, EventBuffer<T>> eventBuffer : objectLists.entrySet()) {
			if (!eventBuffer.getKey().equals(object.getPort())) {
				relevantEventBuffer.add(eventBuffer.getValue());
			}
		}
		
		// Kreuzprodukt berechnen
		List<Set<EventObject<T>>> relevantSets = new ArrayList<Set<EventObject<T>>>();
		for (EventBuffer<T> objects : relevantEventBuffer) {
			relevantSets.add(objects.toSet());
		}
		// Neues Event auch berücksichtigen
		Set<EventObject<T>> objSet = new HashSet<EventObject<T>>();
		objSet.add(object);
		relevantSets.add(objSet);
		return Sets.cartesianProduct(relevantSets);
	}
	
	/**
	 * Prüft für jede gültige Kombination (-> ALL-Pattern), ob alle Bedingungen
	 * erfüllt sind. Als Ausgabe liefert diese Methode eine Liste der
	 * Kombinationen zurück, für die die Bedingungen zutreffen.
	 * 
	 * @param object
	 * @param eventObjectSets
	 * @param type
	 * @return
	 */
	private List<List<EventObject<T>>> calcSatisfiedCombinations(EventObject<T> object,
			Set<List<EventObject<T>>> eventObjectSets) {
		List<List<EventObject<T>>> output = new ArrayList<List<EventObject<T>>>();

		// Expressions überprüfen
		for (List<EventObject<T>> eventObjectSet : eventObjectSets) {
			Iterator<Entry<SDFExpression, AttributeMap[]>> iterator = attrMappings
					.entrySet().iterator();
			boolean satisfied = true;
			while (iterator.hasNext() && satisfied) {
				Entry<SDFExpression, AttributeMap[]> entry = iterator.next();
				satisfied = checkAssertion(object, eventObjectSet, entry);
			}
			if (satisfied) {
				// MatchingSet in Ausgabemenge aufnehmen
				output.add(eventObjectSet);
			}
		}
		return output;
	}
	
}
