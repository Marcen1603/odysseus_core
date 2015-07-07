package de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.parallelization.benchmark.data.BenchmarkPOObservable;

public class ObserverBenchmarkPO<T extends IStreamObject<?>> extends AbstractPipe<T,T>{

	private BenchmarkPOObservable<T> delegate;
	

	public ObserverBenchmarkPO()  {
        super();
		this.delegate = new BenchmarkPOObservable<T>();
    }
 
    public ObserverBenchmarkPO(ObserverBenchmarkPO<T> observerCounterPO) {
        super();
        this.delegate = new BenchmarkPOObservable<T>();
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
		delegate.evaluate(object);
		transfer(object);
	}
	
	@Override
	protected void process_done() {
		
	}

	
}
