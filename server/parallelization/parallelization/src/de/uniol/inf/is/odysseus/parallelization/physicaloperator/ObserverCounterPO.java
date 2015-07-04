package de.uniol.inf.is.odysseus.parallelization.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class ObserverCounterPO<T extends IStreamObject<?>> extends AbstractPipe<T,T>{

	private Long counter = 0l;
	private Long numberOfElements;
	private ObserverCounterPOHelper helper;
	

	public ObserverCounterPO(Long numberOfElements)  {
        super();
		this.numberOfElements = numberOfElements;
		this.helper = new ObserverCounterPOHelper();
    }
 
    public ObserverCounterPO(ObserverCounterPO<T> observerCounterPO) {
        super();
        this.helper = new ObserverCounterPOHelper();
        this.numberOfElements = observerCounterPO.numberOfElements;
        
    }
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		if (counter < numberOfElements){
			counter++;
			if (counter == numberOfElements){
				helper.updateObservers();
			}			
		}
	}

	
}
