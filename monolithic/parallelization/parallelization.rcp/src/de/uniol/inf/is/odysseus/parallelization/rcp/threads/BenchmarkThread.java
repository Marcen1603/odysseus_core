package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkerExecution;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;

public class BenchmarkThread extends Thread {

	private static final String PRE_TRANSFORM_TOKEN = "#PRETRANSFORM BenchmarkPreTransformation";

	private static Logger LOG = LoggerFactory.getLogger(BenchmarkThread.class);
	private UUID processUid;
	private BenchmarkDataHandler data;
	private ParallelizationBenchmarkerWindow window;

	public BenchmarkThread(UUID processUid,
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
			changeProgress(currentPercentage, "Executing analysis "
					+ (executionCounter + 1) + " of " + numberOfExecutions
					+ ": " + benchmarkerExecution.toString());

			executeAnalysis(benchmarkerExecution);
			// if cancel button is pressed, we want to stop the analysis after
			// current execution
			if (isInterrupted()) {
				cleanupBenchmarker();
				return;
			}

			currentPercentage = (int) (((executionCounter + 1) / (double) numberOfExecutions) * 100);
			if (benchmarkerExecution.getExecutionTime() >= data
					.getConfiguration().getMaximumExecutionTime()) {
				changeProgress(
						currentPercentage,
						"Maximum execution time reached. Please restart benchmarker and adjust values "
								+ "for maximum execution time or number of elements."
								+ System.lineSeparator());
			} else if (benchmarkerExecution.getExecutionTime() == -1l) {
				changeProgress(
						currentPercentage,
						"End of evaluation was reached, before "
								+ data.getConfiguration().getNumberOfElements()
								+ " elements are processed. Please decrese the number of elements.");
			} else {
				changeProgress(
						currentPercentage,
						"Done. "
								+ data.getConfiguration().getNumberOfElements()
								+ " elements in "
								+ benchmarkerExecution.getExecutionTime()
								+ " ms processed." + System.lineSeparator());
			}
			executionCounter++;

		}
		changeProgress(100, System.lineSeparator()
				+ "Parallelization Benchmark anaylsis complete");
		evaluateResult(benchmarkerExecutions);
	}

	private void evaluateResult(List<BenchmarkerExecution> benchmarkerExecutions) {
		BenchmarkerExecution currentBestExecution = benchmarkerExecutions
				.get(0);

		for (BenchmarkerExecution benchmarkerExecution : benchmarkerExecutions) {
			if (benchmarkerExecution.getExecutionTime() <= 0l) {
				continue;
			} else {
				if (benchmarkerExecution.getExecutionTime() < currentBestExecution
						.getExecutionTime()) {
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
				window.showResult(resultOdysseusScript);
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
		}
	}

	private void cleanupBenchmarker() {
		BenchmarkDataHandler.removeInstance(processUid);
	}

	private void changeProgress(final int progressProcent,
			final String progressString) {
		window.getWindow().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				window.updateAnalysisProgress(progressProcent, progressString);
			}

		});
	}
}
