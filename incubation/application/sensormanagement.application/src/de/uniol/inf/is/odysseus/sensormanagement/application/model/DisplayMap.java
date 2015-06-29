package de.uniol.inf.is.odysseus.sensormanagement.application.model;

import javax.xml.bind.annotation.XmlElement;

public class DisplayMap 
{
	public String	name;
	public String 	imageFile;	
	public double   topPosition, bottomPosition, leftPosition, rightPosition;
	
	@XmlElement(name = "relativeTo")
	public String	instanceName;	
}