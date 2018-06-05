package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.pattern.util.EventBuffer;
import de.uniol.inf.is.odysseus.pattern.util.EventObject;
import de.uniol.inf.is.odysseus.pattern.util.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.util.PatternType;

/**
 * Matches the trend patterns. Ignores the assertions parameter.
 * Based on time and size intervals. Intervals will be reseted and start again
 * when a satisfaction isn't possible anymore.
 * @author Michael Falk
 * @param <T>
 */
public class TrendPatternMatchingPO<T extends ITimeInterval> extends BufferedPatternMatchingPO<T> {
	
	/**
	 * attribute name which is used for the pattern
	 */
	private String attribute;
	/**
	 * saves whether the pattern is still satisfied
	 */
	private boolean satisfied;
	private boolean[] mixedSatisfied;
	/**
	 * saves the value from the last event
	 */
	private Double lastValue;
	/**
	 * count how many values processed
	 */
	private int countValues;
		
	public TrendPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea, String attribute, Integer inputPort) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea, inputPort);
		this.attribute = attribute;
		this.mixedSatisfied = new boolean[2];
    }
	
    public TrendPatternMatchingPO(TrendPatternMatchingPO<T> patternPO) {
    	super(patternPO);
    }
    
	@Override
	public String toString(){
		return super.toString() + " type: " + type + " eventTypes: " + eventTypes.toString(); 
	}

	@Override
	public void process_internal(Tuple<T> event, int port) {
		String eventType = inputTypeNames.get(port);
		SDFSchema schema = inputSchemas.get(port);
		EventObject<T> eventObj = new EventObject<T>(event, eventType, schema, port);
		// filter events by event types and by assertion satisfaction
		if (eventTypes.contains(inputTypeNames.get(port)) && checkAssertions(eventObj)) {
			if (attribute != null) {
				SDFAttribute attr = eventObj.getSchema().findAttribute(attribute);
				if (attr != null) {
					int index = eventObj.getSchema().indexOf(attr);
					// prevent ClassCastException, attrValue only have to be numeric
					//Integer attrValue = eventObj.getEvent().getAttribute(index);
					Double attrValue = ((Number) eventObj.getEvent().getAttribute(index)).doubleValue();
					if (type != PatternType.MIXED && type != PatternType.NON_STABLE && (satisfied || countValues == 1)) {
						// choose operator by pattern type
						switch (type) {
						case INCREASING:
							satisfied = lastValue < attrValue;
							break;
						case DECREASING:
							satisfied = lastValue > attrValue;
							break;
						case STABLE:
							satisfied = lastValue.equals(attrValue);
							break;
						case NON_INCREASING:
							satisfied = lastValue >= attrValue;
							break;
						case NON_DECREASING:
							satisfied = lastValue <= attrValue;
							break;
						default:
							break;
						}
						if (!satisfied) {
							// pattern now can't be true anymore
							// -> reset interval on current event
							// -> and reset the event buffer list
							// the mixed pattern can also be true later
							startTime = event.getMetadata().getStart();
							eventBuffer.clear();
							countEvents = 0;
							countValues = 0;
						}
					}
					if (type == PatternType.NON_STABLE && countValues >= 1 && !satisfied) {
						satisfied = !lastValue.equals(attrValue);
					}
					if (type == PatternType.MIXED && countValues >= 1 && !satisfied) {
						if (lastValue < attrValue) {
							// detect increasing
							mixedSatisfied[0] = true;
						} else if (lastValue > attrValue) {
							// detect decreasing
							mixedSatisfied[1] = true;
						}
						if (mixedSatisfied[0] && mixedSatisfied[1]) {
							satisfied = true;
						}
					}
					lastValue = attrValue;
					countValues++;
				}
			} else {
				logger.error("attribute is null");
			}
		}
		super.process_internal(event, port);
	}
	
	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		super.process_newHeartbeat(pointInTime);
	}

	@Override
	protected void matching(PointInTime currentTime) {
		if (satisfied) {
			// create complex event
			EventBuffer<T> results = calcSatisfiedEvents(eventBuffer);
			transferEvents(results, currentTime, false);
		}
		// initiate state
		satisfied = false;
		mixedSatisfied[0] = false;
		mixedSatisfied[1] = false;
		lastValue = null;
		countValues = 0;
	}
	
}
