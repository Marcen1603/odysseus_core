package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public abstract class AbstractLoggingStyle 
{
	public long sizeLimit;
	
	AbstractLoggingStyle(Map<String, String> options)
	{
		String sizeLimitStr = options.get("sizeLimit");
		sizeLimit = sizeLimitStr != null && sizeLimitStr.length() > 0 ? Long.parseLong(sizeLimitStr) : 300*1024*1024; // 300 MB		
	}
	
	public abstract boolean process(Tuple<?> object);
	
	public static long parseTime(String timeStr)
	{
		if (timeStr == null || timeStr.length() == 0) return 0;
		
		// Supported suffixes: d, h, m, s, ms. Default = ms
		long factor = 1; // ms
		if (timeStr.endsWith("ms")) 
		{
			factor = 1;
			timeStr = timeStr.substring(0, timeStr.length()-2);
		} 
		else if (timeStr.endsWith("s"))
		{
			factor = 1000;
			timeStr = timeStr.substring(0, timeStr.length()-1);			
		}
		else if (timeStr.endsWith("m"))
		{
			factor = 60*1000;
			timeStr = timeStr.substring(0, timeStr.length()-1);			
		}
		else if (timeStr.endsWith("h"))
		{
			factor = 60*60*1000;
			timeStr = timeStr.substring(0, timeStr.length()-1);			
		}
		else if (timeStr.endsWith("d"))
		{
			factor = 24*60*60*1000;
			timeStr = timeStr.substring(0, timeStr.length()-1);			
		}
		
		return (long) (Double.parseDouble(timeStr.trim()) * factor);
	}
}
