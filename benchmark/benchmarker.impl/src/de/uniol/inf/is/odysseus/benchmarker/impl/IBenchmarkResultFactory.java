package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;

/**
 * @author Jonas Jacobi
 */
public interface IBenchmarkResultFactory<T> {
	public IBenchmarkResult<T> createBenchmarkResult();
}
