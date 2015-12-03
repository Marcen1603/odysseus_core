package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoggingStyleProvider 
{
	static private Map<String, Map<String, String>> loggingStyles = new HashMap<>();
	
	static
	{
		Map<String, String> defaultLoggingStyle = new HashMap<>();
		defaultLoggingStyle.put("minTimeDiff", "minimal time difference of start time stamps of two elements");
		defaultLoggingStyle.put("sampleRate", "sampling rate of logged elements");
		
		Map<String, String> intervalLoggingStyle = new HashMap<>();
		intervalLoggingStyle.putAll(defaultLoggingStyle);
		intervalLoggingStyle.put("upTime", "Duration during which all elements are logged according to minTimeDiff and sapleRate");
		intervalLoggingStyle.put("downTime", "Duration during which all elements are discarded");
		
		loggingStyles.put("Default", defaultLoggingStyle);
		loggingStyles.put("Interval", intervalLoggingStyle);
	}
	
	static public Set<String> getLoggingStyles()
	{
		return loggingStyles.keySet();
	}
	
	static public Set<String> getLoggingStyleOptions(String loggingStyle)
	{
		return loggingStyles.get(loggingStyle).keySet();
	}
}
