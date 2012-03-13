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

import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.uniol.inf.is.odysseus.benchmarker.BenchmarkException;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmark;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.Activator;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkMetadata;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.ProjectView;

/**
 * Diese Klasse startet/bricht den Benchmarkprozess ab
 * 
 * @author Stefanie Witzke
 * 
 */
public class OdysseusBenchmarkUtil extends Thread {

	private static OdysseusBenchmarkUtil util;
	private BenchmarkGroup benchmarkGroup;
	private List<Benchmark> benchmarks;
	private BenchmarkParam benchmarkParam;
	private volatile boolean abortProzess;

	public boolean isAbortProzess() {
		return abortProzess;
	}

	public OdysseusBenchmarkUtil(BenchmarkGroup benchmarkGroup) {
		this.benchmarkGroup = benchmarkGroup;
		benchmarks = this.benchmarkGroup.getBenchmarks();
		util = this;
		abortProzess = false;
	}

	public static OdysseusBenchmarkUtil getDefault() {
		return util;
	}

	/**
	 * Startet den Benchmarkprozess
	 */
	@Override
    public void run() {
		BundleContext ctx = de.uniol.inf.is.odysseus.rcp.benchmarker.gui.Activator.getDefault().getBundle()
				.getBundleContext();
		for (Benchmark benchmark : benchmarks) {
			// überprüfen, ob BenchmarkRun schon results hat
			if (!benchmark.hasResults()) {
				this.benchmarkParam = benchmark.getParam();
				// oder ob BenchmarkRun alle erforderlichen Einstellungen hat
				if (benchmarkParam.isRunnable() && !isAbortProzess()) {
					try {
						@SuppressWarnings({ "rawtypes", "unchecked" })
						ServiceTracker t = new ServiceTracker(ctx, IBenchmark.class.getName(), null);
						t.open();
						int wait = 100;
						IBenchmark iBenchmark = (IBenchmark) t.waitForService(wait);
						if (iBenchmark == null) {
							throw new Exception("cannot find benchmark service");
						}

						int waitConfig;
						if (benchmarkParam.getWaitConfig() != null) {
							waitConfig = Integer.parseInt(benchmarkParam.getWaitConfig());
						} else {
							waitConfig = 0;
						}
						Thread.sleep(waitConfig);

						configureBenchmark(iBenchmark, benchmark);
						final String RELATIVE_FOLDER = "benchmarks";
						String folderName = StringUtils.nameToFoldername(benchmarkGroup.getName());
						folderName = RELATIVE_FOLDER + File.separator + folderName + File.separator + benchmark.getId();
						try {
							System.out.println("after setParameter s, als nächsten forschleife");
							this.benchmarkParam = benchmark.getParam();
							for (int i = 0; i < Integer.parseInt(benchmarkParam.getNumberOfRuns()); i++) {
								if (isAbortProzess()) {
									return;
								}
                                Collection<IBenchmarkResult<Object>> results = iBenchmark.runBenchmark();
                                String filename = folderName + File.separator + "result" + (i + 1) + ".xml";
                                Serializer serializer = new Persister();
                                File file = new File(filename);
                                FileOutputStream oStream = new FileOutputStream(file);
                                for (IBenchmarkResult<?> result : results) {
                                	serializer.write(result, oStream);
                                }

                                if (benchmarkParam.isMemoryUsage()) {
                                	String memFile = filename.replaceAll(".xml", "_memory.xml");
                                	serializer.write(iBenchmark.getMemUsageStatistics(), new File(memFile));
                                }
                                System.out.println("Benchmarkrun erfolgreich");
                                BenchmarkStoreUtil.loadResultsOfGroupAndBenchmark(benchmark);
                                Display.getDefault().asyncExec(new Runnable() {
                                	@Override
                                    public void run() {
                                		ProjectView.getDefault().refresh();
                                	}
                                });
							}
							fetchMetadataInformations(benchmark);
							BenchmarkStoreUtil.storeBenchmark(benchmark);
						} catch (BenchmarkException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Konfiguriert den Benchmark in Odysseus mit den Parametereinstellungen aus
	 * der GUI
	 * 
	 * @param ibenchmark
	 * @param benchmark
	 */
	private void configureBenchmark(IBenchmark ibenchmark, Benchmark benchmark) {
		this.benchmarkParam = benchmark.getParam();
		String query = null;
		if (benchmarkParam.getInputFile() != null) {
			String filename = benchmarkParam.getInputFile();
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
			if (benchmarkParam.getQuery() != null) {
				query = benchmarkParam.getQuery();
			} else {
				throw new RuntimeException("missing input parameter query or input_file");
			}
		}

		if (benchmarkParam.getScheduler() != null) {
			String scheduler = benchmarkParam.getScheduler();
			ibenchmark.setScheduler(scheduler);
		}

		if (benchmarkParam.getSchedulingstrategy() != null) {
			String schedulingStrategy = benchmarkParam.getSchedulingstrategy();
			ibenchmark.setSchedulingStrategy(schedulingStrategy);
		}

		if (benchmarkParam.getBufferplacement() != null) {
			String bufferPlacement = benchmarkParam.getBufferplacement();
			ibenchmark.setBufferPlacementStrategy(bufferPlacement);
		}

		if (benchmarkParam.getDataType() != null) {
			String dataType = benchmarkParam.getDataType();
			ibenchmark.setDataType(dataType);
		}

		String[] metaTypes = benchmarkParam.getMetadataCombination();

		if (benchmarkParam.isPriority()) {
			metaTypes = Arrays.copyOf(metaTypes, metaTypes.length + 1);
			metaTypes[metaTypes.length - 1] = "de.uniol.inf.is.odysseus.priority.IPriority";
			ibenchmark.setResultFactory("de.uniol.inf.is.odysseus.benchmarker.impl.PriorityBenchmarkResultFactory");
		}

		ibenchmark.setMetadataTypes(metaTypes);
		ibenchmark.setNoMetadataCreation(benchmarkParam.isNoMetada());

		if (benchmarkParam.isPunctuations()) {
			ibenchmark.setUsePunctuations(true);
		}

		if (benchmarkParam.isExtendesPostpriorisation()) {
			ibenchmark.setExtendedPostPriorisation(true);
		}

		if (benchmarkParam.getMaxResult() != null) {
			Long maxResults = Long.parseLong(benchmarkParam.getMaxResult());
			ibenchmark.setMaxResults(maxResults);
		}

		if (benchmarkParam.isMemoryUsage()) {
			ibenchmark.setBenchmarkMemUsage(true);
		}

		if (benchmarkParam.isResultPerQuery()) {
			ibenchmark.setResultPerQuery(true);
		}

		String queryLanguage = benchmarkParam.getQueryLanguage();
		ibenchmark.addQuery(queryLanguage, query);
	}

	public void fetchMetadataInformations(Benchmark benchmark) {
		BundleContext context = Activator.getDefault().getBundle().getBundleContext();
		Bundle[] bundles = context.getBundles();
		if (bundles == null) {
			System.out.println("BUNDLES IST NULL!!!!");
		} else {
			BenchmarkMetadata metadata = benchmark.getMetadata();
			if (metadata == null) {
				metadata = new BenchmarkMetadata();
			}
			for (int i = 0; i < bundles.length; i++) {
				Bundle bundle = bundles[i];
				String version = bundle.getVersion().getQualifier();
				String name = StringUtils.splitString(bundle.getSymbolicName());
				metadata.getMetadata().put(name, version);
			}
			benchmark.setMetadata(metadata);
		}
	}

	public void setAbortProzess(boolean b) {
		this.abortProzess = b;
	}
}