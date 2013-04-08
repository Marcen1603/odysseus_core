package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.List;
import java.util.Map;

import model.AllPatternDataStructure;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;

/**
 * @author Michael Falk
 * @param <R>
 */
public class PatternMatchingPO<R extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<R, Tuple<? extends ITimeInterval>> implements IHasPredicate, IProcessInternal<R> {
	
	private IPredicate<? super R> predicate;
	private String type;
	private List<String> eventTypes;
	private Map<Integer, String> inputTypeNames;

	protected IInputStreamSyncArea<R> inputStreamSyncArea;
	protected ITransferArea<R, R> outputTransferArea;
	
	// ALL-Pattern
	//private DefaultTISweepArea<IStreamObject<? extends ITimeInterval>>
	private AllPatternDataStructure allPattern;
	
	
	public PatternMatchingPO(String type, List<String> eventTypes, IPredicate<? super R> predicate,
			Map<Integer, String> inputTypeNames, IInputStreamSyncArea<R> inputStreamSyncArea) {
        super();
        this.type = type;
        this.eventTypes = eventTypes;
        this.predicate = predicate;
        this.inputTypeNames = inputTypeNames;
        this.inputStreamSyncArea = inputStreamSyncArea;
        this.allPattern = new AllPatternDataStructure(this.eventTypes);
    }
	
	// Copy-Konstruktor
    public PatternMatchingPO(PatternMatchingPO<R> patternPO) {
        this.type = patternPO.type;
        this.eventTypes = patternPO.eventTypes;
        this.predicate = patternPO.predicate;
        this.inputTypeNames = patternPO.inputTypeNames;
        this.inputStreamSyncArea = patternPO.inputStreamSyncArea;
        this.allPattern = patternPO.allPattern;
    }
	
	@Override
	public String toString(){
		return super.toString() + " type: " + type + " eventTypes: " + eventTypes.toString() + " predicate: " + predicate.toString(); 
	}
	
	@Override
	public IPredicate<? super R> getPredicate() {
		return predicate;
	}
    
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public AbstractPipe<R,Tuple<? extends ITimeInterval>> clone() {
		return new PatternMatchingPO<R>(this);
	}
	
	@Override
    public void process_open() throws OpenFailedException {
        super.process_open();
        inputStreamSyncArea.init(this);
        predicate.init();
    }
     
	
	@Override
	protected void process_next(R event, int port) {
		inputStreamSyncArea.newElement(event, port);
	}

	@Override
	public void process_internal(R event, int port) {
		String eventType = inputTypeNames.get(port);
		if (eventType == null) {
			throw new InvalidEventException("Der Datentyp des Events ist null!");
		}
		switch(type) {
			// Logische Pattern
			case "ANY":
				if (eventTypes.contains(eventType) && predicate.evaluate(event)) {
					// ANY-Pattern erkannt
					this.transfer(this.createComplexEvent(type));
					// TODO: Komplexes Event erzeugen
				}
			case "ALL":
				// TODO: Prädikat evaluieren
				boolean detected = allPattern.startMatching(eventType);
				if (detected)
					this.transfer(this.createComplexEvent(type));
			case "ABSENCE":
			// Threshold Pattern
			case "COUNT":
			case "VALUE MAX":
			case "VALUE MIN:":
			case "FUNCTOR":
			// Subset Selection Pattern
			case "RELATIVE N HIGHEST":
			case "RELATIVE N LOWEST":
			// Modale Pattern
			case "ALWAYS":
			case "SOMETIMES":
			// Temporal Order Pattern
			case "SEQUENCE":
			case "FIRST N PATTERN":
			case "LAST N PATTERN":
		}	

	}

	@Override
	public void process_punctuation_intern(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);	
	}

	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Erzeugt ein komplexes Event.
	 */
	private Tuple<? extends ITimeInterval> createComplexEvent(String type) {
		Object[] attributes = new Object[2];
		attributes[0] = type;
		attributes[1] = true;
		Tuple<? extends ITimeInterval> returnEvent = new Tuple<ITimeInterval>(attributes, false);
		return returnEvent;
	}

}
