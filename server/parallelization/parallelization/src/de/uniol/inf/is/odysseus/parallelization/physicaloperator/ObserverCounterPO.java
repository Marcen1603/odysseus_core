package de.uniol.inf.is.odysseus.parallelization.physicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class ObserverCounterPO<T extends IStreamObject<?>> extends AbstractPipe<T,T>{

	private Integer counter = 0;
	private Integer numberOfElements;
	private ObserverCounterPOHelper helper;
	

	public ObserverCounterPO(Integer numberOfElements, List<Observer> observers)  {
        super();
		this.numberOfElements = numberOfElements;
		this.helper = new ObserverCounterPOHelper(observers);
    }
 
    public ObserverCounterPO(ObserverCounterPO<T> observerCounterPO) {
        super();
        this.helper = new ObserverCounterPOHelper(observerCounterPO.helper);
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
		counter++;
		if (counter == numberOfElements){
			helper.updateObservers();
		}
	}

	class ObserverCounterPOHelper extends Observable{
		private List<Observer> observers;
		
		public ObserverCounterPOHelper(List<Observer> observers){
			this.observers = observers;			
		}
		
		public ObserverCounterPOHelper(ObserverCounterPOHelper otherHelper){
			super();
			this.observers = new ArrayList<Observer>(otherHelper.observers);	
		}
		
		public void updateObservers(){
			for (Observer observer : observers) {
				observer.update(this, null);
			}
		}
	}
}
