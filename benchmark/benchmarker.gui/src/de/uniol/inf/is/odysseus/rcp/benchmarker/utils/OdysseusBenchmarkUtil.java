/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.benchmarker.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.uniol.inf.is.odysseus.benchmarker.BenchmarkException;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmark;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;

public class OdysseusBenchmarkUtil {

//	private BenchmarkHolder benchmarkHolder;
	private BenchmarkGroup benchmarkGroup;
	private List<Benchmark> benchmark;
	private BenchmarkParam param;

	public OdysseusBenchmarkUtil(BenchmarkGroup benchmarkGroup) {
	//	this.benchmarkHolder = benchmarkHolder;
		this.benchmarkGroup = benchmarkGroup;
		//TODO: Auf Gruppe zugreifen!!
		benchmark = this.benchmarkGroup.getBenchmarks();
	}

	public void startrun() throws Exception {
		BundleContext ctx = de.uniol.inf.is.odysseus.rcp.benchmarker.gui.Activator.getDefault().getBundle()
				.getBundleContext();
		for (Benchmark s : benchmark) {
			// überprüfen, ob BenchmarkRun schon results hat
			if (!s.hasResults()) {
				this.param = s.getParam();
				// oder ob BenchmarkRun alle erfoderlichen Einstellungen hat
				if (param.isRunnable()) {
					System.out.println("startrun, forschleife");

					ServiceTracker t = new ServiceTracker(ctx, IBenchmark.class.getName(), null);
					t.open();
					int wait = 100;
					IBenchmark benchmark = (IBenchmark) t.waitForService(wait);
					if (benchmark == null) {
						throw new Exception("cannot find benchmark service");
					}

					int waitConfig;
					if (param.getWaitConfig() != null) {
						waitConfig = Integer.parseInt(param.getWaitConfig());
					} else {
						waitConfig = 0;
					}
					Thread.sleep(waitConfig);

					configureBenchmark(benchmark, s);
					// TODO Result-Output festlegen
					// Collection<IBenchmarkResult<Object>> results =
					// benchmark.runBenchmark();
					// String filename = DEFAULT_OUT_FILE;
					// if (arguments.hasParameter(OUT)) {
					// filename = arguments.get(OUT);
					// }
					
					final String RELATIVE_FOLDER = "benchmarks";
					String folderName = StringUtils.nameToFoldername(benchmarkGroup.getName());
					folderName = RELATIVE_FOLDER + File.separator + folderName + File.separator + s.getId();
					
					

					System.out.println("hmmm");
					try {
						System.out.println("after setParameter s, als nächsten forschleife");
						this.param = s.getParam();
						System.out.println("hm2");
						for (int i = 0; i < Integer.parseInt(param.getNumberOfRuns()); i++) {
							System.out.println("hm3");
							Collection<IBenchmarkResult<Object>> results = benchmark.runBenchmark();
							String filename = folderName + File.separator + "result"+(i+1)+".xml";//"result" + param.getName() + (i+1) + ".xml";

							Serializer serializer = new Persister();
							File file = new File(filename);

							FileOutputStream oStream = new FileOutputStream(file);
							for (IBenchmarkResult<?> result : results) {
								serializer.write(result, oStream);
							}

							if (param.isMemoryUsage()) {
								String memFile = filename.replaceAll(".xml", "_memory.xml");
								serializer.write(benchmark.getMemUsageStatistics(), new File(memFile));
							}
							System.out.println("Benchmarkrun erfolgreich");
						}
					} catch (BenchmarkException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void configureBenchmark(IBenchmark ibenchmark, Benchmark benchmark) {
		this.param = benchmark.getParam();
		String query = null;
		if (param.getInputFile() != null) {
			String filename = param.getInputFile();
			try {
				FileReader fr = new FileReader(new File(filename));
				BufferedReader br = new BufferedReader(fr);
				String tmp = br.readLine();
				while (tmp != null) {
					query += tmp + "\n";
					tmp = br.readLine();
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			if (param.getQuery() != null) {
				query = param.getQuery();
			} else {
				throw new RuntimeException("missing input parameter query or input_file");
			}
		}

		if (param.getScheduler() != null) {
			String scheduler = param.getScheduler();
			ibenchmark.setScheduler(scheduler);
		}

		if (param.getSchedulingstrategy() != null) {
			String schedulingStrategy = param.getSchedulingstrategy();
			ibenchmark.setSchedulingStrategy(schedulingStrategy);
		}

		if (param.getBufferplacement() != null) {
			String bufferPlacement = param.getBufferplacement();
			ibenchmark.setBufferPlacementStrategy(bufferPlacement);
		}

		if (param.getDataType() != null) {
			String dataType = param.getDataType();
			ibenchmark.setDataType(dataType);
		}

		String[] metaTypes = param.getMetadataCombination();
		// if (param.getMetadataCombination() != null) {
		// String metaTypesStr = param.getMetadataTypes();
		// metaTypes = metaTypesStr.split(":");
		// }

		if (param.isPriority()) {
			metaTypes = Arrays.copyOf(metaTypes, metaTypes.length + 1);
			metaTypes[metaTypes.length - 1] = "de.uniol.inf.is.odysseus.priority.IPriority";
			ibenchmark.setResultFactory("de.uniol.inf.is.odysseus.benchmarker.impl.PriorityBenchmarkResultFactory");
		}

		ibenchmark.setMetadataTypes(metaTypes);

		ibenchmark.setNoMetadataCreation(param.isNoMetada());

		if (param.isPunctuations()) {
			ibenchmark.setUsePunctuations(true);
		}

		if (param.isExtendesPostpriorisation()) {
			ibenchmark.setExtendedPostPriorisation(true);
		}

		if (param.getMaxResult() != null) {
			Long maxResults = Long.parseLong(param.getMaxResult());
			ibenchmark.setMaxResults(maxResults);
		}

		if (param.isMemoryUsage()) {
			ibenchmark.setBenchmarkMemUsage(true);
		}

		if (param.isResultPerQuery()) {
			ibenchmark.setResultPerQuery(true);
		}

		String queryLanguage = param.getQueryLanguage();

		ibenchmark.addQuery(queryLanguage, query);
	}

}