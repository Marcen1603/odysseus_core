package de.uniol.inf.is.odysseus.benchmarker.runner;

import java.io.File;
import java.util.Arrays;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.uniol.inf.is.odysseus.args.Args;
import de.uniol.inf.is.odysseus.args.ArgsException;
import de.uniol.inf.is.odysseus.args.Args.REQUIREMENT;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmark;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;

/**
 * This class controls all aspects of the application's execution
 */
public class OdysseusBenchmarkRunner implements IApplication {

	private static final String DEFAULT_OUT_FILE = "result.xml";
	private static final int DEFAULT_WAIT = 0;
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		Args arguments = new Args();
		BundleContext ctx = Activator.getDefault().getBundle().getBundleContext();
		try {
			initArgs(arguments, args);

			ServiceTracker t = new ServiceTracker(ctx, IBenchmark.class
					.getName(), null);
			t.open();
			int wait = arguments.hasParameter(WAIT) ? arguments.getInteger(WAIT) : DEFAULT_WAIT;
			IBenchmark benchmark = (IBenchmark) t.waitForService(wait);
			if (benchmark == null) {
				throw new Exception("cannot find benchmark service");
			}

			configureBenchmark(benchmark, arguments);

			IBenchmarkResult<?> result = benchmark.runBenchmark();
			String filename = DEFAULT_OUT_FILE;
			if (arguments.hasParameter(OUT)){
				filename = arguments.get(OUT);
			}
			
			Serializer serializer = new Persister();
			serializer.write(result, new File(filename));
			
			if(arguments.hasParameter(MEMORY_USAGE)) {
				for(int i=0; i < benchmark.getMemUsageJoin().size();i++) {
					serializer.write(benchmark.getMemUsageJoin().get(i), new File(i + "JOIN" + filename));
				}
			/*	for(int i=0; i < benchmark.getMemUsageJoinPunctuations().size();i++) {
					serializer.write(benchmark.getMemUsageJoinPunctuations().get(i), new File(i + "JOINPUNC" + filename));
				}
				for(int i=0; i < benchmark.getMemUsagePuffer().size();i++) {
					serializer.write(benchmark.getMemUsagePuffer().get(i), new File(i + "BUFFER" + filename));
				}*/
			}			
			
		} catch (ArgsException e) {
			e.printStackTrace();
			System.out.println("usage:\n");
			System.out.println(arguments.getHelpText());
			return -1;
		} catch (Throwable t) {
			t.printStackTrace();
			return -1;
		} 

