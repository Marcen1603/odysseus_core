package de.uniol.inf.is.odysseus.server.monitoring.physicaloperator;

public interface IMeasurableValue {
	
	public void start(long timestamp);
	
	public void stop(long timestamp);

}