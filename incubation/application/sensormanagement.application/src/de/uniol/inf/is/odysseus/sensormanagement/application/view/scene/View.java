package de.uniol.inf.is.odysseus.sensormanagement.application.view.scene;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "view")
@XmlAccessorType (XmlAccessType.FIELD)
public class View
{
	double startTime;
	double endTime;		
	
	public View()
	{
		startTime = -1.0;
		endTime = -1.0;
	}
	
	public View(double startTime, double endTime) 
	{
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
