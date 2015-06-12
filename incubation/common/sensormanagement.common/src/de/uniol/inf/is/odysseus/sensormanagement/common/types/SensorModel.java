package de.uniol.inf.is.odysseus.sensormanagement.common.types;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.sensormanagement.common.types.position.Position;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.MapXmlAdapter;

@XmlRootElement(name = "sensor")
public class SensorModel 
{	
	@XmlID
	public String id;
	public String type;
	public String displayName;
	
	@XmlJavaTypeAdapter(MapXmlAdapter.class)
	public Map<String, String> options = new HashMap<>();
	public Position position;
			
	public SensorModel()
	{
	}

	public SensorModel(SensorModel other) 
	{
		id = other.id;
		type = other.type;
		displayName = other.displayName;
		
		options = new HashMap<>(other.options);
		position = other.position == null ? null : other.position.clone();
	}
	
	public void generateId()
	{
		id = type + "_" + Integer.toString((int) (Math.random() * Integer.MAX_VALUE));
	}
}
