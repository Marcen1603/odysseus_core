package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.StreamObjectUtilities;

// This logging style logs all elements whose timestamp difference to the previous element is >= minTimeDiff.
// Also, if a sample rate is set, elements are sampled according to this rate
public class DefaultLoggingStyle extends AbstractLoggingStyle
{
	public long minTimeDiff;	// 0 -> Each element passes. 1000ms -> At least 1000ms between begin timestamps 
	public int sampleRate;			// 1 -> Every element passes. 2 -> 1st passes, 2nd not, 3rd passes...	
	
	DefaultLoggingStyle(Map<String, String> options)
	{
		super(options);
		
		minTimeDiff = parseTime(options.get("minTimeDiff"));
		
		String sampleRateStr = options.get("sampleRate");
		sampleRate = sampleRateStr != null && sampleRateStr.length() > 0 ? Integer.parseInt(sampleRateStr) : 0;
	}
	
	private long lastTimeStamp = -1;
	private int sampleCount = 0;
	
	@Override public ProcessResult process(Tuple<?> object)
	{		
		if (minTimeDiff != 0)
		{
			// Skip tuples if the last tuple was sent less than minTimeDiff ms ago
			if (lastTimeStamp != -1)
			{
				long curTimeStamp = StreamObjectUtilities.getTimeStamp(object);				
				if (curTimeStamp - lastTimeStamp < minTimeDiff)
					return new ProcessResult(false, false);
			}
			lastTimeStamp = StreamObjectUtilities.getTimeStamp(object);
		}
		
		if (sampleRate != 0)
		{
			// Skip tuples such that only every xth tuple gets processed, x = sampleRate
			// sampleRate = 3 -> pass skip skip pass skip skip ...
			boolean skip = sampleCount != 0;
			sampleCount++;
			if (sampleCount == sampleRate)
				sampleCount = 0;
			if (skip)
				return new ProcessResult(false, false);
		}
		
		return new ProcessResult(true, false);
	}	
}
