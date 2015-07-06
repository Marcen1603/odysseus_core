package de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.parallelization.benchmark.data.BenchmarkPOObservable;

public class ObserverBenchmarkPO<T extends IStreamObject<?>> extends AbstractPipe<T,T>{

	private BenchmarkPOObservable<T> helper;
	

	public ObserverBenchmarkPO()  {
        super();
		this.helper = new BenchmarkPOObservable<T>();
    }
 
    public ObserverBenchmarkPO(ObserverBenchmarkPO<T> observerCounterPO) {
        super();
        this.helper = new BenchmarkPOObservable<T>();
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
		helper.evaluate(object);
		transfer(object);
	}

	
}
