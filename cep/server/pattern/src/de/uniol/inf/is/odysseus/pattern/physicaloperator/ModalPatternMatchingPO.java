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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.pattern.util.EventBuffer;
import de.uniol.inf.is.odysseus.pattern.util.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.util.PatternType;

/**
 * Operator, um Pattern-Matching durchzuführen.
 * @author Michael Falk
 * @param <T>
 */
public class ModalPatternMatchingPO<T extends ITimeInterval> extends BufferedPatternMatchingPO<T> {
	
	public ModalPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea, Integer inputPort) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea, inputPort);
    }
	
	// Copy-Konstruktor
    public ModalPatternMatchingPO(ModalPatternMatchingPO<T> patternPO) {
    	super(patternPO);
    }
	
	@Override
	public String toString() {
		return super.toString() + " type: " + type + " eventTypes: " + eventTypes.toString(); 
	}
	
	@Override
	public void process_internal(Tuple<T> event, int port) {
		super.process_internal(event, port);
	}
	
	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		super.process_newHeartbeat(pointInTime);
	}
	
	@Override
	protected void matching(PointInTime currentTime) {
		if (eventBuffer.getSize() != 0) {
			// Intervall abgelaufen -> gesammelte Events auswerten
			EventBuffer<T> results = calcSatisfiedEvents(eventBuffer);
			if (type == PatternType.ALWAYS) {
				if (assertions == null || results.equals(eventBuffer)) {
					// ALWAY-Pattern erkannt
					transferEvents(results, currentTime, false);
				}
			} else if (type == PatternType.SOMETIMES) {
				if (assertions == null || results.getSize() >= 1) {
					// SOMETIMES-Pattern erkannt
					transferEvents(results, currentTime, false);
				}
			}
		}
	}
	
}
