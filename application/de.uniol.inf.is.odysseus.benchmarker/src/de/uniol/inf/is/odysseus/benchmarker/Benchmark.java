package de.uniol.inf.is.odysseus.benchmarker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.latency.LatencyCalculationPipe;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

public class Benchmark implements IBenchmark {
	private static Logger logger = LoggerFactory.getLogger(Benchmark.class);
	IAdvancedExecutor executor;

	protected void bindExecutor(IAdvancedExecutor executor) {
		this.executor = executor;
	}

	@SuppressWarnings("unchecked")
	public BenchmarkResult runBenchmark(String query, String language,
			Configuration config) throws BenchmarkException {
		this.executor.setScheduler(config.scheduler, config.schedulingStrategy);
		BenchmarkResult result = new BenchmarkResult();
		MySink sink = new MySink(result, config.maxResults);
		LatencyCalculationPipe latency = new LatencyCalculationPipe<IMetaAttributeContainer<? extends ILatency>>();
		latency.subscribe(sink, 0);

		TransformationConfiguration trafoConfig = new TransformationConfiguration(
				"relational", ITimeInterval.class.getName(), ILatency.class
						.getName());
		try {
			this.executor
					.setDefaultBufferPlacementStrategy(config.bufferPlacement);

			ArrayList<AbstractQueryBuildParameter<?>> parameters = new ArrayList<AbstractQueryBuildParameter<?>>();
			parameters
					.add(new ParameterTransformationConfiguration(trafoConfig));
			parameters.add(new ParameterDefaultRoot(latency));
			if (config.buildParameters != null) {
				parameters.addAll(Arrays.asList(config.buildParameters));
			}

			Collection<Integer> queryIds = this.executor.addQuery(query,
					config.language, parameters
							.toArray(new AbstractQueryBuildParameter<?>[0]));
			result.setStartTime(System.nanoTime());
			this.executor.startExecution();
			result = sink.waitForResult();
			result.setEndTime(System.nanoTime());
			this.executor.stopExecution();
		} catch (PlanManagementException e) {
			throw new BenchmarkException(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
}
