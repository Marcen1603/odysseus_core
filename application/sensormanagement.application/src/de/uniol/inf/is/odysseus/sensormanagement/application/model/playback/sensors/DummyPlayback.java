package de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.sensors;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;

public class DummyPlayback extends TextFilePlayback 
{
	public DummyPlayback(SensorModel sensorModel, LogMetaData logMetaData) 
	{
		super(sensorModel, logMetaData);
	}

	@Override
	public Event parseLine(String line) 
	{
   		double eventSecondsValid = 0; // TODO: 0, funktioniert sonst nicht. ??? // 80ms gültig, da alle 40ms ein neues Event kommt		
   		double timestamp = Long.parseLong(line) / 1000.0;   		
   		return new Event(getSensorModel(), line, timestamp, eventSecondsValid);		
	}
}
