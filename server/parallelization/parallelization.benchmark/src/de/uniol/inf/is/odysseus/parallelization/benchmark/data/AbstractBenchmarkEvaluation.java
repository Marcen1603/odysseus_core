package de.uniol.inf.is.odysseus.parallelization.benchmark.data;

import java.util.Observable;

public abstract class AbstractBenchmarkEvaluation implements IBenchmarkEvaluation{
	
	private IBenchmarkObserver benchmarkObserver;

	public AbstractBenchmarkEvaluation(IBenchmarkObserver benchmarkObserver){
		this.benchmarkObserver = benchmarkObserver;
	}
	
	public void updateObserver(Observable observable, Object arg){
		benchmarkObserver.update(observable, arg);
	}
}
