package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkerExecution;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;

public class AnalyseQueryThread extends Thread {
	
	private static Logger LOG = LoggerFactory
			.getLogger(AnalyseQueryThread.class);
	private UUID processUid;
	private BenchmarkDataHandler data;
	private ParallelizationBenchmarkerWindow window;
	
	public AnalyseQueryThread(UUID processUid, ParallelizationBenchmarkerWindow parallelizationBenchmarkerWindow){
		this.processUid = processUid;
		this.window = parallelizationBenchmarkerWindow;
		data = BenchmarkDataHandler.getExistingInstance(processUid);
	}
	
	@Override
	public void run() {
		LOG.debug("Analysing of current query started.");
		
		List<BenchmarkerExecution> benchmarkerExecutions = data.getConfiguration().getBenchmarkerExecutions();
		int numberOfExecutions = benchmarkerExecutions.size();
		int executionCounter = 0;
		int currentPercentage = 0;
		
		Map<BenchmarkerExecution, Long> analysisTimes = new HashMap<BenchmarkerExecution, Long>();
		
		for (BenchmarkerExecution benchmarkerExecution : benchmarkerExecutions) {
			changeProgress(currentPercentage, "Executing analysis "+(executionCounter+1) +" of "+numberOfExecutions);
			
			Long analysisTime = executeAnalysis(benchmarkerExecution);
			analysisTimes.put(benchmarkerExecution, analysisTime);
			// if cancel button is pressed, we want to stop the analysis after current execution
			if (isInterrupted()){
				cleanupBenchmarker();
				return;
			}
			
			currentPercentage = (int) (((executionCounter+1)/(double) numberOfExecutions)*100);
			changeProgress(currentPercentage, "Done");
			executionCounter++;
			
		}
		changeProgress(100, System.lineSeparator()+"Parallelization Benchmark anaylsis complete");
		evaluateResult(analysisTimes);
	}

	private void evaluateResult(Map<BenchmarkerExecution, Long> analysisTimes) {
		BenchmarkerExecution bestExecution = getBestExecution(analysisTimes);
		showResult(bestExecution.getOdysseusScript());
	}
	
	private BenchmarkerExecution getBestExecution(
			Map<BenchmarkerExecution, Long> analysisTimes) {
		BenchmarkerExecution bestResult = analysisTimes.keySet().iterator().next();
		// TODO evaluate, maybe equals method is needed
		return bestResult;
	}

	private void showResult(final String resultOdysseusScript) {
		window.getWindow().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				window.showResult(resultOdysseusScript);
			}
		});
	}

	private Long executeAnalysis(BenchmarkerExecution benchmarkerExecution) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			interrupt();
		}
		return 0l;
	}

	private void cleanupBenchmarker() {
		BenchmarkDataHandler.removeInstance(processUid);
	}

	private void changeProgress(final int progressProcent, final String progressString) {
		window.getWindow().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				window.updateAnalysisProgress(progressProcent, progressString);
			}

		});
	}
}
