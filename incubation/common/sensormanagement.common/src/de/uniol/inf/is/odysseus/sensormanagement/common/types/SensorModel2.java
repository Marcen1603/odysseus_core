package de.uniol.inf.is.odysseus.sensormanagement.common.types;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.MapXmlAdapter;

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

	public SensorModel2(SensorModel2 other) 
	{
		id = other.id;
		type = other.type;
		displayName = other.displayName;
		
		options = new HashMap<>(other.options);
		position = new Position(other.position);
	}
	
	public void generateId()
	{
		id = type + "_" + Integer.toString((int) (Math.random() * Integer.MAX_VALUE));
	}
}
