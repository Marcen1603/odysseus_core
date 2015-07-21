/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.benchmarker.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.uniol.inf.is.odysseus.args.Args;
import de.uniol.inf.is.odysseus.args.Args.REQUIREMENT;
import de.uniol.inf.is.odysseus.args.ArgsException;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmark;
import de.uniol.inf.is.odysseus.benchmark.result.IBenchmarkResult;

/**
 * This class controls all aspects of the application's execution
 */
public class OdysseusBenchmarkRunner implements IApplication {

	private static final String DEFAULT_OUT_FILE = "result.xml";
	private static final int DEFAULT_WAIT = 0;
	private static final Integer DEFAULT_WAIT_CONFIG = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(
				IApplicationContext.APPLICATION_ARGS);
		Args arguments = new Args();
		BundleContext ctx = Activator.getDefault().getBundle()
				.getBundleContext();

		try {
			initArgs(arguments, args);

			@SuppressWarnings({ "rawtypes", "unchecked" })
			ServiceTracker t = new ServiceTracker(ctx,
					IBenchmark.class.getName(), null);
			t.open();
			int wait = arguments.hasParameter(WAIT) ? arguments
					.getInteger(WAIT) : DEFAULT_WAIT;
			IBenchmark benchmark = (IBenchmark) t.waitForService(wait);
			if (benchmark == null) {
				throw new Exception("cannot find benchmark service");
			}

			int waitConfig = arguments.hasParameter(WAIT_CONFIG) ? arguments
					.getInteger(WAIT_CONFIG) : DEFAULT_WAIT_CONFIG;

			Thread.sleep(waitConfig);

			configureBenchmark(benchmark, arguments);

			Collection<IBenchmarkResult<Object>> results = benchmark
					.runBenchmark();
			String filename = DEFAULT_OUT_FILE;
			if (arguments.hasParameter(OUT)) {
				filename = arguments.get(OUT);
			}

			Serializer serializer = new Persister();
			File file = new File(filename);
			
			FileOutputStream oStream = new FileOutputStream(file);
			oStream.write("<benchmark>\n".getBytes());
			 for (IBenchmarkResult<?> result : results) {
				 serializer.write(result, oStream);
			 }
			oStream.write("\n</benchmark>\n".getBytes());
			oStream.close();

			if ((boolean)arguments.get(MEMORY_USAGE)) {
				String memFile = filename.replaceAll(".xml", "_memory.xml");
				serializer.write(benchmark.getMemUsageStatistics(), new File(
						memFile));
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

		Thread.sleep(100000);
		return IApplication.EXIT_OK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		// nothing to do
	}

	private static final String SCHEDULER = "-scheduler";
	private static final String SCHEDULING_STRATEGY = "-scheduling_strategy";
	private static final String BUFFER_PLACEMENT = "-buffer_placement";
	private static final String DATA_TYPE = "-data_type";
	private static final String METADATA_TYPES = "-metadata_types";
	private static final String QUERY_LANGUAGE = "-query_language";
	private static final String QUERY = "-query";
	private static final String MAX_RESULTS = "-max_results";
	private static final String PRIORITY = "-priority";
	private static final String PUNCTUATIONS = "-punctuations";
	private static final String LOAD_SHEDDING = "-load_shedding";
	private static final String EXTENDED_POSTPRIORISATION = "-extPostPOs";
	private static final String OUT = "-out";
	private static final String WAIT = "-wait";
	private static final String MEMORY_USAGE = "-memUsage";
	private static final String NO_METADATA = "-no_metadata";
	private static final String WAIT_CONFIG = "-wait_config";
	private static final String RESULT_PER_QUERY = "-result_per_query";
	private static final String INPUT_FILE = "-input_file";

	// private static Logger logger =
	// LoggerFactory.getLogger(BenchmarkStarter.class);

	private static void configureBenchmark(IBenchmark benchmark, Args arguments) {
		String query = null;
		if (arguments.hasParameter(INPUT_FILE)) {
			String filename = arguments.getString(INPUT_FILE);
			try {
				FileReader fr = new FileReader(new File(filename));
				BufferedReader br = new BufferedReader(fr);
				String tmp = br.readLine();
				while(tmp != null) {
					query += tmp+"\n";
					tmp = br.readLine();
				}
				br.close();
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			if (arguments.hasParameter(QUERY)){
				query = arguments.get(QUERY);
			} else {
				throw new RuntimeException("missing input parameter -query or -input_file");
			}
		}
		
		if (arguments.hasParameter(SCHEDULER)) {
			String scheduler = arguments.get(SCHEDULER);
			benchmark.setScheduler(scheduler);
		}

		if (arguments.hasParameter(SCHEDULING_STRATEGY)) {
			String schedulingStrategy = arguments.get(SCHEDULING_STRATEGY);
			benchmark.setSchedulingStrategy(schedulingStrategy);
		}

		if (arguments.hasParameter(BUFFER_PLACEMENT)) {
			String bufferPlacement = arguments.get(BUFFER_PLACEMENT);
			benchmark.setBufferPlacementStrategy(bufferPlacement);
		}

		String[] metaTypes = benchmark.getMetadataTypes();
		if (arguments.hasParameter(METADATA_TYPES)) {
			String metaTypesStr = arguments.get(METADATA_TYPES);
			metaTypes = metaTypesStr.split(":");
		}

		if ((boolean)arguments.get(PRIORITY)) {
			metaTypes = Arrays.copyOf(metaTypes, metaTypes.length + 1);
			metaTypes[metaTypes.length - 1] = "de.uniol.inf.is.odysseus.priority.IPriority";
			benchmark
					.setResultFactory("de.uniol.inf.is.odysseus.benchmarker.impl.PriorityBenchmarkResultFactory");
		}

		benchmark.setMetadataTypes(metaTypes);

		benchmark.setNoMetadataCreation(arguments.getBoolean(NO_METADATA));

		if ((boolean)arguments.get(PUNCTUATIONS)) {
			benchmark.setUsePunctuations(true);
		}

		if ((boolean)arguments.get(EXTENDED_POSTPRIORISATION)) {
			benchmark.setExtendedPostPriorisation(true);
		}

		if ((boolean)arguments.get(LOAD_SHEDDING)) {
			benchmark.setUseLoadShedding(true);
		}

		if (arguments.hasParameter(MAX_RESULTS)) {
			Long maxResults = arguments.getLong(MAX_RESULTS);
			benchmark.setMaxResults(maxResults);
		}

		if ((boolean)arguments.get(MEMORY_USAGE)) {
			benchmark.setBenchmarkMemUsage(true);
		}

		if ((boolean)arguments.get(RESULT_PER_QUERY)) {
			benchmark.setResultPerQuery(true);
		}

		String queryLanguage = arguments.get(QUERY_LANGUAGE);
		

		benchmark.addQuery(queryLanguage, query);
	}

	private static void initArgs(Args arguments, String[] args)
			throws ArgsException {
		arguments.addString(INPUT_FILE, REQUIREMENT.OPTIONAL, "<filename> - loads query from file <filename>");
		
		arguments.addString(SCHEDULER, REQUIREMENT.OPTIONAL,
				"<scheduler> - sets the scheduler");
		arguments.addString(SCHEDULING_STRATEGY, REQUIREMENT.OPTIONAL,
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
				.addString(QUERY, REQUIREMENT.OPTIONAL,
						"<query> - query string. may contain multiple queries in one string.");
		arguments
				.addBoolean(
						PRIORITY,
						" - enables tracking of latencies of prioritized elements, automatically adds 'de.uniol.inf.is.odysseus.priority.IPriority' to metadata types");

		arguments.addBoolean(PUNCTUATIONS, " - enables usage of punctuations");

		arguments
				.addBoolean(LOAD_SHEDDING, " - enables usage of load shedding");

		arguments.addBoolean(EXTENDED_POSTPRIORISATION,
				" - enables extended post priorisation");

		arguments.addBoolean(MEMORY_USAGE,
				" - activates the listener to benchmark memory usage");

		arguments
				.addString(OUT, REQUIREMENT.OPTIONAL,
						"<filename> - writes results in file <filename> (default=result.xml)");

		arguments
				.addInteger(
						WAIT,
						REQUIREMENT.OPTIONAL,
						"<time in ms> - wait for time ms for the benchmarker to become available (default is infinite waiting)");
		arguments
				.addBoolean(NO_METADATA, " - don't create MetadataCreationPOs");

		arguments
				.addInteger(
						WAIT_CONFIG,
						REQUIREMENT.OPTIONAL,
						"<time in ms> - add waiting time before the benchmarker gets configured, so optional services can be loaded (default is 0).");

		arguments.addBoolean(RESULT_PER_QUERY,
				" - enable separate measurements for every query");
		arguments.parse(args);
	}
	
	public static void main(String[] args) {
		
	}
}
