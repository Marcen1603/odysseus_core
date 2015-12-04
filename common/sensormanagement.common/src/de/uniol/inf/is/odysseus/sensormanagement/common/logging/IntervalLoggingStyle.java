package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.StreamObjectUtilities;

// This logging style alternates between up- and downtimes.
// The default setting of 1000 / 1000 loggs all elements for 1000ms, 
// then discards all elements for 1000ms, and so on.
// If splitChunks is true, a new raw data chunk will be generated for each uptime
public class IntervalLoggingStyle extends DefaultLoggingStyle
{
	private long upTime = 1000;
	private long downTime = 1000;
	public boolean splitChunks; 	
	private long startTime = -1;
	
	private boolean isUp = true;
	
	public IntervalLoggingStyle(Map<String, String> options) 
	{
		super(options);

		String upTimeStr = options.get("upTime");
		upTime = upTimeStr != null && !upTimeStr.equals("") ? parseTime(upTimeStr) : 1000;		
		
		String downTimeStr = options.get("downTime");
		downTime = downTimeStr != null && !downTimeStr.equals("") ? parseTime(downTimeStr) : 1000;
		
		String splitChunksStr = options.get("splitChunks");
		splitChunks = splitChunksStr != null && splitChunksStr.equals("1");
		
		String startTimeStr = options.get("startTime");
		startTime = startTimeStr != null && !startTimeStr.equals("") ? parseDate(startTimeStr) : -1;		
	}
	
	@Override public ProcessResult process(Tuple<?> object)
	{
		long curTimeStamp = StreamObjectUtilities.getTimeStamp(object);
		if (startTime == -1)
			startTime = curTimeStamp;
		
		// Wait until start time stamp has been reached
		if (curTimeStamp < startTime)
			return new ProcessResult(false, false);
		
		long runTime = curTimeStamp - startTime;
		long periodLength = upTime + downTime;
		
		long timeInPeriod = runTime % periodLength;

		if (timeInPeriod < upTime)
		{
			isUp = true;
			return super.process(object);
		}
		else
		{
			boolean sendSplitChunk = isUp;
			isUp = false;
			return new ProcessResult(false, sendSplitChunk);
		}
	}	
	
}