		return IApplication.EXIT_OK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		// nothing to do
	}

	private static final String SCHEDULER = "-scheduler";
	private static final String SCHEDULING_STRAGEGY = "-scheduling_stragegy";
	private static final String BUFFER_PLACEMENT = "-buffer_placement";
	private static final String DATA_TYPE = "-data_type";
	private static final String METADATA_TYPES = "-metadata_types";
	private static final String QUERY_LANGUAGE = "-query_language";
	private static final String QUERY = "-query";
	private static final String MAX_RESULTS = "-max_results";
	private static final String PRIORITY = "-priority";
	private static final String PUNCTUATIONS = "-punctuations";
	private static final String LOAD_SHEDDING = "-load_shedding";
	private static final String OUT = "-out";
	private static final String WAIT = "-wait";
	private static final String MEMORY_USAGE = "-memUsage";

	// private static Logger logger =
	// LoggerFactory.getLogger(BenchmarkStarter.class);

	private static void configureBenchmark(IBenchmark benchmark, Args arguments) {
		if (arguments.hasParameter(SCHEDULER)) {
			String scheduler = arguments.get(SCHEDULER);
			benchmark.setScheduler(scheduler);
		}

		if (arguments.hasParameter(SCHEDULING_STRAGEGY)) {
			String schedulingStrategy = arguments.get(SCHEDULING_STRAGEGY);
			benchmark.setSchedulingStrategy(schedulingStrategy);
		}

		if (arguments.hasParameter(BUFFER_PLACEMENT)) {
			String bufferPlacement = arguments.get(BUFFER_PLACEMENT);
			benchmark.setBufferPlacementStrategy(bufferPlacement);
		}

		if (arguments.hasParameter(DATA_TYPE)) {
			String dataType = arguments.get(DATA_TYPE);
			benchmark.setDataType(dataType);
		}

		String[] metaTypes = benchmark.getMetadataTypes();
		if (arguments.hasParameter(METADATA_TYPES)) {
			String metaTypesStr = arguments.get(METADATA_TYPES);
			metaTypes = metaTypesStr.split(":");
		}

		if (arguments.get(PRIORITY)) {
			metaTypes = Arrays.copyOf(metaTypes, metaTypes.length + 1);
			metaTypes[metaTypes.length - 1] = "de.uniol.inf.is.odysseus.priority.IPriority";
			benchmark
					.setResultFactory("de.uniol.inf.is.odysseus.benchmarker.impl.PriorityBenchmarkResultFactory");
		}

		benchmark.setMetadataTypes(metaTypes);

		if (arguments.get(PUNCTUATIONS)) {
			benchmark.setUsePunctuations(true);
		}

		if (arguments.get(LOAD_SHEDDING)) {
			benchmark.setUseLoadShedding(true);
		}

		if (arguments.hasParameter(MAX_RESULTS)) {
			Long maxResults = arguments.getLong(MAX_RESULTS);
			benchmark.setMaxResults(maxResults);
		}
		
		if(arguments.get(MEMORY_USAGE)) {
			benchmark.setBenchmarkMemUsage(true);
		}

		String queryLanguage = arguments.get(QUERY_LANGUAGE);
		String query = arguments.get(QUERY);

		benchmark.addQuery(queryLanguage, query);
	}

	private static void initArgs(Args arguments, String[] args)
			throws ArgsException {
		arguments.addString(SCHEDULER, REQUIREMENT.OPTIONAL,
				"<scheduler> - sets the scheduler");
		arguments.addString(SCHEDULING_STRAGEGY, REQUIREMENT.OPTIONAL,
				"<scheduling strategy> - sets the scheduling strategy");
		arguments.addString(BUFFER_PLACEMENT, REQUIREMENT.OPTIONAL,
				"<buffer strategy> - sets the buffer placement strategy");
		arguments.addString(DATA_TYPE, REQUIREMENT.OPTIONAL,
				"<data type> - sets the data type (default=relational)");
		arguments
				.addString(
						METADATA_TYPES,
						REQUIREMENT.OPTIONAL,
						"<metadata types> - ':' separated list of metadata types (default=de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval)");
		arguments
				.addLong(
						MAX_RESULTS,
						REQUIREMENT.OPTIONAL,
						"<int> - stop processing if stream ends or <int> results were produced by the queries");
		arguments.addString(QUERY_LANGUAGE, REQUIREMENT.MANDATORY,
				"<language> - query language");
		arguments
				.addString(QUERY, REQUIREMENT.MANDATORY,
						"<query> - query string. may contain multiple queries in one string.");
		arguments
				.addBoolean(
						PRIORITY,
						" - enables tracking of latencies of prioritized elements, automatically adds 'de.uniol.inf.is.odysseus.priority.IPriority' to metadata types");

		arguments.addBoolean(PUNCTUATIONS, " - enables usage of punctuations");

		arguments
				.addBoolean(LOAD_SHEDDING, " - enables usage of load shedding");
		
		arguments.addBoolean(MEMORY_USAGE, " - activates the listener to benchmark memory usage");		

		arguments
				.addString(OUT, REQUIREMENT.OPTIONAL,
						"<filename> - writes results in file <filename> (default=result.xml)");

		arguments.addInteger(WAIT, REQUIREMENT.OPTIONAL, "<time in ms> - wait for time ms for the benchmarker to become available (default is infinite waiting)");
		arguments.parse(args);
	}
}
