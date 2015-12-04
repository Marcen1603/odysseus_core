package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class StreamObjectUtilities 
{
	public static long getTimeStamp(AbstractStreamObject<?> object)
	{
        TimeInterval timeStamp = (TimeInterval)object.getMetadata();
        if (timeStamp != null)
        	return timeStamp.getStart().getMainPoint();
        else
        {
        	System.out.println("Warning: No timestamp provided!");
        	return System.currentTimeMillis();        	
        }		
	}
}
