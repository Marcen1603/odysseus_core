package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.io.StringWriter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import de.uniol.inf.is.odysseus.benchmarker.AbstractBenchmarkResult;
import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.priority.IPriority;

@Root(name = "priorityresult")
public class PriorityBenchmarkResult<T extends ILatency & IPriority> extends
		AbstractBenchmarkResult<T> {
	@Element(name="prioritizedStatistics")
	private DescriptiveStatistics prioritizedStats = new DescriptiveStatistics();

	@Override
	public void add(T object) {
		if (object.getPriority() == 0) {
			super.add(object);
			getStatistics().addValue(object.getLatency());
		} else {
			++size;
			prioritizedStats.addValue(object.getLatency());
		}
	};

	@Override
	public String toString() {
		Persister p = new Persister();
		StringWriter writer = new StringWriter();
		try {
			p.write(this, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return writer.toString();
	}
	
}
