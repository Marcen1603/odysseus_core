package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.benchmarker.AbstractBenchmarkResult;
import de.uniol.inf.is.odysseus.metadata.ILatency;

public class LatencyBenchmarkResult extends AbstractBenchmarkResult<ILatency> {

	@Override
	public void add(ILatency object) {
		super.add(object);
		getStatistics().addValue(object.getLatency());
	}
}
