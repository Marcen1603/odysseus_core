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
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.pattern.model.EventBuffer;
import de.uniol.inf.is.odysseus.pattern.model.EventObject;
import de.uniol.inf.is.odysseus.pattern.model.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.model.PatternType;

/**
 * Operator, um Pattern-Matching durchzuf�hren.
 * @author Michael Falk
 * @param <T>
 */
public class AllPatternMatchingPO<T extends ITimeInterval> extends PatternMatchingPO<T> {
	
	/**
	 * Port-Map, die f�r jeden Port eine Liste mit Event-Objekten enth�lt.
	 */
	private Map<Integer, EventBuffer<T>> objectLists;
	
	public AllPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea) {
        super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea);
        this.objectLists = new HashMap<Integer, EventBuffer<T>>();
        // create lists for every port that is in the eventTypes list
        for (Integer port : this.inputTypeNames.keySet()) {
        	if (this.eventTypes.contains(this.inputTypeNames.get(port))) {
        		EventBuffer<T> buffer = new EventBuffer<T>();
            	this.objectLists.put(port, buffer);
        	}
        }
        this.init();
    }
	
	// Copy-Konstruktor
    public AllPatternMatchingPO(AllPatternMatchingPO<T> patternPO) {
    	super(patternPO);
        this.objectLists = new HashMap<Integer, EventBuffer<T>>();
        this.objectLists.putAll(patternPO.objectLists);
        this.init();
    }
	
    private void init() {
    	// Pattern-spezifische Initialisierungen
    }
    
	@Override
	public String toString(){
		return super.toString() + " type: " + type + " eventTypes: " + eventTypes.toString(); 
	}

	@Override
	public AllPatternMatchingPO<T> clone() {
		return new AllPatternMatchingPO<T>(this);
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
				// Kombinationen suchen und Bedingungen �berpr�fen
				List<List<EventObject<T>>> output = checkAssertions(eventObj, computeCrossProduct(eventObj));
				for (List<EventObject<T>> outputObject : output) {
					Tuple<T> complexEvent = this.createComplexEvent(outputObject, eventObj, null);
					outputTransferArea.transfer(complexEvent);
				}
			}
		}
	}
	
	private void dropOldEvents(EventObject<T> event) {
		EventBuffer<T> currentEvents = objectLists.get(event.getPort());
		// Anzahl Elemente ber�cksichtigen
		if (size != null) {
			while (currentEvents.getSize() > size) {
				currentEvents.removeFirst();
			}
		}
		// Zeit ber�cksichtigen
		if (time != null) {
			PointInTime newStartTime = event.getEvent().getMetadata().getStart();
			for (EventBuffer<T> eventBuffer : objectLists.values()) {
				// alte Elemente l�schen
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
		// Alle EventBuffer au�er den des empfangenen Events ausw�hlen
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
		// Neues Event auch ber�cksichtigen
		Set<EventObject<T>> objSet = new HashSet<EventObject<T>>();
		objSet.add(object);
		relevantSets.add(objSet);
		return Sets.cartesianProduct(relevantSets);
	}
	
}
