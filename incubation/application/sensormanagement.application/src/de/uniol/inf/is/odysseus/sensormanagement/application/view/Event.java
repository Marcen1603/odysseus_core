package de.uniol.inf.is.odysseus.sensormanagement.application.view;

public class Event
{
	private final double 	timeStamp;
	private final Object 	eventObject;
	private final double 	secondsValid;		

	public double 	getTimeStamp() 	 	{ return timeStamp;   	}
	public Object 	getEventObject() 	{ return eventObject; 	}
	public double 	getSecondsValid()	{ return secondsValid;	}
	
	public Event(Object eventObject, double timeStamp, double timeValid) 
	{
		if (eventObject == null)
			throw new ViewException("eventObject == null");
		
		this.timeStamp = timeStamp;
		this.eventObject = eventObject;
		this.secondsValid = timeValid;
	}
}	
