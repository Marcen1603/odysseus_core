package de.uniol.inf.is.odysseus.parallelization.rcp.threads;

import org.eclipse.swt.widgets.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.parallelization.rcp.windows.InterOperatorParallelizationBenchmarkerWindow;


public class AnalyseQueryThread extends Thread{

	private static Logger LOG = LoggerFactory.getLogger(AnalyseQueryThread.class);
	
	private final InterOperatorParallelizationBenchmarkerWindow window;
	private final ProgressBar progressAnalyseQuery;
	
	public AnalyseQueryThread(InterOperatorParallelizationBenchmarkerWindow interOperatorParallelizationBenchmarkerWindow, ProgressBar progressAnalyseQuery){
		this.window = interOperatorParallelizationBenchmarkerWindow;
		this.progressAnalyseQuery = progressAnalyseQuery;
	}
	
	@Override
	public void run() {
		LOG.debug("Analysing of current query started.");
		
		// TODO dummy values, do stuff here and update progress bar
		changeProgressBarSelection(20);
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		changeProgressBarSelection(40);
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		changeProgressBarSelection(60);
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		changeProgressBarSelection(80);
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		changeProgressBarSelection(100);
		changeWindowOnSuccess();
		LOG.debug("Analysing of current query done.");
	}

	private void changeWindowOnSuccess() {
		window.getWindow().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				window.clearPageContent();
				window.createConfigContent();
			}
			
		});
	}
	
	private void changeProgressBarSelection(final int selectionValue) {
		window.getWindow().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				progressAnalyseQuery.setSelection(selectionValue);
			}
			
		});
	}
}
