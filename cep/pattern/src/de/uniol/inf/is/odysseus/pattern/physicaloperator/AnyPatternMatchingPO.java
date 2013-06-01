package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.AbstractMap.SimpleEntry;
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
public class AnyPatternMatchingPO<T extends ITimeInterval> extends PatternMatchingPO<T> {
	
	/**
	 * Port-Map, die für jeden Port eine Liste mit Event-Objekten enthält.
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
		
		if (eventTypes.contains(eventType)) {
			boolean satisfied = true;
			if (assertions != null) {
				// Assertions überprüfen
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
	}
	
}
