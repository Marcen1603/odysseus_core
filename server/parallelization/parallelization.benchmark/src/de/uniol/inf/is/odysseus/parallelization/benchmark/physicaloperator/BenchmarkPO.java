package de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class BenchmarkPO<T extends IStreamObject<?>> extends AbstractPipe<T,T>{

	private Long counter = 0l;
	private BenchmarkPOObservable helper;
	

	public BenchmarkPO()  {
        super();
		this.helper = new BenchmarkPOObservable();
    }
 
    public BenchmarkPO(BenchmarkPO<T> observerCounterPO) {
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
		if (counter < 200){
			counter++;
			if (counter == 200){
				helper.updateObservers();
			}			
		}
	}

	
}
