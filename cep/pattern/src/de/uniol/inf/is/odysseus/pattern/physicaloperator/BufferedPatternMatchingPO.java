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
public abstract class BufferedPatternMatchingPO<T extends ITimeInterval> extends PatternMatchingPO<T> {
	
	/**
	 * Buffers the relevant events for one interval.
	 */
	protected EventBuffer<T> eventBuffer;
	/**
	 * If set to false, the event buffer is not used.
	 */
	private boolean useEventBuffer;
	
	public BufferedPatternMatchingPO(PatternType type, Integer time,
			Integer size, TimeUnit timeUnit, PatternOutput outputMode,
			List<String> eventTypes, List<SDFExpression> assertions,
			List<SDFExpression> returnExpressions,
			Map<Integer, String> inputTypeNames,
			Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions,
				returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea);
		this.eventBuffer = new EventBuffer<T>();
		this.useEventBuffer = true;
	}

	public BufferedPatternMatchingPO(BufferedPatternMatchingPO<T> patternPO) {
		super(patternPO);
		this.eventBuffer = patternPO.eventBuffer.clone();
		this.useEventBuffer = patternPO.useEventBuffer;
	}
	
	public boolean isUseEventBuffer() {
		return useEventBuffer;
	}

	public void setUseEventBuffer(boolean useEventBuffer) {
		this.useEventBuffer = useEventBuffer;
	}

	@Override
	public void process_internal(Tuple<T> event, int port) {
		super.process_internal(event, port);
		String eventType = inputTypeNames.get(port);
		SDFSchema schema = inputSchemas.get(port);
		EventObject<T> eventObj = new EventObject<T>(event, eventType, schema, port);
		// only add relevant events
		if (eventTypes.contains(eventType)) {
			if (useEventBuffer) {
				eventBuffer.add(eventObj);
			}
		}
		// Check whether the time or size interval is over
		if (checkTimeElapsed() || checkSizeMatched()) {
			PointInTime currentTime = event.getMetadata().getStart();
			matching(currentTime);
			if (useEventBuffer) {
				eventBuffer.clear();
			}
		}
	}
	
	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		super.process_newHeartbeat(pointInTime);
		// Check whether the time interval is over
		if (checkTimeElapsed()) {
			matching(pointInTime.getTime());
			if (useEventBuffer) {
				eventBuffer.clear();
			}
		}
	}
	
	/**
	 * Tries to match the specific pattern and creates the complex events.
	 * @param currentTime
	 */
	protected abstract void matching(PointInTime currentTime);

}
