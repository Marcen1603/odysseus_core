package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import com.google.common.base.Preconditions;

public abstract class ChartUpdater extends Thread {

	private final long updateIntervalMillis;
	
	private boolean isRunning;
	
	public ChartUpdater( long updateIntervalMillis ) {
		// Preconditions.checkArgument(updateIntervalMillis > 0 , "Updater interval for chart updater must be positive");
		
		this.updateIntervalMillis = updateIntervalMillis;
		
		setDaemon(true);
		setName("Chart updater");
	}
	
	@Override
	public void run() {
		isRunning = true;
		
		while( isRunning ) {
			updateChart();
			
			trySleep(updateIntervalMillis);
		}
	}
	
	private static void trySleep(long timeMillis) {
		try {
			Thread.sleep(timeMillis);
		} catch (InterruptedException ex) {
		}
	}

	protected abstract void updateChart();
	
	public void stopRunning() {
		isRunning = false;
	}
}
