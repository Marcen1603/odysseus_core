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
import de.uniol.inf.is.odysseus.pattern.util.EventObject;
import de.uniol.inf.is.odysseus.pattern.util.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.util.PatternType;

/**
 * Abstract pattern operator which based on time or size intervals and buffers all relevant events in this interval.
 * Make sure you have set the parameter time or size to avoid increasing memory usage. 
 * @author Michael Falk
 *
 * @param <T>
 */
public abstract class BufferedPatternMatchingPO<T extends ITimeInterval> extends AbstractPatternMatchingPO<T> {
	
	/**
	 * Buffers the relevant events for one interval.
	 */
	protected EventBuffer<T> eventBuffer;
	
	public BufferedPatternMatchingPO(PatternType type, Integer time,
			Integer size, TimeUnit timeUnit, PatternOutput outputMode,
			List<String> eventTypes, List<SDFExpression> assertions,
			List<SDFExpression> returnExpressions,
			Map<Integer, String> inputTypeNames,
			Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea, Integer inputPort) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions,
				returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea, inputPort);
		this.eventBuffer = new EventBuffer<T>();
	}

	public BufferedPatternMatchingPO(BufferedPatternMatchingPO<T> patternPO) {
		super(patternPO);
		this.eventBuffer = patternPO.eventBuffer.clone();
	}

	@Override
	public void process_internal(Tuple<T> event, int port) {
		super.process_internal(event, port);
		String eventType = inputTypeNames.get(port);
		SDFSchema schema = inputSchemas.get(port);
		EventObject<T> eventObj = new EventObject<T>(event, eventType, schema, port);
		// only add relevant events
		if (eventTypes.contains(eventType)) {
			eventBuffer.add(eventObj);
		}
		// Check whether the time or size interval is over
		if (checkTimeElapsed() || checkSizeMatched()) {
			PointInTime currentTime = event.getMetadata().getStart();
			matching(currentTime);
			eventBuffer.clear();
		}
	}
	
	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		super.process_newHeartbeat(pointInTime);
		// Check whether the time interval is over
		if (checkTimeElapsed()) {
			matching(pointInTime.getTime());
			eventBuffer.clear();
		}
	}
	
	/**
	 * Creates the complex event(s) and transfer it. Depends on the output mode.
	 * SIMPLE in general only produces one complex event,
	 * the other output modes produces a set of complex events.
	 * @param results represents the matching set
	 * @param currentTime current time
	 * @param useTransferArea if true the outputTransferArea is used
	 */
	protected void transferEvents(EventBuffer<T> results, PointInTime currentTime, boolean useTransferArea) {
		if (outputMode == PatternOutput.SIMPLE) {
			Tuple<T> complexEvent = createComplexEvent(null, null, currentTime);
			transfer(complexEvent);
		} else {
			for (EventObject<T> event : results) {
				if (useTransferArea)
					outputTransferArea.transfer(event.getEvent());
				else
					transfer(event.getEvent());
			}
		}
	}
	
	/**
	 * Tries to match the specific pattern and creates the complex events.
	 * @param currentTime
	 */
	protected abstract void matching(PointInTime currentTime);

}
