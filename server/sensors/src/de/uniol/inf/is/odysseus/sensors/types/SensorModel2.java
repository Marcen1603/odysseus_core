package de.uniol.inf.is.odysseus.sensors.types;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sensor")
public class SensorModel2 
{	
	public String id;
	public String type;
	public String displayName;
	
	public HashMap<String, String> options;
	public Position position;
			
	public SensorModel2()
	{
	}	
}
