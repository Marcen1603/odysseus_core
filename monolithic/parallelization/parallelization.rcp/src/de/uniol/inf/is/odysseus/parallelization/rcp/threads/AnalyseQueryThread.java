package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import java.util.List;
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
		
		for (BenchmarkerExecution benchmarkerExecution : benchmarkerExecutions) {
			changeProgress(currentPercentage, "Executing analysis "+(executionCounter+1) +" of "+numberOfExecutions);
			
			executeAnalysis(benchmarkerExecution);
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
		// TODO update window
	}

	private void executeAnalysis(BenchmarkerExecution benchmarkerExecution) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			interrupt();
		}
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
