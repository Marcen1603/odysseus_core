package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

/**
 * Class for measuring overhead of scheduling
 * @author Thomas Vogelgesang
 *
 */
public class OverheadMeasurement {
	
	private long startTS;
	private long totalTime;
	private long outputTS;
	private int calls;
	
	private final long waitingTimeForOutput = 1000000000;

	public OverheadMeasurement() {
		this.outputTS = System.nanoTime(); 
	}
	
	public void start() {
		this.startTS = System.nanoTime();
		this.calls++;
	}
	
	public void stop() {
		this.totalTime += System.nanoTime() - this.startTS;
		if (System.nanoTime() > (outputTS + waitingTimeForOutput)) {
//			System.out.format("%-11.9f%n", (this.totalTime  / 1000000000.0) / (double)this.calls);
			this.totalTime = 0L;
			this.calls = 0;
			this.outputTS = System.nanoTime();
		}
	}
	
}
