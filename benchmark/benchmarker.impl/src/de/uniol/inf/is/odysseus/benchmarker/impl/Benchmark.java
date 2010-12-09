package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.benchmarker.BenchmarkException;
import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmark;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;
import de.uniol.inf.is.odysseus.collection.Pair;
import de.uniol.inf.is.odysseus.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.LatencyCalculationPipe;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParser;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class Benchmark implements IErrorEventListener, IBenchmark {
	private static final String SCRIPT_PARSER = "SCRIPT";
	private long maxResults;
	private String scheduler;
	private String schedulingStrategy;
	private String bufferPlacement;
	private String dataType;
	private IBenchmarkResultFactory<ILatency> resultFactory;
	private String[] metadataTypes;
	private ArrayList<IQueryBuildSetting<?>> buildParameters;
	private List<Pair<String, String>> queries;
	private boolean usePunctuations;
	private boolean useLoadShedding;
	private boolean extendedPostPriorisation = false;
	private boolean useBenchmarkMemUsage = false;

	private User user = UserManagement.getInstance().getSuperUser();

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(Benchmark.class);

	private AtomicReference<ErrorEvent> error = new AtomicReference<ErrorEvent>();
	private IExecutor executor;
	private AvgBenchmarkMemUsageListener avgMemListener = null;

	private boolean noMetadataCreation;
	private boolean resultPerQuery = false;
	private ArrayList<BenchmarkSink<ILatency>> sinks = new ArrayList<BenchmarkSink<ILatency>>();

	public Benchmark() {
		this.dataType = "relational";
		this.maxResults = -1;
		this.buildParameters = null;
		this.queries = new ArrayList<Pair<String, String>>();
		this.buildParameters = new ArrayList<IQueryBuildSetting<?>>();
		this.metadataTypes = new String[] { ITimeInterval.class.getName(),
				ILatency.class.getName() };
		this.resultFactory = new LatencyBenchmarkResultFactory();
		this.usePunctuations = false;
		this.useLoadShedding = false;
	}

	public void activate(ComponentContext c) {
		scheduler = executor.getCurrentSchedulerID();
		schedulingStrategy = executor.getCurrentSchedulingStrategyID();
		bufferPlacement = "Standard Buffer Placement";
	}

	@Override
	public void addQuery(String language, String query) {
		this.queries.add(new Pair<String, String>(language, query));
	}

	public List<Pair<String, String>> getQueries() {
		return this.queries;
	}

	public void addBuildParameters(IQueryBuildSetting<?>... parameters) {
		for (IQueryBuildSetting<?> parameter : parameters) {
			this.buildParameters.add(parameter);
		}
	}

	@Override
	public void setMetadataTypes(String... types) {
		this.metadataTypes = Arrays.copyOf(types, types.length + 1);
		this.metadataTypes[metadataTypes.length - 1] = ILatency.class.getName();
	}

	@Override
	public String[] getMetadataTypes() {
		return metadataTypes;
	}

	public List<IQueryBuildSetting<?>> getBuildParameters() {
		return this.buildParameters;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<IBenchmarkResult<ILatency>> runBenchmark()
			throws BenchmarkException {

		createNexmarkSources();

		IBenchmarkResult<ILatency> result = resultFactory
				.createBenchmarkResult();
		BenchmarkSink<ILatency> sink = new BenchmarkSink<ILatency>(result,
				maxResults);
		LatencyCalculationPipe latency = new LatencyCalculationPipe<IMetaAttributeContainer<? extends ILatency>>();
		latency.subscribeSink(sink, 0, 0, latency.getOutputSchema());

		ISink sinkPO = latency;
		if (!resultPerQuery) {
			IntegrationPipe integration = new IntegrationPipe();
			integration.subscribeSink(latency, 0, 0, latency.getOutputSchema());
			sinkPO = integration;
			this.sinks.add(sink);
		}

		TransformationConfiguration trafoConfig = new TransformationConfiguration(
				dataType, getMetadataTypes());
		trafoConfig.setOption("usePunctuations", this.usePunctuations);
		trafoConfig.setOption("useLoadShedding", this.useLoadShedding);
		trafoConfig.setOption("useExtendedPostPriorisation",
				this.extendedPostPriorisation);
		if (noMetadataCreation) {
			trafoConfig.setOption("NO_METADATA", true);
		}

		try {
			synchronized (this) {
				wait(1000);
			}

			// executor.setDefaultBufferPlacementStrategy(bufferPlacement);
			executor.setScheduler(scheduler, schedulingStrategy);
			if (useBenchmarkMemUsage) {
				this.avgMemListener = new AvgBenchmarkMemUsageListener();
				executor.addPlanExecutionListener(avgMemListener);
			}

			executor.addErrorEventListener(this);

			ArrayList<IQueryBuildSetting<?>> parameters = new ArrayList<IQueryBuildSetting<?>>();
			parameters
					.add(new ParameterTransformationConfiguration(trafoConfig));
			parameters.addAll(getBuildParameters());
			parameters
					.add(new ParameterBufferPlacementStrategy(bufferPlacement));

			for (Pair<String, String> query : getQueries()) {
				String parserId = query.getE1();
				String queryString = query.getE2();
				if (parserId.equalsIgnoreCase(SCRIPT_PARSER)) {
					try {
						QueryTextParser.getInstance().parseAndExecute(
								queryString);
					} catch (QueryTextParseException e) {
						throw new BenchmarkException(e);
					}
				} else {
					executor.addQuery(queryString, parserId, user,
							parameters.toArray(new IQueryBuildSetting[0]));
				}
			}
			int i = 0;
			for (IPhysicalOperator curRoot : executor.getExecutionPlan()
					.getRoots()) {
				ISource<?> source = (ISource<?>) curRoot;
				if (this.resultPerQuery) {
					result = resultFactory.createBenchmarkResult();
					result.setQueryId(getQueryId(curRoot));
					sink = new BenchmarkSink<ILatency>(result, maxResults);
					sinkPO = new LatencyCalculationPipe<IMetaAttributeContainer<? extends ILatency>>();
					latency.subscribeSink(sink, 0, 0, latency.getOutputSchema());
					this.sinks.add(sink);
				}

				source.subscribeSink(sinkPO, i++, 0, source.getOutputSchema());
			}
			for (ISink<?> curSink : this.sinks) {
				try {
					curSink.open();
				} catch (OpenFailedException e) {
					throw new BenchmarkException(e);
				}
			}
			List<IBenchmarkResult<ILatency>> results = new ArrayList<IBenchmarkResult<ILatency>>();

			long startTime = System.nanoTime();
			// result.setStartTime(System.nanoTime());
			executor.startExecution();
			for (BenchmarkSink<ILatency> curSink : this.sinks) {
				result = curSink.waitForResult();
				result.setStartTime(startTime);
				results.add(result);
			}
			executor.stopExecution();
			ErrorEvent errorEvent = this.error.get();
			if (errorEvent != null) {
				throw new BenchmarkException(errorEvent.getValue());
			}
			return results;
		} catch (PlanManagementException e) {
			throw new BenchmarkException(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new BenchmarkException(e);
		}
	}

	private int getQueryId(IPhysicalOperator curRoot) {
		for (IQuery q : this.executor.getQueries()) {
			if (q.getRoots().contains(curRoot)) {
				return q.getID();
			}
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	private void createNexmarkSources() {
		String[] q = new String[4];
		q[0] = "CREATE STREAM nexmark:person2 (timestamp STARTTIMESTAMP,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440";
		q[1] = "CREATE STREAM nexmark:bid2 (timestamp STARTTIMESTAMP,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442";
		q[2] = "CREATE STREAM nexmark:auction2 (timestamp STARTTIMESTAMP,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441";
		q[3] = "CREATE STREAM nexmark:category2 (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65443";
		for (String s : q) {
			try {
				this.executor.addQuery(s, "CQL", user,
						new ParameterTransformationConfiguration(
								new TransformationConfiguration("relational",
										ITimeInterval.class)));
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void errorEventOccured(ErrorEvent eventArgs) {
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

	public void bindExecutor(IExecutor executor) {
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
	public DescriptiveStatistics getMemUsageStatistics() {
		return this.avgMemListener.getMemUsageStatistics();
	}

	@Override
	public void setExtendedPostPriorisation(boolean b) {
		this.extendedPostPriorisation = b;
	}

	@Override
	public void setNoMetadataCreation(boolean b) {
		this.noMetadataCreation = b;
	}

	@Override
	public void setResultPerQuery(boolean b) {
		this.resultPerQuery = b;
	}

}
