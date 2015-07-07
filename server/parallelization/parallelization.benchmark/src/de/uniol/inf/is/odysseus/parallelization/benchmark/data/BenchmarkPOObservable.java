package de.uniol.inf.is.odysseus.parallelization.benchmark.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;


public class BenchmarkPOObservable<T extends IStreamObject<?>> extends Observable{
	private List<IBenchmarkEvaluation> evaluations = new ArrayList<IBenchmarkEvaluation>();
	
	public BenchmarkPOObservable(){
		BenchmarkObserverRegistry registry = BenchmarkObserverRegistry.getInstance();
		for (IBenchmarkObserver benchmarkObserver : registry.getObserverMap().values()) {
			evaluations.add(benchmarkObserver.getBenchmarkEvaluation());
		}
	}
	
	public void evaluate(T object){
		for (IBenchmarkEvaluation benchmarkEvaluation : evaluations) {
			benchmarkEvaluation.evaluate(this, object);
		}
	}
	
	public void evaluationDone(){
		for (IBenchmarkEvaluation benchmarkEvaluation : evaluations) {
			benchmarkEvaluation.evaluationDone(this);
		}
	}
}
