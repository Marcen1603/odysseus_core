/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.util.List;
import java.util.UUID;

import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkerExecution;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;

public class BenchmarkMainThread extends Thread {

	private static final String PRE_TRANSFORM_TOKEN = "#PRETRANSFORM BenchmarkPreTransformation";

	private static Logger LOG = LoggerFactory
			.getLogger(BenchmarkMainThread.class);
	private UUID processUid;
	private BenchmarkDataHandler data;
	private ParallelizationBenchmarkerWindow window;

	public BenchmarkMainThread(UUID processUid,
			ParallelizationBenchmarkerWindow parallelizationBenchmarkerWindow) {
		this.processUid = processUid;
		this.window = parallelizationBenchmarkerWindow;
		data = BenchmarkDataHandler.getExistingInstance(processUid);
	}

	@Override
	public void run() {
		LOG.debug("Analysing of current query started.");

		List<BenchmarkerExecution> benchmarkerExecutions = data
				.getConfiguration().getBenchmarkerExecutions();
		int numberOfExecutions = benchmarkerExecutions.size();
		int executionCounter = 0;
		int currentPercentage = 0;

		for (BenchmarkerExecution benchmarkerExecution : benchmarkerExecutions) {
			if (!isInterrupted()) {
				changeProgress(currentPercentage, -1l, "Executing analysis "
						+ (executionCounter + 1) + " of " + numberOfExecutions
						+ ": " + benchmarkerExecution.toString());
			}

			for (int i = 0; i < benchmarkerExecution.getNumberOfExecutions(); i++) {
				if (!isInterrupted()) {
					executeAnalysis(benchmarkerExecution);
				}
			}

			if (!isInterrupted()) {
				currentPercentage = printResult(numberOfExecutions,
						executionCounter, benchmarkerExecution);
				executionCounter++;
			}

			if (isInterrupted()) {
				cleanupBenchmarker();
				return;
			}

		}
		changeProgress(100, 0l, System.lineSeparator()
				+ "Parallelization Benchmark anaylsis complete");
		evaluateResult(benchmarkerExecutions);
	}

	private int printResult(int numberOfConfigurations, int configurationCounter,
			BenchmarkerExecution benchmarkerExecution) {
		int currentPercentage = (int) (((configurationCounter + 1) / (double) numberOfConfigurations) * 100);
		
		long averageExecutionTime = benchmarkerExecution.getAverageExecutionTime(data
				.getConfiguration().getMaximumExecutionTime());
		long remainingTimeMillis = ((numberOfConfigurations-configurationCounter) * averageExecutionTime * benchmarkerExecution.getNumberOfExecutions());
		
		if (averageExecutionTime >= data
				.getConfiguration().getMaximumExecutionTime()) {
			changeProgress(
					currentPercentage, remainingTimeMillis,
					"Maximum execution time reached. Please restart benchmarker and adjust values "
							+ "for maximum execution time or number of elements."
							+ System.lineSeparator());
		} else if (averageExecutionTime == -1l) {
			changeProgress(
					currentPercentage, remainingTimeMillis,
					"End of evaluation was reached, before "
							+ data.getConfiguration().getNumberOfElements()
							+ " elements are processed. Please decrese the number of elements.");
		} else {
			changeProgress(
					currentPercentage, remainingTimeMillis,
					"Done. "
							+ data.getConfiguration().getNumberOfElements()
							+ " elements in an average time of "
							+ averageExecutionTime
							+ " ms processed. ("
							+ benchmarkerExecution.getNumberOfExecutions()
							+ " executions)" + System.lineSeparator());
		}
		return currentPercentage;
	}

	private void evaluateResult(List<BenchmarkerExecution> benchmarkerExecutions) {
		BenchmarkerExecution currentBestExecution = benchmarkerExecutions
				.get(0);

		for (BenchmarkerExecution benchmarkerExecution : benchmarkerExecutions) {
			if (benchmarkerExecution.getAverageExecutionTime(data
					.getConfiguration().getMaximumExecutionTime()) <= 0l) {
				continue;
			} else {
				if (benchmarkerExecution.getAverageExecutionTime(data
						.getConfiguration().getMaximumExecutionTime()) < currentBestExecution
						.getAverageExecutionTime(data.getConfiguration()
								.getMaximumExecutionTime())) {
					currentBestExecution = benchmarkerExecution;
				}
			}
		}
		showResult(currentBestExecution.getOdysseusScript());
	}

	private void showResult(final String resultOdysseusScript) {
		window.getWindow().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				window.analyseDone(resultOdysseusScript);
			}
		});
	}

	private void executeAnalysis(BenchmarkerExecution benchmarkerExecution) {
		List<String> queryStringArray = data.getQueryStringArray();
		StringBuilder builder = new StringBuilder();
		for (String queryStringLine : queryStringArray) {
			builder.append(queryStringLine);
			// put parallelization keywords directly after parser command
			if (queryStringLine.trim().startsWith("#PARSER")) {
				builder.append(benchmarkerExecution.getOdysseusScript());
				builder.append(PRE_TRANSFORM_TOKEN);
			}
		}

		// start execution thread
		BenchmarkExecutionThread executionThread = new BenchmarkExecutionThread(
				builder.toString(), processUid, benchmarkerExecution);
		executionThread.setName("BenchmarkExecutionThread");
		executionThread.setDaemon(true);
		executionThread.start();

		try {
			// wait for the execution thread to finish. if its finish start next
			// execution
			executionThread.join();
		} catch (InterruptedException e) {
			// do nothing
			interrupt();
		}
	}

	private void cleanupBenchmarker() {
		BenchmarkDataHandler.removeInstance(processUid);
	}

	private void changeProgress(final int progressProcent,
			final long remainingTimeMillis, final String progressString) {
		Shell currentWindow = window.getWindow();
		if (!currentWindow.isDisposed()) {
			currentWindow.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					window.getAnalyseComposite().updateAnalysisProgress(
							progressProcent, remainingTimeMillis, progressString);
				}

			});
		} else {
			interrupt();
		}
	}
}
