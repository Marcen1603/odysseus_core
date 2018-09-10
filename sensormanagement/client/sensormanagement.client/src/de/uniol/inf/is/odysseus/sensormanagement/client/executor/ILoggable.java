package de.uniol.inf.is.odysseus.sensormanagement.client.executor;

public interface ILoggable 
{
	// Start logging with default style
	public void startLogging();
	
	// Start logging with specified style
	public void startLogging(String loggingStyle);
	
	// Stops all logging
	public void stopAllLogging();
	
	// Stops logging for specified style
	public void stopLogging(String loggingStyle);	
}
