package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.scene;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "interval")
@XmlAccessorType (XmlAccessType.FIELD)
public class TimeInterval
{
	public final double startTime;
	public final double endTime;		
	
	public TimeInterval()
	{
		startTime = -1.0;
		endTime = -1.0;
	}
	
	public TimeInterval(double startTime, double endTime) 
	{
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
