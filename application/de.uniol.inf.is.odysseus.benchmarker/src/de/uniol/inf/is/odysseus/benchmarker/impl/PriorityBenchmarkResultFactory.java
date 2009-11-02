package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.benchmarker.AbstractBenchmarkResult;
import de.uniol.inf.is.odysseus.latency.ILatency;

public class PriorityBenchmarkResultFactory implements IBenchmarkResultFactory {

	@SuppressWarnings("unchecked")
	@Override
	public AbstractBenchmarkResult<? extends ILatency> createBenchmarkResult() {
		return new PriorityBenchmarkResult();
	}

}
