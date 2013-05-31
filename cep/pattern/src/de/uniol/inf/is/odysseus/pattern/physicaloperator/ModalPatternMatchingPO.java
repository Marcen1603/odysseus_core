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
 * Operator, um Pattern-Matching durchzuf�hren.
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
	
}
