package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.pattern.model.AttributeMap;
import de.uniol.inf.is.odysseus.pattern.model.EventBuffer;
import de.uniol.inf.is.odysseus.pattern.model.EventObject;
import de.uniol.inf.is.odysseus.pattern.model.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.model.PatternType;

/**
 * Operator, um Pattern-Matching durchzuführen.
 * @author Michael Falk
 * @param <T>
 */
public class ModalPatternMatchingPO<T extends ITimeInterval> extends PatternMatchingPO<T> {
	
	private EventBuffer<T> eventBuffer;
	
	public ModalPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea);
        eventBuffer = new EventBuffer<T>();
		this.init();
    }
	
	// Copy-Konstruktor
    public ModalPatternMatchingPO(ModalPatternMatchingPO<T> patternPO) {
    	super(patternPO);
        this.init();
    }
	
    private void init() {
    	// Pattern-spezifische Initialisierungen
    }
    
	@Override
	public String toString() {
		return super.toString() + " type: " + type + " eventTypes: " + eventTypes.toString(); 
	}

	@Override
	public ModalPatternMatchingPO<T> clone() {
		return new ModalPatternMatchingPO<T>(this);
	}
	
	@Override
	public void process_internal(Tuple<T> event, int port) {
		super.process_internal(event, port);
		String eventType = inputTypeNames.get(port);
		SDFSchema schema = inputSchemas.get(port);
		EventObject<T> eventObj = new EventObject<T>(event, eventType, schema, port);
		if (eventTypes.contains(eventType)) {
			eventBuffer.add(eventObj);
		}
		if (time != null) {
			// Annahme: Zeiteinheit von PointInTime ist Millisekunden 
			PointInTime currentTime = event.getMetadata().getStart();
			if (currentTime.minus(startTime).getMainPoint() >= time) {
				matching(currentTime);
				startTime = currentTime;
				eventBuffer.clear();
			}
		}
	}
	
	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		super.process_newHeartbeat(pointInTime);
		if (time != null) {
			// Annahme: Zeiteinheit von PointInTime ist Millisekunden 
			if (pointInTime.getTime().minus(startTime).getMainPoint() >= time) {
				matching(pointInTime.getTime());
				startTime = pointInTime.getTime();
				eventBuffer.clear();
			}
		}
	}
	
	/**
	 * Versucht, das Pattern zu erkennen. Bei Erfolg werden komplexe Events versandt.
	 * @param currentTime
	 */
	private void matching(PointInTime currentTime) {
		if (eventBuffer.getSize() != 0) {
			// Intervall abgelaufen -> gesammelte Events auswerten
			EventBuffer<T> results = checkAssertions(eventBuffer);
			if (type == PatternType.ALWAYS) {
				if (assertions == null || results.equals(eventBuffer)) {
					// ALWAY-Pattern erkannt
					Tuple<T> complexEvent = createComplexEvent(null, null, currentTime);
					outputTransferArea.transfer(complexEvent);
				}
			} else if (type == PatternType.SOMETIMES) {
				if (assertions == null || results.getSize() >= 1) {
					// SOMETIMES-Pattern erkannt
					Tuple<T> complexEvent = createComplexEvent(null, null, currentTime);
					outputTransferArea.transfer(complexEvent);
				}
			}
		}
	}
	
	/**
	 * Prüft für eine Liste von Events, ob die Bedingungen erfüllt sind.
	 * Als Ausgabe liefert diese Methode eine Liste der Events zurück,
	 * die die Bedingungen erfüllen.
	 * @param object
	 * @param eventObjectSets
	 * @param type
	 * @return
	 */
	protected EventBuffer<T> checkAssertions(EventBuffer<T> eventBuffer) {
		EventBuffer<T> output = new EventBuffer<T>();
		if (attrMappings == null) return output; 
		// Expressions für jedes Objekt überprüfen
		for (EventObject<T> event : eventBuffer) {
			Iterator<Entry<SDFExpression, AttributeMap[]>> iterator = attrMappings.entrySet().iterator();
			boolean satisfied = true;
			while (iterator.hasNext() && satisfied) {
				Entry<SDFExpression, AttributeMap[]> entry = iterator.next();
				satisfied = checkAssertion(event, Arrays.asList(event), entry);
			}
			if (satisfied) {
				// MatchingSet in Ausgabemenge aufnehmen
				output.add(event);
			}
		}
		return output;
	}
	
}
