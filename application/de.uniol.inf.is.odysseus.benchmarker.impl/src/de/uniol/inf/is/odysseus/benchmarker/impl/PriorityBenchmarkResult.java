package de.uniol.inf.is.odysseus.benchmarker.impl;

import org.simpleframework.xml.Element;

import de.uniol.inf.is.odysseus.benchmarker.AbstractBenchmarkResult;
import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityBenchmarkResult<T extends ILatency & IPriority> extends
		AbstractBenchmarkResult<T> {
	@Element
	private DescriptiveStatistics prioritizedStats = new DescriptiveStatistics();

	public void add(T object) {
		if (object.getPriority() == 0) {
			super.add(object);
			getStatistics().addValue(object.getLatency());
		} else {
			++size;
			prioritizedStats.addValue(object.getLatency());
		}
	};

}
