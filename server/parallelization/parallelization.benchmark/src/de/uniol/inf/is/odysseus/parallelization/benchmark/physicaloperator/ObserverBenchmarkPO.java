package de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class ObserverBenchmarkPO<T extends IStreamObject<?>> extends AbstractPipe<T,T>{

	private Long counter = 0l;
	private BenchmarkPOObservable helper;
	

	public ObserverBenchmarkPO()  {
        super();
		this.helper = new BenchmarkPOObservable();
    }
 
    public ObserverBenchmarkPO(ObserverBenchmarkPO<T> observerCounterPO) {
        super();
        this.helper = new BenchmarkPOObservable();
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
		if (counter < 2000){
			counter++;
			if (counter == 2000){
				helper.updateObservers();
			}			
		}
		transfer(object);
	}

	
}
