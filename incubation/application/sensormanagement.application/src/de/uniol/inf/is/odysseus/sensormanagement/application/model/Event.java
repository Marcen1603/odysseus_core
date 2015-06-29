package de.uniol.inf.is.odysseus.sensormanagement.application.model;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;

public class Event
{
	private final SensorModel source;
	private final double timeStamp;
	private final Object eventObject;
	private final double secondsValid;
	private final Object originalData;		

	public SensorModel getSource() { return source; }
	public double getTimeStamp() { return timeStamp;   	}
	public Object getEventObject() { return eventObject; 	}
	public double getSecondsValid() { return secondsValid;	}
	public Object getOriginalData() { return originalData; 	}
	
	public Event(SensorModel source, Object eventObject, double timeStamp, double timeValid)
	{
		this(source, eventObject, timeStamp, timeValid, null);
	}
	
	public Event(SensorModel source, Object eventObject, double timeStamp, double timeValid, Object originalData) 
	{
		if (eventObject == null) throw new IllegalArgumentException("eventObject may not be null!");
		
		this.source = source;
		this.timeStamp = timeStamp;
		this.eventObject = eventObject;
		this.secondsValid = timeValid;
		this.originalData = originalData;
	}
}	
