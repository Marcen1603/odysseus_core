package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
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
 * Matches the Subset-Selection-Pattern.
 * @author Michael Falk
 * @param <T>
 */
public class SubsetSelectionPatternMatchingPO<T extends ITimeInterval> extends BufferedPatternMatchingPO<T> {
	
	/**
	 * attribute name which is used for the pattern
	 */
	private String attribute;
	/**
	 * amount of output events
	 */
	private Integer count;
	
	public SubsetSelectionPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea, Integer count, String attribute, Integer inputPort) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea, inputPort);
		this.attribute = attribute;
		this.count = count;
    }
	
	// Copy-Konstruktor
    public SubsetSelectionPatternMatchingPO(SubsetSelectionPatternMatchingPO<T> patternPO) {
    	super(patternPO);
        this.attribute = patternPO.attribute;
        this.count = new Integer(count);
    }
    
	@Override
	public String toString(){
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
		if (eventBuffer.getSize() != 0 && count != null && count > 0 && attribute != null) {
			EventBuffer<T> eventBufferClone = calcSatisfiedEvents(eventBuffer).clone();
			List<EventObject<T>> output = new ArrayList<>();
			List<EventObject<T>> equalValues = new ArrayList<>();
			
			// select count times an event
			for (int i = 0; i < count; i++) {
				EventObject<T> extremeObj = null;
				if (eventBufferClone.getSize() > 1) {
					extremeObj = eventBufferClone.get(0);
					// Search for max or min
					for (int j = 1; j < eventBufferClone.getSize(); j++) {
						EventObject<T> obj = eventBufferClone.get(j);
						SDFAttribute attr = obj.getSchema().findAttribute(attribute);
						if (attr == null) {
							continue;
						}
						int index = obj.getSchema().indexOf(attr);
						Double attrValue = obj.getEvent().getAttribute(index);
						// handle equals values in the last iteration
						if (i == (count - 1) && (Double) extremeObj.getEvent().getAttribute(index) == attrValue) {
							equalValues.add(obj);
						}
						if (type == PatternType.RELATIVE_N_LOWEST) {
							if ((Double) extremeObj.getEvent().getAttribute(index) > attrValue) {
								extremeObj = obj;
								equalValues.clear();
							}
						} else if (type == PatternType.RELATIVE_N_HIGHEST) {
							if ((Double) extremeObj.getEvent().getAttribute(index) < attrValue) {
								extremeObj = obj;
								equalValues.clear();
							}
						}
					}
				} else if (eventBufferClone.getSize() == 1) {
					extremeObj = eventBufferClone.get(0);
				}
				// add to output
				if (extremeObj != null) {
					output.add(extremeObj);
					eventBufferClone.remove(extremeObj);
				}
			}
			output.addAll(equalValues);
			// sort events to assure correct time order
			Collections.sort(output);
			// create complex events
			for (EventObject<T> obj : output) {
				Tuple<T> complexEvent = createComplexEvent(obj);
				transfer(complexEvent);
			}
		}
	}
	
}
