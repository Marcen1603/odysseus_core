package de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.model.Measurement;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.physicaloperator.access.LMS1xxProtocolHandler;

public class Lms1xxPlayback extends TextFilePlayback
{
	static final double timeOffset = 3.24f; // TODO: This is a hack to correct a time difference for the LMS scanners
	
	public Lms1xxPlayback(SensorModel sensorModel, LogMetaData logMetaData) 
	{
		super(sensorModel, logMetaData);
	}
	
	@Override public Event parseLine(String line)
	{
		// TODO: Dies entfernt STX und ETX. Momentan entfernt es auch ', welche am Anfang und Ende stehen!
		if (line.charAt(0) == '\'')
			line = (String) line.substring(2, line.length()-2);
		else
			line = (String) line.substring(1, line.length()-1);
			
		Measurement m = LMS1xxProtocolHandler.parseLMS1xxScanData(line.split(" "));
		if (m == null) 
			return null;
		
		double eventSecondsValid = 0.08; // 80ms gültig, da alle 40ms ein neues Event kommt
		double lastEventTime = m.getTimeStamp() / 1000.0 + timeOffset;
		
		return new Event(m, lastEventTime, eventSecondsValid);
	}
}
