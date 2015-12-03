package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

// This logging style alternates between up- and downtimes.
// The default setting of 1.0 / 1.0 loggs all elements for 1s, 
// then discards all elements for 1s, and so on  
public class IntervalLoggingStyle extends DefaultLoggingStyle
{
	public double upTime = 1.0;
	public double downTime = 1.0;
}
