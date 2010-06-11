package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.Pair;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.benchmarker.BenchmarkException;
import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
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
	private List<Pair<String, String>> queries;
	private boolean usePunctuations;
	private boolean useLoadShedding;
	private boolean extendedPostPriorisation = false;
	private boolean useBenchmarkMemUsage = false;
	
	private User user = new User("Benchmark");
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(Benchmark.class);

	private AtomicReference<ErrorEvent> error = new AtomicReference<ErrorEvent>();
	private IAdvancedExecutor executor;
	private AvgBenchmarkMemUsageListener avgMemListener = null;
	
	


	public Benchmark() {
		this.dataType = "relational";
		this.maxResults = -1;
		this.buildParameters = null;
		this.queries = new ArrayList<Pair<String, String>>();
		this.buildParameters = new ArrayList<AbstractQueryBuildParameter<?>>();
		this.metadataTypes = new String[] { ITimeInterval.class.getName(),
				ILatency.class.getName() };
		this.resultFactory = new LatencyBenchmarkResultFactory();
		this.usePunctuations = false;
		this.useLoadShedding = false;
	}

	public void activate(ComponentContext c) {
		scheduler = executor.getCurrentScheduler();
		schedulingStrategy = executor.getCurrentSchedulingStrategy();
		bufferPlacement = "Standard Buffer Placement";
	}

	public void addQuery(String language, String query) {
		this.queries.add(new Pair<String, String>(language, query));
	}

	public List<Pair<String, String>> getQueries() {
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


	@SuppressWarnings("unchecked")
	@Override
	public IBenchmarkResult runBenchmark() throws BenchmarkException {

		createNexmarkSources();

		IBenchmarkResult<ILatency> result = resultFactory
				.createBenchmarkResult();
		BenchmarkSink sink = new BenchmarkSink<ILatency>(result, maxResults);
		LatencyCalculationPipe latency = new LatencyCalculationPipe<IMetaAttributeContainer<? extends ILatency>>();
		latency.subscribeSink(sink, 0, 0, latency.getOutputSchema());

		TransformationConfiguration trafoConfig = new TransformationConfiguration(
				dataType, getMetadataTypes());
		trafoConfig.setOption("usePunctuations", this.usePunctuations);
		trafoConfig.setOption("useLoadShedding", this.useLoadShedding);
		trafoConfig.setOption("useExtendedPostPriorisation", this.extendedPostPriorisation);
		
		try {
			executor.setDefaultBufferPlacementStrategy(bufferPlacement);
			executor.setScheduler(scheduler, schedulingStrategy);
			if(useBenchmarkMemUsage) {
				this.avgMemListener  = new AvgBenchmarkMemUsageListener();
				executor.addPlanExecutionListener(avgMemListener);
			}

			executor.addErrorEventListener(this);
			

			ArrayList<AbstractQueryBuildParameter<?>> parameters = new ArrayList<AbstractQueryBuildParameter<?>>();
			parameters
					.add(new ParameterTransformationConfiguration(trafoConfig));
			parameters.add(new ParameterDefaultRoot(latency));
			parameters.addAll(getBuildParameters());
			for (Pair<String, String> query : getQueries()) {
				executor.addQuery(query.getE2(), query.getE1(), user, parameters
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

	private void createNexmarkSources() {
		String[] q = new String[4];
		q[0] = "CREATE STREAM nexmark:person2 (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440";
		q[1] = "CREATE STREAM nexmark:bid2 (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442";
		q[2] = "CREATE STREAM nexmark:auction2 (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441";
		q[3] = "CREATE STREAM nexmark:category2 (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65443";
		for (String s : q) {
			try {
				this.executor.addQuery(s, "CQL", user);
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
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

	@Override
	public void setUseLoadShedding(boolean b) {
		this.useLoadShedding = b;
	}

	@Override
	public void setUsePunctuations(boolean b) {
		this.usePunctuations = b;
	}


	@Override
	public void setBenchmarkMemUsage(boolean b) {
		this.useBenchmarkMemUsage = b;
		
	}

	@Override
	public List<DescriptiveStatistics> getMemUsageJoin() {
		if(useBenchmarkMemUsage) {
			return avgMemListener.getMemUsageJoinStatistics();
		}
		return null;
	}

	@Override
	public List<DescriptiveStatistics> getMemUsageJoinPunctuations() {
		if(useBenchmarkMemUsage) {
			return avgMemListener.getMemUsageJoinPunctuationStatistics();
		}
		return null;
	}

	@Override
	public List<DescriptiveStatistics> getMemUsagePuffer() {
		if(useBenchmarkMemUsage) {
			return avgMemListener.getMemUsageBufferStatistics();
		}
		return null;
	}
	
	@Override
	public void setExtendedPostPriorisation(boolean b) {
		this.extendedPostPriorisation = b;	
	}

}
