package de.uniol.inf.is.odysseus.benchmark;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.benchmark.result.BenchmarkResultFactoryRegistry;
import de.uniol.inf.is.odysseus.benchmark.result.LatencyBenchmarkResultFactory;
import de.uniol.inf.is.odysseus.core.server.monitoring.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.core.server.monitoring.IncrementalDescriptiveStatistics;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		BenchmarkResultFactoryRegistry.addEntry(new LatencyBenchmarkResultFactory());
		BenchmarkResultFactoryRegistry.addStatistic("INCREMENTAL", new IncrementalDescriptiveStatistics());
		BenchmarkResultFactoryRegistry.addStatistic("ALL", new DescriptiveStatistics());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		BenchmarkResultFactoryRegistry.removeEntry(new LatencyBenchmarkResultFactory());
		BenchmarkResultFactoryRegistry.removeStatistics("INCREMENTAL");
		BenchmarkResultFactoryRegistry.removeStatistics("ALL");
	}

}
