package de.uniol.inf.is.odysseus.server.monitoring.physicaloperator;

public interface IMeasurableValue {
	
	public void startMeasurement(long timestamp);
	
	public void stopMeasurement(long timestamp);
	
	public boolean isConfirmed();

}