package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// TODO: Dynamically add logging styles etc.
public class LoggingStyleProvider 
{
	static private Map<String, Map<String, String>> loggingStyles = new HashMap<>();
	
	static
	{
		Map<String, String> defaultLoggingStyle = new HashMap<>();
		defaultLoggingStyle.put("minTimeDiff", "minimal time difference of start time stamps of two elements");
		defaultLoggingStyle.put("sampleRate", "sampling rate of logged elements");
		defaultLoggingStyle.put("sizeLimit", "size limit of each raw data chunk");
		
		Map<String, String> intervalLoggingStyle = new HashMap<>();
		intervalLoggingStyle.putAll(defaultLoggingStyle);
		intervalLoggingStyle.put("upTime", "Duration during which all elements are logged according to minTimeDiff and sapleRate");
		intervalLoggingStyle.put("downTime", "Duration during which all elements are discarded");
		intervalLoggingStyle.put("splitChunks", "If set to 1, creates a new raw data chunk for each uptime");
		intervalLoggingStyle.put("startTime", "The first uptime cycle will start at this timestamp");
		
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
	
	static public AbstractLoggingStyle createLoggingStyle(Map<String, String> options)
	{
		if (options == null)
			return new DefaultLoggingStyle(new HashMap<String, String>());
		
		String loggingStyleBase = options.get("baseStyle");
		
		if (loggingStyleBase.equalsIgnoreCase("Default")) 	return new DefaultLoggingStyle(options);
		if (loggingStyleBase.equalsIgnoreCase("Interval"))	return new IntervalLoggingStyle(options);
			
		throw new InvalidParameterException("Unknown logging style: " + loggingStyleBase);
	}
}
