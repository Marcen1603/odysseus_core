package de.uniol.inf.is.odysseus.parallelization.benchmark.data;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public class CountElementsBenchmarkEvaluation extends
		AbstractBenchmarkEvaluation {

	private int counter;
	private long startTimestamp;
	private long endTimestamp;
	private int numberOfElements;

	public CountElementsBenchmarkEvaluation(
			IBenchmarkObserver benchmarkObserver, int numberOfElements) {
		super(benchmarkObserver);
		this.numberOfElements = numberOfElements;
	}

	@Override
	public <T extends IStreamObject<?>> void evaluate(
			BenchmarkPOObservable<?> observable, T object) {
		if (counter > numberOfElements) {
			return;
		}

		if (counter == 0) {
			startTimestamp = System.currentTimeMillis();
		}

		if (counter < numberOfElements) {
			counter++;
			if (counter == numberOfElements) {
				endTimestamp = System.currentTimeMillis();
				long executionTime = endTimestamp - startTimestamp;
				updateObserver(observable, executionTime);
			}
		}

	}

	@Override
	public void evaluationDone(BenchmarkPOObservable<?> observable) {
		// update observer and tell them, that evaluation was done before number
		// of elements was reached
		updateObserver(observable, -1l);
	}

}
