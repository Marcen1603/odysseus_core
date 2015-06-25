package de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.model.Measurement;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.physicaloperator.access.LMS1xxProtocolHandler;

public class Lms1xxPlayback extends TextFilePlayback
{
	public Lms1xxPlayback(SensorModel sensorModel, LogMetaData logMetaData) 
	{
		super(sensorModel, logMetaData);
	}
	
	private Event parseOldLine(String line)
	{
//		double timeOffset = 3.24f; // BHV TODO: This is a hack to correct a time difference for the LMS scanners
		
//		timeOffset = 3.54f; // Senckenberg 14:30-15:00 LMS 100
//		timeOffset = 3.54f; // Senckenberg 15:10-15:20 LMS 100
//		timeOffset = 4.34f; // Senckenberg 15:30-15:50 LMS 100
		
		// TODO: Dies entfernt STX und ETX. Momentan entfernt es auch ', welche am Anfang und Ende stehen!
		String processedLine;
		if (line.charAt(0) == '\'')
			processedLine = (String) line.substring(2, line.length()-2);
		else
			processedLine = (String) line.substring(1, line.length()-1);
			
		Measurement m = LMS1xxProtocolHandler.parseLMS1xxScanData(processedLine.split(" "));
		if (m == null) 
			return null;
		
		double eventSecondsValid = 0.08; // 80ms gültig, da alle 40ms ein neues Event kommt
		double lastEventTime = m.getTimeStamp() / 1000.0;// + timeOffset;
		
		return new Event(m, lastEventTime, eventSecondsValid, line);
	}
	
	@Override public Event parseLine(String line)
	{
		String[] lines = line.split(",", 2);
		
		if (lines.length != 2)
		{			
			// throw new IllegalArgumentException("line.split(\",\",2) did not return two parts! Possibly old LMS format?");
			return parseOldLine(line);
		}

		double timestamp = Long.parseLong(lines[0]) / 1000.0;		
		
		// remove STX, ETX and possibly ' at start and end of line
		if (lines[1].charAt(0) == '\'')
			lines[1] = (String) lines[1].substring(2, lines[1].length()-2);
		else
			lines[1] = (String) lines[1].substring(1, lines[1].length()-1);
			
		Measurement m = LMS1xxProtocolHandler.parseLMS1xxScanData(lines[1].split(" "));
		if (m == null) 
			return null;
		
		double eventSecondsValid = 0.08; // 80ms gültig, da alle 40ms ein neues Event kommt
		
		return new Event(m, timestamp, eventSecondsValid);
	}
	
}
