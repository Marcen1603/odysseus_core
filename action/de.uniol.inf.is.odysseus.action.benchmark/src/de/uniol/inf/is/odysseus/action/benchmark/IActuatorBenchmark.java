package de.uniol.inf.is.odysseus.action.benchmark;


public interface IActuatorBenchmark {
	
	String ID = "ActuatorBMID";
	String Identifier = "ActuatorBMIdentifier";
	
	public enum Operation{DATAEXTRACTION, QUERYPROCESSING, ACTIONEXECTION};

	
	public String notifyStart(String identifier, Operation op);
	
	public long notifyEnd(String identifier, String id, Operation op);

}
