package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.pattern.util.EventObject;
import de.uniol.inf.is.odysseus.pattern.util.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.util.PatternType;

/**
 * Operator, um Pattern-Matching durchzuführen.
 * @author Michael Falk
 * @param <T>
 */
public class FunctorPatternMatchingPO<T extends ITimeInterval> extends AbstractPatternMatchingPO<T> {
	
	// FUNCTOR
	private String attribute;
	private Double value;
	
	public FunctorPatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea, String attribute, Double value, Integer inputPort) {
		super(type, time, size, timeUnit, outputMode, eventTypes, assertions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea, inputPort);
        this.attribute = attribute;
        this.value = value;
        this.init();
    }
	
	// Copy-Konstruktor
    public FunctorPatternMatchingPO(FunctorPatternMatchingPO<T> patternPO) {
    	super(patternPO);
        this.attribute = patternPO.attribute;
        this.value = patternPO.value;
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
	public void process_internal(Tuple<T> event, int port) {
		super.process_internal(event, port);
		String eventType = inputTypeNames.get(port);
		SDFSchema schema = inputSchemas.get(port);
		EventObject<T> eventObj = new EventObject<T>(event, eventType, schema, port);
		
		if (type == PatternType.FUNCTOR) {
			if (eventTypes.contains(eventType)) {
				SDFAttribute attr = schema.findAttribute(attribute);
				int index = schema.indexOf(attr);
				Double attrValue = event.getAttribute(index);
				logger.debug("attrValue " + attrValue);
				if (value < attrValue) {
					Tuple<T> complexEvent = createComplexEvent(eventObj);
					outputTransferArea.transfer(complexEvent);
				}
			}
		}
	}
	
}
