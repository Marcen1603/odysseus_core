package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.ArrayList;
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
import de.uniol.inf.is.odysseus.pattern.util.EventObject;
import de.uniol.inf.is.odysseus.pattern.util.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.util.PatternType;

/**
 * Matches the FIRST_N and the LAST_N pattern.
 * @author Michael Falk
 * @param <T>
 */
public class NPatternMatchingPO<T extends ITimeInterval> extends BufferedPatternMatchingPO<T> {
	
	/**
	 * Represents the parameter N.
	 */
	private Integer count;
	
	public NPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea, Integer count, Integer inputPort) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea, inputPort);
        this.count = count;
    }
	
	// Copy-Konstruktor
    public NPatternMatchingPO(NPatternMatchingPO<T> patternPO) {
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
		if (eventBuffer.getSize() != 0 && count != null && count > 0) {
			// Intervall abgelaufen -> gesammelte Events auswerten
			EventBuffer<T> results = calcSatisfiedEvents(eventBuffer);
			List<EventObject<T>> objectsToSend = null;
			if (type == PatternType.FIRST_N) {
				objectsToSend = results.getFirstEventObjects(count);
			} else if (type == PatternType.LAST_N) {
				List<EventObject<T>> nLastObjects = results.getLastEventObjects(count);
				objectsToSend = new ArrayList<>();
				// switch list to keep time order
				for (int i=0; i < nLastObjects.size(); i++) {
					objectsToSend.add(nLastObjects.get(nLastObjects.size() - 1 - i));
				}				
			}
			if (objectsToSend != null) {
				for (EventObject<T> obj : objectsToSend) {
					Tuple<T> complexEvent = createComplexEvent(obj);
					// don't work with outputSyncArea
					transfer(complexEvent);
				}
			}
		}
	}
	
}
