package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;

public class Activator implements BundleActivator {

	private static final String BENCHMARK = "Benchmark";
	private static final String BATCH_PRODUCER = "BatchProducer";
	private static final String TEST_PRODUCER = "TestProducer";
	private static final String BUFFER = "Buffer";

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putOperatorBuilderType(BATCH_PRODUCER,
				BatchProducerBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(TEST_PRODUCER,
				TestProducerPOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(BENCHMARK,
				BenchmarkBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(BUFFER,
				BufferAOBuilder.class);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removeOperatorBuilderType(BATCH_PRODUCER);
		OperatorBuilderFactory.removeOperatorBuilderType(BENCHMARK);
		OperatorBuilderFactory.removeOperatorBuilderType(TEST_PRODUCER);
		OperatorBuilderFactory.removeOperatorBuilderType(BUFFER);
	}
}
