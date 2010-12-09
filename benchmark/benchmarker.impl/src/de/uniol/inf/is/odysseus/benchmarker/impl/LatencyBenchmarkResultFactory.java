package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;
import de.uniol.inf.is.odysseus.metadata.ILatency;

/**
 * @author Jonas Jacobi
 */
public class LatencyBenchmarkResultFactory implements IBenchmarkResultFactory<ILatency> {

	@Override
	public IBenchmarkResult<ILatency> createBenchmarkResult() {
		return new LatencyBenchmarkResult();
	}

}
