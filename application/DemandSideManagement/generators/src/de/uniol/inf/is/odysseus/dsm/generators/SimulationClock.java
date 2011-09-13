package de.uniol.inf.is.odysseus.dsm.generators;

public class SimulationClock {
	
	private long start;
	private int speed;

	static SimulationClock instance = new SimulationClock();
	
	private SimulationClock(){
		start = System.currentTimeMillis();
		speed = 10;
	}
	
	public static SimulationClock getInstance() {
		return instance;
	}
	
	public long getTime(){
		return ((System.currentTimeMillis()-start)*speed)+start;
	}
}
