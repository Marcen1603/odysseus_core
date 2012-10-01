package de.uniol.inf.is.odysseus.benchmark;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.benchmark.result.BenchmarkResultFactoryRegistry;
import de.uniol.inf.is.odysseus.benchmark.result.LatencyBenchmarkResultFactory;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		BenchmarkResultFactoryRegistry.addEntry(new LatencyBenchmarkResultFactory());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		BenchmarkResultFactoryRegistry.removeEntry(new LatencyBenchmarkResultFactory());		
	}

}
