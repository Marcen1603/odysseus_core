package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.parser.pql.PQLParser;

public class Activator implements BundleActivator{
	
	private static final String BENCHMARK = "Benchmark";
	private static final String BATCH_PRODUCER = "BatchProducer";
	private static final String TEST_PRODUCER = "TestProducer";
	private static final String BUFFER = "Buffer";

	@Override
	public void start(BundleContext context) throws Exception {
		PQLParser.addOperatorBuilder(BATCH_PRODUCER, new BatchProducerBuilder());
		PQLParser.addOperatorBuilder(TEST_PRODUCER, new TestProducerPOBuilder());
		PQLParser.addOperatorBuilder(BENCHMARK, new BenchmarkBuilder());
		PQLParser.addOperatorBuilder(BUFFER, new BufferAOBuilder());
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		PQLParser.removeOperatorBuilder(BATCH_PRODUCER);
		PQLParser.removeOperatorBuilder(BENCHMARK);
		PQLParser.removeOperatorBuilder(TEST_PRODUCER);
		PQLParser.removeOperatorBuilder(BUFFER);
	}
}
