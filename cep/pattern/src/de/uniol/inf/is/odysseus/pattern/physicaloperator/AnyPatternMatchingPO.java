package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.pattern.model.AttributeMap;
import de.uniol.inf.is.odysseus.pattern.model.EventBuffer;
import de.uniol.inf.is.odysseus.pattern.model.EventObject;
import de.uniol.inf.is.odysseus.pattern.model.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.model.PatternType;

/**
 * Operator, um Pattern-Matching durchzuf�hren.
 * @author Michael Falk
 * @param <T>
 */
public class AnyPatternMatchingPO<T extends ITimeInterval> extends PatternMatchingPO<T> {
	
	/**
	 * Port-Map, die f�r jeden Port eine Liste mit Event-Objekten enth�lt.
	 */
	private Map<Integer, EventBuffer<T>> objectLists;
	
	public AnyPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea);
        this.init();
    }
	
	// Copy-Konstruktor
    public AnyPatternMatchingPO(AnyPatternMatchingPO<T> patternPO) {
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
	public AnyPatternMatchingPO<T> clone() {
		return new AnyPatternMatchingPO<T>(this);
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
		switch(type) {
			case ANY:
				if (eventTypes.contains(eventType)) {
					boolean satisfied = true;
					if (assertions != null) {
						// Assertions �berpr�fen
						int index = eventTypes.indexOf(eventType);
						SDFExpression assertion = assertions.get(index);
						if (assertion != null) {
							Entry<SDFExpression, AttributeMap[]> entry = new SimpleEntry<>(assertion, attrMappings.get(assertion));
							satisfied = checkAssertion(eventObj, entry);
						}
					}
					if (satisfied) {
						// ANY-Pattern erkannt
						Tuple<T> complexEvent = this.createComplexEvent(eventObj);
						outputTransferArea.transfer(complexEvent);
					}
				}
				break;
			default:
				break;
		}	

	}
	
	private boolean checkAssertion(EventObject<T> object, Entry<SDFExpression, AttributeMap[]> entry) {
		List<EventObject<T>> eventObjectSet = new ArrayList<>();
		eventObjectSet.add(object);
		return checkAssertion(object, eventObjectSet, entry);
	}
	
	private Tuple<T> createComplexEvent(EventObject<T> currentObj) {
		List<EventObject<T>> eventObjects = new ArrayList<>();
		return createComplexEvent(eventObjects, currentObj, null);
	}
	
}
