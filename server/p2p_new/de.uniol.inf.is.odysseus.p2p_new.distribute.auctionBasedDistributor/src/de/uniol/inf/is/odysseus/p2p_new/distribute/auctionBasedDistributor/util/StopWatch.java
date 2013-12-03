package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util;

public class StopWatch {
	private long startTime;
	private long stopTime;
	
	private StopWatch() {
		
	}
	
	public static StopWatch start() {
		StopWatch sw = new StopWatch();
		sw.startTime = System.currentTimeMillis();
		return sw;
	}
	
	public StopWatch stop() {
		this.stopTime = System.currentTimeMillis();
		return this;
	}
	
	public long getTimeInMillis() {
		return stopTime-startTime;
	}
}
