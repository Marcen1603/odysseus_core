package de.uniol.inf.is.odysseus.parallelization.benchmark.data;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public interface IBenchmarkEvaluation {
	
	<T extends IStreamObject<?>> void evaluate(BenchmarkPOObservable<?> observable, T object);
	
	void evaluationDone(BenchmarkPOObservable<?> observable);
}
