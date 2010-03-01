package de.uniol.inf.is.odysseus.action.benchmark;

public interface IActuatorBenchmark {
	
	public String notifyStart(String identifier);
	
	public long notifyEnd(String identifier);

}
