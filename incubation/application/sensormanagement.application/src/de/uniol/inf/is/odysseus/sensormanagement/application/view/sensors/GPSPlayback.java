package de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;

public class GPSPlayback extends TextFilePlayback 
{
	public GPSPlayback(LogMetaData logMetaData) 
	{
		super(logMetaData);
	}

	@Override
	public Event parseLine(String line) 
	{
   		double eventSecondsValid = 0; // TODO: 0, funktioniert sonst nicht. ??? // 80ms gültig, da alle 40ms ein neues Event kommt		
   		double timestamp = Long.parseLong(line.split(",")[0]) / 1000.0;   		
   		return new Event(line, timestamp, eventSecondsValid);		
	}
}
