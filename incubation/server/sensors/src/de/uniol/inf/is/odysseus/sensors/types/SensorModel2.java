package de.uniol.inf.is.odysseus.sensors.types;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.sensors.utilities.MapXmlAdapter;

@XmlRootElement(name = "sensor")
public class SensorModel2 
{	
	public String id;
	public String type;
	public String displayName;
	
	@XmlJavaTypeAdapter(MapXmlAdapter.class)
	public Map<String, String> options = new HashMap<>();
	public Position position = new Position();
			
	public SensorModel2()
	{
	}	
}
