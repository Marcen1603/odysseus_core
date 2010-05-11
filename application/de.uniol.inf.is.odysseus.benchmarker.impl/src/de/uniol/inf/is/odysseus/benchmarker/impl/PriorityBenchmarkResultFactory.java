package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.benchmarker.AbstractBenchmarkResult;
import de.uniol.inf.is.odysseus.latency.ILatency;

@SuppressWarnings("unchecked")
public class PriorityBenchmarkResultFactory implements IBenchmarkResultFactory {

	@Override
	public AbstractBenchmarkResult<? extends ILatency> createBenchmarkResult() {
		return new PriorityBenchmarkResult();
	}

}
