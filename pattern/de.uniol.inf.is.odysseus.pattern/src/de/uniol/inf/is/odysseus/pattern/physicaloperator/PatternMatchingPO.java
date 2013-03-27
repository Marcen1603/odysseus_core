package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
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
 * @param <T>
 */
public class PatternMatchingPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements IHasPredicate, IProcessInternal<T> {
	
	private IPredicate<? super T> predicate;
	private String type;
	private List<String> eventTypes;

	protected IInputStreamSyncArea<T> inputStreamSyncArea;
	protected ITransferArea<T, T> outputTransferArea;
	
	public PatternMatchingPO(String type, List<String> eventTypes, IPredicate<? super T> predicate,
			IInputStreamSyncArea<T> inputStreamSyncArea) {
        super();
        initAttributes(type, eventTypes, predicate, inputStreamSyncArea);
    }
	
	// Copy-Konstruktor
    public PatternMatchingPO(PatternMatchingPO<T> patternPO) {
        this(patternPO.type, patternPO.eventTypes, patternPO.predicate, patternPO.inputStreamSyncArea);
    }
     
    private void initAttributes(String type, List<String> eventTypes, IPredicate<? super T> predicate, IInputStreamSyncArea<T> inputStreamSyncArea) {
        this.type = type;
        this.eventTypes = new ArrayList<String>();
        for (String e : eventTypes) {
            this.eventTypes.add(e);
        }
        this.predicate = predicate.clone();
        this.inputStreamSyncArea = inputStreamSyncArea.clone();
    }
	
	@Override
	public String toString(){
		return super.toString() + " type: " + type + " eventTypes: " + eventTypes.toString() + " predicate: " + predicate.toString(); 
	}
	
	@Override
	public IPredicate<? super T> getPredicate() {
		return predicate;
	}
    
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new PatternMatchingPO<T>(this);
	}
	
	@Override
    public void process_open() throws OpenFailedException {
        super.process_open();
        predicate.init();
    }
     
	
	@Override
	protected void process_next(T event, int port) {
		inputStreamSyncArea.newElement(event, port);
	}

	@Override
	public void process_internal(T event, int port) {
		// Any-Pattern
		if (type == "ANY") {
			if (predicate.evaluate(event)) {
				// ANY-Pattern erkannt
				System.out.println(event.getMetadata().getClass().getName());
				this.transfer(event);
				// TODO: Komplexes Event erzeugen
			}
		}
	}

	@Override
	public void process_punctuation_intern(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		// TODO Auto-generated method stub
		
	}

}
