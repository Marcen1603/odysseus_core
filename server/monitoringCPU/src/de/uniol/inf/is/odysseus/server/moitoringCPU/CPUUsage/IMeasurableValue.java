package de.uniol.inf.is.odysseus.server.moitoringCPU.CPUUsage;

public interface IMeasurableValue {
	
	public void startMeasurement(long timestamp);
	
	public void stopMeasurement(long timestamp);
	
	public boolean isConfirmed();

}