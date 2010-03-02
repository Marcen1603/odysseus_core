package de.uniol.inf.is.odysseus.action.benchmark;


public interface IActuatorBenchmark {
	
	public enum Operation{DATAEXTRACTION, QUERYPROCESSING, ACTIONEXECTION};

	
	public String notifyStart(String identifier, Operation op);
	
	public long notifyEnd(String identifier, String id, Operation op);

}
