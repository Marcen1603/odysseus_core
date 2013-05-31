package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.pattern.model.EventBuffer;
import de.uniol.inf.is.odysseus.pattern.model.EventObject;
import de.uniol.inf.is.odysseus.pattern.model.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.model.PatternType;

/**
 * Operator, um Pattern-Matching durchzuführen.
 * @author Michael Falk
 * @param <T>
 */
public class NPatternMatchingPO<T extends ITimeInterval> extends PatternMatchingPO<T> {
	
	private Integer count;
	private EventBuffer<T> eventBuffer;
	
	public NPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea, Integer count) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea);
        eventBuffer = new EventBuffer<T>();
        this.count = count;
		this.init();
    }
	
	// Copy-Konstruktor
    public NPatternMatchingPO(NPatternMatchingPO<T> patternPO) {
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
	public NPatternMatchingPO<T> clone() {
		return new NPatternMatchingPO<T>(this);
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
		if (checkTimeElapsed() || checkSizeMatched()) {
			PointInTime currentTime = event.getMetadata().getStart();
			matching(currentTime);
			eventBuffer.clear();
		}
	}
	
	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		super.process_newHeartbeat(pointInTime);
		if (checkTimeElapsed()) {
			matching(pointInTime.getTime());
			eventBuffer.clear();
		}
	}
	
	/**
	 * Versucht, das Pattern zu erkennen. Bei Erfolg werden komplexe Events versandt.
	 * @param currentTime
	 */
	private void matching(PointInTime currentTime) {
		if (eventBuffer.getSize() != 0 && count != null && count > 0) {
			// Intervall abgelaufen -> gesammelte Events auswerten
			EventBuffer<T> results = checkAssertions(eventBuffer);
			List<EventObject<T>> objectsToSend = null;
			if (type == PatternType.FIRST_N) {
				objectsToSend = results.getFirstEventObjects(count);
			} else if (type == PatternType.LAST_N) {
				objectsToSend = results.getLastEventObjects(count);
			}
			if (objectsToSend != null) {
				for (EventObject<T> obj : objectsToSend) {
					Tuple<T> complexEvent = createComplexEvent(obj);
					outputTransferArea.transfer(complexEvent);
				}
			}
		}
	}
	
}
