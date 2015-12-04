package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.StreamObjectUtilities;

// This logging style alternates between up- and downtimes.
// The default setting of 1000 / 1000 loggs all elements for 1000ms, 
// then discards all elements for 1000ms, and so on  
public class IntervalLoggingStyle extends DefaultLoggingStyle
{
	private long upTime = 1000;
	private long downTime = 1000;
	
	private long startTime = -1;
	
	public IntervalLoggingStyle(Map<String, String> options) 
	{
		super(options);
		
		upTime = parseTime(options.get("upTime"));		
		downTime = parseTime(options.get("downTime"));		
	}
	
	@Override public boolean process(Tuple<?> object)
	{
		long curTimeStamp = StreamObjectUtilities.getTimeStamp(object);
		if (startTime == -1)
			startTime = curTimeStamp;
		
		long runTime = curTimeStamp - startTime;
		long periodLength = upTime + downTime;
		
		long timeInPeriod = runTime % periodLength;

		if (timeInPeriod < upTime)
			return super.process(object);
		else
			return false;
	}	
	
}
