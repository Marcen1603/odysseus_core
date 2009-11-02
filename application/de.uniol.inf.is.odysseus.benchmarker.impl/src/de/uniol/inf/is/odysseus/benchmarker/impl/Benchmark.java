package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.UnsortedPair;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.benchmarker.BenchmarkException;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmark;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.latency.LatencyCalculationPipe;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

public class Benchmark implements IErrorEventListener, IBenchmark {
	private long maxResults;
	private String scheduler;
	private String schedulingStrategy;
	private String bufferPlacement;
	private String dataType;
	private IBenchmarkResultFactory<ILatency> resultFactory;
	private String[] metadataTypes;
	private ArrayList<AbstractQueryBuildParameter<?>> buildParameters;
	private List<UnsortedPair<String, String>> queries;

	public Benchmark() {
		this.dataType = "relational";
		this.maxResults = -1;
		this.buildParameters = null;
		this.queries = new ArrayList<UnsortedPair<String, String>>();
		this.buildParameters = new ArrayList<AbstractQueryBuildParameter<?>>();
		this.metadataTypes = new String[] { ITimeInterval.class.getName(),
				ILatency.class.getName() };
		this.resultFactory = new LatencyBenchmarkResultFactory();
	}

	public void activate(ComponentContext c) {
		scheduler = executor.getCurrentScheduler();
		schedulingStrategy = executor.getCurrentSchedulingStrategy();
		bufferPlacement = "Standard Buffer Placement";
	}

	public void addQuery(String language, String query) {
		this.queries.add(new UnsortedPair<String, String>(language, query));
	}

	public List<UnsortedPair<String, String>> getQueries() {
		return this.queries;
	}

	public void addBuildParameters(AbstractQueryBuildParameter<?>... parameters) {
		for (AbstractQueryBuildParameter<?> parameter : parameters) {
			this.buildParameters.add(parameter);
		}
	}

	public void setMetadataTypes(String... types) {
		this.metadataTypes = Arrays.copyOf(types, types.length + 1);
		this.metadataTypes[metadataTypes.length - 1] = ILatency.class.getName();
	}

	public String[] getMetadataTypes() {
		return metadataTypes;
	}

	public List<AbstractQueryBuildParameter<?>> getBuildParameters() {
		return this.buildParameters;
	}

	private static Logger logger = LoggerFactory.getLogger(Benchmark.class);

	private AtomicReference<ErrorEvent> error;
	private IAdvancedExecutor executor;

	@SuppressWarnings("unchecked")
	@Override
	public IBenchmarkResult runBenchmark() throws BenchmarkException {
		IBenchmarkResult<ILatency> result = resultFactory
				.createBenchmarkResult();
		BenchmarkSink sink = new BenchmarkSink<ILatency>(result, maxResults);
		LatencyCalculationPipe latency = new LatencyCalculationPipe<IMetaAttributeContainer<? extends ILatency>>();
		latency.subscribe(sink, 0, 0);

		TransformationConfiguration trafoConfig = new TransformationConfiguration(
				dataType, getMetadataTypes());
		try {
			executor.setDefaultBufferPlacementStrategy(bufferPlacement);
			executor.setScheduler(scheduler, schedulingStrategy);
			executor.addErrorEventListener(this);

			ArrayList<AbstractQueryBuildParameter<?>> parameters = new ArrayList<AbstractQueryBuildParameter<?>>();
			parameters
					.add(new ParameterTransformationConfiguration(trafoConfig));
			parameters.add(new ParameterDefaultRoot(latency));
			parameters.addAll(getBuildParameters());
			for (UnsortedPair<String, String> query : getQueries()) {
				executor.addQuery(query.getE2(), query.getE1(), parameters
						.toArray(new AbstractQueryBuildParameter<?>[0]));
			}
			result.setStartTime(System.nanoTime());
			executor.startExecution();
			result = sink.waitForResult();
			result.setEndTime(System.nanoTime());
			executor.stopExecution();
			ErrorEvent errorEvent = this.error.get();
			if (errorEvent != null) {
				throw new BenchmarkException(errorEvent.getValue());
			}
		} catch (PlanManagementException e) {
			throw new BenchmarkException(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new BenchmarkException(e);
		}
		return result;
	}

	@Override
	public void sendErrorEvent(ErrorEvent eventArgs) {
		this.error.set(eventArgs);
	}

	@Override
	public void setBufferPlacementStrategy(String schedStrat) {
		this.bufferPlacement = schedStrat;
	}

	@Override
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public void setOption(String name, Object value) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setResultFactory(String className) {
		try {
			this.resultFactory = (IBenchmarkResultFactory<ILatency>) Class
					.forName(className).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void bindExecutor(IAdvancedExecutor executor) {
		this.executor = executor;
	}

	@Override
	public void setScheduler(String schedStrat) {
		this.scheduler = schedStrat;
	}

	@Override
	public void setSchedulingStrategy(String schedStrat) {
		this.schedulingStrategy = schedStrat;
	}
	
	@Override
	public void setMaxResults(long maxResults) {
		this.maxResults = maxResults;
	}

}
