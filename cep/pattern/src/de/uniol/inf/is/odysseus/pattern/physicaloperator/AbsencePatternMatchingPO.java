package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.pattern.model.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.model.PatternType;

/**
 * Operator, um Pattern-Matching durchzuführen.
 * @author Michael Falk
 * @param <T>
 */
public class AbsencePatternMatchingPO<T extends ITimeInterval> extends PatternMatchingPO<T> {
	
	// ABSENCE
	private boolean noIncomingEvent = true;
	private int times = 0;
	
	public AbsencePatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea) {
        super(type, size, size, timeUnit, outputMode, eventTypes, returnExpressions, returnExpressions, inputTypeNames, inputSchemas, inputStreamSyncArea);
        this.init();
    }
	
	// Copy-Konstruktor
    public AbsencePatternMatchingPO(AbsencePatternMatchingPO<T> patternPO) {
    	super(patternPO);
    	this.noIncomingEvent = patternPO.noIncomingEvent;
    	this.times = patternPO.times;
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
	public AbsencePatternMatchingPO<T> clone() {
		return new AbsencePatternMatchingPO<T>(this);
	}
	
	@Override
	public void process_internal(Tuple<T> event, int port) {
		super.process_internal(event, port);
	}
	
	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		super.process_newHeartbeat(pointInTime);
		logger.info(pointInTime.toString());
		if (type == PatternType.ABSENCE && time != null) {
			// Annahme: Zeiteinheit von PointInTime ist Millisekunden 
			if ((pointInTime.getTime().getMainPoint() - time * times) >= time) {
				// Intervall abgelaufen
				if (noIncomingEvent) {
					// ABSENCE-Pattern erkannt
					Tuple<T> complexEvent = createComplexEvent(null, null, pointInTime.getTime());
					outputTransferArea.transfer(complexEvent);
				}
				times++;
				noIncomingEvent = true;
			}
		}
	}
	
}
